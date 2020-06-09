package com.fjx.mg.setting.password.pay;

import com.library.common.base.BasePresenter;
import com.library.common.base.BaseView;

public interface ModifyPayPwdContract {

    interface View extends BaseView {


        void showTimeCount(String s);

        void modifySuccess();
    }

    abstract class Presenter extends BasePresenter<View> {

        public Presenter(View view) {
            super(view);
        }

        abstract void sendSmsCode(String mobile, String areaCode);


        abstract void setPayPassword(String smsCode, String newPwd, String confirmPsw);

        abstract void modifyPayPassword(String smsCode, String newPwd, String confirmPsw, String oldPwd);

        abstract void releaseTimer();

    }

}

