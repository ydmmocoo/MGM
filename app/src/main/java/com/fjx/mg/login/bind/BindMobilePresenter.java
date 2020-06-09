package com.fjx.mg.login.bind;

import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;

import com.common.paylibrary.model.AliUserModel;
import com.fjx.mg.R;
import com.fjx.mg.utils.RegUtil;
import com.google.gson.JsonObject;
import com.library.common.utils.CommonToast;
import com.library.common.utils.JsonUtil;
import com.library.common.utils.StringUtil;
import com.library.repository.Constant;
import com.library.repository.core.net.CommonObserver;
import com.library.repository.core.net.RxScheduler;
import com.library.repository.data.UserCenter;
import com.library.repository.models.ResponseModel;
import com.library.repository.models.UserInfoModel;
import com.library.repository.repository.RepositoryFactory;
import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMFriendshipManager;
import com.tencent.imsdk.TIMUserProfile;
import com.tencent.imsdk.TIMValueCallBack;

import java.util.HashMap;
import java.util.Map;

import static com.tencent.imsdk.TIMUserProfile.TIM_PROFILE_TYPE_KEY_FACEURL;
import static com.tencent.imsdk.TIMUserProfile.TIM_PROFILE_TYPE_KEY_GENDER;
import static com.tencent.imsdk.TIMUserProfile.TIM_PROFILE_TYPE_KEY_LOCATION;
import static com.tencent.imsdk.TIMUserProfile.TIM_PROFILE_TYPE_KEY_NICK;

class BindMobilePresenter extends BindMobileContract.Presenter {

    private boolean isTimeStart;

    BindMobilePresenter(BindMobileContract.View view) {
        super(view);
    }

    @Override
    void bind(final String nickName, String sn, String phone, String smsCode, String psw, String openId, final String sex, final String avatar, String loginType, String confirmPwd) {

        if (TextUtils.isEmpty(phone)) {
            CommonToast.toast(mView.getCurContext().getString(R.string.login_input_phone));
            return;
        }
        if (TextUtils.isEmpty(smsCode)) {
            CommonToast.toast(mView.getCurContext().getString(R.string.login_input_smscode));
            return;
        }

//        if (TextUtils.isEmpty(mView.getSignPassword())) {
//            if (TextUtils.isEmpty(psw)) {
//                CommonToast.toast(mView.getCurContext().getString(R.string.login_password));
//                return;
//            } else if (!RegUtil.checkPassword(psw)) {
//                CommonToast.toast(mView.getCurContext().getString(R.string.wrong_password_pattern));
//                return;
//            }
//            if (!TextUtils.equals(confirmPwd, psw)) {
//                CommonToast.toast(mView.getCurContext().getString(R.string.error_confirm_pwd));
//                return;
//            }
//
//            psw = StringUtil.getPassword(psw);
//        } else {
//            psw = mView.getSignPassword();
//        }


        String deviceId = RepositoryFactory.getShareApi().getRegistrationId();
        String longitude = RepositoryFactory.getLocalRepository().getLongitude();
        String latitude = RepositoryFactory.getLocalRepository().getLatitude();
//        psw = StringUtil.getPassword(psw);
        mView.showLoading();
        RepositoryFactory.getRemoteAccountRepository().register(nickName, sn, phone, smsCode, psw, openId, loginType, sex, avatar, deviceId, longitude, latitude)
                .compose(RxScheduler.<ResponseModel<UserInfoModel>>toMain())
                .as(mView.<ResponseModel<UserInfoModel>>bindAutoDispose())
                .subscribe(new CommonObserver<UserInfoModel>() {
                    @Override
                    public void onSuccess(UserInfoModel data) {
//                        updateImUserImage(avatar, nickName, sex);
                        UserCenter.saveUserInfo(data);
                        UserCenter.bindLoginIm(avatar, nickName, StringUtil.isEmpty(sex) ? data.getUSex() : sex);
                        mView.hideLoading();
                        mView.bindSuccess(data);
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
    void getAliUserInfo(String code) {
        RepositoryFactory.getRemoteAccountRepository()
                .getAliUserInfo(code)
                .compose(RxScheduler.<ResponseModel<AliUserModel>>toMain())
                .as(mView.<ResponseModel<AliUserModel>>bindAutoDispose())
                .subscribe(new CommonObserver<AliUserModel>() {
                    @Override
                    public void onSuccess(AliUserModel data) {

                    }

                    @Override
                    public void onError(ResponseModel data) {

                    }

                    @Override
                    public void onNetError(ResponseModel data) {

                    }
                });

    }

    @Override
    void getWeixinUserInfo(String code) {
        RepositoryFactory.getRemoteAccountRepository()
                .getWeixinUserInfo(code)
                .compose(RxScheduler.<ResponseModel<Object>>toMain())
                .as(mView.<ResponseModel<Object>>bindAutoDispose())
                .subscribe(new CommonObserver<Object>() {
                    @Override
                    public void onSuccess(Object data) {

                    }

                    @Override
                    public void onError(ResponseModel data) {

                    }

                    @Override
                    public void onNetError(ResponseModel data) {

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
        mView.showLoading();
        timer.start();
        RepositoryFactory.getRemoteAccountRepository().sendSmsCode(sb.toString(), areaCode)
                .compose(RxScheduler.<ResponseModel<UserInfoModel>>toMain())
                .as(mView.<ResponseModel<UserInfoModel>>bindAutoDispose())
                .subscribe(new CommonObserver<UserInfoModel>() {
                    @Override
                    public void onSuccess(UserInfoModel data) {
                        mView.hideLoading();
                        CommonToast.toast(mView.getCurContext().getString(R.string.sms_send_success));
                        isTimeStart = true;
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

    @Override
    void releaseTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    @Override
    void getUser(String phone) {
        RepositoryFactory.getRemoteAccountRepository().getUser(phone, "")
                .compose(RxScheduler.<ResponseModel<JsonObject>>toMain())
                .as(mView.<ResponseModel<JsonObject>>bindAutoDispose())
                .subscribe(new CommonObserver<JsonObject>() {
                    @Override
                    public void onSuccess(JsonObject data) {
                        String pwd = data.get("psw").getAsString();
                        mView.showUser(pwd);
                        mView.sethasPass(true);
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        CommonToast.toast(mView.getCurContext().getString(R.string.error_net));
                        mView.getCurActivity().finish();
                        mView.sethasPass(true);
                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                        mView.hideLoading();
                        CommonToast.toast(data.getMsg());
                    }
                });

    }
}
