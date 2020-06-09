package com.fjx.mg.house.fragment;

import com.library.common.base.BasePresenter;
import com.library.common.base.BaseView;
import com.library.common.view.dropmenu.DropMenuModel;
import com.library.repository.models.AdListModel;
import com.library.repository.models.HouseListModel;
import com.library.repository.models.JobListModel;

import java.util.List;

import retrofit2.http.Field;

public interface HouseLeaseContract {

    interface View extends BaseView {

        void showHouseListModel(HouseListModel data);

        void loadError();

        int getType();

        void showBanners(AdListModel data);
    }

    abstract class Presenter extends BasePresenter<View> {

        Presenter(HouseLeaseContract.View view) {
            super(view);
        }

        abstract void getHouseLIst(String type, int htype, String cityId, String layoutId, String order, int page);


        abstract List<DropMenuModel> getTab1Datalist();

        abstract List<DropMenuModel> getTab2Datalist();

        abstract List<DropMenuModel> getTab3Datalist();

        abstract List<DropMenuModel> getTab4Datalist();

        abstract void getAd();
    }
}
