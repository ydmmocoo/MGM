package com.fjx.mg.main.yellowpage.publish;

import android.text.TextUtils;
import android.view.View;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.fjx.mg.R;
import com.google.gson.JsonObject;
import com.library.common.utils.CommonToast;
import com.library.common.utils.JsonUtil;
import com.library.common.utils.StringUtil;
import com.library.repository.core.net.CommonObserver;
import com.library.repository.core.net.RxScheduler;
import com.library.repository.data.MultipartBodyHBuilder;
import com.library.repository.db.DBDaoFactory;
import com.library.repository.db.model.CompanyTypeModel;
import com.library.repository.models.CmpanydetaisModel;
import com.library.repository.models.CompanyDetailModel;
import com.library.repository.models.CompanyTypeListModelV1;
import com.library.repository.models.CompanyTypeModelV1;
import com.library.repository.models.CountryListModel;
import com.library.repository.models.HouseConfigModel;
import com.library.repository.models.ResponseModel;
import com.library.repository.repository.RepositoryFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;

class YellowPagePublicPresenter extends YellowPagePublicContract.Presenter {


    private CompanyTypeListModelV1 listModel;

    YellowPagePublicPresenter(YellowPagePublicContract.View view) {
        super(view);
    }

    @Override
    void updateImage(final String title, final String type, final String contact, final String phone,
                     final String address, final String desc, final List<String> filePaths,
                     final String secondService, final String countryId, final String cityId) {
        if (TextUtils.isEmpty(title)) {
            CommonToast.toast(mView.getCurContext().getString(R.string.hint_input_title));
            return;
        }
        if (TextUtils.isEmpty(type)) {
            CommonToast.toast(mView.getCurContext().getString(R.string.hint_input_service_tyle));
            return;
        }
        if (TextUtils.isEmpty(contact)) {
            CommonToast.toast(mView.getCurContext().getString(R.string.hint_input_contact));
            return;
        }
        if (TextUtils.isEmpty(phone)) {
            CommonToast.toast(mView.getCurContext().getString(R.string.hint_input_contact_phone));
            return;
        }
        if (TextUtils.isEmpty(address)) {
            CommonToast.toast(mView.getCurContext().getString(R.string.hint_input_company_address));
            return;
        }
        if (TextUtils.isEmpty(desc)) {
            CommonToast.toast(mView.getCurContext().getString(R.string.hint_input_detail_describe));
            return;
        }
        final List<String> remoteUrl = new ArrayList<>();
        mView.showLoading();
        for (int i = 0; i < filePaths.size(); i++) {

            final String key = "feedback".concat(String.valueOf(i));
            List<MultipartBody.Part> requestBody
                    = MultipartBodyHBuilder.Builder()
                    .addParams("k", key)
                    .addParams(key, new File(filePaths.get(i)))
                    .builder();

            RepositoryFactory.getRemoteRepository().uploadFile(requestBody)
                    .compose(RxScheduler.<ResponseModel<JsonObject>>toMain())
                    .as(mView.<ResponseModel<JsonObject>>bindAutoDispose())
                    .subscribe(new CommonObserver<JsonObject>() {
                        @Override
                        public void onSuccess(JsonObject data) {
                            Map<String, Object> map = JsonUtil.jsonToMap(data.toString());
                            String value = (String) map.get(key);
                            remoteUrl.add(value);
                            if (remoteUrl.size() == filePaths.size()) {
//                                mView.uploadImageSucces(remoteUrl);
                                String urls = "";
                                for (String url : remoteUrl) {
                                    urls = urls.concat(url).concat(",");
                                }
                                if (TextUtils.isEmpty(mView.getCid()))
                                    commit(title, type, contact, phone, address, desc, urls, secondService, countryId, cityId);
                                else
                                    companyEdit(title, type, contact, phone, address, desc, mView.getCid(), urls, secondService, countryId, cityId);
                            }

                        }

                        @Override
                        public void onError(ResponseModel data) {
                            mView.hideLoading();
                            if (StringUtil.isNotEmpty(data.getMsg())) {
                                CommonToast.toast(data.getMsg());
                            }
                        }

                        @Override
                        public void onNetError(ResponseModel data) {
                            mView.hideLoading();
                            if (StringUtil.isNotEmpty(data.getMsg())) {
                                CommonToast.toast(data.getMsg());
                            }
                        }
                    });
        }
    }

