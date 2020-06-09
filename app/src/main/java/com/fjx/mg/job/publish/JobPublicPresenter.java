package com.fjx.mg.job.publish;

import android.text.TextUtils;
import android.view.View;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.fjx.mg.R;
import com.library.common.utils.CommonToast;
import com.library.repository.core.net.CommonObserver;
import com.library.repository.core.net.RxScheduler;
import com.library.repository.models.HouseConfigModel;
import com.library.repository.models.JobConfigModel;
import com.library.repository.models.JobModel;
import com.library.repository.models.ResponseModel;
import com.library.repository.repository.RepositoryFactory;

import java.util.ArrayList;
import java.util.List;

class JobPublicPresenter extends JobPublicContract.Presenter {
    private JobConfigModel configModel;

    JobPublicPresenter(JobPublicContract.View view) {
        super(view);
    }

    @Override
    void getConfig() {

        JobConfigModel model = RepositoryFactory.getLocalRepository().getJobConfig();
        configModel = model;
        RepositoryFactory.getRemoteJobApi().getConf()
                .compose(RxScheduler.<ResponseModel<JobConfigModel>>toMain())
                .as(mView.<ResponseModel<JobConfigModel>>bindAutoDispose())
                .subscribe(new CommonObserver<JobConfigModel>() {
                    @Override
                    public void onSuccess(JobConfigModel data) {
                        RepositoryFactory.getLocalRepository().saveJobConfig(data);
                        configModel = data;
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
    void addJob(String title, String countryName, String cityName, String typeIds, String sex, String pay,
                String phone, String weixin, String desc, String companyDesc, String workyear,
                String education, String expireId, int type) {

        mView.showLoading();
        RepositoryFactory.getRemoteJobApi().addJob(title, countryName, cityName, typeIds, sex, pay,
                phone, weixin, desc, companyDesc, workyear, education, expireId, type)
                .compose(RxScheduler.<ResponseModel<Object>>toMain())
                .as(mView.<ResponseModel<Object>>bindAutoDispose())
                .subscribe(new CommonObserver<Object>() {
                    @Override
                    public void onSuccess(Object data) {
                        mView.commitSuccess();
                        mView.hideLoading();
                    }

                    @Override
                    public void onError(ResponseModel data) {
//                        CommonToast.toast(data.getMsg());
                        mView.hideLoading();
                    }

                    @Override
                    public void onNetError(ResponseModel data) {

                    }
                });
    }

    @Override
    void editJob(String title, String countryName, String cityName, String typeIds, String sex, String pay, String phone, String weixin, String desc, String companyDesc, String workyear, String education, String jobId, String expireId, int type) {
        mView.showLoading();
        RepositoryFactory.getRemoteJobApi().editJob(title, countryName, cityName, typeIds, sex, pay,
                phone, weixin, desc, companyDesc, workyear, education, jobId, expireId, type)
                .compose(RxScheduler.<ResponseModel<Object>>toMain())
                .as(mView.<ResponseModel<Object>>bindAutoDispose())
                .subscribe(new CommonObserver<Object>() {
                    @Override
                    public void onSuccess(Object data) {
                        mView.commitSuccess();
                        mView.hideLoading();
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        CommonToast.toast(data.getMsg());
                        mView.hideLoading();
                    }

                    @Override
                    public void onNetError(ResponseModel data) {

                    }
                });
    }

    @Override
    void getJobDetail(String jobId) {
        if (TextUtils.isEmpty(jobId)) return;

        mView.showLoading();
        RepositoryFactory.getRemoteJobApi().jobDetail(jobId, "")
                .compose(RxScheduler.<ResponseModel<JobModel>>toMain())
                .as(mView.<ResponseModel<JobModel>>bindAutoDispose())
                .subscribe(new CommonObserver<JobModel>() {
                    @Override
                    public void onSuccess(JobModel data) {
                        mView.hideLoading();
                        mView.showJobModel(data);
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        mView.hideLoading();
//                        CommonToast.toast(data.getMsg());
                    }

                    @Override
                    public void onNetError(ResponseModel data) {

                    }
                });
    }

    @Override
    void showGenderDialog() {
        List<String> sexs = new ArrayList<>();
        final List<HouseConfigModel.SexConfBean> data = RepositoryFactory.getLocalRepository().getHouseConfig().getSexConf();
        for (HouseConfigModel.SexConfBean s : data) {
            sexs.add(s.getName());
        }


        //条件选择器
        OptionsPickerView pvOptions = new OptionsPickerBuilder(mView.getCurContext(), new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                mView.selectGender(data.get(options1));
            }
        }).build();
        pvOptions.setPicker(sexs);
        pvOptions.show();

    }

    @Override
    void showTypeDialog() {
        final List<String> types = new ArrayList<>();
        types.add(mView.getCurContext().getString(R.string.recruit));
        types.add(mView.getCurContext().getString(R.string.job_wanted));
        //条件选择器
        OptionsPickerView pvOptions = new OptionsPickerBuilder(mView.getCurContext(), new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                mView.selectType(types.get(options1), options1 + 1);
            }
        }).build();
        pvOptions.setPicker(types);
        pvOptions.show();
    }

