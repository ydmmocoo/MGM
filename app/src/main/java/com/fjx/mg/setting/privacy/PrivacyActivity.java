package com.fjx.mg.setting.privacy;

import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.fjx.mg.R;
import com.fjx.mg.ToolBarManager;
import com.fjx.mg.utils.FriendCheckStateSpUtil;
import com.library.common.base.BaseMvpActivity;
import com.library.common.view.popupwindow.CommonPopupWindow;
import com.library.common.view.popupwindow.Utils;
import com.tencent.imsdk.TIMManager;

import butterknife.BindView;
import butterknife.OnClick;

public class PrivacyActivity extends BaseMvpActivity<PrivacyPresenter> implements PrivacyContract.View, CommonPopupWindow.ViewInterface {

    @BindView(R.id.imgAllowNum)
    ImageView imgAllowNum;

    @BindView(R.id.tvAllowScope)
    TextView tvAllowScope;
    private CommonPopupWindow popWindow;
    private String frinendAccess = "1";//'朋友圈好友可见范围,1:最近三天，2：最近1个月,3:最近半年,0全部',
    private String strangeAccess = "0";//''陌生人可见范围,1:允许查看10条,0:不允许'

    @BindView(R.id.cbFriendCheck)
    CheckBox mCbFriendCheck;

    @Override
    protected PrivacyPresenter createPresenter() {
        return new PrivacyPresenter(this);
    }

    public static Intent newInstance(Context context) {
        return new Intent(context, PrivacyActivity.class);
    }


    @Override
    protected int layoutId() {
        return R.layout.activity_privacy;
    }

    @Override
    protected void initView() {
        ToolBarManager.with(this).setTitle(getString(R.string.privacy));
        mPresenter.getUserInfo();
        FriendCheckStateSpUtil util = new FriendCheckStateSpUtil();
        mCbFriendCheck.setChecked(util.get(TIMManager.getInstance().getLoginUser()));
        mCbFriendCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                FriendCheckStateSpUtil util = new FriendCheckStateSpUtil();
                util.put(TIMManager.getInstance().getLoginUser(), b);
            }
        });
    }


    @OnClick({R.id.btnAllowScope, R.id.btnAllowNum})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.btnAllowNum://允许陌生人查看十条动态
                mPresenter.setSecret(frinendAccess, strangeAccess.equals("1") ? "0" : "1");
                imgAllowNum.setImageDrawable(getResources().getDrawable(strangeAccess.equals("1") ? R.drawable.close_gray : R.drawable.open_red));
                break;
            case R.id.btnAllowScope://允许好友查看动态的范围
                Popup();
                break;
        }
    }

    private void Popup() {
        if (popWindow != null && popWindow.isShowing()) return;
        View upView = LayoutInflater.from(this).inflate(R.layout.popup_up_allow_scope, null);
        //测量View的宽高
        Utils.measureWidthAndHeight(upView);
        popWindow = new CommonPopupWindow.Builder(this)
                .setView(R.layout.popup_up_allow_scope)
                .setWidthAndHeight(ViewGroup.LayoutParams.MATCH_PARENT, upView.getMeasuredHeight())
                .setBackGroundLevel(0.5f)//取值范围0.0f-1.0f 值越小越暗
                .setAnimationStyle(R.style.AnimUp)
                .setViewOnclickListener(this)
                .create();
        popWindow.showAtLocation(findViewById(android.R.id.content), Gravity.BOTTOM, 0, 0);
    }


    @Override
    public void getChildView(View view, int layoutResId) {
        if (layoutResId == R.layout.popup_up_allow_scope) {
            TextView tvAllowAll = view.findViewById(R.id.tvAllowAll);//全部
            TextView tvAllowThree = view.findViewById(R.id.tvAllowThree);//三天
            TextView tvAllowMonth = view.findViewById(R.id.tvAllowMonth);//一个月
            TextView tvAllowYear = view.findViewById(R.id.tvAllowYear);//半年
            tvAllowAll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (popWindow != null) {
                        frinendAccess = "0";
                        tvAllowScope.setText(getString(R.string.all));
                        mPresenter.setSecret(frinendAccess, strangeAccess);
                        popWindow.dismiss();
                    }
                }
            });

            tvAllowThree.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (popWindow != null) {
                        frinendAccess = "1";
                        tvAllowScope.setText(getString(R.string.last_three_days));
                        mPresenter.setSecret(frinendAccess, strangeAccess);
                        popWindow.dismiss();
                    }
                }
            });

            tvAllowMonth.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (popWindow != null) {
                        frinendAccess = "2";
                        tvAllowScope.setText(getString(R.string.last_one_month));
                        mPresenter.setSecret(frinendAccess, strangeAccess);
                        popWindow.dismiss();
                    }
                }
            });

            tvAllowYear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (popWindow != null) {
                        frinendAccess = "3";
                        tvAllowScope.setText(getString(R.string.last_six_months));
                        mPresenter.setSecret(frinendAccess, strangeAccess);
                        popWindow.dismiss();
                    }
                }
            });

            view.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (popWindow != null) {
                        popWindow.dismiss();
                    }
                    return true;
                }
            });
        }
    }

    /**
     * @param momentAccessForFriend   允许朋友查看朋友圈0--全部 1--三天 2--最近一个月 3--最近半年
     * @param momentAccessForStranger 允许陌生人查看朋友圈 0--不允许 1--允许
     */
    @Override
    public void showUserInfo(String momentAccessForFriend, String momentAccessForStranger) {
        frinendAccess = momentAccessForFriend;
        strangeAccess = momentAccessForStranger;
        switch (frinendAccess) {
            case "0":
                tvAllowScope.setText(getString(R.string.all));
                break;
            case "1":
                tvAllowScope.setText(getString(R.string.last_three_days));
                break;
            case "2":
                tvAllowScope.setText(getString(R.string.last_one_month));
                break;
            case "3":
                tvAllowScope.setText(getString(R.string.last_six_months));
                break;
        }
        imgAllowNum.setImageDrawable(getResources().getDrawable(strangeAccess.equals("1") ? R.drawable.open_red : R.drawable.close_gray));
    }

    @Override
    public void setSecretSuccess() {
        mPresenter.getUserInfo();
    }
}
