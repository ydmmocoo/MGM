package com.fjx.mg.setting.password.login;

import com.library.common.base.BasePresenter;
import com.library.common.base.BaseView;

public interface ModifyLoginPwdContract {

    interface View extends BaseView {


        void showTimeCount(String s);

        void modifySuccess();
    }

    abstract class Presenter extends BasePresenter<View> {

        public Presenter(View view) {
            super(view);
        }

        abstract void sendSmsCode(String mobile, String areaCode);


        abstract void modifyPassword(String smsCode,String oldPsw, String newPwd, String confirmPsw);


        abstract void resetPassword(String newPwd, String confirmPsw);

        abstract void releaseTimer();

    }

}

