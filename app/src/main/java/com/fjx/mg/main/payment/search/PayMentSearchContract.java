package com.fjx.mg.main.payment.search;

import com.library.common.base.BasePresenter;
import com.library.common.base.BaseView;
import com.library.common.view.dropmenu.DropMenuModel;
import com.library.repository.models.QuestionListModel;

import java.util.List;

public interface PayMentSearchContract {
    interface View extends BaseView {
        void showQuestionListModel(QuestionListModel data);

        void loadError();
    }

    abstract class Presenter extends BasePresenter<PayMentSearchContract.View> {
        public Presenter(View view) {
            super(view);
        }

        abstract void getQuestionList(String title, String t, String p, String status, int page);


    }
}
