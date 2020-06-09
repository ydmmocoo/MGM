package com.fjx.mg.main.payment;

import com.library.common.base.BasePresenter;
import com.library.common.base.BaseView;
import com.library.common.view.dropmenu.DropMenuModel;
import com.library.repository.models.QuestionListDetailModel;

import java.util.List;

public interface PayMentContract {
    interface View extends BaseView {
        void showQuestionListModel(List<QuestionListDetailModel> data,Boolean hasnext);

        void loadError();
    }

    abstract class Presenter extends BasePresenter<PayMentContract.View> {
        public Presenter(View view) {
            super(view);
        }

        abstract void getQuestionList(String title, String t, String p, String status, int page);

        abstract List<DropMenuModel> getzHComDatalist();

        abstract List<DropMenuModel> getTabComDatalist();


        abstract List<DropMenuModel> getTabTimeDatalist();

        abstract List<DropMenuModel> getTabPriceDatalist();
    }
}
