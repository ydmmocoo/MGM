package com.fjx.mg.me.safe_center.bind;

import android.os.CountDownTimer;
import android.text.TextUtils;

import com.fjx.mg.R;
import com.library.common.utils.CommonToast;
import com.library.common.utils.StringUtil;
import com.library.repository.core.net.CommonObserver;
import com.library.repository.core.net.RxScheduler;
import com.library.repository.data.UserCenter;
import com.library.repository.models.ResponseModel;
import com.library.repository.models.UserInfoModel;
import com.library.repository.repository.RepositoryFactory;

public class RebindMobilePresenter extends RebindMobileContract.Presenter {

    private boolean isTimeStart;

    RebindMobilePresenter(RebindMobileContract.View view) {
        super(view);
    }

    @Override
    void sendSmsCode(String areaCode, String mobiles) {
        StringBuffer sb = new StringBuffer();
        sb.append(mobiles);
        if (TextUtils.isEmpty(mobiles)) {
            CommonToast.toast(mView.getCurContext().getString(R.string.login_input_phone));
            return;
        }
        if (areaCode.equals("261")) {
            if (sb.toString().startsWith("32")) {
            } else if (sb.toString().startsWith("33")) {
            } else if (sb.toString().startsWith("34")) {
            } else {
                CommonToast.toast(R.string.rebind_moblie_tips);
            }
        }
        if (isTimeStart) return;
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
                        if (StringUtil.isNotEmpty(data.getMsg())) {
                            CommonToast.toast(data.getMsg());
                        }
                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                        mView.hideLoading();
                        if (StringUtil.isNotEmpty(data.getMsg())) {
                            CommonToast.toast(data.getMsg());
                        }
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
    void reBindMobile(final String phone, String areaCode, String smsCode) {
        if (TextUtils.isEmpty(phone)) {
            CommonToast.toast(mView.getCurContext().getString(R.string.login_input_phone));
            return;
        }

        if (TextUtils.isEmpty(smsCode)) {
            CommonToast.toast(mView.getCurContext().getString(R.string.login_input_smscode));
            return;
        }

        mView.showLoading();
        RepositoryFactory.getRemoteAccountRepository().modifyPhone(phone, areaCode, smsCode)
                .compose(RxScheduler.<ResponseModel<Object>>toMain())
                .as(mView.<ResponseModel<Object>>bindAutoDispose())
                .subscribe(new CommonObserver<Object>() {
                    @Override
                    public void onSuccess(Object data) {
                        UserInfoModel infoModel = UserCenter.getUserInfo();
                        infoModel.setPhone(phone);
                        UserCenter.saveUserInfo(infoModel);
                        mView.hideLoading();
                        mView.reBindSuccess();

                    }

                    @Override
                    public void onError(ResponseModel data) {
                        if (StringUtil.isNotEmpty(data.getMsg())) {
                            CommonToast.toast(data.getMsg());
                        }
                        mView.hideLoading();
                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                        if (StringUtil.isNotEmpty(data.getMsg())) {
                            CommonToast.toast(data.getMsg());
                        }
                        mView.hideLoading();
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
