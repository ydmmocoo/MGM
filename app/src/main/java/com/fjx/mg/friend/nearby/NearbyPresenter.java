package com.fjx.mg.friend.nearby;

import android.text.TextUtils;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.fjx.mg.R;
import com.library.common.utils.CommonToast;
import com.library.common.utils.JsonUtil;
import com.library.common.utils.StringUtil;
import com.library.repository.Constant;
import com.library.repository.core.net.CommonObserver;
import com.library.repository.core.net.RxScheduler;
import com.library.repository.data.UserCenter;
import com.library.repository.models.NearbyUserModel;
import com.library.repository.models.ResponseModel;
import com.library.repository.repository.RepositoryFactory;
import com.tencent.imsdk.TIMUserProfile;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.friendship.TIMFriend;

import java.util.List;

public class NearbyPresenter extends NearbyContact.Presenter {
    //声明mlocationClient对象
    private AMapLocationClient mlocationClient;
    //声明mLocationOption对象
    private AMapLocationClientOption mLocationOption = null;

    public NearbyPresenter(NearbyContact.View view) {
        super(view);
    }

    @Override
    void locationAddress() {
        mView.createAndShowDialog();
        mlocationClient = new AMapLocationClient(mView.getCurContext());
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        //设置定位监听
        mlocationClient.setLocationListener(locationListener);
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
//        //设置定位间隔,单位毫秒,默认为2000ms
//        mLocationOption.setInterval(600000);
        //设置定位参数
        mlocationClient.setLocationOption(mLocationOption);
        mLocationOption.setOnceLocation(true);
        // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
        // 注意设置合适的定位时间的间隔（最小间隔支持为1000ms），并且在合适时间调用stopLocation()方法来取消定位请求
        // 在定位结束后，在合适的生命周期调用onDestroy()方法
        // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
        //启动定位
        mlocationClient.startLocation();

    }

    @Override
    void findAround(String lng, String lat) {
        RepositoryFactory.getRemoteRepository().findAround(lng, lat)
                .compose(RxScheduler.<ResponseModel<List<NearbyUserModel>>>toMain())
                .as(mView.<ResponseModel<List<NearbyUserModel>>>bindAutoDispose())
                .subscribe(new CommonObserver<List<NearbyUserModel>>() {
                    @Override
                    public void onSuccess(List<NearbyUserModel> data) {
                        mView.destoryAndDismissDialog();
                        if (mView != null && data != null) {
                            mView.showAroundUsers(data);
                        }
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        mView.destoryAndDismissDialog();
                        if (StringUtil.isNotEmpty(data.getMsg())) {
                            CommonToast.toast(data.getMsg());
                        }
                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                        mView.destoryAndDismissDialog();
                        if (StringUtil.isNotEmpty(data.getMsg())) {
                            CommonToast.toast(data.getMsg());
                        }
                    }
                });

    }

    @Override
    void findImUser(String uid) {
        if (mView != null) {
            mView.createAndShowDialog();
        }
        RepositoryFactory.getChatRepository().getUsersProfile(uid, true,
                new TIMValueCallBack<List<TIMUserProfile>>() {

                    @Override
                    public void onError(int i, String s) {
                        if (mView != null) {
                            mView.hideLoading();
                        }
                        if (TextUtils.equals(s, "Err_Profile_Invalid_Account")) {
                            CommonToast.toast(mView.getCurContext().getString(R.string.no_user));
                        }
                    }

                    @Override
                    public void onSuccess(List<TIMUserProfile> timUserProfiles) {
                        if (mView != null) {
                            mView.destoryAndDismissDialog();
                        }
                        Log.d(Constant.TIM_LOG, JsonUtil.moderToString(timUserProfiles));
                        if (timUserProfiles.size() == 0) return;
                        getAllFriend(timUserProfiles.get(0));
                    }
                });
    }

    void getAllFriend(final TIMUserProfile userProfile) {
        TIMFriend friend = UserCenter.getFriend(userProfile.getIdentifier());
        if (mView != null) {
            if (friend == null) {
                mView.getImUserSuccess(userProfile);
            } else {
                mView.getImUserSuccess(friend);
            }
        }
    }

    private AMapLocationListener locationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation amapLocation) {
//            mView.showLoading();
            Log.d("locationListener", JsonUtil.moderToString(amapLocation));
            if (amapLocation != null) {
                if (amapLocation.getErrorCode() == 0) {
                    mlocationClient.stopLocation();
                    //定位成功回调信息，设置相关消息
                    amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
                    double lat = amapLocation.getLatitude();//获取纬度
                    double lon = amapLocation.getLongitude();//获取经度
                    amapLocation.getAccuracy();//获取精度信息
                    RepositoryFactory.getLocalRepository().saveLatitude(String.valueOf(amapLocation.getLatitude()));
                    RepositoryFactory.getLocalRepository().saveLongitude(String.valueOf(amapLocation.getLongitude()));
                    findAround(String.valueOf(lon), String.valueOf(lat));

                } else {
                    //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                    Log.e("AmapError", "location Error, ErrCode:"
                            + amapLocation.getErrorCode() + ", errInfo:"
                            + amapLocation.getErrorInfo());
                    mView.destoryAndDismissDialog();
                }
            }
        }

    };


}
