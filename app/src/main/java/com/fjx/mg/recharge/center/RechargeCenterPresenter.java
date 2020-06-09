package com.fjx.mg.recharge.center;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Log;

import com.fjx.mg.R;
import com.fjx.mg.utils.SharedPreferencesHelper;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.library.common.utils.CommonToast;
import com.library.repository.core.net.CommonObserver;
import com.library.repository.core.net.RxScheduler;
import com.library.repository.models.RechargeModel;
import com.library.repository.models.ResponseModel;
import com.library.repository.models.TabModel;
import com.library.repository.repository.RepositoryFactory;

import java.util.ArrayList;
import java.util.List;

public class RechargeCenterPresenter extends RechargeCenterContract.Presenter {
    RechargeCenterPresenter(RechargeCenterContract.View view) {
        super(view);
    }

    private List<RechargeModel> phonePackageDatas;
    private List<RechargeModel> dataPackageDatas;

    @Override
    void initData(Context context) {
        ArrayList<CustomTabEntity> tabEntity = new ArrayList<>();
        tabEntity.add(new TabModel(mView.getCurContext().getString(R.string.Pay_phone_bill)));
//        tabEntity.add(new TabModel(mView.getCurContext().getString(R.string.Charging_capacity)));
        mView.showTabAndItems(tabEntity);
        //默认加载手机充实套餐
        getPhonePackage();
        try {
            SharedPreferencesHelper sp = new SharedPreferencesHelper(context);
            Gson gson = new Gson();
            String json = sp.getString("json");
            if (!json.equals("")) {
                Log.e("json:", "" + json);
                List<RechargeModel> statusLs = gson.fromJson(json, new TypeToken<List<RechargeModel>>() {
                }.getType());

                mView.showPhonePackage(statusLs);
                mView.show9PhonePackage(statusLs);
            }
            sp.close();

        } catch (Exception e) {
            Log.e("Exception:", "" + e.toString());
        }

    }

    @Override
    void getPhonePackage() {
        if (phonePackageDatas != null) {
            mView.showPhonePackage(phonePackageDatas);
            mView.show9PhonePackage(phonePackageDatas);
            return;
        }
        RepositoryFactory.getRemoteRepository()
                .phonePackage()
                .compose(RxScheduler.<ResponseModel<List<RechargeModel>>>toMain())
                .as(mView.<ResponseModel<List<RechargeModel>>>bindAutoDispose())
                .subscribe(new CommonObserver<List<RechargeModel>>() {
                    @Override
                    public void onSuccess(List<RechargeModel> data) {
                        if (data != null && data.size() > 0)
                            data.get(0).setCheck(true);
                        if (data.size() == 9) {
                            data.remove(8);
                        }
                        mView.showPhonePackage(data);
                        mView.show9PhonePackage(data);
                        phonePackageDatas = data;
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
    void getDataPackage() {
        if (dataPackageDatas != null) {
            mView.showPhonePackage(dataPackageDatas);
            return;
        }
        RepositoryFactory.getRemoteRepository()
                .dataPackage()
                .compose(RxScheduler.<ResponseModel<List<RechargeModel>>>toMain())
                .as(mView.<ResponseModel<List<RechargeModel>>>bindAutoDispose())
                .subscribe(new CommonObserver<List<RechargeModel>>() {
                    @Override
                    public void onSuccess(List<RechargeModel> data) {
                        if (data != null && data.size() > 0)
                            data.get(0).setCheck(true);
                        mView.showPhonePackage(data);
                        dataPackageDatas = data;
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
    void getPhoneNum(Intent data) {
        try {
            if (data.getData() != null) {
                Cursor cursor = mView.getCurContext().getContentResolver()
                        .query(data.getData(),
                                new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME},
                                null, null, null);
                while (cursor.moveToNext()) {
                    //取出该条数据的联系人姓名
                    String name = cursor.getString(1).replaceAll(" ", "");
                    //取出该条数据的联系人的手机号
                    String number = cursor.getString(0).replaceAll(" ", "").replaceAll("-", "");
                    mView.showSelectPhoneNUm(number);
                }
                cursor.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
