package com.fjx.mg.job.detail;

import com.library.common.base.BasePresenter;
import com.library.common.base.BaseView;
import com.library.repository.models.JobListModel;
import com.library.repository.models.JobModel;
import com.tencent.imsdk.TIMUserProfile;
import com.tencent.imsdk.friendship.TIMFriend;

public interface JobDetailContract {

    interface View extends BaseView {


        void showJobDetaol(JobModel jobModel);

        void toggleCollectResult(boolean isCollect);

        void getImUserSuccess(TIMUserProfile profile);

        void getImUserSuccess(TIMFriend friend);

        JobModel getJobModel();

        void refreshData();

        void toggleCollect();

        void share();
    }

    abstract class Presenter extends BasePresenter<View> {

        Presenter(JobDetailContract.View view) {
            super(view);
        }

        abstract void getJobDetail(String jobId);

        abstract void toggleCollect(String jobId, boolean hasCollect);

        abstract void findImUser(String uid);

        abstract void showPublishDialog(android.view.View view, boolean isLove, boolean isShare);


    }
}
