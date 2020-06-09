package com.fjx.mg.me.safe_center.auth;

import android.content.Context;
import android.content.Intent;
import androidx.core.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import com.fjx.mg.R;
import com.fjx.mg.ToolBarManager;
import com.fjx.mg.me.safe_center.AuthType;
import com.library.common.base.BaseMvpActivity;
import com.library.common.constant.IntentConstants;
import com.library.common.utils.GradientDrawableHelper;
import com.library.repository.data.UserCenter;
import com.library.repository.models.UserInfoModel;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 修改/重置登录密码
 */
public class AuthCenterActivity extends BaseMvpActivity<AuthCenterPresenter> implements AuthCenterContract.View {
    @BindView(R.id.tvHint)
    TextView tvHint;
    @BindView(R.id.tvChange)
    TextView tvChange;
    @BindView(R.id.tvReset)
    TextView tvReset;


    private int mType;

    public static Intent newInstance(Context context, int type) {
        Intent intent = new Intent(context, AuthCenterActivity.class);
        intent.putExtra(IntentConstants.TYPE, type);
        return intent;
    }


    @Override
    protected int layoutId() {
        return R.layout.ac_auth_center;
    }

    @Override
    protected AuthCenterPresenter createPresenter() {
        return new AuthCenterPresenter(this);
    }

    @Override
    protected void initView() {
        mType = getIntent().getIntExtra(IntentConstants.TYPE, AuthType.GESTURE);
        UserInfoModel infoModel = UserCenter.getUserInfo();
        String title = "";
        switch (mType) {
            case AuthType.GESTURE:
                title = getString(R.string.gesture_pwd);
                tvHint.setText(getString(R.string.hint_gesture_set));
                tvChange.setText(getString(R.string.change).concat(getString(R.string.gesture_pwd)));
                tvReset.setText(getString(R.string.reset).concat(getString(R.string.gesture_pwd)));
                GradientDrawableHelper.whit(tvChange).setColor(R.color.colorAccent).setCornerRadius(50);
                GradientDrawableHelper.whit(tvReset).setColor(R.color.white).setCornerRadius(50);
                break;

            case AuthType.SECURITY_ISSUES:
                title = getString(R.string.safe_question);
                tvHint.setText(getString(R.string.hint_safe_question_set));
                tvChange.setVisibility(View.GONE);
                tvReset.setText(getString(R.string.reset).concat(getString(R.string.safe_question)));
                GradientDrawableHelper.whit(tvReset).setColor(R.color.colorAccent).setCornerRadius(50);
                tvReset.setTextColor(ContextCompat.getColor(getCurContext(), R.color.white));
                break;

            case AuthType.LOGIN_PASSWORD:
                title = getString(R.string.login_password);
                tvHint.setText(getString(R.string.hint_login_pwd_set));
                tvChange.setText(getString(R.string.change).concat(getString(R.string.login_password)));
                tvReset.setText(getString(R.string.reset).concat(getString(R.string.login_password)));
                GradientDrawableHelper.whit(tvChange).setColor(R.color.colorAccent).setCornerRadius(50);
                GradientDrawableHelper.whit(tvReset).setColor(R.color.white).setCornerRadius(50);
                break;
            case AuthType.BIND_MOBILE:
                title = getString(R.string.bind_mobile);
                tvHint.setText(getString(R.string.hint_cur_mobile).concat(infoModel.getSn()).concat(infoModel.getPhone()));
                tvChange.setVisibility(View.GONE);
                tvReset.setText(getString(R.string.change_mobile));
                GradientDrawableHelper.whit(tvReset).setColor(R.color.colorAccent).setCornerRadius(50);
                tvReset.setTextColor(ContextCompat.getColor(getCurContext(), R.color.white));
                break;

            case AuthType.PAY_PASSWORD:
                title = getString(R.string.pay_password);
                tvHint.setText(getString(R.string.hint_pay_password_set));
                tvChange.setText(getString(R.string.change).concat(getString(R.string.pay_password)));
                tvReset.setText(getString(R.string.reset).concat(getString(R.string.pay_password)));
                GradientDrawableHelper.whit(tvChange).setColor(R.color.colorAccent).setCornerRadius(50);
                GradientDrawableHelper.whit(tvReset).setColor(R.color.white).setCornerRadius(50);
                break;
        }
        ToolBarManager.with(this).setTitle(title);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mType == AuthType.BIND_MOBILE) {
            UserInfoModel infoModel = UserCenter.getUserInfo();
            tvHint.setText(getString(R.string.hint_cur_mobile).concat(infoModel.getSn()).concat(infoModel.getPhone()));
        }
    }

    @OnClick({R.id.tvChange, R.id.tvReset})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tvChange:
                mPresenter.change(mType);
                break;
            case R.id.tvReset:
                mPresenter.reset(mType);
                break;
        }
    }
}
