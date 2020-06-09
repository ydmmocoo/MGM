package com.fjx.mg.main.yellowpage;

import com.library.common.base.BaseFragment;
import com.library.common.base.BasePresenter;
import com.library.common.base.BaseView;
import com.library.repository.db.model.CompanyTypeModel;
import com.library.repository.models.CompanyListModel;
import com.library.repository.models.CompanyTypeListModel;
import com.library.repository.models.CompanyTypeModelV1;
import com.library.repository.models.CountryListModel;

import java.util.List;

import retrofit2.http.Field;

public interface YellowPageContract {

    interface View extends BaseView {
        void showCompanyList(CompanyListModel data);

        void loadDataError();

        void showTabsAndFragment(List<CompanyTypeModel> data, String[] titles, List<BaseFragment> fragments);


        void showSelectCountry(CountryListModel model);

        void showSelectCity(CountryListModel.CityListBean model);

        void showSelectService(CompanyTypeModelV1 modelV1);

        void showSelectSecondService(CompanyTypeModelV1.SecondListBean model);
    }

    abstract class Presenter extends BasePresenter<YellowPageContract.View> {

        Presenter(YellowPageContract.View view) {
            super(view);
        }


        abstract void getCompanyList(int page, String title);

        abstract void getCompanyListV1(int page, String title, String serviceId, String secondServiceId, String countryId, String cityId);

        abstract void getCompanyTypes();

        abstract void getCompanyTypesV1();


        abstract void showProvinceDialog();

        abstract void showCityDialog(String pid);

        abstract void showFirstSortDialog();

        abstract void showSecondSortDialog(String sid);

    }
}
