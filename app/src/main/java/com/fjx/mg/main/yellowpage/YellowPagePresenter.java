package com.fjx.mg.main.yellowpage;

import android.text.TextUtils;

import com.fjx.mg.R;
import com.library.common.base.BaseFragment;
import com.library.common.utils.DimensionUtil;
import com.library.repository.core.net.CommonObserver;
import com.library.repository.core.net.RxScheduler;
import com.library.repository.db.DBDaoFactory;
import com.library.repository.db.model.CompanyTypeModel;
import com.library.repository.models.CompanyListModel;
import com.library.repository.models.CompanyTypeListModel;
import com.library.repository.models.CompanyTypeListModelV1;
import com.library.repository.models.CompanyTypeModelV1;
import com.library.repository.models.CountryListModel;
import com.library.repository.models.ResponseModel;
import com.library.repository.repository.RepositoryFactory;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnSelectListener;

import java.util.ArrayList;
import java.util.List;

class YellowPagePresenter extends YellowPageContract.Presenter {


    private CompanyTypeListModelV1 listModel;


    YellowPagePresenter(YellowPageContract.View view) {
        super(view);
    }

    @Override
    void getCompanyList(final int page, String title) {
        RepositoryFactory.getRemoteCompanyApi().companyList(page, title, "")
                .compose(RxScheduler.<ResponseModel<CompanyListModel>>toMain())
                .as(mView.<ResponseModel<CompanyListModel>>bindAutoDispose())
                .subscribe(new CommonObserver<CompanyListModel>() {
                    @Override
                    public void onSuccess(CompanyListModel data) {
                        if (page == 1) {
                            DBDaoFactory.getCompanyListDaos().deleteAll();
                        }
                        DBDaoFactory.getCompanyListDaos().insertList(data.getCompanyList());
                        if (mView == null) return;
                        mView.showCompanyList(data);


                    }

                    @Override
                    public void onError(ResponseModel data) {
                        if (mView == null) return;
                        mView.loadDataError();
                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                        if (mView == null) return;
                        mView.loadDataError();
                    }
                });

    }

    @Override
    void getCompanyListV1(final int page, String title, String serviceId, String secondServiceId, String countryId, String cityId) {
        RepositoryFactory.getRemoteCompanyApi().companyListV1(page, title, serviceId, secondServiceId, countryId, cityId)
                .compose(RxScheduler.<ResponseModel<CompanyListModel>>toMain())
                .as(mView.<ResponseModel<CompanyListModel>>bindAutoDispose())
                .subscribe(new CommonObserver<CompanyListModel>() {
                    @Override
                    public void onSuccess(CompanyListModel data) {
                        if (page == 1) {
                            DBDaoFactory.getCompanyListDaos().deleteAll();
                        }
                        DBDaoFactory.getCompanyListDaos().insertList(data.getCompanyList());
                        if (mView == null) return;
                        mView.showCompanyList(data);


                    }

                    @Override
                    public void onError(ResponseModel data) {
                        if (mView == null) return;
                        mView.loadDataError();
                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                        if (mView == null) return;
                        mView.loadDataError();
                    }
                });
    }


