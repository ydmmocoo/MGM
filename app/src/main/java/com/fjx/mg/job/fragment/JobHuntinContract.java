package com.fjx.mg.job.fragment;

import com.library.common.base.BasePresenter;
import com.library.common.base.BaseView;
import com.library.common.view.dropmenu.DropMenuModel;
import com.library.repository.models.AdListModel;
import com.library.repository.models.JobConfigModel;
import com.library.repository.models.JobListModel;

import java.util.List;

public interface JobHuntinContract {

    interface View extends BaseView {

        void showJobListModel(JobListModel data);

        void showConfigTab(JobConfigModel configModel);

        void loadError();

        void showBanners(AdListModel data);
    }

    abstract class Presenter extends BasePresenter<View> {

        Presenter(JobHuntinContract.View view) {
            super(view);
        }

        abstract void getConfig();

        abstract void getJobList(int page, int type, String typeId, String payId, String workId, String educationId);


        public abstract List<DropMenuModel> getTab1Datalist();

        public abstract List<DropMenuModel> getTab2Datalist();

        public abstract List<DropMenuModel> getTab3Datalist();

        public abstract List<DropMenuModel> getTab4Datalist();

        abstract void getAd();
    }
}
