package com.fjx.mg.moments.all;

import com.library.repository.core.net.CommonObserver;
import com.library.repository.core.net.RxScheduler;
import com.library.repository.models.PersonalMomentListModel;
import com.library.repository.models.ResponseModel;
import com.library.repository.repository.RepositoryFactory;

class AllMomentsPresenter extends AllMomentsContract.Presenter {

    AllMomentsPresenter(AllMomentsContract.View view) {
        super(view);
    }

    @Override
    void personalMomentList(String identifier, int page) {
        RepositoryFactory.getRemoteRepository()
                .personalMomentList(identifier, page)
                .compose(RxScheduler.<ResponseModel<PersonalMomentListModel>>toMain())
                .as(mView.<ResponseModel<PersonalMomentListModel>>bindAutoDispose())
                .subscribe(new CommonObserver<PersonalMomentListModel>() {
                    @Override
                    public void onSuccess(PersonalMomentListModel data) {
                        if (mView != null && data != null) {
                            mView.showPersonalMomentList(data);
                        }
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