    @Override
    void getCompanyTypes() {
        RepositoryFactory.getRemoteCompanyApi().getCompanyType()
                .compose(RxScheduler.<ResponseModel<CompanyTypeListModel>>toMain())
                .as(mView.<ResponseModel<CompanyTypeListModel>>bindAutoDispose())
                .subscribe(new CommonObserver<CompanyTypeListModel>() {
                    @Override
                    public void onSuccess(CompanyTypeListModel data) {
                        if (data == null || data.getCompanyTypeList() == null || data.getCompanyTypeList().isEmpty())
                            return;

                        DBDaoFactory.getCompanyTypeDao().deleteAll();
                        DBDaoFactory.getCompanyTypeDao().insertList(data.getCompanyTypeList());
                        initData(data.getCompanyTypeList());

                    }

                    @Override
                    public void onError(ResponseModel data) {
                        List<CompanyTypeModel> list = DBDaoFactory.getCompanyTypeDao().queryList();
                        if (list == null || list.isEmpty()) return;
                        initData(list);
                    }

                    @Override
                    public void onNetError(ResponseModel data) {

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
                    }

                    @Override
                    public void onError(ResponseModel data) {
//                        CommonToast.toast(data.getMsg());
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
    void showProvinceDialog() {
        if (listModel == null) return;
        final List<CountryListModel> countryList = listModel.getCountryList();
        if (countryList == null || countryList.isEmpty()) return;
        List<String> countryNames = new ArrayList<>();
        for (CountryListModel model : countryList) {
            countryNames.add(model.getCountryName());
        }
        String[] countryArr = new String[countryNames.size()];
        countryArr = countryNames.toArray(countryArr);
        new XPopup.Builder(mView.getCurContext())
                .maxHeight(DimensionUtil.getScreenHight() / 2)
                .asCenterList(mView.getCurActivity().getString(R.string.hint_select), countryArr,
                        new OnSelectListener() {
                            @Override
                            public void onSelect(int position, String text) {
                                mView.showSelectCountry(countryList.get(position));
                            }
                        })
                .show();

    }

    @Override
    void showCityDialog(String pid) {
        if (listModel == null) return;
        final List<CountryListModel> countryList = listModel.getCountryList();
        if (countryList == null || countryList.isEmpty()) return;
        List<CountryListModel.CityListBean> cityList = null;
        for (CountryListModel model : countryList) {
            if (TextUtils.equals(pid, model.getCId())) {
                cityList = model.getCityList();
                break;
            }
        }
        if (cityList == null || cityList.isEmpty()) return;
        List<String> cityNames = new ArrayList<>();

        for (CountryListModel.CityListBean city : cityList) {
            cityNames.add(city.getCityName());
        }
        String[] cityArr = new String[cityNames.size()];
        cityArr = cityNames.toArray(cityArr);
        final List<CountryListModel.CityListBean> finalCityList = cityList;
        new XPopup.Builder(mView.getCurContext())
                .maxHeight(DimensionUtil.getScreenHight() / 2)
                .asCenterList("请选择", cityArr,
                        new OnSelectListener() {
                            @Override
                            public void onSelect(int position, String text) {
                                mView.showSelectCity(finalCityList.get(position));
                            }
                        })
                .show();

    }

    @Override
    void showFirstSortDialog() {
        if (listModel == null) return;
        final List<CompanyTypeModelV1> typeList = listModel.getCompanyTypeList();
        if (typeList == null || typeList.isEmpty()) return;
        List<String> typeName = new ArrayList<>();

        for (CompanyTypeModelV1 typeModel : typeList) {
            typeName.add(typeModel.getName());
        }
        String[] typeArr = new String[typeList.size()];
        typeArr = typeName.toArray(typeArr);
        new XPopup.Builder(mView.getCurContext())
                .maxHeight(DimensionUtil.getScreenHight() / 2)
                .asCenterList(mView.getCurActivity().getString(R.string.select), typeArr,
                        new OnSelectListener() {
                            @Override
                            public void onSelect(int position, String text) {
                                mView.showSelectService(typeList.get(position));
                            }
                        })
                .show();
    }

    @Override
    void showSecondSortDialog(String sid) {
        if (listModel == null) return;
        List<CompanyTypeModelV1> typeList = listModel.getCompanyTypeList();
        if (typeList == null || typeList.isEmpty()) return;
        List<CompanyTypeModelV1.SecondListBean> secondServiceList = null;
        for (CompanyTypeModelV1 model : typeList) {
            if (TextUtils.equals(sid, model.getCId())) {
                secondServiceList = model.getSecondList();
            }
        }
        if (secondServiceList == null || secondServiceList.isEmpty()) return;
        List<String> secondNames = new ArrayList<>();
        for (CompanyTypeModelV1.SecondListBean secondBean : secondServiceList) {
            secondNames.add(secondBean.getSecondName());
        }

        String[] secondArr = new String[secondNames.size()];
        secondArr = secondNames.toArray(secondArr);

        final List<CompanyTypeModelV1.SecondListBean> finalSecondServiceList = secondServiceList;
        new XPopup.Builder(mView.getCurContext())
                .maxHeight(DimensionUtil.getScreenHight() / 2)
                .asCenterList(mView.getCurActivity().getString(R.string.select), secondArr,
                        new OnSelectListener() {
                            @Override
                            public void onSelect(int position, String text) {
                                mView.showSelectSecondService(finalSecondServiceList.get(position));
                            }
                        })
                .show();
    }

    private void initData(List<CompanyTypeModel> list) {
        String[] titles = new String[list.size()];
        List<BaseFragment> fragments = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            titles[i] = list.get(i).getName();
            fragments.add(YellowPageTabFragment.newInstance(list.get(i).getcId()));
        }
        mView.showTabsAndFragment(list, titles, fragments);
    }

}
