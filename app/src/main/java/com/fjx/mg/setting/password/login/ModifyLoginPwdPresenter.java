package com.fjx.mg.setting.password.login;

import android.os.CountDownTimer;
import android.text.TextUtils;

import com.fjx.mg.R;
import com.fjx.mg.utils.RegUtil;
import com.library.common.utils.CommonToast;
import com.library.common.utils.StringUtil;
import com.library.repository.core.net.CommonObserver;
import com.library.repository.core.net.RxScheduler;
import com.library.repository.models.ResponseModel;
import com.library.repository.models.UserInfoModel;
import com.library.repository.repository.RepositoryFactory;

import java.lang.ref.WeakReference;

public class ModifyLoginPwdPresenter extends ModifyLoginPwdContract.Presenter {

    private boolean isTimeStart;
    private WeakReference<ModifyLoginPwdContract.View> weakReference;

    public ModifyLoginPwdPresenter(ModifyLoginPwdContract.View view) {
        super(view);
        weakReference = new WeakReference<>(view);
    }

    @Override
    public void sendSmsCode(String mobile, String areaCode) {
        final ModifyLoginPwdContract.View view = weakReference.get() == null ? mView : weakReference.get();
        if (isTimeStart) return;
        timer.start();
        isTimeStart = true;
        if (view == null) return;
        mView.createAndShowDialog();
        RepositoryFactory.getRemoteAccountRepository().sendSmsCode(mobile, areaCode)
                .compose(RxScheduler.<ResponseModel<UserInfoModel>>toMain())
                .as(view.<ResponseModel<UserInfoModel>>bindAutoDispose())
                .subscribe(new CommonObserver<UserInfoModel>() {
                    @Override
                    public void onSuccess(UserInfoModel data) {
                        if (view != null) {
                            view.destoryAndDismissDialog();
                        }
                        CommonToast.toast(mView.getCurContext().getString(R.string.sms_send_success));

                    }

                    @Override
                    public void onError(ResponseModel data) {
                        if (view != null)
                            view.destoryAndDismissDialog();
                        CommonToast.toast(data.getMsg());
                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                        CommonToast.toast(data.getMsg());
                        if (mView != null)
                            mView.destoryAndDismissDialog();
                    }
                });
    }

    @Override
    void modifyPassword(String smsCode, String oldPsw, String newPwd, String confirmPsw) {
        if (TextUtils.isEmpty(smsCode)) {
            CommonToast.toast(mView.getCurContext().getString(R.string.login_input_smscode));
            return;
        }

        if (TextUtils.isEmpty(oldPsw)) {
            CommonToast.toast(mView.getCurContext().getString(R.string.hint_input_oldpassword));
            return;
        }
        if (TextUtils.isEmpty(newPwd)) {
            CommonToast.toast(mView.getCurContext().getString(R.string.set_new_password));
            return;
        }

        if (!RegUtil.checkPassword(newPwd)) {
            CommonToast.toast(mView.getCurContext().getString(R.string.wrong_password_pattern));
            return;
        }


        if (TextUtils.isEmpty(confirmPsw)) {
            CommonToast.toast(mView.getCurContext().getString(R.string.reinput_password));
            return;
        }

        if (!TextUtils.equals(newPwd, confirmPsw)) {
            CommonToast.toast(mView.getCurContext().getString(R.string.error_confirm_pwd));
            return;
        }

        if (mView == null) return;
        mView.createAndShowDialog();
        oldPsw = StringUtil.getPassword(oldPsw);
        newPwd = StringUtil.getPassword(newPwd);
        confirmPsw = StringUtil.getPassword(confirmPsw);
        RepositoryFactory.getRemoteAccountRepository().modifyPassword(smsCode, oldPsw, newPwd, confirmPsw)
                .compose(RxScheduler.<ResponseModel<Object>>toMain())
                .as(mView.<ResponseModel<Object>>bindAutoDispose())
                .subscribe(new CommonObserver<Object>() {
                    @Override
                    public void onSuccess(Object data) {
                        CommonToast.toast(mView.getCurContext().getString(R.string.edit_success));
                        if (mView != null) {
                            mView.destoryAndDismissDialog();
                            mView.modifySuccess();
                        }
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        CommonToast.toast(data.getMsg());
                        if (mView != null)
                            mView.destoryAndDismissDialog();

                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                        CommonToast.toast(data.getMsg());
                        if (mView != null)
                            mView.destoryAndDismissDialog();
                    }
                });
    }

    @Override
    void resetPassword(String newPwd, String confirmPsw) {
        if (TextUtils.isEmpty(newPwd)) {
            CommonToast.toast(mView.getCurContext().getString(R.string.set_new_password));
            return;
        }
        if (TextUtils.isEmpty(confirmPsw)) {
            CommonToast.toast(mView.getCurContext().getString(R.string.reinput_password));
            return;
        }

        if (!TextUtils.equals(newPwd, confirmPsw)) {
            CommonToast.toast(mView.getCurContext().getString(R.string.error_confirm_pwd));
            return;
        }
        newPwd = StringUtil.getPassword(newPwd);

        RepositoryFactory.getRemoteAccountRepository().fogotPassword(newPwd)
                .compose(RxScheduler.<ResponseModel<Object>>toMain())
                .as(mView.<ResponseModel<Object>>bindAutoDispose())
                .subscribe(new CommonObserver<Object>() {
                    @Override
                    public void onSuccess(Object data) {
                        CommonToast.toast(mView.getCurContext().getString(R.string.edit_success));
                        if (mView != null) {
                            mView.destoryAndDismissDialog();
                            mView.modifySuccess();
                        }
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        CommonToast.toast(data.getMsg());
                        if (mView != null)
                            mView.destoryAndDismissDialog();

                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                        CommonToast.toast(data.getMsg());
                        if (mView != null)
                            mView.destoryAndDismissDialog();
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

}
