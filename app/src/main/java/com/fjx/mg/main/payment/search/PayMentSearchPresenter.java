package com.fjx.mg.main.payment.search;

import com.library.repository.core.net.CommonObserver;
import com.library.repository.core.net.RxScheduler;
import com.library.repository.models.QuestionListModel;
import com.library.repository.models.ResponseModel;
import com.library.repository.repository.RepositoryFactory;

class PayMentSearchPresenter extends PayMentSearchContract.Presenter {

    PayMentSearchPresenter(PayMentSearchContract.View view) {
        super(view);
    }


    @Override
    void getQuestionList(String title, String t, String p, String status, final int page) {
        RepositoryFactory.getRemoteJobApi().questionList(Integer.parseInt(status), p, t, title, "" + page)
                .compose(RxScheduler.<ResponseModel<QuestionListModel>>toMain())
                .as(mView.<ResponseModel<QuestionListModel>>bindAutoDispose())
                .subscribe(new CommonObserver<QuestionListModel>() {
                    @Override
                    public void onSuccess(QuestionListModel data) {
                        mView.showQuestionListModel(data);
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
