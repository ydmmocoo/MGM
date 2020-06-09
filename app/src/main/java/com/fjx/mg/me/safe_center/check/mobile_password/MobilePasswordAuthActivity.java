package com.fjx.mg.me.safe_center.check.mobile_password;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.fjx.mg.R;
import com.fjx.mg.ToolBarManager;
import com.fjx.mg.main.MainActivity;
import com.fjx.mg.me.safe_center.AuthType;
import com.fjx.mg.me.safe_center.bind.RebindMobileActivity;
import com.fjx.mg.me.safe_center.fingerprint.FingerprintSetActivity;
import com.fjx.mg.me.safe_center.lock.GestureLockActivity;
import com.fjx.mg.me.safe_center.question.QuestionSetActivity;
import com.fjx.mg.setting.password.login.ModifyLoginPwdActivity;
import com.fjx.mg.setting.password.pay.ModifyPayPwdActivity;
import com.library.common.base.BaseMvpActivity;
import com.library.common.callback.CActivityManager;
import com.library.common.utils.CommonToast;
import com.library.common.utils.StringUtil;
import com.library.repository.data.UserCenter;
import com.library.repository.models.UserInfoModel;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class MobilePasswordAuthActivity extends BaseMvpActivity<MobilePasswordAuthPresenter> implements MobilePasswordAuthContract.View {


    @BindView(R.id.tvHint)
    TextView tvHint;
    @BindView(R.id.etSmsCode)
    EditText etSmsCode;
    @BindView(R.id.tvGetSmsCode)
    TextView tvGetSmsCode;
    @BindView(R.id.etPassword)
    EditText etPassword;
    @BindView(R.id.llMobileHint)
    LinearLayout llMobileHint;
    @BindView(R.id.cvMobileHint)
    CardView cvMobileHint;
    @BindView(R.id.llPwdHint)
    LinearLayout llPwdHint;
    @BindView(R.id.cvPwdHint)
    CardView cvPwdHint;
    private int mType;
    private boolean isThirdOrSmsLogin;
    private int checkType = 5;

    public static Intent newInstance(Context context, int type) {
        Intent intent = new Intent(context, MobilePasswordAuthActivity.class);
        intent.putExtra("type", type);
        return intent;
    }

    public static Intent newInstance(Context context, int type, boolean isThirdOrSmsLogin) {
        Intent intent = new Intent(context, MobilePasswordAuthActivity.class);
        intent.putExtra("type", type);
        intent.putExtra("isThirdOrSmsLogin", isThirdOrSmsLogin);
        return intent;
    }

    @Override
    protected MobilePasswordAuthPresenter createPresenter() {
        return new MobilePasswordAuthPresenter(this);
    }

    @Override
    protected int layoutId() {
        return R.layout.ac_question_password;
    }

    @Override
    protected void initView() {
        mType = getIntent().getIntExtra("type", -1);
        isThirdOrSmsLogin = getIntent().getBooleanExtra("isThirdOrSmsLogin", false);
        ToolBarManager.with(this).setTitle(getString(R.string.verification));
        String phone = UserCenter.getUserInfo().getPhone();
        tvHint.setText(getString(R.string.send_sms_to).concat(StringUtil.phoneText(phone)));


        switch (mType) {
            case AuthType.BIND_DEVICE:

            case AuthType.FORGET_PASSWORD:
            case AuthType.BIND_MOBILE:
            case AuthType.LOGIN_PASSWORD:
                //绑定手机区分情况
                if (isThirdOrSmsLogin) {
                    //第三方登录和验证码登录时，只使用密码验证
                    llMobileHint.setVisibility(View.GONE);
                    cvMobileHint.setVisibility(View.GONE);
                    checkType = 6;
                } else {
                    //密码登录的，只使用验证码验证
                    checkType = 7;
                    llPwdHint.setVisibility(View.GONE);
                    cvPwdHint.setVisibility(View.GONE);
                }
                break;

        }
    }


    @OnClick({R.id.tvGetSmsCode, R.id.confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tvGetSmsCode:
                mPresenter.sendSmsCode();
                break;
            case R.id.confirm:
                String smsCode = etSmsCode.getText().toString();
                String psw = etPassword.getText().toString();
                String phone = UserCenter.getUserInfo().getPhone();


                if (mType == AuthType.BIND_DEVICE || mType == AuthType.LOGIN_PASSWORD|| mType == AuthType.FORGET_PASSWORD|| mType == AuthType.BIND_MOBILE) {
                    if (isThirdOrSmsLogin) {
                        if (TextUtils.isEmpty(psw)) {
                            CommonToast.toast(getString(R.string.hint_input_answer));
                            return;
                        }
                    } else {
                        if (TextUtils.isEmpty(smsCode)) {
                            CommonToast.toast(getString(R.string.login_input_smscode));
                            return;
                        }
                    }
                } else {
                    if (TextUtils.isEmpty(smsCode)) {
                        CommonToast.toast(getString(R.string.login_input_smscode));
                        return;
                    }
                    if (TextUtils.isEmpty(psw)) {
                        CommonToast.toast(getString(R.string.hint_input_answer));
                        return;
                    }

                }


                Map<String, Object> map = new HashMap<>();
                map.put("type", checkType);
                map.put("smsCode", smsCode);
                map.put("psw", StringUtil.getPassword(psw));
                map.put("phone", phone);
                mPresenter.check(map);
                break;
        }
    }

    @Override
    public void showTimeCount(String s) {
        tvGetSmsCode.setText(s);
    }

    @Override
    public void checkSuccess() {
        switch (mType) {
            case AuthType.GESTURE:
                startActivity(GestureLockActivity.newInstance(getCurContext(), GestureLockActivity.TYPE_SETTING_GESTURE));
                finish();
                break;
            case AuthType.FINGERPRINT:
                startActivity(FingerprintSetActivity.newInstance(getCurContext()));
                finish();
                break;
            case AuthType.BIND_DEVICE:
                mPresenter.bindDevice();
                break;
            case AuthType.SECURITY_ISSUES:
                startActivity(QuestionSetActivity.newInstance(getCurContext()));
                finish();
                break;
            case AuthType.PAY_PASSWORD:
                startActivityForResult(ModifyPayPwdActivity.newInstance(getCurContext(), true), 111);
                finish();
                break;
            case AuthType.BIND_MOBILE:
                startActivity(RebindMobileActivity.newInstance(getCurContext()));
                finish();
                break;
            case AuthType.LOGIN_PASSWORD:
            case AuthType.FORGET_PASSWORD:
                startActivity(ModifyLoginPwdActivity.newInstance(getCurContext(), true));
                finish();
                break;
        }

    }

    @Override
    public void bindDeviceSuccess() {
        UserInfoModel infoModel = UserCenter.getUserInfo();
        infoModel.setCheckDevice(false);
        UserCenter.saveUserInfo(infoModel);
        startActivity(MainActivity.newInstance(getCurContext()));
        CActivityManager.getAppManager().finishOthersActivity(MainActivity.class);
    }

    @Override
    protected void onDestroy() {
        mPresenter.releaseTimer();
        super.onDestroy();
    }
}
