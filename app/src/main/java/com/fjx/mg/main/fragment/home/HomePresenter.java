package com.fjx.mg.main.fragment.home;

import android.util.Log;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.fjx.mg.R;
import com.fjx.mg.utils.SharedPreferencesHelper;
import com.fjx.mg.utils.SharedPreferencesUtils;
import com.library.common.base.BaseApp;
import com.library.common.utils.CommonToast;
import com.library.common.utils.JsonUtil;
import com.library.common.utils.SharedPreferencesUtil;
import com.library.repository.core.net.CommonObserver;
import com.library.repository.core.net.RxScheduler;
import com.library.repository.data.UserCenter;
import com.library.repository.db.DBDaoFactory;
import com.library.repository.models.AdListModel;
import com.library.repository.models.NewsItemModel;
import com.library.repository.models.NewsListModel;
import com.library.repository.models.ResponseModel;
import com.library.repository.repository.RepositoryFactory;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.enums.PopupAnimation;
import com.lxj.xpopup.interfaces.OnSelectListener;
import com.tencent.qcloud.uikit.TimConfig;

class HomePresenter extends HomeContract.Presenter {

    //声明mlocationClient对象
    private AMapLocationClient mlocationClient;
    //声明mLocationOption对象
    private AMapLocationClientOption mLocationOption = null;

    HomePresenter(HomeContract.View view) {
        super(view);
    }

    @Override
    void getRecommendStore() {
        mView.showMarqueeView(null);
        mView.showRecommendStore();
    }

    @Override
    void getNewsList(final int page) {
        RepositoryFactory.getRemoteNewsRepository().newsList(1, page, "")
                .compose(RxScheduler.<ResponseModel<NewsItemModel>>toMain())
                .as(mView.<ResponseModel<NewsItemModel>>bindAutoDispose())
                .subscribe(new CommonObserver<NewsItemModel>() {
                    @Override
                    public void onSuccess(NewsItemModel data) {
                        if (mView != null) {
                            mView.showNewsList(data);
                        }
                        try {
                            if (data.getNewsList() != null && data.getNewsList().size() > 0) {
                                for (NewsListModel item : data.getNewsList()) {
                                    item.setTypeId(1);
                                    item.setUniqueId(String.valueOf(1).concat("_").concat(item.getNewsId()));
                                }
                            }
                            if (page == 1) {
                                DBDaoFactory.getNewsListDao().deleteAll(1);
                                DBDaoFactory.getNewsListDao().insertList(data.getNewsList());
                            }
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        if (mView != null) {
                            mView.responseFailed(data);
                        }
                        CommonToast.toast(data.getMsg());
                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                        if (mView != null)
                            mView.hideLoading();
                        CommonToast.toast(data.getMsg());
                    }
                });
    }

    @Override
    void locationAddress() {
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
    void recUseApp(String appId) {
        RepositoryFactory.getRemoteNewsRepository()
                .recUseApp(appId)
                .compose(RxScheduler.<ResponseModel<Object>>toMain())
                .as(mView.<ResponseModel<Object>>bindAutoDispose())
                .subscribe(new CommonObserver<Object>() {
                    @Override
                    public void onSuccess(Object data) {
                    }

                    @Override
                    public void onError(ResponseModel data) {
                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                    }
                });
    }

    @Override
    void showLanguageDialog(TextView view) {
        //( zh-ch:中文简体,zh-tw:中文繁体，en-us:英语，fr:法语)
        String[] texts = null;
        if (TimConfig.isRelease) {
            texts = new String[]{"简体", "繁体", "English", "Français"};
        } else {
            texts = new String[]{"中文简体", "中文繁体", "English", "Français"};
        }

        new XPopup.Builder(mView.getCurActivity())
                .atView(view)// 依附于所点击的View，内部会自动判断在上方或者下方显示
                .hasShadowBg(false)
                .popupAnimation(PopupAnimation.ScrollAlphaFromLeftTop)
                .asAttachList(texts, new int[]{},
                        (position, text) -> {
                            mView.showCheckLanguage(position, text);
                            String l = "zh-ch";
                            if (position == 1) {
                                l = "zh-tw";
                            } else if (position == 2) {
                                l = "en-us";
                            } else if (position == 3) {
                                l = "fr";
                            }
                            RepositoryFactory.getLocalRepository().saveLanguageType(l);
                        })
                .show();
    }

    @Override
    void getAd() {
        RepositoryFactory.getRemoteRepository().getAd("1")
                .compose(RxScheduler.<ResponseModel<AdListModel>>toMain())
                .as(mView.<ResponseModel<AdListModel>>bindAutoDispose())
                .subscribe(new CommonObserver<AdListModel>() {
                    @Override
                    public void onSuccess(AdListModel data) {

                        if (mView != null) {
                            mView.showBanners(data);
                        }
                        SharedPreferencesUtil.name("BannerList".concat(String.valueOf(TimConfig.isRelease))).put("jsonHomeshowBanners", JsonUtil.moderToString(data)).apply();
                    }

                    @Override
                    public void onError(ResponseModel data) {

                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                    }
                });
    }

    private AMapLocationListener locationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation amapLocation) {
            Log.d("locationListener", JsonUtil.moderToString(amapLocation));
            if (amapLocation != null) {
                if (amapLocation.getErrorCode() == 0) {
                    mlocationClient.stopLocation();
                    //定位成功回调信息，设置相关消息
                    amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
                    amapLocation.getLatitude();//获取纬度
                    amapLocation.getLongitude();//获取经度
                    amapLocation.getAccuracy();//获取精度信息
                    String address = amapLocation.getCity();
                    SharedPreferencesUtils.setString(BaseApp.getInstance(),"address",address);
                    RepositoryFactory.getLocalRepository().saveLatitude(String.valueOf(amapLocation.getLatitude()));
                    RepositoryFactory.getLocalRepository().saveLongitude(String.valueOf(amapLocation.getLongitude()));
//                    mView.showLocation(address);
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
