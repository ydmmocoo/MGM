package com.fjx.mg.recharge.record.fragment;

import android.content.Context;

import com.library.common.base.BasePresenter;
import com.library.common.base.BaseView;
import com.library.repository.models.PhoneRechargeModel;

public interface BillRecordTabContract {

    interface View extends BaseView {

        /**
         * 收据充值账单
         */
        void showRecordDatas(PhoneRechargeModel phoneRechargeModel, boolean b);

        int getType();
    }

    abstract class Presenter extends BasePresenter<BillRecordTabContract.View> {

        Presenter(BillRecordTabContract.View view) {
            super(view);
        }

        abstract void initData();

        abstract void getPhoneRechargeRecord(int page, Context context,int typeWater);

        abstract void getRechargeRecord(int page, Context context,int typeWater);


    }
}
