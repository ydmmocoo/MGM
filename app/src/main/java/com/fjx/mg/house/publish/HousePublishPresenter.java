package com.fjx.mg.house.publish;

import android.text.TextUtils;
import android.view.View;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.fjx.mg.R;
import com.google.gson.JsonObject;
import com.library.common.utils.CommonToast;
import com.library.common.utils.JsonUtil;
import com.library.repository.core.net.CommonObserver;
import com.library.repository.core.net.RxScheduler;
import com.library.repository.data.MultipartBodyHBuilder;
import com.library.repository.models.HouseConfigModel;
import com.library.repository.models.HouseDetailModel;
import com.library.repository.models.ResponseModel;
import com.library.repository.repository.RepositoryFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;


class HousePublishPresenter extends HousePublishContract.Presenter {


    private HouseConfigModel configModel;

    HousePublishPresenter(HousePublishContract.View view) {
        super(view);
    }

    @Override
    void getConfig() {
        HouseConfigModel houseConfigModel = RepositoryFactory.getLocalRepository().getHouseConfig();
        configModel = houseConfigModel;

        RepositoryFactory.getRemoteJobApi().getFabuConf()
                .compose(RxScheduler.<ResponseModel<HouseConfigModel>>toMain())
                .as(mView.<ResponseModel<HouseConfigModel>>bindAutoDispose())
                .subscribe(new CommonObserver<HouseConfigModel>() {
                    @Override
                    public void onSuccess(HouseConfigModel data) {
                        RepositoryFactory.getLocalRepository().saveHouseConfig(data);
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
    void showTypeDialog() {

        final int type = mView.getPublishType();
        final List<String> types = new ArrayList<>();
        if (type == 0) {
            types.add(mView.getCurContext().getString(R.string.rent_seeking));
            types.add(mView.getCurContext().getString(R.string.lease));
        } else {
            types.add(mView.getCurContext().getString(R.string.ask_for_buy));
            types.add(mView.getCurContext().getString(R.string.sell));
        }
        //条件选择器
        OptionsPickerView pvOptions = new OptionsPickerBuilder(mView.getCurContext(), new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                int t;
                if (type == 0) {
                    if (options1 == 0) {
                        t = HousePublishActivity.TYPE_QZ;
                    } else {
                        t = HousePublishActivity.TYPE_CZ;
                    }
                } else {
                    if (options1 == 0) {
                        t = HousePublishActivity.TYPE_QS;
                    } else {
                        t = HousePublishActivity.TYPE_CS;
                    }
                }
                mView.selectType(types.get(options1), t);
            }
        }).build();
        pvOptions.setPicker(types);
        pvOptions.show();
    }

    @Override
    void showAddressDialog() {

        final List<String> countryList = new ArrayList<>();
        final List<List<String>> cityList = new ArrayList<>();

        final List<HouseConfigModel.CountryListBean> addressList = configModel.getCountryList();
        if (addressList == null || addressList.isEmpty()) return;

        for (HouseConfigModel.CountryListBean bean : addressList) {
            countryList.add(bean.getCountryName());
            List<String> cities = new ArrayList<>();
            for (HouseConfigModel.CountryListBean.CityListBean city : bean.getCityList()) {
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

    @Override
    void showHtypeDialog() {
        final List<HouseConfigModel.LayoutConfBean> typeList = configModel.getLayoutConf();

        List<String> typeNameList = new ArrayList<>();
        for (HouseConfigModel.LayoutConfBean b : typeList) {
            typeNameList.add(b.getName());
        }

        //条件选择器
        OptionsPickerView pvOptions = new OptionsPickerBuilder(mView.getCurContext(), new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                mView.selectHType(typeList.get(options1).getName(), typeList.get(options1).getLayoutId());
            }
        }).build();
        pvOptions.setPicker(typeNameList);
        pvOptions.show();

    }

    @Override
    void showLanguageDialog() {
        final List<HouseConfigModel.LaguageConfBean> typeList = configModel.getLaguageConf();

        List<String> typeNameList = new ArrayList<>();
        for (HouseConfigModel.LaguageConfBean b : typeList) {
            typeNameList.add(b.getName());
        }

        //条件选择器
        OptionsPickerView pvOptions = new OptionsPickerBuilder(mView.getCurContext(), new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                mView.selectLanguage(typeList.get(options1).getName(), typeList.get(options1).getLaguageId());
            }
        }).build();
        pvOptions.setPicker(typeNameList);
        pvOptions.show();
    }

    @Override
    void showExpireDialog() {
        final List<HouseConfigModel.ExpireTypeBean> expireList = configModel.getExpireTypeConf();

        List<String> typeNameList = new ArrayList<>();
        for (HouseConfigModel.ExpireTypeBean b : expireList) {
            typeNameList.add(b.getExpireType());
        }

        //条件选择器
        OptionsPickerView pvOptions = new OptionsPickerBuilder(mView.getCurContext(), new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                mView.selectExpire(expireList.get(options1).getExpireType(), expireList.get(options1).geteId());
            }
        }).build();
        pvOptions.setPicker(typeNameList);
        pvOptions.show();
    }

    @Override
    void updateImage(final Map<String, Object> mapParams, final List<String> filePaths) {
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
                                String urls = "";
                                for (String url : remoteUrl) {
                                    urls = urls.concat(url).concat(",");
                                }
                                mapParams.put("images", urls);
                                String hid = (String) mapParams.get("hId");
                                if (TextUtils.isEmpty(hid))
                                    commit(mapParams);
                                else
                                    edit(mapParams);
                            }

                        }

                        @Override
                        public void onError(ResponseModel data) {
                            mView.hideLoading();
                            CommonToast.toast(data.getMsg());
                        }

                        @Override
                        public void onNetError(ResponseModel data) {

                        }
                    });
        }
    }

    @Override
    void commit(Map<String, Object> map) {
        mView.showLoading();
        RepositoryFactory.getRemoteJobApi().houseAdd(map)
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
    void edit(Map<String, Object> map) {
        mView.showLoading();
        RepositoryFactory.getRemoteJobApi().houseEdit(map)
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
                        mView.hideLoading();
                        if (data.getCode() == 10004) {
                            CommonToast.toast("请修改内容");
                            return;
                        }
                        CommonToast.toast(data.getMsg());


                    }

                    @Override
                    public void onNetError(ResponseModel data) {

                    }
                });
    }

    @Override
    void houseDetail(String hId) {
        if (TextUtils.isEmpty(hId)) return;

        mView.showLoading();
        RepositoryFactory.getRemoteJobApi().houseDetail(hId, "")
                .compose(RxScheduler.<ResponseModel<HouseDetailModel>>toMain())
                .as(mView.<ResponseModel<HouseDetailModel>>bindAutoDispose())
                .subscribe(new CommonObserver<HouseDetailModel>() {
                    @Override
                    public void onSuccess(HouseDetailModel data) {
                        mView.showHouseDetail(data);
                        mView.hideLoading();
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        mView.hideLoading();
                        CommonToast.toast(data.getMsg());
                    }

                    @Override
                    public void onNetError(ResponseModel data) {

                    }
                });
    }

    @Override
    String getTypeIdByName(String type, String name) {
        HouseConfigModel model = RepositoryFactory.getLocalRepository().getHouseConfig();
        if (model == null) return "";
        String id = "";
        switch (type) {
            case "countryId":
                for (HouseConfigModel.CountryListBean bean : model.getCountryList()) {
                    if (TextUtils.equals(bean.getCountryName(), name)) {
                        id = bean.getCId();
                        break;
                    }
                }
                break;
            case "cityId":
                for (HouseConfigModel.CountryListBean bean : model.getCountryList()) {
                    for (HouseConfigModel.CountryListBean.CityListBean city : bean.getCityList()) {
                        if (TextUtils.equals(city.getCityName(), name)) {
                            id = city.getCityId();
                            break;
                        }
                    }
                }
                break;

            case "layoutId":
                for (HouseConfigModel.LayoutConfBean bean : model.getLayoutConf()) {
                    if (TextUtils.equals(bean.getName(), name)) {
                        id = bean.getLayoutId();
                        break;
                    }
                }
                break;

            case "laguageId":
                for (HouseConfigModel.LaguageConfBean bean : model.getLaguageConf()) {
                    if (TextUtils.equals(bean.getName(), name)) {
                        id = bean.getLaguageId();
                        break;
                    }
                }
            case "expireId":
                for (HouseConfigModel.ExpireTypeBean bean : model.getExpireTypeConf()) {
                    if (TextUtils.equals(bean.getExpireType(), name)) {
                        id = bean.geteId();
                        break;
                    }
                }
                break;

        }


        return id;
    }


    @Override
    Object getDefaultData(String type) {
        if (configModel == null) return null;

        switch (type) {
            case "address":
                return configModel.getCountryList().get(0);
            case "hType":
                return configModel.getLayoutConf().get(0);
            case "language":
                return configModel.getLaguageConf().get(0);
            case "expire":
                return configModel.getExpireTypeConf().get(0);
        }
        return null;
    }
}
