package com.fjx.mg.setting.password.pay;

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

public class ModifyPayPwdPresenter1 extends ModifyPayPwdContract.Presenter {

    private boolean isTimeStart;

    public ModifyPayPwdPresenter1(ModifyPayPwdContract.View view) {
        super(view);
    }

    @Override
    public void sendSmsCode(String mobile, String areaCode) {
        if (isTimeStart) return;
        timer.start();
        isTimeStart = true;
        mView.createAndShowDialog();
        RepositoryFactory.getRemoteAccountRepository().sendSmsCode(mobile, areaCode)
                .compose(RxScheduler.<ResponseModel<UserInfoModel>>toMain())
                .as(mView.<ResponseModel<UserInfoModel>>bindAutoDispose())
                .subscribe(new CommonObserver<UserInfoModel>() {
                    @Override
                    public void onSuccess(UserInfoModel data) {
                        mView.destoryAndDismissDialog();
                        CommonToast.toast(mView.getCurContext().getString(R.string.sms_send_success));

                    }

                    @Override
                    public void onError(ResponseModel data) {
                        mView.destoryAndDismissDialog();
                        CommonToast.toast(data.getMsg());
                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                        mView.destoryAndDismissDialog();
                        CommonToast.toast(data.getMsg());
                    }
                });
    }

    @Override
    void setPayPassword(String smsCode, String newPwd, String confirmPsw) {
//        if (TextUtils.isEmpty(smsCode)) {
//            CommonToast.toast(mView.getCurContext().getString(R.string.login_input_smscode));
//            return;
//        }

        if (TextUtils.isEmpty(newPwd)) {
            CommonToast.toast(mView.getCurContext().getString(R.string.set_new_password));
            return;
        }

        if (TextUtils.isEmpty(confirmPsw)) {
            CommonToast.toast(mView.getCurContext().getString(R.string.hint_input_confirm_password));
            return;
        }

        if (newPwd.length() != 6) {
            CommonToast.toast(mView.getCurContext().getString(R.string.hint_pay_password_length));
            return;
        }

        if (!TextUtils.equals(newPwd, confirmPsw)) {
            CommonToast.toast(mView.getCurContext().getString(R.string.error_confirm_pwd));
            return;
        }


        mView.createAndShowDialog();
        newPwd = StringUtil.getPassword(newPwd);
        RepositoryFactory.getRemoteAccountRepository().setPayPassword(newPwd)
                .compose(RxScheduler.<ResponseModel<Object>>toMain())
                .as(mView.<ResponseModel<Object>>bindAutoDispose())
                .subscribe(new CommonObserver<Object>() {
                    @Override
                    public void onSuccess(Object data) {

                        //修改本地状态
                        UserInfoModel model = UserCenter.getUserInfo();
                        model.setIsSetPayPsw(1);
                        UserCenter.saveUserInfo(model);
                        CommonToast.toast(mView.getCurContext().getString(R.string.set_success));
                        mView.destoryAndDismissDialog();
                        mView.modifySuccess();
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        CommonToast.toast(data.getMsg());
                        mView.destoryAndDismissDialog();

                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                        mView.destoryAndDismissDialog();
                        CommonToast.toast(data.getMsg());
                    }
                });
    }

    @Override
    void modifyPayPassword(String smsCode, String newPwd, String confirmPsw, String oldPwd) {
        if (TextUtils.isEmpty(smsCode)) {
            CommonToast.toast(mView.getCurContext().getString(R.string.login_input_smscode));
            return;
        }
        if (TextUtils.isEmpty(oldPwd)) {
            CommonToast.toast(mView.getCurContext().getString(R.string.hint_input_oldpassword));
            return;
        }
        if (TextUtils.isEmpty(newPwd)) {
            CommonToast.toast(mView.getCurContext().getString(R.string.set_new_password));
            return;
        }

        if (TextUtils.isEmpty(confirmPsw)) {
            CommonToast.toast(mView.getCurContext().getString(R.string.hint_input_confirm_password));
            return;
        }
        if (newPwd.length() != 6) {
            CommonToast.toast(mView.getCurContext().getString(R.string.hint_pay_password_length));
            return;
        }


        mView.createAndShowDialog();
        oldPwd = StringUtil.getPassword(oldPwd);
        newPwd = StringUtil.getPassword(newPwd);
        confirmPsw = StringUtil.getPassword(confirmPsw);
        RepositoryFactory.getRemoteAccountRepository().modifyPayPassword(smsCode, newPwd, confirmPsw, oldPwd)
                .compose(RxScheduler.<ResponseModel<Object>>toMain())
                .as(mView.<ResponseModel<Object>>bindAutoDispose())
                .subscribe(new CommonObserver<Object>() {
                    @Override
                    public void onSuccess(Object data) {
                        CommonToast.toast(mView.getCurContext().getString(R.string.set_success));
                        UserInfoModel model = UserCenter.getUserInfo();
                        model.setIsSetPayPsw(1);
                        UserCenter.saveUserInfo(model);
                        mView.destoryAndDismissDialog();
                        mView.modifySuccess();
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        CommonToast.toast(data.getMsg());
                        mView.destoryAndDismissDialog();

                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                        mView.destoryAndDismissDialog();
                        CommonToast.toast(data.getMsg());
                    }
                });
    }


    private CountDownTimer timer = new CountDownTimer(60000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            long second = millisUntilFinished / 1000;
            mView.showTimeCount(second + "stroke");
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

}
