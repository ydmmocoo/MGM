package com.fjx.mg.login.login;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.CountDownTimer;
import androidx.core.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.common.paylibrary.AliPayUtil;
import com.common.paylibrary.listener.ThirdLoginListener;
import com.common.paylibrary.model.AliUserModel;
import com.common.sharesdk.ShareLoginListener;
import com.fjx.mg.R;
import com.fjx.mg.main.MainActivity;
import com.fjx.mg.me.safe_center.AuthType;
import com.fjx.mg.me.safe_center.check.identity.IdentityAuthActivity;
import com.fjx.mg.me.safe_center.check.identity_mobile.IdentityMobileAuthActivity;
import com.fjx.mg.me.safe_center.check.mobile_password.MobilePasswordAuthActivity;
import com.fjx.mg.me.safe_center.check.mobile_question.MobileQuestionAuthActivity;
import com.fjx.mg.me.safe_center.check.question.QuestionAuthActivity;
import com.fjx.mg.me.safe_center.lock.GestureLockActivity;
import com.google.gson.JsonObject;
import com.library.common.utils.CommonToast;
import com.library.common.utils.JsonUtil;
import com.library.common.utils.LogTUtil;
import com.library.common.utils.SharedPreferencesUtil;
import com.library.common.utils.StringUtil;
import com.library.repository.Constant;
import com.library.repository.core.net.CommonObserver;
import com.library.repository.core.net.RxScheduler;
import com.library.repository.data.UserCenter;
import com.library.repository.models.AuthQuestionModel;
import com.library.repository.models.PersonCerModel;
import com.library.repository.models.ResponseModel;
import com.library.repository.models.UserInfoModel;
import com.library.repository.repository.RepositoryFactory;

class LoginPresenter extends LoginContract.Presenter {

    private boolean isTimeStart;
    private MaterialDialog gestureDialog;

    LoginPresenter(LoginContract.View view) {
        super(view);
    }

    @Override
    void loginPwd(final String mobile, String pwd) {
        if (TextUtils.isEmpty(mobile)) {
            CommonToast.toast(mView.getCurContext().getString(R.string.login_input_phone));
            return;
        }
        if (TextUtils.isEmpty(pwd)) {
            CommonToast.toast(mView.getCurContext().getString(R.string.login_input_password));
            return;
        }
//        mView.showLoading();
        String longitude = RepositoryFactory.getLocalRepository().getLongitude();
        String latitude = RepositoryFactory.getLocalRepository().getLatitude();
        String deviceId = RepositoryFactory.getShareApi().getRegistrationId();
        RepositoryFactory.getRemoteAccountRepository()
                .loginPwd(mobile, StringUtil.getPassword(pwd), longitude, latitude, deviceId)
                .compose(RxScheduler.<ResponseModel<UserInfoModel>>toMain())
                .as(mView.<ResponseModel<UserInfoModel>>bindAutoDispose())
                .subscribe(new CommonObserver<UserInfoModel>() {
                    @Override
                    public void onSuccess(UserInfoModel data) {
                        loginResult(data, false);
                    }

                    @Override
                    public void onError(ResponseModel data) {
//                        mView.hideLoading();
                        if (mView != null) {
                            mView.loginFalied();
                        }
                        CommonToast.toast(data.getMsg());
                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                        if (mView != null) {
                            mView.loginFalied();
                        }
                        CommonToast.toast(data.getMsg());
                    }
                });


    }

    @Override
    void loginAuth(final String openId, final String nickName, final String type,
                   final String avatar, final String sex) {

        String deviceId = RepositoryFactory.getShareApi().getRegistrationId();
        String longitude = RepositoryFactory.getLocalRepository().getLongitude();
        String latitude = RepositoryFactory.getLocalRepository().getLatitude();
        if (mView != null) {
            RepositoryFactory.getRemoteAccountRepository().loginAuth(openId, type, longitude, latitude, deviceId)
                    .compose(RxScheduler.<ResponseModel<UserInfoModel>>toMain())
                    .as(mView.<ResponseModel<UserInfoModel>>bindAutoDispose())
                    .subscribe(new CommonObserver<UserInfoModel>() {
                        @Override
                        public void onSuccess(UserInfoModel data) {
//                        mView.userNoRegister(openId, nickName, type, avatar, sex);
                            loginResult2(data, true);
                        }

                        @Override
                        public void onSetPassword() {
                            super.onSetPassword();
                            if (mView != null) {
                                mView.loginFalied();
                                mView.userNoRegister(openId, nickName, type, avatar, sex);
                            }
                        }

                        @Override
                        public void onError(ResponseModel data) {
                            if (mView != null) {
//                                mView.hideLoading();
                                mView.loginFalied();
                                if (data.getCode() == Constant.ERROR.NO_USER) {
                                    mView.userNoRegister(openId, nickName, type, avatar, sex);
                                    return;
                                }
                                CommonToast.toast(data.getMsg());
                            }
                        }

                        @Override
                        public void onNetError(ResponseModel data) {
                            if (mView != null) {
                                mView.loginFalied();
                            }
                            CommonToast.toast(data.getMsg());
                        }
                    });
        }
    }

