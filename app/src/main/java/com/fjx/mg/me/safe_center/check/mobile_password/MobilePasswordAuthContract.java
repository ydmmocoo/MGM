package com.fjx.mg.me.safe_center.check.mobile_password;

import com.library.common.base.BasePresenter;
import com.library.common.base.BaseView;

import java.util.Map;

public interface MobilePasswordAuthContract {


    interface View extends BaseView {

        void showTimeCount(String s);

        void checkSuccess();

        void bindDeviceSuccess();
    }

    abstract class Presenter extends BasePresenter<MobilePasswordAuthContract.View> {

        Presenter(MobilePasswordAuthContract.View view) {
            super(view);
        }

        abstract void sendSmsCode();

        abstract void check(Map<String, Object> map);

        abstract void releaseTimer();

        abstract void bindDevice();

    }
}
