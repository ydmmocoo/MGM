package com.fjx.mg.setting.address.edit;

import android.text.TextUtils;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.fjx.mg.R;
import com.library.common.utils.CommonToast;
import com.library.common.utils.JsonUtil;
import com.library.repository.core.net.CommonObserver;
import com.library.repository.core.net.RxScheduler;
import com.library.repository.models.ResponseModel;
import com.library.repository.repository.RepositoryFactory;

public class EditAddressPresenter extends EditAddressContract.Presenter {
    //声明mlocationClient对象
    private AMapLocationClient mlocationClient;
    //声明mLocationOption对象
    private AMapLocationClientOption mLocationOption = null;

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
        mlocationClient = new AMapLocationClient(mView.getCurContext());
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        //设置定位监听
        mlocationClient.setLocationListener(locationListener);
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(600000);
        //设置定位参数
        mlocationClient.setLocationOption(mLocationOption);
        // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
        // 注意设置合适的定位时间的间隔（最小间隔支持为1000ms），并且在合适时间调用stopLocation()方法来取消定位请求
        // 在定位结束后，在合适的生命周期调用onDestroy()方法
        // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
        //启动定位
        mlocationClient.startLocation();
    }

    @Override
    void stopLocation() {
        if (mlocationClient == null) return;
        mlocationClient.stopLocation();
    }

    private AMapLocationListener locationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation amapLocation) {
            mView.hideLoading();
            Log.d("locationListener", JsonUtil.moderToString(amapLocation));
            if (amapLocation != null) {
                if (amapLocation.getErrorCode() == 0) {
                    //定位成功回调信息，设置相关消息
                    amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
                    amapLocation.getLatitude();//获取纬度
                    amapLocation.getLongitude();//获取经度
                    amapLocation.getAccuracy();//获取精度信息
                    String address = amapLocation.getProvince().concat(amapLocation.getCity());
                    String la = String.valueOf(amapLocation.getLatitude());
                    String lo = String.valueOf(amapLocation.getLongitude());
                    mView.locationResult(address, lo, la);
                } else {
                    //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                    Log.e("AmapError", "location Error, ErrCode:"
                            + amapLocation.getErrorCode() + ", errInfo:"
                            + amapLocation.getErrorInfo());
                }
            }
        }

    };


}