    @Override
    void commit(String title, String type, String contact, String phone, String address, String desc,
                String imageUrls, String secondService, String countryId, String cityId) {

        RepositoryFactory.getRemoteCompanyApi().companyAdd(title, type, contact, phone, address, desc, imageUrls, secondService, countryId, cityId)
                .compose(RxScheduler.<ResponseModel<CmpanydetaisModel>>toMain())
                .as(mView.<ResponseModel<CmpanydetaisModel>>bindAutoDispose())
                .subscribe(new CommonObserver<CmpanydetaisModel>() {
                    @Override
                    public void onSuccess(CmpanydetaisModel data) {
                        mView.hideLoading();
                        mView.commitSuccess();
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        mView.hideLoading();
                        if (StringUtil.isNotEmpty(data.getMsg())) {
                            CommonToast.toast(data.getMsg());
                        }

                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                        mView.hideLoading();
                        if (StringUtil.isNotEmpty(data.getMsg())) {
                            CommonToast.toast(data.getMsg());
                        }
                    }
                });
    }

    @Override
    void companyEdit(String title, String type, String contact, String phone, String address, String desc, String cid, String imageUrls
            , String secondService, String countryId, String cityId) {

        RepositoryFactory.getRemoteCompanyApi().companyEdit(title, type, contact, phone, address, desc, cid, imageUrls, secondService, countryId, cityId)
                .compose(RxScheduler.<ResponseModel<CmpanydetaisModel>>toMain())
                .as(mView.<ResponseModel<CmpanydetaisModel>>bindAutoDispose())
                .subscribe(new CommonObserver<CmpanydetaisModel>() {
                    @Override
                    public void onSuccess(CmpanydetaisModel data) {
                        mView.hideLoading();
                        mView.commitSuccess();
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        mView.hideLoading();
                        if (StringUtil.isNotEmpty(data.getMsg())) {
                            CommonToast.toast(data.getMsg());
                        }

                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                        mView.hideLoading();
                        if (StringUtil.isNotEmpty(data.getMsg())) {
                            CommonToast.toast(data.getMsg());
                        }
                    }
                });
    }

    @Override
    void getCompanyDetail(String cId) {
        final List<CompanyTypeModel> list = DBDaoFactory.getCompanyTypeDao().queryList();
        if (list != null && !list.isEmpty()) {
            mView.showSelectData(list.get(0));
        }


        if (TextUtils.isEmpty(cId)) return;
        mView.showLoading();
        RepositoryFactory.getRemoteCompanyApi().companyDetail(cId)
                .compose(RxScheduler.<ResponseModel<CmpanydetaisModel>>toMain())
                .as(mView.<ResponseModel<CmpanydetaisModel>>bindAutoDispose())
                .subscribe(new CommonObserver<CmpanydetaisModel>() {
                    @Override
                    public void onSuccess(CmpanydetaisModel data) {
                        mView.showCompanyDetail(data);
                        mView.hideLoading();

                    }

                    @Override
                    public void onError(ResponseModel data) {
                        mView.hideLoading();
                        if (StringUtil.isNotEmpty(data.getMsg())) {
                            CommonToast.toast(data.getMsg());
                        }
                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                        mView.hideLoading();
                        if (StringUtil.isNotEmpty(data.getMsg())) {
                            CommonToast.toast(data.getMsg());
                        }
                    }
                });
    }

