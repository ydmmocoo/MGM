package com.fjx.mg.login.register;

import com.library.common.base.BasePresenter;
import com.library.common.base.BaseView;
import com.library.repository.models.UserInfoModel;

public interface RegisterContract {

    interface View extends BaseView {

        void registerSuccess(UserInfoModel data);

        void showTimeCount(String text);

        void showRegisterPro(String text);

        void setMobile(String text);
    }

    abstract class Presenter extends BasePresenter<RegisterContract.View> {

        Presenter(RegisterContract.View view) {
            super(view);
        }

        abstract void register(String nickName, String sn, String phone, String smsCode, String psw, String confirmPwd);

        abstract void sendSmsCode(String mobile, String areaCode);

        abstract void releaseTimer();

        abstract void registerPro();
    }

}
