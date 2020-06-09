package com.fjx.mg.network;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.fjx.mg.R;
import com.fjx.mg.network.adapter.GoogleMapsInfoWindowAdapter;
import com.fjx.mg.network.fragment.FilterDialogFragment;
import com.fjx.mg.network.fragment.MapDialogFragment;
import com.fjx.mg.network.mvp.MvolaNetworkContract;
import com.fjx.mg.network.mvp.MvolaNetworkPresenter;
import com.fjx.mg.utils.MapNaviUtils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.library.common.base.BaseMvpActivity;
import com.library.common.constant.IntentConstants;
import com.library.common.utils.CommonToast;
import com.library.common.utils.ContextManager;
import com.library.repository.models.ResponseModel;
import com.library.repository.models.SearchAgentListModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * Author    by hanlz
 * Date      on 2020/1/14.
 * Description：营业网点
 */
public class KiosqueNetworkActivity extends BaseMvpActivity<MvolaNetworkPresenter> implements
        MvolaNetworkContract.View, OnMapReadyCallback, GoogleMap.OnMarkerClickListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    private GoogleMap mMap;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption = null;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private LocationRequest mLocationRequest;
    private LocationCallback mLocationCallback;
    private Marker mSelectedMarker;
    private double lat;//获取纬度
    private double lng;//获取经度
    private String sName;
    private ArrayList<SearchAgentListModel> mItems;

    @BindView(R.id.etSearch)
    EditText mEtSearch;

    public static Intent newIntent(Context context, String type) {
        return newIntent(context, type, "");
    }

    /**
     * @param context
     * @param type    3:店铺，4:营业网点
     * @param remark  备注搜索
     * @return
     */
    public static Intent newIntent(Context context, String type, String remark) {
        Intent intent = new Intent(context, KiosqueNetworkActivity.class);
        intent.putExtra(IntentConstants.TYPE, type);
        intent.putExtra(IntentConstants.REMARK, remark);
        return intent;
    }

    @Override
    protected int layoutId() {
        return R.layout.act_mvola_network;
    }

    @Override
    protected void initView() {
        super.initView();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mEtSearch.setFocusable(false);
        location();
    }

    private void location() {
        showLoading();
        mlocationClient = new AMapLocationClient(this);
        mLocationOption = new AMapLocationClientOption();
        mlocationClient.setLocationListener(locationListener);
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        mLocationOption.setInterval(600000);
        mLocationOption.setOnceLocationLatest(true);
        mlocationClient.setLocationOption(mLocationOption);
        mlocationClient.startLocation();
//        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
//        mLocationRequest = LocationRequest.create();
//        mLocationRequest.setInterval(600000);
//        mLocationRequest.setFastestInterval(300000);
//        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
////        mLocationRequest
//        mLocationCallback = new LocationCallback(){
//            @Override
//            public void onLocationResult(LocationResult locationResult) {
//                super.onLocationResult(locationResult);
//
//            }
//        };
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        // Set listener for marker click event.  See the bottom of this class for its behavior.
        mMap.setOnMarkerClickListener(this);


    }

    private AMapLocationListener locationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation amapLocation) {
            if (amapLocation != null) {
                if (amapLocation.getErrorCode() == 0) {
                    mlocationClient.stopLocation();

                    lat = amapLocation.getLatitude();
                    lng = amapLocation.getLongitude();
                    sName = amapLocation.getAddress();
                    LatLng appointLoc = new LatLng(lat, lng);
                    // 移动地图到指定经度的位置
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(appointLoc, 15f));
//                    //添加标记到指定经纬度
//                    mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).title("Marker")
//                            .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher)));
                    mPresenter.requestAgentList(lng + "", lat + "", "4", "", "");
                } else {

                }
            }
        }

    };

    @Override
    protected MvolaNetworkPresenter createPresenter() {
        return new MvolaNetworkPresenter(this);
    }

    @Override
    public void responseAgentList(List<SearchAgentListModel> items, final String lng, final String lat) {
        hideLoading();
        if (items == null || items.size() <= 0) {
            CommonToast.toast(R.string.no_search_result);
            return;
        }
        mItems = (ArrayList<SearchAgentListModel>) items;
        for (int i = 0; i < items.size(); i++) {
            final SearchAgentListModel model = items.get(i);
            String latitude = items.get(i).getLat();
            String longitude = items.get(i).getLng();
            Marker marker = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude)))
                    .title(items.get(i).getAddress())
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.map_location_ic))
                    .snippet("Population: 2,074,200"));
            marker.setTag(items.get(i));
            mMap.setInfoWindowAdapter(new GoogleMapsInfoWindowAdapter(getCurContext()));
            mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(Marker marker) {
                    MapDialogFragment fragment = new MapDialogFragment();
                    fragment.show(getSupportFragmentManager(), model.getNickName());
                    fragment.setOnNavigationLinsenter(new MapDialogFragment.OnNavigationLinsenter() {
                        @Override
                        public void onGoogleMapNavi() {
                            if (MapNaviUtils.isGoogleMapInstalled(getCurActivity())) {
                                MapNaviUtils.startNaviGoogle(ContextManager.getContext(), model.getLat(), model.getLng());
                            } else {
                                MapNaviUtils.installGoogle(ContextManager.getContext());
                            }
                        }

                        @Override
                        public void onGaodeMapNavi() {
                            if (MapNaviUtils.isGdMapInstalled()) {
                                MapNaviUtils.openGaoDeNavi(ContextManager.getContext(), 0d, 0d, sName, Double.parseDouble(model.getLat()), Double.parseDouble(model.getLng()), model.getAddress());
                            } else {
                                MapNaviUtils.installGaode(ContextManager.getContext());
                            }
                        }

                        @Override
                        public void onBaiduMapNavi() {
                            if (MapNaviUtils.isBaiduMapInstalled()) {
                                MapNaviUtils.openBaiDuNavi(ContextManager.getContext(), Double.parseDouble(lat), Double.parseDouble(lng), sName,
                                        Double.parseDouble(model.getLat()), Double.parseDouble(model.getLng()), model.getAddress());
                            } else {
                                MapNaviUtils.installBaidu(ContextManager.getContext());
                            }
                        }
                    });
                }
            });
        }
    }

    @OnClick({R.id.ivRightIcon, R.id.etSearch, R.id.tvFilter, R.id.tvList})
    public void onClickView(View view) {
        switch (view.getId()) {
            case R.id.ivRightIcon://返回
                finish();
                break;
            case R.id.tvFilter://网店过滤
                FilterDialogFragment dialogFragment = new FilterDialogFragment();
                dialogFragment.show(getSupportFragmentManager(), "FilterDialogFragment");
                dialogFragment.setOnFilterListener(new FilterDialogFragment.OnFilterListener() {
                    @Override
                    public void onFilter(String money) {
                        showLoading();
                        if (mMap != null) {
                            mMap.clear();
                            mPresenter.requestAgentList(lng + "", lat + "", "4", "", money);
                        }
                    }
                });
                break;
            case R.id.etSearch://搜索
            case R.id.tvList://网点列表
                if (mItems == null || mItems.size() < -0) {
                    return;
                }
                startActivity(SalesNetworkSearchActivity.newIntent(getCurContext(), mItems, sName, lat + "", lng + ""));
                break;
            default:

        }
    }

    @Override
    public void responseFailed(ResponseModel data) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        marker.showInfoWindow();
        if (marker.equals(mSelectedMarker)) {
            mSelectedMarker = null;
            return true;
        }
        mSelectedMarker = marker;
        return false;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mMap != null && mSelectedMarker != null) {
            if (mSelectedMarker.isInfoWindowShown()) {
                mSelectedMarker.hideInfoWindow();
                return true;
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
