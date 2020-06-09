package com.fjx.mg.login.register;

import android.os.CountDownTimer;
import android.text.TextUtils;

import com.fjx.mg.R;
import com.fjx.mg.utils.RegUtil;
import com.library.common.utils.CommonToast;
import com.library.common.utils.StringUtil;
import com.library.repository.core.net.CommonObserver;
import com.library.repository.core.net.RxScheduler;
import com.library.repository.data.UserCenter;
import com.library.repository.models.ResponseModel;
import com.library.repository.models.UserInfoModel;
import com.library.repository.repository.RepositoryFactory;

class RegisterPresenter extends RegisterContract.Presenter {

    private boolean isTimeStart;

    RegisterPresenter(RegisterContract.View view) {
        super(view);
    }

    @Override
    void register(String nickName, String sn, String phone, String smsCode, String psw, String confirmPwd) {
//        if (TextUtils.isEmpty(nickName)) {
//            CommonToast.toast(mView.getCurContext().getString(R.string.register_input_nickname));
//            return;
//        }

        if (TextUtils.isEmpty(phone)) {
            CommonToast.toast(mView.getCurContext().getString(R.string.login_input_phone));
            return;
        }
        if (TextUtils.isEmpty(smsCode)) {
            CommonToast.toast(mView.getCurContext().getString(R.string.login_input_smscode));
            return;
        }
//        if (TextUtils.isEmpty(psw)) {
//            CommonToast.toast(mView.getCurContext().getString(R.string.login_input_password));
//            return;
//        }
//        if (TextUtils.isEmpty(confirmPwd)) {
//            CommonToast.toast(mView.getCurContext().getString(R.string.hint_input_confirm_password));
//            return;
//        }
//        if (!TextUtils.isEmpty(psw)) {
//            if (!RegUtil.checkPassword(psw)) {
//                CommonToast.toast(mView.getCurContext().getString(R.string.wrong_password_pattern));
//                return;
//            }
//
//            if (!TextUtils.equals(confirmPwd, psw)) {
//                CommonToast.toast(mView.getCurContext().getString(R.string.error_confirm_pwd));
//                return;
//            }
//            psw = StringUtil.getPassword(psw);
//        }


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
//                        UserCenter.loginTim();
                        UserCenter.imLogin();
                        mView.hideLoading();
                        mView.registerSuccess(data);
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
                        timer.cancel();
                        isTimeStart = false;
                        mView.showTimeCount(mView.getCurContext().getString(R.string.getSmscode));
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
    void registerPro() {
        RepositoryFactory.getRemoteRepository().registerPro()
                .compose(RxScheduler.<ResponseModel<String>>toMain())
                .as(mView.<ResponseModel<String>>bindAutoDispose())
                .subscribe(new CommonObserver<String>() {
                    @Override
                    public void onSuccess(String data) {
                        mView.showRegisterPro(data);
                    }

                    @Override
                    public void onError(ResponseModel data) {

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

}
