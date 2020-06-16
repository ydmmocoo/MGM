package com.fjx.mg.moments.add.pois;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.fjx.mg.R;
import com.fjx.mg.ToolBarManager;
import com.fjx.mg.food.adapter.RvGoogleMapSearchAdapter;
import com.fjx.mg.utils.HttpUtil;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.library.common.base.BaseMvpActivity;
import com.library.common.base.adapter.decoration.SpacesItemDecoration;
import com.library.common.utils.JsonUtil;
import com.library.common.utils.StatusBarManager;
import com.library.repository.models.GoogleMapGeocodeSearchBean;
import com.library.repository.models.GoogleMapKeywordSearchBean;
import com.library.repository.repository.RepositoryFactory;

import java.util.List;

import butterknife.BindView;
import okhttp3.Response;

public class AoisActivity extends BaseMvpActivity<AoisPresenter>
        implements AoisContract.View, OnMapReadyCallback {

    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.rv_search)
    RecyclerView mRvSearch;
    @BindView(R.id.et_address)
    EditText mEtAddress;
    private AoisAdapter aoisAdapter;

    private FusedLocationProviderClient mClient;
    private LocationRequest mLocationRequest;
    private LocationCallback mLocationCallback;
    private List<GoogleMapGeocodeSearchBean.ResultsBean> mList;

    private RvGoogleMapSearchAdapter mAdapter;
    private List<GoogleMapKeywordSearchBean.ResultsBean> mSearchList;

    private String mLat,mLng;
    private Marker mMarker;

    public static Intent newInstance(Context context) {
        return new Intent(context, AoisActivity.class);
    }

    @Override
    protected int layoutId() {
        return R.layout.activity_aois;
    }

    @Override
    protected AoisPresenter createPresenter() {
        return new AoisPresenter(this);
    }

    @Override
    protected void initView() {

        StatusBarManager.setLightFontColor(this, R.color.colorAccent);
        ToolBarManager.with(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //定位相关
        mClient = LocationServices.getFusedLocationProviderClient(getCurActivity());
        mLocationRequest = new LocationRequest()
                .setInterval(1000)
                .setFastestInterval(5000)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                double latitude = locationResult.getLastLocation().getLatitude();
                double longitude = locationResult.getLastLocation().getLongitude();
                RepositoryFactory.getLocalRepository().saveLatitude(String.valueOf(latitude));
                RepositoryFactory.getLocalRepository().saveLongitude(String.valueOf(longitude));

                getAddress(String.valueOf(latitude), String.valueOf(longitude));
            }
        };

        aoisAdapter = new AoisAdapter();
        recycler.setLayoutManager(new LinearLayoutManager(getCurContext()));
        recycler.setAdapter(aoisAdapter);
        recycler.addItemDecoration(new SpacesItemDecoration(1));

        mAdapter=new RvGoogleMapSearchAdapter();
        mRvSearch.setLayoutManager(new LinearLayoutManager(getCurContext()));
        mRvSearch.setAdapter(mAdapter);
        mRvSearch.addItemDecoration(new SpacesItemDecoration(1));

        aoisAdapter.setOnItemClickListener((adapter, view, position) -> {
            Intent intent = new Intent();
            intent.putExtra("lng", mList.get(position).getGeometry().getLocation().getLng());
            intent.putExtra("lat", mList.get(position).getGeometry().getLocation().getLat());
            intent.putExtra("adr", mList.get(position).getFormatted_address());
            setResult(9, intent);
            finish();
        });

        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                Intent intent = new Intent();
                intent.putExtra("lng", mSearchList.get(position).getGeometry().getLocation().getLng());
                intent.putExtra("lat", mSearchList.get(position).getGeometry().getLocation().getLat());
                intent.putExtra("adr", mSearchList.get(position).getName());
                setResult(9, intent);
                finish();
            }
        });

        //监听输入
        mEtAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s.toString().trim())) {
                    mRvSearch.setVisibility(View.VISIBLE);
                    recycler.setVisibility(View.GONE);
                    getAddressLatLng(mLat, mLng, s.toString());
                }else {
                    mRvSearch.setVisibility(View.GONE);
                    recycler.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public void LocationSuccess(List<GoogleMapGeocodeSearchBean.ResultsBean> pois) {
        mList = pois;
        aoisAdapter.setList(pois);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                LatLng target = cameraPosition.target;
                double latitude = target.latitude;
                double longitude = target.longitude;
                mLat=String.valueOf(latitude);
                mLng=String.valueOf(longitude);
                if (mMarker != null) {
                    mMarker.remove();
                }
                mMarker = googleMap.addMarker(new MarkerOptions().position(target));

                getAddress(String.valueOf(latitude), String.valueOf(longitude));
            }
        });
        String lat = RepositoryFactory.getLocalRepository().getLatitude();
        String lon = RepositoryFactory.getLocalRepository().getLongitude();
        if (!TextUtils.isEmpty(lat) && !TextUtils.isEmpty(lon)) {
            LatLng sydney = new LatLng(Double.parseDouble(lat), Double.parseDouble(lon));
            mMarker = googleMap.addMarker(new MarkerOptions().position(sydney));
            googleMap.setMinZoomPreference(14);
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        }
    }

    private void getAddress(String lat, String lon) {
        String language = RepositoryFactory.getLocalRepository().getLangugeType();
        String url = "https://maps.googleapis.com/maps/api/geocode/json?latlng=" + lat + "," + lon + "+"
                + "&key=AIzaSyAOtsgwEAwJ7SjsM1oHDVmp6oLfOS24Rj4&language=" + language;

        new HttpUtil().sendPost(url, new HttpUtil.OnRequestListener() {
            @Override
            public void onSuccess(String json) {
                GoogleMapGeocodeSearchBean data = JsonUtil.strToModel(json, GoogleMapGeocodeSearchBean.class);
                mList = data.getResults();
                mHandler.sendEmptyMessage(1);
            }

            @Override
            public void onFailed() {
            }

            @Override
            public void onSuccess(Response response) {
            }
        });
    }

    private void getAddressLatLng(String lat, String lng,String keyword){
        String language = RepositoryFactory.getLocalRepository().getLangugeType();
        String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="+lat+","+lng+"&radius=10000&type=restaurant&keyword="+keyword
                + "&key=AIzaSyAOtsgwEAwJ7SjsM1oHDVmp6oLfOS24Rj4&language=" + language;
        new HttpUtil().sendPost(url, new HttpUtil.OnRequestListener() {
            @Override
            public void onSuccess(String json) {
                GoogleMapKeywordSearchBean data = JsonUtil.strToModel(json, GoogleMapKeywordSearchBean.class);
                mSearchList = data.getResults();
                mHandler.sendEmptyMessage(2);
            }

            @Override
            public void onFailed() {
            }

            @Override
            public void onSuccess(Response response) {
            }
        });
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what==2){
                mAdapter.setList(mSearchList);
            }else {
                aoisAdapter.setList(mList);
            }
        }
    };
}
