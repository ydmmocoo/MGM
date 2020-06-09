package com.fjx.mg.recharge.record.fragment;

import android.content.Context;

import com.fjx.mg.utils.SharedPreferencesHelper;
import com.google.gson.Gson;
import com.library.common.utils.CommonToast;
import com.library.repository.Constant;
import com.library.repository.core.net.CommonObserver;
import com.library.repository.core.net.RxScheduler;
import com.library.repository.models.PhoneRechargeModel;
import com.library.repository.models.ResponseModel;
import com.library.repository.repository.RepositoryFactory;

import static com.fjx.mg.recharge.record.fragment.BillRecordTabFragment.TYPE_ELECT;
import static com.fjx.mg.recharge.record.fragment.BillRecordTabFragment.TYPE_MOBILE;
import static com.fjx.mg.recharge.record.fragment.BillRecordTabFragment.TYPE_NET;
import static com.fjx.mg.recharge.record.fragment.BillRecordTabFragment.TYPE_WATER;

public class BillRecordTabPresenter extends BillRecordTabContract.Presenter {


    BillRecordTabPresenter(BillRecordTabContract.View view) {
        super(view);
    }

    @Override
    void initData() {


    }

    @Override
    void getPhoneRechargeRecord(int page, final Context context,final int typeWater) {
        RepositoryFactory.getRemoteRepository()
                .phoneChargeRecord(page, phoneType())
                .compose(RxScheduler.<ResponseModel<PhoneRechargeModel>>toMain())
                .as(mView.<ResponseModel<PhoneRechargeModel>>bindAutoDispose())
                .subscribe(new CommonObserver<PhoneRechargeModel>() {
                    @Override
                    public void onSuccess(PhoneRechargeModel data) {
                        SharedPreferencesHelper sp = new SharedPreferencesHelper(context);
                        Gson gson = new Gson();
                        sp.putString("PhoneRechargeModel" + typeWater, gson.toJson(data));
                        sp.close();
                        mView.showRecordDatas(data, false);
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        CommonToast.toast(data.getMsg());
                    }

                    @Override
                    public void onNetError(ResponseModel data) {

                    }
                });
    }

    @Override
    void getRechargeRecord(int page, final Context context,final int typeWater) {
        RepositoryFactory.getRemoteRepository()
                .chargeRecord(page, type())
                .compose(RxScheduler.<ResponseModel<PhoneRechargeModel>>toMain())
                .as(mView.<ResponseModel<PhoneRechargeModel>>bindAutoDispose())
                .subscribe(new CommonObserver<PhoneRechargeModel>() {
                    @Override
                    public void onSuccess(PhoneRechargeModel data) {
                        SharedPreferencesHelper sp = new SharedPreferencesHelper(context);
                        Gson gson = new Gson();
                        sp.putString("PhoneRechargeModelx" + typeWater, gson.toJson(data));
                        sp.close();
                        mView.showRecordDatas(data, true);
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        CommonToast.toast(data.getMsg());
                    }

                    @Override
                    public void onNetError(ResponseModel data) {

                    }
                });
    }

    private int type() {
        int type = mView.getType();
        if (type == TYPE_ELECT) {
            return Constant.RecordType.ELECT;
        } else if (type == TYPE_NET) {
            return Constant.RecordType.NET;
        } else if (type == TYPE_WATER) {
            return Constant.RecordType.WATER;
        }
        return 0;
    }

    private int phoneType() {
        int type = mView.getType();
        if (type == TYPE_MOBILE) {
            return Constant.RecordPhoneType.PHONE;
        } else {
            return Constant.RecordPhoneType.DATA;
        }
    }
}