    @Override
    void loginFacebook() {
        RepositoryFactory.getShareApi().loginFacebook(mView.getCurActivity(), new ShareLoginListener() {

            @Override
            public void loginSuccess(String openid, String nickName, String avatar, String sex) {
                loginAuth(openid, nickName, Constant.LoginType.FACEBOOK, avatar, sex);
//                mView.userNoRegister(openid, nickName, Constant.LoginType.WX, avatar, sex);
            }

            @Override
            public void loginError(boolean isCabcle) {
//                mView.hideLoading();
                if (mView != null) {
                    mView.loginFalied();
                }
            }
        });
    }

    private void loginResult(UserInfoModel data, boolean isThirdOrSmsLogin) {

        UserCenter.saveUserInfo(data);
        Boolean isRealNmae = UserCenter.getUserInfo().isRealName() == 1;
        boolean setSecurityIssues = data.isSetSecurityIssues();//安全问题
        if (data.isCheckDevice()) {
            mView.loginFalied();
            showAuthDialog(AuthType.BIND_DEVICE, isThirdOrSmsLogin, isRealNmae, setSecurityIssues);
            return;
        }
        UserCenter.imLogin();
        mView.loginSuccess(data);
    }

    private void loginResult2(UserInfoModel data, boolean isThirdOrSmsLogin) {
//
        UserCenter.saveUserInfo(data);
        LogTUtil.d("", JsonUtil.moderToString(UserCenter.getUserInfo()));
        Boolean isRealNmae = UserCenter.getUserInfo().isRealName() == 1;
        boolean setSecurityIssues = data.isSetSecurityIssues();//安全问题
        if (data.isCheckDevice()) {
//        if (true) {
            if (mView != null) {
                mView.loginFalied();
            }
//            mView.hideLoading();
            showAuthDialog(AuthType.BIND_DEVICE, isThirdOrSmsLogin, isRealNmae, setSecurityIssues);
            return;
        }
        UserCenter.imLogin();
        mView.loginSuccess(data);
    }

    @Override
    void loginWx() {
//        mView.showLoading();
        RepositoryFactory.getShareApi().loginWx(mView.getCurActivity(), new ShareLoginListener() {

            @Override
            public void loginSuccess(String openid, String nickName, String avatar, String sex) {
                loginAuth(openid, nickName, Constant.LoginType.WX, avatar, sex);
//                mView.userNoRegister(openid, nickName, Constant.LoginType.WX, avatar, sex);
            }

            @Override
            public void loginError(boolean isCabcle) {
//                mView.hideLoading();
                if (mView != null) {
                    mView.loginFalied();
                }
            }
        });
    }

    @Override
    void loginAli() {
//        mView.showLoading();
        if (mView == null) return;
        RepositoryFactory.getRemoteAccountRepository().getAliAuth()
                .compose(RxScheduler.<ResponseModel<String>>toMain())
                .as(mView.<ResponseModel<String>>bindAutoDispose())
                .subscribe(new CommonObserver<String>() {
                    @Override
                    public void onSuccess(String data) {
//                        mView.hideLoading();
                        if (mView != null) {
                            mView.loginFalied();
                        }
                        AliPayUtil.with(mView.getCurActivity()).doLogin(data, new ThirdLoginListener() {
                            @Override
                            public void onSuccess(String openId, String authToken) {
                                getAliUserInfo(openId, authToken);
                            }

                        });

                    }

                    @Override
                    public void onError(ResponseModel data) {
//                        mView.hideLoading();
                        if (mView != null) {
                            mView.loginFalied();
                        }
                        CommonToast.toast(data.getMsg());
                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                        if (mView != null) {
                            mView.loginFalied();
                        }
                        CommonToast.toast(data.getMsg());
                    }
                });
    }

