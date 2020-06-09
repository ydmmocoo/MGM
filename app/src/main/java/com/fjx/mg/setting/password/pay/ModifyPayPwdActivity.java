package com.fjx.mg.setting.password.pay;

import android.content.Context;
import android.content.Intent;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fjx.mg.R;
import com.fjx.mg.ToolBarManager;
import com.fjx.mg.me.safe_center.question.QuestionSetActivity;
import com.fjx.mg.setting.certification.CertificationActivity;
import com.library.common.base.BaseMvpActivity;
import com.library.common.utils.CommonToast;
import com.library.common.utils.GradientDrawableHelper;
import com.library.repository.data.UserCenter;
import com.library.repository.models.UserInfoModel;

import butterknife.BindView;
import butterknife.OnClick;

public class ModifyPayPwdActivity extends BaseMvpActivity<ModifyPayPwdPresenter1> implements ModifyPayPwdContract.View {


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
    EditText etOldPwd;
    @BindView(R.id.tvConfirm)
    TextView tvConfirm;
    @BindView(R.id.tvNote)
    TextView tvNote;

    @BindView(R.id.llOldPwd)
    LinearLayout llOldPwd;

    @BindView(R.id.llPhone)
    LinearLayout llPhone;
    @BindView(R.id.llSmsCode)
    LinearLayout llSmsCode;
    private UserInfoModel infoModel;

    private boolean hasPayPassword, isReset;

    @Override
    protected ModifyPayPwdPresenter1 createPresenter() {
        return new ModifyPayPwdPresenter1(this);
    }

    public static Intent newInstance(Context context, boolean isReset) {
        Intent intent = new Intent(context, ModifyPayPwdActivity.class);
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
        infoModel = UserCenter.getUserInfo();
        hasPayPassword = infoModel.hasSetPayPwd();
        isReset = getIntent().getBooleanExtra("isReset", false);
        ToolBarManager.with(this).setTitle(hasPayPassword && !isReset ? getString(R.string.edit_pay_password) : getString(R.string.set_pay_password));

        GradientDrawableHelper.whit(tvConfirm).setColor(R.color.colorAccent).setCornerRadius(50);

        etNewPwd.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        etConfirmPwd.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);


        llPhone.setVisibility(hasPayPassword && !isReset ? View.VISIBLE : View.GONE);
        llSmsCode.setVisibility(hasPayPassword && !isReset ? View.VISIBLE : View.GONE);

        if (infoModel == null) return;
        tvPhone.setText(infoModel.getPhone());
        llOldPwd.setVisibility(hasPayPassword && !isReset ? View.VISIBLE : View.GONE);
        tvNote.setText(getString(R.string.hint_note_password));
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

                if (isReset || !hasPayPassword) {
                    mPresenter.setPayPassword(smsCode, newPassword, confirmPasword);
                } else {
                    String oldPwd = etOldPwd.getText().toString();
                    mPresenter.modifyPayPassword(smsCode, newPassword, confirmPasword, oldPwd);
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
        //TODO 2019-10-29 11:17:49 目前版本不需要实名认证 先屏蔽
//        if (infoModel.isRealName() == 0) {
//            startActivity(CertificationActivity.newInstance(getCurContext()));
//        } else
        if (!infoModel.isSetSecurityIssues()) {
            startActivity(QuestionSetActivity.newInstance(getCurContext()));
            CommonToast.toast(getString(R.string.set_success));
            finish();
            return;
        }
        setResult(111);
        finish();
    }

    @Override
    protected void onDestroy() {
        mPresenter.releaseTimer();
        super.onDestroy();
    }
}
