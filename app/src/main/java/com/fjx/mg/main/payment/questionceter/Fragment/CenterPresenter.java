package com.fjx.mg.main.payment.questionceter.Fragment;

import com.library.repository.core.net.CommonObserver;
import com.library.repository.core.net.RxScheduler;
import com.library.repository.models.MyQuestionListModel;
import com.library.repository.models.ResponseModel;
import com.library.repository.repository.RepositoryFactory;

class CenterPresenter extends CenterContract.Presenter {


    CenterPresenter(CenterContract.View view) {
        super(view);
    }

    @Override
    void getQuestionList( String status, final int page) {
        RepositoryFactory.getRemoteJobApi().myquestionList(Integer.parseInt(status), "" + page)
                .compose(RxScheduler.<ResponseModel<MyQuestionListModel>>toMain())
                .as(mView.<ResponseModel<MyQuestionListModel>>bindAutoDispose())
                .subscribe(new CommonObserver<MyQuestionListModel>() {
                    @Override
                    public void onSuccess(MyQuestionListModel data) {
                        if (page == 1) {
//                            DBDaoFactory.getHouseDetailDao().deleteAll(htype);
                        }
                        mView.showQuestionListModel(data);
//                        DBDaoFactory.getHouseDetailDao().insertList(data.getHouseList());
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