    @Override
    void loginCode(final String mobile, final String code) {
        if (TextUtils.isEmpty(mobile)) {
            CommonToast.toast(mView.getCurContext().getString(R.string.login_input_phone));
            return;
        }
        if (TextUtils.isEmpty(code)) {
            CommonToast.toast(mView.getCurContext().getString(R.string.login_input_smscode));
            return;
        }
        if (mView == null) return;
        mView.showLoading();
        String deviceId = RepositoryFactory.getShareApi().getRegistrationId();
        String longitude = RepositoryFactory.getLocalRepository().getLongitude();
        String latitude = RepositoryFactory.getLocalRepository().getLatitude();
        RepositoryFactory.getRemoteAccountRepository().loginCode(mobile, code, longitude, latitude, deviceId)
                .compose(RxScheduler.<ResponseModel<UserInfoModel>>toMain())
                .as(mView.<ResponseModel<UserInfoModel>>bindAutoDispose())
                .subscribe(new CommonObserver<UserInfoModel>() {
                    @Override
                    public void onSuccess(UserInfoModel data) {
                        loginResult(data, true);
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        mView.hideLoading();
                        CommonToast.toast(data.getMsg());
                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                        if (mView != null) {
                            mView.hideLoading();
                        }
                        CommonToast.toast(data.getMsg());
                    }

                    @Override
                    public void onSetPassword() {
                        super.onSetPassword();
                        register("", SharedPreferencesUtil.name("sp_code").getString("code", "86"), mobile, code, "", "");
                        //TODO
//                        mView.getCurActivity().startActivity(SetPasswordActivity.newIntent(mView.getCurContext(), mobile
//                                , code, SharedPreferencesUtil.name("sp_code").getString("code", "86")));
                    }
                });
    }

    void register(String nickName, String sn, String phone, String smsCode, String psw, String confirmPwd) {

        if (TextUtils.isEmpty(phone)) {
            CommonToast.toast(mView.getCurContext().getString(R.string.login_input_phone));
            return;
        }
        if (TextUtils.isEmpty(smsCode)) {
            CommonToast.toast(mView.getCurContext().getString(R.string.login_input_smscode));
            return;
        }
        if (mView == null) return;
        mView.showLoading();
        String deviceId = RepositoryFactory.getShareApi().getRegistrationId();
        String longitude = RepositoryFactory.getLocalRepository().getLongitude();
        String latitude = RepositoryFactory.getLocalRepository().getLatitude();
        RepositoryFactory.getRemoteAccountRepository().register(nickName, sn, phone, smsCode, psw, "", "", "", "", deviceId, longitude, latitude)
                .compose(RxScheduler.<ResponseModel<UserInfoModel>>toMain())
                .as(mView.<ResponseModel<UserInfoModel>>bindAutoDispose())
                .subscribe(new CommonObserver<UserInfoModel>() {
                    @Override
                    public void onSuccess(UserInfoModel data) {
                        UserCenter.saveUserInfo(data);
                        UserCenter.imLogin();
                        mView.hideLoading();
                        CommonToast.toast(mView.getCurActivity().getString(R.string.register_success));
                        if (TextUtils.isEmpty(data.getGestureCode())) {
                            mView.getCurActivity().startActivity(GestureLockActivity.newInstance(mView.getCurContext(), GestureLockActivity.TYPE_SETTING_GESTURE));
                        } else {
                            mView.getCurActivity().startActivity(MainActivity.newInstance(mView.getCurContext()));
                        }
                        mView.getCurActivity().finish();
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        mView.hideLoading();
                        CommonToast.toast(data.getMsg());
                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                        mView.hideLoading();
                        CommonToast.toast(data.getMsg());
                    }
                });
    }


    @Override
    void sendSmsCode(String mobiles, String areaCode) {
        StringBuffer sb = new StringBuffer();
        sb.append(mobiles);
        if (isTimeStart) return;
        if (TextUtils.isEmpty(mobiles)) {
            CommonToast.toast(mView.getCurContext().getString(R.string.login_input_phone));
            return;
        }
        if (areaCode.equals("261")) {
            if (sb.toString().startsWith("32")) {
            } else if (sb.toString().startsWith("33")) {
            } else if (sb.toString().startsWith("34")) {
            } else {
                CommonToast.toast(R.string.please_rule_input);
            }
        }
        timer.start();
        isTimeStart = true;
        if (mView == null) return;
        mView.showLoading();
        RepositoryFactory.getRemoteAccountRepository().sendSmsCode(sb.toString(), areaCode)
                .compose(RxScheduler.<ResponseModel<UserInfoModel>>toMain())
                .as(mView.<ResponseModel<UserInfoModel>>bindAutoDispose())
                .subscribe(new CommonObserver<UserInfoModel>() {
                    @Override
                    public void onSuccess(UserInfoModel data) {
                        mView.hideLoading();
                        CommonToast.toast(mView.getCurContext().getString(R.string.sms_send_success));

                    }

                    @Override
                    public void onError(ResponseModel data) {
                        mView.hideLoading();
                        CommonToast.toast(data.getMsg());
                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                        mView.hideLoading();
                        CommonToast.toast(data.getMsg());
                    }
                });
    }

