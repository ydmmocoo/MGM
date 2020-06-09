package com.fjx.mg.me.safe_center.check.mobile_question;

import android.os.CountDownTimer;
import android.text.TextUtils;

import com.fjx.mg.R;
import com.library.common.constant.IntentConstants;
import com.library.common.utils.CommonToast;
import com.library.common.utils.SharedPreferencesUtil;
import com.library.common.utils.StringUtil;
import com.library.repository.core.net.CommonObserver;
import com.library.repository.core.net.RxScheduler;
import com.library.repository.data.UserCenter;
import com.library.repository.models.AuthQuestionModel;
import com.library.repository.models.ResponseModel;
import com.library.repository.models.UserInfoModel;
import com.library.repository.repository.RepositoryFactory;

import java.util.Map;

public class MobileQuestionAuthPresenter extends MobileQuestionAuthContract.Presenter {

    private boolean isTimeStart;

    MobileQuestionAuthPresenter(MobileQuestionAuthContract.View view) {
        super(view);
    }

    @Override
    void sendSmsCode() {
        if (isTimeStart) return;
        String mobile = UserCenter.getUserInfo().getPhone();
        String areaCode = SharedPreferencesUtil.name(IntentConstants.SP_CODE).getString(IntentConstants.CODE, "261");

        if (TextUtils.isEmpty(areaCode)) {
            areaCode = UserCenter.getUserInfo().getSn();
        }
        timer.start();
        isTimeStart = true;
        mView.showLoading();
        RepositoryFactory.getRemoteAccountRepository().sendSmsCode(mobile, areaCode)
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
    void getSecurityIssue() {
        mView.showLoading();
        RepositoryFactory.getRemoteAccountRepository().getSecurityIssue()
                .compose(RxScheduler.<ResponseModel<AuthQuestionModel>>toMain())
                .as(mView.<ResponseModel<AuthQuestionModel>>bindAutoDispose())
                .subscribe(new CommonObserver<AuthQuestionModel>() {
                    @Override
                    public void onSuccess(AuthQuestionModel data) {
                        mView.hideLoading();
                        mView.showAuthQuestionModel(data);
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
    void check(Map<String, Object> map) {
        mView.showLoading();
        RepositoryFactory.getRemoteAccountRepository().check(map)
                .compose(RxScheduler.<ResponseModel<Object>>toMain())
                .as(mView.<ResponseModel<Object>>bindAutoDispose())
                .subscribe(new CommonObserver<Object>() {
                    @Override
                    public void onSuccess(Object data) {
                        mView.hideLoading();
                        mView.checkSuccess();
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
    void bindDevice() {
        mView.showLoading();
        String phone = UserCenter.getUserInfo().getPhone();
        RepositoryFactory.getRemoteAccountRepository().bindDevice(phone)
                .compose(RxScheduler.<ResponseModel<Object>>toMain())
                .as(mView.<ResponseModel<Object>>bindAutoDispose())
                .subscribe(new CommonObserver<Object>() {
                    @Override
                    public void onSuccess(Object data) {
                        mView.hideLoading();
                        mView.bindDeviceSuccess();
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
