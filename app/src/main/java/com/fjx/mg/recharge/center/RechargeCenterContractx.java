package com.fjx.mg.recharge.center;

import android.content.Context;
import android.content.Intent;

import com.flyco.tablayout.listener.CustomTabEntity;
import com.library.common.base.BasePresenter;
import com.library.common.base.BaseView;
import com.library.repository.models.RechargeModel;

import java.util.ArrayList;
import java.util.List;

public interface RechargeCenterContractx {

    interface View extends BaseView {


        void showSelectPhoneNUm(String number);

        void show9PhonePackage(List<RechargeModel> datas);

//        void setMobile(String text);
    }

    abstract class Presenter extends BasePresenter<RechargeCenterContractx.View> {

        Presenter(RechargeCenterContractx.View view) {
            super(view);
        }

        abstract void initData(Context context);

        abstract void getPhoneNum(Intent data);

        abstract void getPhonePackage();



    }
}
