package com.fjx.mg.job.fragment;

import com.fjx.mg.R;
import com.library.common.view.dropmenu.DropMenuModel;
import com.library.repository.core.net.CommonObserver;
import com.library.repository.core.net.RxScheduler;
import com.library.repository.db.DBDaoFactory;
import com.library.repository.models.AdListModel;
import com.library.repository.models.JobConfigModel;
import com.library.repository.models.JobListModel;
import com.library.repository.models.JobModel;
import com.library.repository.models.ResponseModel;
import com.library.repository.repository.RepositoryFactory;

import java.util.ArrayList;
import java.util.List;

public class JobHuntinPresenter extends JobHuntinContract.Presenter {

    private JobConfigModel configModel;

    JobHuntinPresenter(JobHuntinContract.View view) {
        super(view);
        configModel = RepositoryFactory.getLocalRepository().getJobConfig();
    }

    @Override
    void getConfig() {
        RepositoryFactory.getRemoteJobApi().getConf()
                .compose(RxScheduler.<ResponseModel<JobConfigModel>>toMain())
                .as(mView.<ResponseModel<JobConfigModel>>bindAutoDispose())
                .subscribe(new CommonObserver<JobConfigModel>() {
                    @Override
                    public void onSuccess(JobConfigModel data) {
                        RepositoryFactory.getLocalRepository().saveJobConfig(data);
                        configModel = data;
                        mView.showConfigTab(configModel);
                    }

                    @Override
                    public void onError(ResponseModel data) {

                    }

                    @Override
                    public void onNetError(ResponseModel data) {

                    }
                });

    }

    @Override
    void getJobList(final int page, final int type, String typeId, String payId, String workId, String educationId) {
        RepositoryFactory.getRemoteJobApi().jobsList(page, type, typeId, payId, workId, educationId)
                .compose(RxScheduler.<ResponseModel<JobListModel>>toMain())
                .as(mView.<ResponseModel<JobListModel>>bindAutoDispose())
                .subscribe(new CommonObserver<JobListModel>() {
                    @Override
                    public void onSuccess(JobListModel data) {
                        if (page == 1) {
                            DBDaoFactory.getJobListModelDao().deleteAll(type);
                        }

                        mView.showJobListModel(data);
                        for (JobModel item : data.getJobsList()) {
                            item.setType(type);
                        }
                        DBDaoFactory.getJobListModelDao().insertList(data.getJobsList());
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        mView.loadError();
//                        CommonToast.toast(data.getMsg());
                    }

                    @Override
                    public void onNetError(ResponseModel data) {

                    }
                });

    }

    @Override
    public List<DropMenuModel> getTab1Datalist() {
        List<DropMenuModel> dataList = new ArrayList<>();
        dataList.add(new DropMenuModel("", mView.getCurContext().getString(R.string.unlimited), true, null));
        if (configModel != null) {
            List<JobConfigModel.JobTypesBean> jobTypes = configModel.getJobTypes();
            if (jobTypes == null) return dataList;
            for (JobConfigModel.JobTypesBean b : jobTypes) {
                dataList.add(new DropMenuModel(b.getTypeId(), b.getName(), false, null));
            }
        }
        return dataList;
    }

    @Override
    public List<DropMenuModel> getTab2Datalist() {
        List<DropMenuModel> dataList = new ArrayList<>();
        dataList.add(new DropMenuModel("", mView.getCurContext().getString(R.string.unlimited), true, null));
        if (configModel != null) {
            List<JobConfigModel.PayTypeBean> payTypes = configModel.getPayType();
            if (payTypes == null) return dataList;
            for (JobConfigModel.PayTypeBean b : payTypes) {
                dataList.add(new DropMenuModel(b.getPId(), b.getName(), false, null));
            }
        }
        return dataList;
    }

    @Override
    public List<DropMenuModel> getTab3Datalist() {
        List<DropMenuModel> dataList = new ArrayList<>();
        dataList.add(new DropMenuModel("", mView.getCurContext().getString(R.string.unlimited), true, null));
        if (configModel != null) {
            List<JobConfigModel.WorkYearTypeBean> workYearTypes = configModel.getWorkYearType();
            if (workYearTypes == null) return dataList;
            for (JobConfigModel.WorkYearTypeBean b : workYearTypes) {
                dataList.add(new DropMenuModel(b.getWId(), b.getName(), false, null));
            }
        }
        return dataList;
    }

    @Override
    public List<DropMenuModel> getTab4Datalist() {
        List<DropMenuModel> dataList = new ArrayList<>();
        dataList.add(new DropMenuModel("", mView.getCurContext().getString(R.string.unlimited), true, null));
        if (configModel != null) {
            List<JobConfigModel.EducationConfBean> educationConfs = configModel.getEducationConf();
            for (JobConfigModel.EducationConfBean b : educationConfs) {
                dataList.add(new DropMenuModel(b.getEId(), b.getName(), false, null));
            }
        }
        return dataList;
    }

    @Override
    void getAd() {
        RepositoryFactory.getRemoteRepository().getAd("3")
                .compose(RxScheduler.<ResponseModel<AdListModel>>toMain())
                .as(mView.<ResponseModel<AdListModel>>bindAutoDispose())
                .subscribe(new CommonObserver<AdListModel>() {
                    @Override
                    public void onSuccess(AdListModel data) {

                        mView.showBanners(data);

                    }

                    @Override
                    public void onError(ResponseModel data) {

                    }

                    @Override
                    public void onNetError(ResponseModel data) {

                    }
                });
    }


}
