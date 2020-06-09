package com.fjx.mg.me.collect.house;

import com.library.common.base.BasePresenter;
import com.library.common.base.BaseView;
import com.library.repository.models.HouseListModel;

import java.util.List;

public interface HouseResourceContract {
    interface View extends BaseView {
        void showHouseList(HouseListModel houseListModel);

        void loadError();
    }

    abstract class Presenter extends BasePresenter<HouseResourceContract.View> {

        Presenter(HouseResourceContract.View view) {
            super(view);
        }

        abstract void getMyCollectHouse(int page);
    }

}
