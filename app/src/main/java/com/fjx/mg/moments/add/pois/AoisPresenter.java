package com.fjx.mg.moments.add.pois;


import com.fjx.mg.utils.HttpUtil;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.library.common.utils.JsonUtil;
import com.library.repository.models.GoogleMapGeocodeSearchBean;
import com.library.repository.repository.RepositoryFactory;

import okhttp3.Response;

import static com.library.common.utils.RxJavaUtls.runOnUiThread;

class AoisPresenter extends AoisContract.Presenter {

    AoisPresenter(AoisContract.View view) {
        super(view);
    }

    @Override
    void locationAddress() {
        String lat=RepositoryFactory.getLocalRepository().getLatitude();
        String lon=RepositoryFactory.getLocalRepository().getLongitude();
        String language = RepositoryFactory.getLocalRepository().getLangugeType();
        String url = "https://maps.googleapis.com/maps/api/geocode/json?latlng=" + lat + "," + lon + "+"
                + "&key=AIzaSyAOtsgwEAwJ7SjsM1oHDVmp6oLfOS24Rj4&language=" + language;

        new HttpUtil().sendPost(url, new HttpUtil.OnRequestListener() {
            @Override
            public void onSuccess(String json) {
                runOnUiThread(() -> {
                    GoogleMapGeocodeSearchBean data= JsonUtil.strToModel(json,GoogleMapGeocodeSearchBean.class);
                    if (data!=null) {
                        mView.LocationSuccess(data.getResults());
                    }
                });
            }

            @Override
            public void onFailed() {
            }

            @Override
            public void onSuccess(Response response) {
            }
        });
    }
}

