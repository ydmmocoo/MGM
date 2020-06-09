package com.fjx.mg.me.wallet;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.fjx.mg.R;
import com.fjx.mg.ToolBarManager;
import com.fjx.mg.me.record.BillRecord2Activity;
import com.fjx.mg.me.score.ScoreActivity;
import com.fjx.mg.me.transfer.TransUserActivity;
import com.fjx.mg.me.wallet.detail.BalanceDetailActivity;
import com.fjx.mg.me.wallet.recharge.BalanceRechargeActivity;
import com.fjx.mg.utils.RankPermissionHelper;
import com.fjx.mg.utils.SharedPreferencesHelper;
import com.library.common.base.BaseMvpActivity;
import com.library.common.utils.CommonToast;
import com.library.common.utils.GradientDrawableHelper;
import com.library.common.utils.StringUtil;
import com.library.repository.data.UserCenter;
import com.library.repository.models.UserInfoModel;

import butterknife.BindView;
import butterknife.OnClick;

public class WalletActivity extends BaseMvpActivity<WalletPresenter> implements WalletContract.View {

    @BindView(R.id.tvBalance)
    TextView tvBalance;
    @BindView(R.id.tvBalanceDetail)
    TextView tvBalanceDetail;
    @BindView(R.id.tvMyPoint)
    TextView tvMyPoint;
    @BindView(R.id.tvPointDetail)
    TextView tvPointDetail;
    @BindView(R.id.tvPointExchange)
    TextView tvPointExchange;
    @BindView(R.id.clPoint)
    ConstraintLayout clPoint;
    private boolean isClick=false;

    @Override
    protected WalletPresenter createPresenter() {
        return new WalletPresenter(this);
    }

    public static Intent newInstance(Context context) {
        Intent intent = new Intent(context, WalletActivity.class);
        return intent;
    }


    @Override
    protected int layoutId() {
        return R.layout.ac_my_wallet;
    }

    @Override
    protected void initView() {
        SharedPreferencesHelper sp = new SharedPreferencesHelper(this);
        String string = sp.getString("walletbalance");
        if (string != null && !string.equals("")) {
            tvBalance.setText(string);
        }
        sp.close();
        ToolBarManager.with(this).setTitle(getString(R.string.my_wallet));
        GradientDrawableHelper.whit(tvPointExchange).setColor(R.color.trans).setStroke(1, R.color.text_color_gray).setCornerRadius(50);
        GradientDrawableHelper.whit(tvBalanceDetail).setColor(R.color.trans).setStroke(1, R.color.white).setCornerRadius(50);
        GradientDrawableHelper.whit(tvPointDetail).setColor(R.color.textColorYellow1).setCornerRadius(50);
        GradientDrawableHelper.whit(clPoint).setColor(R.color.trans).setStroke(2, R.color.textColorGray1);
        showUserInfo(UserCenter.getUserInfo());
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.getUserProfile();
        isClick=false;
    }

    @OnClick({R.id.tvBalanceDetail, R.id.llRecharge, R.id.llTransfer, R.id.tvPointDetail, R.id.clPoint, R.id.tvPointExchange, R.id.tvBalanceWithDraw})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tvBalanceDetail:
                if (isClick) {
                    return;
                }
                isClick=true;
                startActivity(BalanceDetailActivity.newInstance(getCurContext()));
                break;
            case R.id.llRecharge:
                if (isClick) {
                    return;
                }
                isClick=true;
                if (RankPermissionHelper.isForbidden()) return;
                startActivity(BalanceRechargeActivity.newInstance(getCurContext()));
                break;
            case R.id.llTransfer:
                if (isClick) {
                    return;
                }
                isClick=true;
                if (RankPermissionHelper.NoPrivileges(2)) return;
                startActivityForResult(TransUserActivity.newInstance(getCurContext()), 111);
                break;
            case R.id.tvPointDetail:
                startActivity(ScoreActivity.newInstance(getCurContext()));
                break;
            case R.id.clPoint:
                break;
            case R.id.tvPointExchange:
                CommonToast.toast(getString(R.string.coming_soon));
                break;
            case R.id.tvBalanceWithDraw://提现
                WalletTipsDialogFragment dialogFragment = new WalletTipsDialogFragment();
                dialogFragment.setOnShowDialogListener(new WalletTipsDialogFragment.OnShowDialogListener() {
                    @Override
                    public void onShow() {
                        showContactUsDialog();
                    }
                });
                dialogFragment.show(getSupportFragmentManager(), "WalletTipsDialogFragment");
                break;
            default:
                break;
        }
    }

    public void showContactUsDialog() {
        final MaterialDialog contactDialog = new MaterialDialog.Builder(getCurActivity())
                .customView(R.layout.dialog_contact_us, true)
                .backgroundColor(ContextCompat.getColor(getCurContext(), R.color.trans))
                .build();
        contactDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View view = contactDialog.getCustomView();
        if (view == null) return;
        TextView textEmail = view.findViewById(R.id.textEmail);
        final TextView tvTel = view.findViewById(R.id.tvTel);
        final TextView tvWechat = view.findViewById(R.id.tvWechat);
        textEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contactDialog.dismiss();
                StringUtil.copyClip(getString(R.string.mg_mail));
                CommonToast.toast(getString(R.string.copy_success));
            }
        });
        tvTel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contactDialog.dismiss();
                StringUtil.copyClip(tvTel.getText().toString());
                CommonToast.toast(getString(R.string.copy_success));
            }
        });
        tvWechat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contactDialog.dismiss();
                StringUtil.copyClip(tvWechat.getText().toString());
                CommonToast.toast(getString(R.string.copy_success));
            }
        });
        contactDialog.show();
    }

    @Override
    public void showUserInfo(UserInfoModel data) {
        SharedPreferencesHelper sp = new SharedPreferencesHelper(this);
        UserInfoModel infoModel = UserCenter.getUserInfo();
        sp.putString("walletbalance", infoModel.getUMoney());
        sp.close();
        tvBalance.setText(infoModel.getUMoney());
        tvMyPoint.setText(infoModel.getScore());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 111) {
            //转账成功显示提示框
            AlertDialog dialog = new AlertDialog.Builder(WalletActivity.this)
                    .setMessage(getString(R.string.transfer_suc))
                    .setPositiveButton(getString(R.string.look), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            //跳转账单详情
                            startActivity(BillRecord2Activity.newInstance(WalletActivity.this, "7"));
                        }
                    })
                    .setNegativeButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    }).create();
            if (!dialog.isShowing() && !isFinishing()) {
                dialog.show();
            }
        }
    }
}