    @Override
    void getCompanyTypesV1() {
        RepositoryFactory.getRemoteCompanyApi().getCompanyTypeV1()
                .compose(RxScheduler.<ResponseModel<CompanyTypeListModelV1>>toMain())
                .as(mView.<ResponseModel<CompanyTypeListModelV1>>bindAutoDispose())
                .subscribe(new CommonObserver<CompanyTypeListModelV1>() {
                    @Override
                    public void onSuccess(CompanyTypeListModelV1 data) {
                        if (data == null || data.getCompanyTypeList() == null || data.getCompanyTypeList().isEmpty())
                            return;
                        listModel = data;
                        List<CountryListModel> countryList = listModel.getCountryList();
                        List<CompanyTypeModelV1> typeList = listModel.getCompanyTypeList();

                        if (countryList != null && !countryList.isEmpty()) {
                            String countryName = "", cityName = "", countryId = "", cityId = "";
                            CountryListModel country = countryList.get(0);
                            countryName = country.getCountryName();
                            countryId = country.getCId();
                            List<CountryListModel.CityListBean> cityList = country.getCityList();
                            if (cityList != null && !cityList.isEmpty()) {
                                cityName = cityList.get(0).getCityName();
                                cityId = cityList.get(0).getCityId();
                            }
                            mView.selecrtAddress(countryName, cityName, countryId, cityId);
                        }

                        if (typeList != null && !typeList.isEmpty()) {
                            String serviceId = "", serviceName = "", secondServiceIs = "", secondServiceName = "";
                            CompanyTypeModelV1 modelV1 = typeList.get(0);
                            serviceId = modelV1.getCId();
                            serviceName = modelV1.getName();
                            List<CompanyTypeModelV1.SecondListBean> secondList = modelV1.getSecondList();
                            if (secondList != null && !secondList.isEmpty()) {
                                secondServiceName = secondList.get(0).getSecondName();
                                secondServiceIs = secondList.get(0).getSecondId();
                            }
                            mView.selectService(serviceId, serviceName, secondServiceIs, secondServiceName);
                        }
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        if (StringUtil.isNotEmpty(data.getMsg())) {
                            CommonToast.toast(data.getMsg());
                        }
//                        List<CompanyTypeModel> list = DBDaoFactory.getCompanyTypeDao().queryList();
//                        if (list == null || list.isEmpty()) return;
//                        initData(list);
                    }

                    @Override
                    public void onNetError(ResponseModel data) {

                    }
                });
    }

    @Override
    void showTypeDialog() {
        if (listModel == null) return;
        final List<CompanyTypeModelV1> typeList = listModel.getCompanyTypeList();
        if (typeList == null || typeList.isEmpty()) return;

        List<String> serviceNames = new ArrayList<>();
        final List<List<String>> secondServiceNames = new ArrayList<>();


        for (CompanyTypeModelV1 typeModel : typeList) {
            serviceNames.add(typeModel.getName());
            List<String> secondNames = new ArrayList<>();
            for (CompanyTypeModelV1.SecondListBean second : typeModel.getSecondList()) {
                secondNames.add(second.getSecondName());
            }
            secondServiceNames.add(secondNames);
        }

        //条件选择器
        OptionsPickerView pvOptions = new OptionsPickerBuilder(mView.getCurContext(), new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                String serviceName = typeList.get(options1).getName();
                String serviceId = typeList.get(options1).getCId();
                String secondServiceName = "", secondServiceIs = "";
                List<CompanyTypeModelV1.SecondListBean> secondList = typeList.get(options1).getSecondList();
                if (secondList != null && !secondList.isEmpty()) {
                    secondServiceName = typeList.get(options1).getSecondList().get(option2).getSecondName();
                    secondServiceIs = typeList.get(options1).getSecondList().get(option2).getSecondId();
                }

                mView.selectService(serviceId, serviceName, secondServiceIs, secondServiceName);
            }
        }).build();
        pvOptions.setPicker(serviceNames, secondServiceNames);
        pvOptions.show();
    }

    @Override
    void showAreaDialog() {

        final List<String> countryList = new ArrayList<>();
        final List<List<String>> cityList = new ArrayList<>();

        final List<CountryListModel> addressList = listModel.getCountryList();
        if (addressList == null || addressList.isEmpty()) return;

        for (CountryListModel bean : addressList) {
            countryList.add(bean.getCountryName());
            List<String> cities = new ArrayList<>();
            for (CountryListModel.CityListBean city : bean.getCityList()) {
                cities.add(city.getCityName());
            }
            cityList.add(cities);
        }

        //条件选择器
        OptionsPickerView pvOptions = new OptionsPickerBuilder(mView.getCurContext(), new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                String countryName = addressList.get(options1).getCountryName();
                String countryId = addressList.get(options1).getCId();
                String cityName = addressList.get(options1).getCityList().get(option2).getCityName();
                String cityId = addressList.get(options1).getCityList().get(option2).getCityId();


                mView.selecrtAddress(countryName, cityName, countryId, cityId);
            }
        }).build();
        pvOptions.setPicker(countryList, cityList);
        pvOptions.show();

    }

}
