package com.fjx.mg.moments.add.pois;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.library.common.utils.ContextManager;

class AoisPresenter extends AoisContract.Presenter {

    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption = null;

    AoisPresenter(AoisContract.View view) {
        super(view);
    }

    @Override
    void locationAddress() {
        mView.createAndShowDialog();
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
                    poiSearch(amapLocation.getLongitude(), amapLocation.getLatitude(), 2000, "" + lat, "" + lon);
                }
            }
        }

    };

    private void poiSearch(double longitude, double latitude, int distances, final String lat, final String lon) {
        LatLonPoint point = new LatLonPoint(latitude, longitude);
        GeocodeSearch geocodeSearch = new GeocodeSearch(ContextManager.getContext());
        RegeocodeQuery regeocodeQuery = new RegeocodeQuery(point, distances, geocodeSearch.AMAP);
        geocodeSearch.getFromLocationAsyn(regeocodeQuery);
        geocodeSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {
            @Override
            public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int rCode) {
                if (1000 == rCode) {
                    mView.destoryAndDismissDialog();
                    mView.LocationSuccess(regeocodeResult.getRegeocodeAddress().getPois(), lat, lon);
                }
            }

            @Override
            public void onGeocodeSearched(GeocodeResult geocodeResult, int rCode) {
            }
        });

    }
}

