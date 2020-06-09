package com.fjx.mg.main.payment.questionceter.Fragment;

import com.library.common.base.BasePresenter;
import com.library.common.base.BaseView;
import com.library.common.view.dropmenu.DropMenuModel;
import com.library.repository.models.AdListModel;
import com.library.repository.models.JobConfigModel;
import com.library.repository.models.JobListModel;
import com.library.repository.models.MyQuestionListModel;
import com.library.repository.models.QuestionListModel;

import java.util.List;

public interface CenterContract {

    interface View extends BaseView {

        void showQuestionListModel(MyQuestionListModel data);

        void loadError();
    }

    abstract class Presenter extends BasePresenter<View> {

        Presenter(CenterContract.View view) {
            super(view);
        }

        abstract void getQuestionList(String status, int page);
    }
}