    @Override
    void showAddressDialog() {

        final List<String> countryList = new ArrayList<>();
        final List<List<String>> cityList = new ArrayList<>();

        if (configModel.getCountryList() == null || configModel.getCountryList().isEmpty()) return;
        for (JobConfigModel.CountryListBean bean : configModel.getCountryList()) {
            countryList.add(bean.getCountryName());
            List<String> cities = new ArrayList<>();
            for (JobConfigModel.CountryListBean.CityListBean city : bean.getCityList()) {
                cities.add(city.getCityName());
            }
            cityList.add(cities);
        }

        //条件选择器
        OptionsPickerView pvOptions = new OptionsPickerBuilder(mView.getCurContext(), new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                mView.selecrtAddress(countryList.get(options1), cityList.get(options1).get(option2));
            }
        }).build();
        pvOptions.setPicker(countryList, cityList);
        pvOptions.show();

    }

    @Override
    void showJobTypeDoalog() {
        final List<JobConfigModel.JobTypesBean> data = RepositoryFactory.getLocalRepository().getJobConfig().getJobTypes();

        final List<String> types = new ArrayList<>();
        for (JobConfigModel.JobTypesBean bean : data) {
            types.add(bean.getName());
        }
        //条件选择器
        OptionsPickerView pvOptions = new OptionsPickerBuilder(mView.getCurContext(), new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                mView.selectJobType(types.get(options1), data.get(options1).getTypeId());
            }
        }).build();
        pvOptions.setPicker(types);
        pvOptions.show();
    }

    @Override
    void showEducationDialog() {
        final List<JobConfigModel.EducationConfBean> data = RepositoryFactory.getLocalRepository().getJobConfig().getEducationConf();

        final List<String> types = new ArrayList<>();
        for (JobConfigModel.EducationConfBean bean : data) {
            types.add(bean.getName());
        }
        //条件选择器
        OptionsPickerView pvOptions = new OptionsPickerBuilder(mView.getCurContext(), new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                mView.selectEducation(types.get(options1), data.get(options1).getEId());
            }
        }).build();
        pvOptions.setPicker(types);
        pvOptions.show();
    }

    @Override
    void showExpireDialog() {
        final List<JobConfigModel.ExpireTypeConfBean> expireList = configModel.getExpireTypeConf();

        List<String> typeNameList = new ArrayList<>();
        for (JobConfigModel.ExpireTypeConfBean b : expireList) {
            typeNameList.add(b.getExpireType());
        }

        //条件选择器
        OptionsPickerView pvOptions = new OptionsPickerBuilder(mView.getCurContext(), new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                mView.selectExpire(expireList.get(options1).getExpireType(), expireList.get(options1).getEId());
            }
        }).build();
        pvOptions.setPicker(typeNameList);
        pvOptions.show();
    }

    @Override
    void showMoneyDialog() {
        final List<JobConfigModel.PayTypeBean> payType = configModel.getPayType();

        List<String> typeNameList = new ArrayList<>();
        for (JobConfigModel.PayTypeBean b : payType) {
            typeNameList.add(b.getName());
        }

        //条件选择器
        OptionsPickerView pvOptions = new OptionsPickerBuilder(mView.getCurContext(), new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                mView.selectPayType(payType.get(options1).getName(), payType.get(options1).getPId());
            }
        }).build();
        pvOptions.setPicker(typeNameList);
        pvOptions.show();
    }

    @Override
    public void showWorkYearDialog() {
        final List<JobConfigModel.WorkYearTypeBean> workYearList = configModel.getWorkYearType();

        List<String> typeNameList = new ArrayList<>();
        for (JobConfigModel.WorkYearTypeBean b : workYearList) {
            typeNameList.add(b.getName());
        }

        //条件选择器
        OptionsPickerView pvOptions = new OptionsPickerBuilder(mView.getCurContext(), new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                mView.selectWorkYear(workYearList.get(options1).getName(), workYearList.get(options1).getWId());
            }
        }).build();
        pvOptions.setPicker(typeNameList);
        pvOptions.show();
    }

    @Override
    String getTypeIdByName(String type, String name) {
        String id = "";
        JobConfigModel model = RepositoryFactory.getLocalRepository().getJobConfig();
        if (model == null) return "";

        switch (type) {
            case "jobTypeIds":
                final List<JobConfigModel.JobTypesBean> jobTypes = RepositoryFactory.getLocalRepository().getJobConfig().getJobTypes();
                for (JobConfigModel.JobTypesBean bean : jobTypes) {
                    if (TextUtils.equals(name, bean.getName())) {
                        id = bean.getTypeId();
                        break;
                    }
                }
                break;

            case "edution":
                final List<JobConfigModel.EducationConfBean> educationList = RepositoryFactory.getLocalRepository().getJobConfig().getEducationConf();
                for (JobConfigModel.EducationConfBean bean : educationList) {
                    if (TextUtils.equals(name, bean.getName())) {
                        id = bean.getEId();
                        break;
                    }
                }
                break;

            case "sex":
                if (TextUtils.equals(name, "男")) {
                    id = "1";
                } else if (TextUtils.equals(name, "女")) {
                    id = "2";
                } else {
                    id = "3";
                }
                break;
        }
        return id;
    }

    @Override
    Object getDefaultData(String type) {
        if (configModel == null) return null;

        switch (type) {
            case "cuntryName":
                if (configModel.getCountryList() != null)
                    return configModel.getCountryList().get(0);
                else return null;
            case "jobTypeIds":
                if (configModel.getJobTypes() == null||configModel.getJobTypes().isEmpty()) return null;
                return configModel.getJobTypes().get(0);
            case "edution":
                if (configModel.getEducationConf() == null||configModel.getEducationConf().isEmpty()) return null;
                return configModel.getEducationConf().get(0);
            case "expireId":
                if (configModel.getExpireTypeConf() == null||configModel.getExpireTypeConf().isEmpty()) return null;
                return configModel.getExpireTypeConf().get(0);
            case "pay":
                if (configModel.getPayType() == null||configModel.getPayType().isEmpty()) return null;
                return configModel.getPayType().get(0);
            case "workYear":
                if (configModel.getWorkYearType() == null||configModel.getWorkYearType().isEmpty()) return null;
                return configModel.getWorkYearType().get(0);

        }
        return null;
    }

}
