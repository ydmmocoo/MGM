package com.fjx.mg.me.safe_center.bind;

import com.library.common.base.BasePresenter;
import com.library.common.base.BaseView;

public interface RebindMobileContract {


    interface View extends BaseView {
        void setMobile(String text);
        void showTimeCount(String s);

        void reBindSuccess();

    }

    abstract class Presenter extends BasePresenter<RebindMobileContract.View> {

        Presenter(RebindMobileContract.View view) {
            super(view);
        }

        abstract void sendSmsCode(String areaCode, String mobile);

        abstract void releaseTimer();

        abstract void reBindMobile(String phone, String areaCode, String smsCode);

    }
}