    @Override
    void releaseTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }


    @Override
    void getAliUserInfo(final String openid, String code) {
        if (mView == null) return;
        mView.showLoading();
        RepositoryFactory.getRemoteAccountRepository()
                .getAliUserInfo(code)
                .compose(RxScheduler.<ResponseModel<AliUserModel>>toMain())
                .as(mView.<ResponseModel<AliUserModel>>bindAutoDispose())
                .subscribe(new CommonObserver<AliUserModel>() {
                    @Override
                    public void onSuccess(AliUserModel data) {
                        String sex = TextUtils.equals("m", data.getGender()) ? "1" : "2";
                        loginAuth(openid, data.getNick_name(), Constant.LoginType.ZFB, data.getAvatar(), sex);
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        mView.hideLoading();
                        CommonToast.toast(data.getMsg());
                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                        mView.hideLoading();
                        CommonToast.toast(data.getMsg());
                    }
                });

    }

    @Override
    void getUser(final String phone) {

        if (TextUtils.isEmpty(phone)) {
            CommonToast.toast(mView.getCurContext().getString(R.string.login_input_phone));
            return;
        }
        if (mView == null) return;
        mView.showLoading();
        RepositoryFactory.getRemoteAccountRepository().getUser(phone, "1")
                .compose(RxScheduler.<ResponseModel<JsonObject>>toMain())
                .as(mView.<ResponseModel<JsonObject>>bindAutoDispose())
                .subscribe(new CommonObserver<JsonObject>() {
                    @Override
                    public void onSuccess(JsonObject data) {
                        mView.hideLoading();
                        //闪退日志，这边报空，谜一样的数据
                        if (data == null || !data.toString().contains("token")) return;
                        String token = data.get("token").getAsString();
                        UserInfoModel infoModel = new UserInfoModel();
                        infoModel.setPhone(phone);
                        infoModel.setToken(token);
                        UserCenter.saveUserInfo(infoModel);
                        getUserInfo();

                    }

                    @Override
                    public void onError(ResponseModel data) {
                        CommonToast.toast(data.getMsg());
                        mView.hideLoading();
                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                        mView.hideLoading();
                        CommonToast.toast(data.getMsg());
                    }
                });
    }

    private void getUserInfo() {
        if (mView == null) return;
        RepositoryFactory.getRemoteAccountRepository().userAuditInfo()
                .compose(RxScheduler.<ResponseModel<PersonCerModel>>toMain())
                .as(mView.<ResponseModel<PersonCerModel>>bindAutoDispose())
                .subscribe(new CommonObserver<PersonCerModel>() {
                    @Override
                    public void onSuccess(PersonCerModel data) {
                        mView.hideLoading();
                        getSecurityIssue(true);
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        mView.hideLoading();
                        getSecurityIssue(false);
                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                        mView.hideLoading();
                        CommonToast.toast(data.getMsg());
                    }
                });


    }

    void getSecurityIssue(final Boolean isRealName) {
        if (mView == null) return;
        mView.showLoading();
        RepositoryFactory.getRemoteAccountRepository().getSecurityIssue()
                .compose(RxScheduler.<ResponseModel<AuthQuestionModel>>toMain())
                .as(mView.<ResponseModel<AuthQuestionModel>>bindAutoDispose())
                .subscribe(new CommonObserver<AuthQuestionModel>() {
                    @Override
                    public void onSuccess(AuthQuestionModel data) {
                        mView.hideLoading();
                        boolean setSecrityIssues;

                        if (data.getQId() == null) {
                            setSecrityIssues = false;
                        } else {
                            setSecrityIssues = true;
                        }
                        showAuthDialog(AuthType.FORGET_PASSWORD, false, isRealName, setSecrityIssues);
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        mView.hideLoading();
                        showAuthDialog(AuthType.FORGET_PASSWORD, false, isRealName, false);
                    }

                    @Override
                    public void onNetError(ResponseModel data) {

                    }
                });
    }

    private CountDownTimer timer = new CountDownTimer(60000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            long second = millisUntilFinished / 1000;
            mView.showTimeCount(second + "s");
        }

        @Override
        public void onFinish() {
            isTimeStart = false;
            mView.showTimeCount(mView.getCurContext().getString(R.string.getSmscode));
        }
    };


    private void showAuthDialog(final int type, final boolean isThirdOrSmsLogin, boolean isRealNme, final boolean setSecurityIssues) {
        gestureDialog = new MaterialDialog.Builder(mView.getCurActivity())
                .customView(R.layout.dialog_auth, true)
                .backgroundColor(ContextCompat.getColor(mView.getCurContext(), R.color.trans))
                .build();
        gestureDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View view = gestureDialog.getCustomView();
        if (view == null) return;
//        TextView textEmail = view.findViewById(R.id.textEmail);
        TextView textView1 = view.findViewById(R.id.textView1);
        TextView textView2 = view.findViewById(R.id.textView2);
        TextView textView3 = view.findViewById(R.id.textView3);
        View ivw = view.findViewById(R.id.view);
        View ivw1 = view.findViewById(R.id.view1);//textView1下面的线
        textView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gestureDialog.dismiss();
                switch (type) {
                    case AuthType.BIND_DEVICE:
                        mView.getCurContext().startActivity(QuestionAuthActivity.newInstance(mView.getCurContext(), type));
                        break;
                    case AuthType.FORGET_PASSWORD:
                        mView.getCurContext().startActivity(IdentityMobileAuthActivity.newInstance(mView.getCurContext(), type));
                        break;
                }
            }
        });
        textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gestureDialog.dismiss();
                switch (type) {
                    case AuthType.BIND_DEVICE:
                        mView.getCurContext().startActivity(MobilePasswordAuthActivity.newInstance(mView.getCurContext(), type, isThirdOrSmsLogin));
                        break;
                    case AuthType.FORGET_PASSWORD:
                        mView.getCurContext().startActivity(MobileQuestionAuthActivity.newInstance(mView.getCurContext(), type));
                        break;

                }
            }
        });
        textView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gestureDialog.dismiss();
                switch (type) {
                    case AuthType.BIND_DEVICE:
                        mView.getCurContext().startActivity(IdentityAuthActivity.newInstance(mView.getCurContext(), type));
                        break;
                    case AuthType.FORGET_PASSWORD:
                        mView.getCurContext().startActivity(MobilePasswordAuthActivity.newInstance(mView.getCurContext(), type, false));
                        break;
                }

            }
        });
        switch (type) {
            case AuthType.BIND_DEVICE:
                textView3.setVisibility(isRealNme ? View.VISIBLE : View.GONE);
                ivw.setVisibility(isRealNme ? View.VISIBLE : View.GONE);
                textView1.setVisibility(setSecurityIssues ? View.VISIBLE : View.GONE);
                textView3.setText(mView.getCurContext().getString(R.string.identity_auth));
                textView1.setText(mView.getCurContext().getString(R.string.safe_question));
                if (isThirdOrSmsLogin) {
                    //第三方登录、验证码登录
                    textView2.setText(mView.getCurContext().getString(R.string.login_password));
                } else {
                    //密码登录
                    textView2.setText(mView.getCurContext().getString(R.string.mobile_code));
                }
                break;
            case AuthType.FORGET_PASSWORD:
                textView1.setVisibility(isRealNme ? View.VISIBLE : View.GONE);
                ivw1.setVisibility(isRealNme ? View.VISIBLE : View.GONE);
                textView2.setVisibility(setSecurityIssues ? View.VISIBLE : View.GONE);
                if (!isRealNme && !setSecurityIssues) {
                    textView3.setVisibility(View.VISIBLE);
                    ivw.setVisibility(View.VISIBLE);
                }
                textView1.setText(mView.getCurContext().getString(R.string.mobile_code).concat("+").concat(mView.getCurContext().getString(R.string.identity_auth)));
                textView2.setText(mView.getCurContext().getString(R.string.mobile_code).concat("+").concat(mView.getCurContext().getString(R.string.safe_question)));
                textView3.setText(mView.getCurContext().getString(R.string.mobile_code));
                break;
        }

        gestureDialog.show();
    }

}
