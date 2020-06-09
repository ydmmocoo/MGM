package com.fjx.mg.setting.password.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fjx.mg.R;
import com.fjx.mg.ToolBarManager;
import com.library.common.base.BaseMvpActivity;
import com.library.common.utils.GradientDrawableHelper;
import com.library.common.utils.StringUtil;
import com.library.repository.data.UserCenter;
import com.library.repository.models.UserInfoModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ModifyLoginPwdActivity extends BaseMvpActivity<ModifyLoginPwdPresenter> implements ModifyLoginPwdContract.View {


    @BindView(R.id.tvPhone)
    TextView tvPhone;
    @BindView(R.id.etSmsCode)
    EditText etSmsCode;
    @BindView(R.id.tvGetSmsCode)
    TextView tvGetSmsCode;
    @BindView(R.id.etNewPwd)
    EditText etNewPwd;
    @BindView(R.id.etConfirmPwd)
    EditText etConfirmPwd;
    @BindView(R.id.etOldPwd)
    EditText mEtOldPwd;
    @BindView(R.id.tvConfirm)
    TextView tvConfirm;
    @BindView(R.id.llPhone)
    LinearLayout llPhone;
    @BindView(R.id.llOldPwd)
    LinearLayout llOldPwd;
    @BindView(R.id.llSmsCode)
    LinearLayout llSmsCode;
    private UserInfoModel infoModel;
    private boolean isReset;


    @Override
    protected ModifyLoginPwdPresenter createPresenter() {
        return new ModifyLoginPwdPresenter(this);
    }

    public static Intent newInstance(Context context, boolean isReset) {
        Intent intent = new Intent(context, ModifyLoginPwdActivity.class);
        intent.putExtra("isReset", isReset);
        return intent;
    }

    public static Intent newInstance(Context context) {
        return newInstance(context, false);
    }


    @Override
    protected int layoutId() {
        return R.layout.ac_modify_login_pwd;
    }

    @Override
    protected void initView() {
        isReset = getIntent().getBooleanExtra("isReset", false);
        ToolBarManager.with(this).setTitle(getString(R.string.edit_login_password));
        GradientDrawableHelper.whit(tvConfirm).setColor(R.color.colorAccent).setCornerRadius(50);
        infoModel = UserCenter.getUserInfo();
        if (infoModel == null) return;
        tvPhone.setText(infoModel.getPhone());

        llPhone.setVisibility(isReset ? View.GONE : View.VISIBLE);
        llOldPwd.setVisibility(isReset ? View.GONE : View.VISIBLE);
        llSmsCode.setVisibility(isReset ? View.GONE : View.VISIBLE);
    }


    @OnClick({R.id.tvGetSmsCode, R.id.tvConfirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tvGetSmsCode:
                String mobile = infoModel.getPhone();
                String areaCode = infoModel.getSn();
                mPresenter.sendSmsCode(mobile, areaCode);
                break;
            case R.id.tvConfirm:
                String smsCode = etSmsCode.getText().toString();
                String newPassword = etNewPwd.getText().toString();
                String confirmPasword = etConfirmPwd.getText().toString();
                String oldPassword = mEtOldPwd.getText().toString();

                if (isReset) {
                    mPresenter.resetPassword(newPassword, confirmPasword);
                } else {
                    mPresenter.modifyPassword(smsCode, oldPassword, newPassword, confirmPasword);

                }
                break;
        }
    }

    @Override
    public void showTimeCount(String s) {
        tvGetSmsCode.setText(s);
    }

    @Override
    public void modifySuccess() {
        finish();
    }

    @Override
    protected void onDestroy() {
        mPresenter.releaseTimer();
        super.onDestroy();
    }
}
