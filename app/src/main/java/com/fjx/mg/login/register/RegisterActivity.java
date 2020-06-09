package com.fjx.mg.login.register;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.fjx.mg.R;
import com.fjx.mg.login.areacode.AreaCodeActivity;
import com.fjx.mg.main.MainActivity;
import com.fjx.mg.me.safe_center.lock.GestureLockActivity;
import com.fjx.mg.view.PhoneTextWatcher;
import com.fjx.mg.web.CommonWebActivity;
import com.library.common.base.BaseMvpActivity;
import com.library.common.utils.CommonToast;
import com.library.common.utils.GradientDrawableHelper;
import com.library.common.utils.JsonUtil;
import com.library.common.utils.SharedPreferencesUtil;
import com.library.common.utils.StringUtil;
import com.library.repository.models.UserInfoModel;

import butterknife.BindView;
import butterknife.OnClick;

public class RegisterActivity extends BaseMvpActivity<RegisterPresenter> implements RegisterContract.View {

    @BindView(R.id.tvRegister)
    TextView tvRegister;
    @BindView(R.id.etUserName)
    EditText etUserName;
    @BindView(R.id.etMobile)
    EditText etMobile;
    @BindView(R.id.etCode)
    EditText etCode;
    @BindView(R.id.tvGetSmsCode)
    TextView tvGetSmsCode;
    @BindView(R.id.loginHint)
    TextView loginHint;
    @BindView(R.id.etPwd)
    EditText etPwd;
    @BindView(R.id.etConfirmPwd)
    EditText etConfirmPwd;

    @BindView(R.id.tvAreaCode)
    TextView tvAreaCode;
    @BindView(R.id.checkbox)
    CheckBox checkbox;
    @BindView(R.id.tvRegisterPro)
    TextView tvRegisterPro;
    private String registerPro;
    private PhoneTextWatcher phoneTextWatcher;

    public static Intent newInstance(Context context) {
        Intent intent = new Intent(context, RegisterActivity.class);
        return intent;
    }

    @Override
    protected int layoutId() {
        mCommonStatusBarEnable = false;
        return R.layout.ac_register;
    }


    @Override
    protected void initView() {
//        StatusBarManager.setLightFontColor(this, R.color.colorAccent);
        GradientDrawableHelper.whit(tvRegister).setColor(R.color.colorAccent).setCornerRadius(80);
        phoneTextWatcher = new PhoneTextWatcher();
        phoneTextWatcher.setAreaCode("86");
        etMobile.addTextChangedListener(phoneTextWatcher);
        mPresenter.registerPro();
    }


    @Override
    protected RegisterPresenter createPresenter() {
        return new RegisterPresenter(this);
    }


    @Override
    public void registerSuccess(UserInfoModel data) {
        CommonToast.toast(getString(R.string.register_success));
//        startActivity(CashListActivity.newInstance(getCurContext()));
//        finish();
        if (TextUtils.isEmpty(data.getGestureCode())) {
            startActivity(GestureLockActivity.newInstance(getCurContext(), GestureLockActivity.TYPE_SETTING_GESTURE));
        } else {
            startActivity(MainActivity.newInstance(getCurContext()));
        }
        finish();
    }

    @Override
    public void showTimeCount(String text) {
        tvGetSmsCode.setText(text);
    }

    @Override
    public void showRegisterPro(String text) {
        this.registerPro = text;
    }

    @Override
    public void setMobile(String text) {
        etMobile.setText(text);
    }

    @OnClick(R.id.tvRegister)
    public void clickRegister() {

        if (!checkbox.isChecked()) {
            CommonToast.toast(getString(R.string.hint_user_pro));
            return;
        }
        String nickName = etUserName.getText().toString();
        String mobile = etMobile.getText().toString().replace(" ", "");
        String sms = etCode.getText().toString();
        String pwd = etPwd.getText().toString();
        String confirmPwd = etConfirmPwd.getText().toString();
        String areaCode = tvAreaCode.getText().toString().replace("+", "");
        if (TextUtils.isEmpty(mobile)) {
            CommonToast.toast(getString(R.string.login_input_phone));
            return;
        }

        String code = SharedPreferencesUtil.name("sp_code").getString("code", "86");
        if (code.contains("261")) {
            if (!StringUtil.phoneLegal(mobile)) {
                CommonToast.toast(getString(R.string.error_format_phone));
                return;
            }
        }
        mPresenter.register(nickName, areaCode, mobile, sms, pwd, confirmPwd);
    }

    @OnClick(R.id.ivBack)
    public void clickBack() {
        onBackPressed();
    }

    @OnClick(R.id.tvGetSmsCode)
    public void clickGetSmsCode() {
        String mobile = etMobile.getText().toString().replace(" ", "");
        ;
        String areaCode = tvAreaCode.getText().toString().replace("+", "");
        if (TextUtils.isEmpty(mobile)) {
            CommonToast.toast(getString(R.string.login_input_phone));
            return;
        }

        String code = SharedPreferencesUtil.name("sp_code").getString("code", "86");
        if (code.contains("261")) {
            if (!StringUtil.phoneLegal(mobile)) {
                CommonToast.toast(getString(R.string.error_format_phone));
                return;
            }
        }
        mPresenter.sendSmsCode(mobile, areaCode);
    }


    @OnClick(R.id.tvAreaCode)
    public void clickAreaCode() {
        startActivityForResult(AreaCodeActivity.newInstance(getCurContext()), 111);
//        CommonDialogHelper.showAreaCodeDialog(this, new OnSelectListener() {
//            @Override
//            public void onSelect(int position, String text) {
//                tvAreaCode.setText(text);
//            }
//        });
    }

    @Override
    protected void onDestroy() {
        mPresenter.releaseTimer();
        super.onDestroy();
    }


    @OnClick(R.id.tvRegisterPro)
    public void onViewClicked() {
        CommonWebActivity.Options options = new CommonWebActivity.Options();
        options.setLoadUrl(registerPro);
        options.setEnableShare(false);
        options.setTitle(getString(R.string.user_pro));
        startActivity(CommonWebActivity.newInstance(getCurContext(), JsonUtil.moderToString(options)));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == resultCode) {
            if (data == null) return;
            String areaCode = data.getStringExtra("areaCode");
            tvAreaCode.setText("+" + areaCode);
            phoneTextWatcher.setAreaCode(areaCode);
            etMobile.setHint(getString(R.string.login_input_phone_tip));
        }
    }
}
