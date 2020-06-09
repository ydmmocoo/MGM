package com.fjx.mg.login.login;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.fjx.mg.R;
import com.fjx.mg.dialog.CommonDialogHelper;
import com.fjx.mg.login.bind.BindMobileActivity;
import com.fjx.mg.login.register.RegisterActivity;
import com.fjx.mg.main.MainActivity;
import com.fjx.mg.me.safe_center.lock.GestureLockActivity;
import com.fjx.mg.view.PhoneTextWatcher;
import com.library.common.base.BaseMvpActivity;
import com.library.common.utils.GradientDrawableHelper;
import com.library.common.utils.SharedPreferencesUtil;
import com.library.common.utils.StatusBarManager;
import com.library.repository.data.UserCenter;
import com.library.repository.models.UserInfoModel;
import com.lxj.xpopup.interfaces.OnSelectListener;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 验证码登录页
 */
public class IdentifyLoginActivity extends BaseMvpActivity<LoginPresenter> implements LoginContract.View {

    @BindView(R.id.tvLogin)
    TextView tvLogin;
    @BindView(R.id.etMobile)
    EditText etMobile;
    @BindView(R.id.etCode)
    EditText etCode;
    @BindView(R.id.tvAreaCode)
    TextView tvAreaCode;
    @BindView(R.id.tvGetSmsCode)
    TextView tvGetSmsCode;
    private PhoneTextWatcher phoneTextWatcher;

    @Override
    public void setMobile(String text) {
        etMobile.setText(text);
    }

    public static Intent newInstance(Context context) {
        Intent intent = new Intent(context, IdentifyLoginActivity.class);
        return intent;
    }

    @Override
    protected int layoutId() {
        return R.layout.ac_third_login;
    }


    @Override
    protected void initView() {
        super.initView();
        phoneTextWatcher = new PhoneTextWatcher();
        etMobile.addTextChangedListener(phoneTextWatcher);
        StatusBarManager.setLightFontColor(this, R.color.colorAccent);
        GradientDrawableHelper.whit(tvLogin).setColor(R.color.colorAccent).setCornerRadius(80);

        UserInfoModel model = UserCenter.getUserInfo();
        if (model == null) return;
        etMobile.setText(model.getPhone());
        if (!TextUtils.isEmpty(model.getSn()))
            tvAreaCode.setText("+" + model.getSn());

        phoneTextWatcher.setAreaCode(model.getSn());

    }


    @Override
    protected LoginPresenter createPresenter() {
        return new LoginPresenter(this);
    }

    @OnClick(R.id.tvRegister)
    public void clickRegister() {
        startActivity(RegisterActivity.newInstance(getCurContext()));

    }

    @OnClick(R.id.tvLogin)
    public void clickLogin() {
        String mobile = etMobile.getText().toString().replace(" ", "");
        String code = etCode.getText().toString();
        SharedPreferencesUtil.name("sp_code").put("code",tvAreaCode.getText().toString()).apply();
        mPresenter.loginCode(mobile, code);

    }


    @Override
    public void loginSuccess(UserInfoModel data) {
        if (TextUtils.isEmpty(data.getGestureCode())) {
            startActivity(GestureLockActivity.newInstance(getCurContext(), GestureLockActivity.TYPE_SETTING_GESTURE));
        } else {
            startActivity(MainActivity.newInstance(getCurContext()));
            finish();
        }
    }

    @Override
    public void loginFalied() {

    }

    @Override
    public void showTimeCount(String text) {
        tvGetSmsCode.setText(text);
    }

    @Override
    public void userNoRegister(String openId, String nickName, String loginType, String avatar, String sex) {
        startActivity(BindMobileActivity.newInstance(getCurContext(), openId, nickName, loginType, avatar, sex));

    }

    @OnClick(R.id.tvAreaCode)
    public void clickAreaCode() {
        CommonDialogHelper.showAreaCodeDialog(this, new OnSelectListener() {
            @Override
            public void onSelect(int position, String text) {
                tvAreaCode.setText(text);
                phoneTextWatcher.setAreaCode(text.replace("+", ""));
            }
        });
    }

    @OnClick({R.id.tvUserLogin, R.id.ivClose})
    public void clickUserLogin() {
        onBackPressed();
    }


    @OnClick(R.id.tvGetSmsCode)
    public void clickGetSmsCode() {
        String mobile = etMobile.getText().toString().replace(" ", "");
        String areaCode = tvAreaCode.getText().toString().replace("+", "");
        mPresenter.sendSmsCode(mobile, areaCode);
    }

    @Override
    protected void onDestroy() {
        mPresenter.releaseTimer();
        super.onDestroy();
    }

    @OnClick(R.id.ivLoginWx)
    public void clickLoginWx() {
        mPresenter.loginWx();
    }

    @OnClick(R.id.ivAliLogin)
    public void clickAliLogin() {
        mPresenter.loginAli();
    }
}
