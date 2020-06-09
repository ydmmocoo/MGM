package com.fjx.mg.job.publish;

import com.library.common.base.BasePresenter;
import com.library.common.base.BaseView;
import com.library.repository.models.HouseConfigModel;
import com.library.repository.models.JobModel;

import java.util.List;

import retrofit2.http.Field;

public interface JobPublicContract {

    interface View extends BaseView {

        void commitSuccess();

        void selectGender(HouseConfigModel.SexConfBean sexConfBean);

        void selectType(String name, int type);

        void selecrtAddress(String countryName, String cityName);

        void selectJobType(String typeName, String typeId);

        void selectEducation(String name, String id);

        void showJobModel(JobModel jobModel);

        void selectExpire(String name, String id);

        void selectPayType(String name, String pId);

        void selectWorkYear(String name, String wId);
    }

    abstract class Presenter extends BasePresenter<JobPublicContract.View> {

        Presenter(JobPublicContract.View view) {
            super(view);
        }

        abstract void getConfig();

        abstract void addJob(String title,
                             String countryName,
                             String cityName,
                             String typeIds,
                             String sex,
                             String pay,
                             String phone,
                             String weixin,
                             String desc,
                             String companyDesc,
                             String workyear,
                             String education,
                             String expireId,
                             int type);

        abstract void editJob(String title,
                              String countryName,
                              String cityName,
                              String typeIds,
                              String sex,
                              String pay,
                              String phone,
                              String weixin,
                              String desc,
                              String companyDesc,
                              String workyear,
                              String education,
                              String jobId,
                              String expireId,
                              int type);

        abstract void getJobDetail(String jobId);


        abstract void showGenderDialog();

        abstract void showTypeDialog();

        abstract void showAddressDialog();

        abstract void showJobTypeDoalog();

        abstract void showEducationDialog();

        abstract void showExpireDialog();

        abstract void showMoneyDialog();
        public abstract void showWorkYearDialog();


        abstract String getTypeIdByName(String type, String name);

        abstract Object getDefaultData(String type);


    }

}
