package com.fjx.mg.network;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.fjx.mg.R;
import com.fjx.mg.network.adapter.SalesNetworkSearchAdapter;
import com.fjx.mg.network.fragment.FilterDialogFragment;
import com.fjx.mg.network.fragment.MapDialogFragment;
import com.fjx.mg.network.mvp.SalesNetworkSearchContract;
import com.fjx.mg.network.mvp.SalesNetworkSearchPresenter;
import com.fjx.mg.utils.HttpUtil;
import com.fjx.mg.utils.MapNaviUtils;
import com.fjx.mg.utils.SharedPreferencesUtils;
import com.fjx.mg.widget.recyclerview.LinearManagerItemDecaration;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.hjq.permissions.OnPermission;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.library.common.base.BaseApp;
import com.library.common.base.BaseMvpActivity;
import com.library.common.utils.JsonUtil;
import com.library.common.utils.StatusBarManager;
import com.library.repository.models.GoogleMapGeocodeSearchBean;
import com.library.repository.models.ResponseModel;
import com.library.repository.models.SearchAgentListModel;
import com.library.repository.repository.RepositoryFactory;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Response;

/**
 * Author    by hanlz
 * Date      on 2019/10/20.
 * Description：营业网点搜索页
 */
public class SalesNetworkSearchActivity extends BaseMvpActivity<SalesNetworkSearchPresenter> implements OnItemClickListener, SalesNetworkSearchContract.View {


    private String mRemark, mPrice;
    @BindView(R.id.rvSearch)
    RecyclerView mRvSearch;
    private SalesNetworkSearchAdapter mAdapter;
    @BindView(R.id.etSearch)
    EditText mEtSearch;
    @BindView(R.id.tvSearchGo)
    TextView mTvSearchGo;
    private String lng;
    private String lat;
    private String sName;

    private FusedLocationProviderClient mClient;
    private LocationRequest mLocationRequest;
    private LocationCallback mLocationCallback;

