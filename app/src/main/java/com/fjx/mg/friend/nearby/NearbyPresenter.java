package com.fjx.mg.friend.nearby;

import android.text.TextUtils;
import android.util.Log;

import com.fjx.mg.R;
import com.fjx.mg.utils.SharedPreferencesUtils;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.library.common.base.BaseApp;
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

    private FusedLocationProviderClient mClient;
    private LocationRequest mLocationRequest;
    private LocationCallback mLocationCallback;

    public NearbyPresenter(NearbyContact.View view) {
        super(view);
    }

    @Override
    void locationAddress() {
        mView.createAndShowDialog();
        //定位相关
        mClient= LocationServices.getFusedLocationProviderClient(mView.getCurActivity());
        mLocationRequest=new LocationRequest()
                .setInterval(1000)
                .setFastestInterval(5000)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationCallback=new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                double lat = locationResult.getLastLocation().getLatitude();//获取纬度
                double lon = locationResult.getLastLocation().getLongitude();//获取经度
                RepositoryFactory.getLocalRepository().saveLatitude(String.valueOf(lat));
                RepositoryFactory.getLocalRepository().saveLongitude(String.valueOf(lon));

                findAround(String.valueOf(lon), String.valueOf(lat));
            }
        };
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
}
