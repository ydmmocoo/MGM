package com.fjx.mg.me.safe_center.question;

import com.library.common.base.BasePresenter;
import com.library.common.base.BaseView;

public interface QuestionSetContract {


    interface View extends BaseView {

        void setProblemSuccess();


    }

    abstract class Presenter extends BasePresenter<QuestionSetContract.View> {

        Presenter(QuestionSetContract.View view) {
            super(view);
        }

        abstract void setProblem(String question, String answer);


    }
}
