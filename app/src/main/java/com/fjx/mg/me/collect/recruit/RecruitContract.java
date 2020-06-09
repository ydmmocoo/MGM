package com.fjx.mg.me.collect.recruit;

import com.library.common.base.BasePresenter;
import com.library.common.base.BaseView;
import com.library.repository.models.JobListModel;

import java.util.List;

public interface RecruitContract {
    interface View extends BaseView {

        void showCollectData(JobListModel model);

        void loadError();

    }

    abstract class Presenter extends BasePresenter<RecruitContract.View> {

        Presenter(RecruitContract.View view) {
            super(view);
        }

        abstract void getMyCollectJob(int page);

    }

}
