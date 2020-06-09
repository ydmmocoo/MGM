package com.fjx.mg.me.safe_center.check.identity_mobile;

import com.library.common.base.BasePresenter;
import com.library.common.base.BaseView;

import java.util.Map;

public interface IdentityMobileAuthContract {


    interface View extends BaseView {


        void checkSuccess();

        void showTimeCount(String s);
    }

    abstract class Presenter extends BasePresenter<IdentityMobileAuthContract.View> {

        Presenter(IdentityMobileAuthContract.View view) {
            super(view);
        }


        abstract void check(Map<String, Object> map);
        abstract void sendSmsCode();
        abstract void releaseTimer();

    }
}
