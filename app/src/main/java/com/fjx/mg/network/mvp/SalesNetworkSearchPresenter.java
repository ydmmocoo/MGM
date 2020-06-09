package com.fjx.mg.network.mvp;


import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.library.repository.core.net.CommonObserver;
import com.library.repository.core.net.RxScheduler;
import com.library.repository.models.ResponseModel;
import com.library.repository.models.SearchAgentModel;
import com.library.repository.repository.RepositoryFactory;

public class SalesNetworkSearchPresenter extends SalesNetworkSearchContract.Presenter {

    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption = null;

    public SalesNetworkSearchPresenter(SalesNetworkSearchContract.View view) {
        super(view);

    }

    @Override
    public void requestAgentList(final String lng, final String lat, String type, String remark, String price, final String sName) {
        RepositoryFactory.getRemoteSalesNetworkApi()
                .agentList(lng, lat, type, remark, price)
                .compose(RxScheduler.<ResponseModel<SearchAgentModel>>toMain())
                .as(mView.<ResponseModel<SearchAgentModel>>bindAutoDispose())
                .subscribe(new CommonObserver<SearchAgentModel>() {
                    @Override
                    public void onSuccess(SearchAgentModel data) {
                        if (mView != null) {
                            mView.responseAgentList(data.getAgentList(), lng, lat, sName);
                            mView.hideLoading();
                        }
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        if (mView != null) {
                            mView.responseFailed(data);
                            mView.hideLoading();
                        }
                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                        if (mView != null) {
                            mView.responseFailed(data);
                            mView.hideLoading();
                        }
                    }
                });
    }

    @Override
    public void requestLocationAddress() {
        if (mView != null) {
            mView.showLoading();
        }
        mlocationClient = new AMapLocationClient(mView.getCurContext());
        mLocationOption = new AMapLocationClientOption();
        mlocationClient.setLocationListener(locationListener);
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        mLocationOption.setInterval(600000);
        mLocationOption.setOnceLocationLatest(true);
        mlocationClient.setLocationOption(mLocationOption);
        mlocationClient.startLocation();
    }

    private AMapLocationListener locationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation amapLocation) {
            if (amapLocation != null) {
                if (amapLocation.getErrorCode() == 0) {
                    mlocationClient.stopLocation();
                    double lat = amapLocation.getLatitude();//获取纬度
                    double lon = amapLocation.getLongitude();//获取经度
                    String sName = amapLocation.getAddress();
                    if (mView != null) {
                        mView.responseLocationAddress(lon + "", lat + "", sName);
                    }
                } else {
                    if (mView != null) {
                        mView.responseFailed(null);

                    }
                }
            }
        }

    };
}
