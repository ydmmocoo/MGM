package com.fjx.mg.me.safe_center.check.identity;

import com.library.common.base.BasePresenter;
import com.library.common.base.BaseView;
import com.library.repository.models.AuthQuestionModel;

import java.util.Map;

public interface IdentityAuthContract {


    interface View extends BaseView {

        void showAuthQuestionModel(AuthQuestionModel model);

        void checkSuccess();

        void bindDeviceSuccess();

    }

    abstract class Presenter extends BasePresenter<IdentityAuthContract.View> {

        Presenter(IdentityAuthContract.View view) {
            super(view);
        }
        abstract void getSecurityIssue();


        abstract void check(Map<String, Object> map);

        abstract void bindDevice();


    }
}
