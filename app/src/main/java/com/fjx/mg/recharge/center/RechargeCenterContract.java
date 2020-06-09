package com.fjx.mg.recharge.center;

import android.content.Context;
import android.content.Intent;

import com.flyco.tablayout.listener.CustomTabEntity;
import com.library.common.base.BasePresenter;
import com.library.common.base.BaseView;
import com.library.repository.models.RechargeModel;

import java.util.ArrayList;
import java.util.List;

public interface RechargeCenterContract {

    interface View extends BaseView {

        void showTabAndItems(ArrayList<CustomTabEntity> tabEntitys);

        void showSelectPhoneNUm(String number);

        void showPhonePackage(List<RechargeModel> datas);
        void show9PhonePackage(List<RechargeModel> datas);

        void setMobile(String text);
    }

    abstract class Presenter extends BasePresenter<RechargeCenterContract.View> {

        Presenter(RechargeCenterContract.View view) {
            super(view);
        }

        abstract void initData(Context context);

        abstract void getPhoneNum(Intent data);

        abstract void getPhonePackage();

        abstract void getDataPackage();


    }
}
