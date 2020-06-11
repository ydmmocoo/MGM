package com.fjx.mg.setting.address.edit;

import android.location.Geocoder;
import android.text.TextUtils;
import android.util.Log;

import com.fjx.mg.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.library.common.utils.CommonToast;
import com.library.common.utils.JsonUtil;
import com.library.repository.core.net.CommonObserver;
import com.library.repository.core.net.RxScheduler;
import com.library.repository.models.ResponseModel;
import com.library.repository.repository.RepositoryFactory;

import java.util.Locale;

public class EditAddressPresenter extends EditAddressContract.Presenter {

    private FusedLocationProviderClient mClient;
    private LocationRequest mLocationRequest;
    private LocationCallback mLocationCallback;

    public EditAddressPresenter(EditAddressContract.View view) {
        super(view);
    }

    @Override
    void addAddress(String name, String sex, String phone, String address, String longitude, String latitude, String roomNo) {

        if (TextUtils.isEmpty(name)) {
            CommonToast.toast(mView.getCurContext().getString(R.string.hint_input_receiver_username));
            return;
        }

        if (TextUtils.isEmpty(phone)) {
            CommonToast.toast(mView.getCurContext().getString(R.string.hint_input_receiver_phone));
            return;
        }

        if (TextUtils.isEmpty(roomNo)) {
            CommonToast.toast(mView.getCurContext().getString(R.string.hint_input_receiver_detail_address));
            return;
        }

        mView.showLoading();
        RepositoryFactory.getRemoteAccountRepository().addAddress(name, sex, phone, address, longitude, latitude, roomNo)
                .compose(RxScheduler.<ResponseModel<Object>>toMain())
                .as(mView.<ResponseModel<Object>>bindAutoDispose())
                .subscribe(new CommonObserver<Object>() {
                    @Override
                    public void onSuccess(Object data) {
                        CommonToast.toast(R.string.add_success);
                        mView.editSuccess();
                        mView.hideLoading();
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        CommonToast.toast(data.getMsg());
                        mView.hideLoading();
                    }

                    @Override
                    public void onNetError(ResponseModel data) {

                    }
                });
    }

    @Override
    void modifyAddress(String name, String sex, String phone, String address, String longitude, String latitude, String roomNo, String addressId) {
        if (TextUtils.isEmpty(name)) {
            CommonToast.toast(mView.getCurContext().getString(R.string.hint_input_receiver_username));
            return;
        }

        if (TextUtils.isEmpty(phone)) {
            CommonToast.toast(mView.getCurContext().getString(R.string.hint_input_receiver_phone));
            return;
        }

        if (TextUtils.isEmpty(roomNo)) {
            CommonToast.toast(mView.getCurContext().getString(R.string.hint_input_receiver_detail_address));
            return;
        }
        mView.showLoading();
        RepositoryFactory.getRemoteAccountRepository().modifyAddress(name, sex, phone, address, longitude, latitude, roomNo, addressId)
                .compose(RxScheduler.<ResponseModel<Object>>toMain())
                .as(mView.<ResponseModel<Object>>bindAutoDispose())
                .subscribe(new CommonObserver<Object>() {
                    @Override
                    public void onSuccess(Object data) {
                        mView.showLoading();
                        CommonToast.toast(mView.getCurContext().getString(R.string.edit_success));
                        mView.editSuccess();
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        CommonToast.toast(data.getMsg());
                        mView.showLoading();
                    }

                    @Override
                    public void onNetError(ResponseModel data) {

                    }
                });
    }

    @Override
    void locationAddress() {
        mView.showLoading();
        //定位相关
        mClient = LocationServices.getFusedLocationProviderClient(mView.getCurActivity());
        mLocationRequest = new LocationRequest()
                .setInterval(1000)
                .setFastestInterval(5000)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                double latitude=locationResult.getLastLocation().getLatitude();
                double longitude=locationResult.getLastLocation().getLongitude();

                /*String address = amapLocation.getProvince().concat(amapLocation.getCity());
                String la = String.valueOf(amapLocation.getLatitude());
                String lo = String.valueOf(amapLocation.getLongitude());
                mView.locationResult(address, lo, la);*/
            }
        };
    }

    @Override
    void stopLocation() {
        if (mClient == null) return;
        //停止定位
        mClient.removeLocationUpdates(mLocationCallback);
    }
}
