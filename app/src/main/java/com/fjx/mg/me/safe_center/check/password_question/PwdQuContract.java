package com.fjx.mg.me.safe_center.check.password_question;

import com.library.common.base.BasePresenter;
import com.library.common.base.BaseView;
import com.library.repository.models.AuthQuestionModel;

import java.util.Map;

public interface PwdQuContract {


    interface View extends BaseView {

        void showAuthQuestionModel(AuthQuestionModel model);

        void checkSuccess();
    }

    abstract class Presenter extends BasePresenter<PwdQuContract.View> {

        Presenter(PwdQuContract.View view) {
            super(view);
        }

        abstract void getSecurityIssue();

        abstract void check(Map<String, Object> map);


    }
}
