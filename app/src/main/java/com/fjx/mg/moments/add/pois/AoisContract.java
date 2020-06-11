package com.fjx.mg.moments.add.pois;

import com.library.common.base.BasePresenter;
import com.library.common.base.BaseView;
import com.library.repository.models.GoogleMapGeocodeSearchBean;

import java.util.List;


public interface AoisContract {

    interface View extends BaseView {
        void LocationSuccess(List<GoogleMapGeocodeSearchBean.ResultsBean> pois);
    }

    abstract class Presenter extends BasePresenter<AoisContract.View> {
        public Presenter(AoisContract.View view) {
            super(view);
        }

        abstract void locationAddress();
    }
}
