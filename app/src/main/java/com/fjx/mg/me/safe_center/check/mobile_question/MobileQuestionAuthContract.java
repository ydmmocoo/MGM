package com.fjx.mg.me.safe_center.check.mobile_question;

import com.library.common.base.BasePresenter;
import com.library.common.base.BaseView;
import com.library.repository.models.AuthQuestionModel;

import java.util.Map;

public interface MobileQuestionAuthContract {


    interface View extends BaseView {
        void showAuthQuestionModel(AuthQuestionModel model);


        void showTimeCount(String s);

        void checkSuccess();

        void bindDeviceSuccess();
    }

    abstract class Presenter extends BasePresenter<MobileQuestionAuthContract.View> {

        Presenter(MobileQuestionAuthContract.View view) {
            super(view);
        }


        abstract void getSecurityIssue();


        abstract void check(Map<String, Object> map);

        abstract void sendSmsCode();

        abstract void releaseTimer();

        abstract void bindDevice();
    }
}
