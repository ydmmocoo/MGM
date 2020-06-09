package com.fjx.mg.main.payment;

import com.fjx.mg.R;
import com.library.common.view.dropmenu.DropMenuModel;
import com.library.repository.core.net.CommonObserver;
import com.library.repository.core.net.RxScheduler;
import com.library.repository.db.DBDaoFactory;
import com.library.repository.models.QuestionListModel;
import com.library.repository.models.ResponseModel;
import com.library.repository.repository.RepositoryFactory;

import java.util.ArrayList;
import java.util.List;

public class PayMentPresenter extends PayMentContract.Presenter {

    PayMentPresenter(PayMentContract.View view) {
        super(view);
    }


    @Override
    void getQuestionList(String title, String t, String p, String status, final int page) {

        if (!status.isEmpty()) {
            RepositoryFactory.getRemoteJobApi().questionList(Integer.parseInt(status), p, t, title, "" + page)
                    .compose(RxScheduler.<ResponseModel<QuestionListModel>>toMain())
                    .as(mView.<ResponseModel<QuestionListModel>>bindAutoDispose())
                    .subscribe(new CommonObserver<QuestionListModel>() {
                        @Override
                        public void onSuccess(QuestionListModel data) {
                            if (page == 1) {
                                DBDaoFactory.getQuestionListDao().deleteAll();
                            }
                            DBDaoFactory.getQuestionListDao().insertList(data.getQuestionList());
                            mView.showQuestionListModel(data.getQuestionList(), data.isHasNext());
                        }

                        @Override
                        public void onError(ResponseModel data) {
                            mView.loadError();
                        }

                        @Override
                        public void onNetError(ResponseModel data) {

                        }
                    });
        } else {
            RepositoryFactory.getRemoteJobApi().questionList(p, t, title, "" + page)
                    .compose(RxScheduler.<ResponseModel<QuestionListModel>>toMain())
                    .as(mView.<ResponseModel<QuestionListModel>>bindAutoDispose())
                    .subscribe(new CommonObserver<QuestionListModel>() {
                        @Override
                        public void onSuccess(QuestionListModel data) {
                            if (page == 1) {
                                DBDaoFactory.getQuestionListDao().deleteAll();
                            }
                            DBDaoFactory.getQuestionListDao().insertList(data.getQuestionList());
                            mView.showQuestionListModel(data.getQuestionList(), data.isHasNext());
                        }

                        @Override
                        public void onError(ResponseModel data) {
                            mView.loadError();
                        }

                        @Override
                        public void onNetError(ResponseModel data) {
                            mView.loadError();
                        }
                    });
        }


    }

    @Override
    List<DropMenuModel> getzHComDatalist() {
        List<DropMenuModel> com = new ArrayList<>();
        com.add(new DropMenuModel("", mView.getCurActivity().getString(R.string.synthesize), true, null));
        return com;
    }

    @Override
    List<DropMenuModel> getTabComDatalist() {
        List<DropMenuModel> com = new ArrayList<>();
        com.add(new DropMenuModel("", mView.getCurActivity().getString(R.string.all), true, null));
        com.add(new DropMenuModel("1", mView.getCurActivity().getString(R.string.going), false, null));
        com.add(new DropMenuModel("2", mView.getCurActivity().getString(R.string.finished), false, null));
        return com;
    }

    @Override
    List<DropMenuModel> getTabTimeDatalist() {
        List<DropMenuModel> time = new ArrayList<>();
        time.add(new DropMenuModel("", mView.getCurActivity().getString(R.string.default_text), true, null));
        time.add(new DropMenuModel("1", mView.getCurActivity().getString(R.string.time_decline), false, null));
        time.add(new DropMenuModel("2", mView.getCurActivity().getString(R.string.time_escalation), false, null));
        return time;
    }

    @Override
    List<DropMenuModel> getTabPriceDatalist() {
        List<DropMenuModel> price = new ArrayList<>();
        price.add(new DropMenuModel("", mView.getCurActivity().getString(R.string.default_text), true, null));
        price.add(new DropMenuModel("1", mView.getCurActivity().getString(R.string.price_decline), false, null));
        price.add(new DropMenuModel("2", mView.getCurActivity().getString(R.string.price_escalation), false, null));
        return price;
    }

}
