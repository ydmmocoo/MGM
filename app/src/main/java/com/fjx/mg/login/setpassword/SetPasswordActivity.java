package com.fjx.mg.login.setpassword;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.fjx.mg.R;
import com.fjx.mg.main.MainActivity;
import com.fjx.mg.me.safe_center.lock.GestureLockActivity;
import com.fjx.mg.utils.RegUtil;
import com.library.common.base.BaseActivity;
import com.library.common.constant.IntentConstants;
import com.library.common.utils.CommonToast;
import com.library.common.utils.StringUtil;
import com.library.repository.core.net.CommonObserver;
import com.library.repository.core.net.RxScheduler;
import com.library.repository.data.UserCenter;
import com.library.repository.models.ResponseModel;
import com.library.repository.models.UserInfoModel;
import com.library.repository.repository.RepositoryFactory;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * Author    by hanlz
 * Date      on 2019/10/27.
 * Description：没注册的用户直接用验证码登录跳转此页设置密码
 */
public class SetPasswordActivity extends BaseActivity {

    @BindView(R.id.etPassword)
    EditText mEtPassword;
    @BindView(R.id.etAgainPassword)
    EditText mEtAgainPassword;

    private String phone, smsCode, sn;


    public static Intent newIntent(Context context, String phone, String smsCode, String sn) {
        Intent intent = new Intent(context, SetPasswordActivity.class);
        intent.putExtra(IntentConstants.PHONE, phone);
        intent.putExtra(IntentConstants.SMS_CODE, smsCode);
        intent.putExtra(IntentConstants.SN, sn);
        return intent;
    }

    @Override
    protected int layoutId() {
        return R.layout.act_set_password;
    }

    @Override
    protected void initView() {
        super.initView();
        if (getIntent() == null) {
            return;
        }
        phone = getIntent().getStringExtra(IntentConstants.PHONE);
        smsCode = getIntent().getStringExtra(IntentConstants.SMS_CODE);
        sn = getIntent().getStringExtra(IntentConstants.SN);

    }

    @OnClick({R.id.ivBack,R.id.btnConfirm1})
    public void onClickView(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                hideLoading();
                finish();
                break;
            case R.id.btnConfirm1://确认设置密码
                register("", sn,phone, smsCode,  mEtPassword.getText().toString(), mEtAgainPassword.getText().toString());
                break;
            default:
        }
    }

    void register(String nickName, String sn, String phone, String smsCode, String psw, String confirmPwd) {
        if (TextUtils.isEmpty(psw)) {
            CommonToast.toast(getString(R.string.login_input_password));
            return;
        }
        if (TextUtils.isEmpty(confirmPwd)) {
            CommonToast.toast(getString(R.string.hint_input_confirm_password));
            return;
        }
        if (!TextUtils.isEmpty(psw)) {
            if (!RegUtil.checkPassword(psw)) {
                CommonToast.toast(getString(R.string.wrong_password_pattern));
                return;
            }

            if (!TextUtils.equals(confirmPwd, psw)) {
                CommonToast.toast(getString(R.string.error_confirm_pwd));
                return;
            }
            psw = StringUtil.getPassword(psw);
        }
        String deviceId = RepositoryFactory.getShareApi().getRegistrationId();
        String longitude = RepositoryFactory.getLocalRepository().getLongitude();
        String latitude = RepositoryFactory.getLocalRepository().getLatitude();
        RepositoryFactory.getRemoteAccountRepository().register(nickName, sn, phone, smsCode, psw, "", "", "", "", deviceId, longitude, latitude)
                .compose(RxScheduler.<ResponseModel<UserInfoModel>>toMain())
                .as(this.<ResponseModel<UserInfoModel>>bindAutoDispose())
                .subscribe(new CommonObserver<UserInfoModel>() {
                    @Override
                    public void onSuccess(UserInfoModel data) {
                        UserCenter.saveUserInfo(data);
//                        UserCenter.loginTim();
                        UserCenter.imLogin();
                        CommonToast.toast(getString(R.string.register_success));
                        if (TextUtils.isEmpty(data.getGestureCode())) {
                            startActivity(GestureLockActivity.newInstance(getCurContext(), GestureLockActivity.TYPE_SETTING_GESTURE));
                        } else {
                            startActivity(MainActivity.newInstance(getCurContext()));
                            finish();
                        }
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        CommonToast.toast(data.getMsg());
                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                        CommonToast.toast(data.getMsg());
                    }
                });
    }
}
