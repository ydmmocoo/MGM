package com.fjx.mg.setting.address.edit;

import com.library.common.base.BasePresenter;
import com.library.common.base.BaseView;

import retrofit2.http.Field;

public interface EditAddressContract {

    interface View extends BaseView {

        void editSuccess();

        void locationResult(String address, String longitude, String latitude);


    }

    abstract class Presenter extends BasePresenter<View> {

        public Presenter(View view) {
            super(view);
        }

        abstract void addAddress(String name, String sex, String phone, String address, String longitude, String latitude, String roomNo);

        abstract void modifyAddress(String name, String sex, String phone, String address, String longitude, String latitude, String roomNo, String addressId);


        abstract void locationAddress();

        abstract void stopLocation();
    }

}
