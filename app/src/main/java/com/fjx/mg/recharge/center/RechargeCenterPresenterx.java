package com.fjx.mg.recharge.center;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Log;

import com.fjx.mg.utils.SharedPreferencesHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.library.common.utils.CommonToast;
import com.library.repository.core.net.CommonObserver;
import com.library.repository.core.net.RxScheduler;
import com.library.repository.models.RechargeModel;
import com.library.repository.models.ResponseModel;
import com.library.repository.repository.RepositoryFactory;

import java.util.List;

class RechargeCenterPresenterx extends RechargeCenterContractx.Presenter {
    RechargeCenterPresenterx(RechargeCenterContractx.View view) {
        super(view);
    }

    private List<RechargeModel> phonePackageDatas;

    @Override
    void initData(Context context) {

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
                if (statusLs != null && mView != null) {
                    mView.show9PhonePackage(statusLs);
                }
            }
            sp.close();

        } catch (Exception e) {
            Log.e("Exception:", "" + e.toString());
        }

    }

    @Override
    void getPhonePackage() {
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
                        if (data != null && mView != null) {
                            mView.show9PhonePackage(data);
                        }
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
