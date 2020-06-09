package com.fjx.mg.house.fragment;

import com.fjx.mg.R;
import com.library.common.view.dropmenu.DropMenuModel;
import com.library.repository.core.net.CommonObserver;
import com.library.repository.core.net.RxScheduler;
import com.library.repository.db.DBDaoFactory;
import com.library.repository.models.AdListModel;
import com.library.repository.models.HouseConfigModel;
import com.library.repository.models.HouseDetailModel;
import com.library.repository.models.HouseListModel;
import com.library.repository.models.ResponseModel;
import com.library.repository.repository.RepositoryFactory;

import java.util.ArrayList;
import java.util.List;

public class HouseLeasePresenter extends HouseLeaseContract.Presenter {

    HouseLeasePresenter(HouseLeaseContract.View view) {
        super(view);
    }


    @Override
    void getHouseLIst(String type, final int htype, String cityId, String layoutId, String order, final int page) {
        RepositoryFactory.getRemoteJobApi().houseList(type, htype, cityId, layoutId, order, page)
                .compose(RxScheduler.<ResponseModel<HouseListModel>>toMain())
                .as(mView.<ResponseModel<HouseListModel>>bindAutoDispose())
                .subscribe(new CommonObserver<HouseListModel>() {
                    @Override
                    public void onSuccess(HouseListModel data) {
                        if (page == 1) {
                            DBDaoFactory.getHouseDetailDao().deleteAll(htype);
                        }

                        mView.showHouseListModel(data);
                        for (HouseDetailModel item : data.getHouseList()) {
                            item.setType(htype);
                        }
                        DBDaoFactory.getHouseDetailDao().insertList(data.getHouseList());
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        mView.loadError();
                    }

                    @Override
                    public void onNetError(ResponseModel data) {

                    }
                });
    }

    @Override
    List<DropMenuModel> getTab1Datalist() {
        List<DropMenuModel> dataList = new ArrayList<>();
        dataList.add(new DropMenuModel("", mView.getCurContext().getString(R.string.all), true, null));

        if (mView.getType() == 1) {
            dataList.add(new DropMenuModel("1", mView.getCurContext().getString(R.string.rent_seeking), false, null));
            dataList.add(new DropMenuModel("3", mView.getCurContext().getString(R.string.lease), false, null));
        } else {
            dataList.add(new DropMenuModel("2", mView.getCurContext().getString(R.string.ask_for_buy), false, null));
            dataList.add(new DropMenuModel("4", mView.getCurContext().getString(R.string.sell), false, null));
        }
        return dataList;
    }

    @Override
    List<DropMenuModel> getTab2Datalist() {
        List<DropMenuModel> countryList = new ArrayList<>();

        HouseConfigModel configModel = RepositoryFactory.getLocalRepository().getHouseConfig();
        if (configModel != null) {
            List<HouseConfigModel.CountryListBean> countrys = configModel.getCountryList();
            if (countrys == null) return countryList;
            for (HouseConfigModel.CountryListBean country : countrys) {
                List<DropMenuModel> cityList = new ArrayList<>();
                for (HouseConfigModel.CountryListBean.CityListBean city : country.getCityList()) {
                    cityList.add(new DropMenuModel(city.getCityId(), city.getCityName(), false, null));
                }
                cityList.add(0, new DropMenuModel("", mView.getCurContext().getString(R.string.unlimited), true, null));
                countryList.add(new DropMenuModel(country.getCId(), country.getCountryName(), false, cityList));
            }
        }
        return countryList;
    }

    @Override
    List<DropMenuModel> getTab3Datalist() {
        List<DropMenuModel> address = new ArrayList<>();
        HouseConfigModel configModel = RepositoryFactory.getLocalRepository().getHouseConfig();
        if (configModel != null) {
            address.add(new DropMenuModel("", mView.getCurContext().getString(R.string.unlimited), true, null));
            List<HouseConfigModel.LayoutConfBean> list = configModel.getLayoutConf();
            for (HouseConfigModel.LayoutConfBean bean : list) {
                address.add(new DropMenuModel(bean.getLayoutId(), bean.getName(), false, null));
            }
        }

        return address;
    }

    @Override
    List<DropMenuModel> getTab4Datalist() {
        //排序1:价格降序,2:价格升序
        List<DropMenuModel> sortList = new ArrayList<>();
        sortList.add(new DropMenuModel("", mView.getCurContext().getString(R.string.default_text), true, null));
        sortList.add(new DropMenuModel("1", mView.getCurContext().getString(R.string.price_decline), false, null));
        sortList.add(new DropMenuModel("2", mView.getCurContext().getString(R.string.price_escalation), false, null));
        return sortList;
    }

    @Override
    void getAd() {
        RepositoryFactory.getRemoteRepository().getAd("4")
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
