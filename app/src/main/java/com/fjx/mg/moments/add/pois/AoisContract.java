package com.fjx.mg.moments.add.pois;

import com.amap.api.services.core.PoiItem;
import com.library.common.base.BasePresenter;
import com.library.common.base.BaseView;

import java.util.List;

public interface AoisContract {

    interface View extends BaseView {
        void LocationSuccess(List<PoiItem> pois, String lat, String lon);
    }

    abstract class Presenter extends BasePresenter<AoisContract.View> {
        public Presenter(AoisContract.View view) {
            super(view);
        }

        abstract void locationAddress();
    }
}
