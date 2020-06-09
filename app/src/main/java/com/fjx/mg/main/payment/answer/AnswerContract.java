package com.fjx.mg.main.payment.answer;

import com.library.common.base.BasePresenter;
import com.library.common.base.BaseView;

import java.util.List;

public interface AnswerContract {
    interface View extends BaseView {

        void feedbackSuccess();
    }

    abstract class Presenter extends BasePresenter<AnswerContract.View> {
        public Presenter(View view) {
            super(view);
        }

        abstract void updateImage(String qId, String content, final List<String> filePaths);


    }
}