    public static Intent newIntent(Context context, ArrayList<SearchAgentListModel> items, String sName, String lat, String lng) {
        Intent intent = new Intent(context, SalesNetworkSearchActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("items", items);
        bundle.putString("name", sName);
        bundle.putString("lat", lat);
        bundle.putString("lng", lng);
        intent.putExtra("ext", bundle);
        return intent;
    }


    @Override
    protected int layoutId() {
        return R.layout.atc_sales_network_seach;
    }

    @Override
    protected void initView() {
        super.initView();
        StatusBarManager.setLightFontColor(this, R.color.colorAccent);
        mRvSearch.addItemDecoration(new LinearManagerItemDecaration(1, LinearManagerItemDecaration.VERTICAL_LIST));
        mAdapter = new SalesNetworkSearchAdapter();
        mAdapter.setEmptyView(R.layout.layout_empty);
        mAdapter.setOnItemClickListener(this);
        mAdapter.setOnItemClickListener(this);
        mTvSearchGo.setVisibility(View.VISIBLE);
        if (getIntent() == null) {
            requestLocationAddress();
        } else {
            Bundle ext = getIntent().getBundleExtra("ext");
            sName = ext.getString("name");
            lat = ext.getString("lat");
            lng = ext.getString("lng");
            if (ext != null && !TextUtils.isEmpty(sName) && !TextUtils.isEmpty(lat) && !TextUtils.isEmpty(lng)) {
                ArrayList<SearchAgentListModel> items = ext.getParcelableArrayList("items");
                mAdapter.setList(items);
            } else {
                requestLocationAddress();
            }
        }
    }


    @OnClick({R.id.ivRightIcon, R.id.tvSearchGo, R.id.tvFilter, R.id.tvMap})
    public void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.tvMap://回到地图页
            case R.id.ivRightIcon://返回
                finish();
                break;
            case R.id.tvSearchGo://搜索
                search();
                break;
            case R.id.tvFilter://网店过滤
                filter();
                break;
            default:
        }
    }


    private void filter() {
        FilterDialogFragment dialogFragment = new FilterDialogFragment();
        dialogFragment.show(getSupportFragmentManager(), "FilterDialogFragment");
        dialogFragment.setOnFilterListener(new FilterDialogFragment.OnFilterListener() {
            @Override
            public void onFilter(String money) {
                mPrice = money;
                mRemark = "";
                requestLocationAddress();
            }
        });
    }


    private void search() {
        mRemark = mEtSearch.getText().toString();
        mPrice = "";
        requestLocationAddress();
    }


    @Override
    protected SalesNetworkSearchPresenter createPresenter() {
        return new SalesNetworkSearchPresenter(this);
    }

    @Override
    public void responseAgentList(List<SearchAgentListModel> items, String lng, String lat, String sName) {
        mAdapter.setList(items);
        this.lng = lng;
        this.lat = lat;
        this.sName = sName;
    }

    @Override
    public void responseFailed(ResponseModel data) {
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        final SearchAgentListModel item = (SearchAgentListModel) adapter.getItem(position);
        MapDialogFragment fragment = new MapDialogFragment();
        fragment.show(getSupportFragmentManager(), "SalesNetworkSearchActivity");
        fragment.setOnNavigationLinsenter(new MapDialogFragment.OnNavigationLinsenter() {
            @Override
            public void onGoogleMapNavi() {
                if (MapNaviUtils.isGoogleMapInstalled(getCurActivity())){
                    MapNaviUtils.startNaviGoogle(getCurActivity(),item.getLat(),item.getLng());
                }else {
                    MapNaviUtils.installGoogle(getCurActivity());
                }
            }

            @Override
            public void onGaodeMapNavi() {
                if (MapNaviUtils.isGdMapInstalled()) {
                    MapNaviUtils.openGaoDeNavi(getCurActivity(), 0d, 0d, sName, Double.parseDouble(item.getLat()), Double.parseDouble(item.getLng()), item.getAddress());
                } else {
                    MapNaviUtils.installGaode(getCurActivity());
                }
            }

            @Override
            public void onBaiduMapNavi() {
                if (MapNaviUtils.isBaiduMapInstalled()) {
                    MapNaviUtils.openBaiDuNavi(getCurActivity(), Double.parseDouble(lat), Double.parseDouble(lng), sName,
                            Double.parseDouble(item.getLat()), Double.parseDouble(item.getLng()), item.getAddress());
                } else {
                    MapNaviUtils.installBaidu(getCurActivity());
                }
            }
        });
    }

    private void requestLocationAddress(){
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
                lat=String.valueOf(locationResult.getLastLocation().getLatitude());
                lng=String.valueOf(locationResult.getLastLocation().getLongitude());

                getAddress(lat,lng);
            }
        };

        requestLocationUpdate();
    }

    private void getAddress(String lat, String lon) {
        String language = RepositoryFactory.getLocalRepository().getLangugeType();
        String url = "https://maps.googleapis.com/maps/api/geocode/json?latlng=" + lat + "," + lon + "+"
                + "&key=AIzaSyAOtsgwEAwJ7SjsM1oHDVmp6oLfOS24Rj4&language=" + language;

        new HttpUtil().sendPost(url, new HttpUtil.OnRequestListener() {
            @Override
            public void onSuccess(String json) {
                GoogleMapGeocodeSearchBean data = JsonUtil.strToModel(json, GoogleMapGeocodeSearchBean.class);
                sName = data.getResults().get(0).getFormatted_address();
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

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mPresenter.requestAgentList(lng, lat, "4", mRemark, mPrice, sName);
        }
    };

    @SuppressLint("MissingPermission")
    private void requestLocationUpdate() {
        XXPermissions.with(getCurActivity())
                .permission(Permission.Group.LOCATION)
                .request(new OnPermission() {

                    @Override
                    public void hasPermission(List<String> granted, boolean all) {
                        if (all) {
                            //启动定位
                            mClient.requestLocationUpdates(mLocationRequest, mLocationCallback, null);
                        } else {
                        }
                    }

                    @Override
                    public void noPermission(List<String> denied, boolean quick) {
                    }
                });
    }
}
