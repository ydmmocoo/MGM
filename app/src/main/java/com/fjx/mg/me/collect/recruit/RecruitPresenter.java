package com.fjx.mg.me.collect.recruit;


import com.library.repository.core.net.CommonObserver;
import com.library.repository.core.net.RxScheduler;
import com.library.repository.models.JobListModel;
import com.library.repository.models.ResponseModel;
import com.library.repository.repository.RepositoryFactory;

import java.util.ArrayList;
import java.util.List;

public class RecruitPresenter extends RecruitContract.Presenter {

    RecruitPresenter(RecruitContract.View view) {
        super(view);
    }


    @Override
    void getMyCollectJob(int page) {
        RepositoryFactory.getRemoteJobApi().myCollect(page)
                .compose(RxScheduler.<ResponseModel<JobListModel>>toMain())
                .as(mView.<ResponseModel<JobListModel>>bindAutoDispose())
                .subscribe(new CommonObserver<JobListModel>() {
                    @Override
                    public void onSuccess(JobListModel data) {
                        mView.showCollectData(data);
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
