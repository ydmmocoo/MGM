package com.fjx.mg.food;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fjx.mg.R;
import com.fjx.mg.dialog.FilterDialog;
import com.fjx.mg.food.adapter.GvClassificationAdapter;
import com.fjx.mg.food.adapter.GvSecondMenuAdapter;
import com.fjx.mg.food.adapter.GvSelectedGoodStoreAdapter;
import com.fjx.mg.food.adapter.RvStoreAdapter;
import com.fjx.mg.food.contract.FoodContract;
import com.fjx.mg.food.presenter.FoodPresenter;
import com.fjx.mg.main.MainActivity;
import com.fjx.mg.moments.add.pois.AoisActivity;
import com.fjx.mg.setting.address.list.AddressListActivity;
import com.fjx.mg.utils.GPSUtils;
import com.fjx.mg.utils.HttpUtil;
import com.fjx.mg.utils.SharedPreferencesUtils;
import com.fjx.mg.view.WrapContentGridView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.hjq.permissions.OnPermission;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.library.common.base.BaseApp;
import com.library.common.base.BaseMvpFragment;
import com.library.common.utils.CommonToast;
import com.library.common.utils.DimensionUtil;
import com.library.common.utils.JsonUtil;
import com.library.common.view.BannerView;
import com.library.repository.models.AdModel;
import com.library.repository.models.GoogleMapGeocodeSearchBean;
import com.library.repository.models.HomeShopListBean;
import com.library.repository.models.HotShopBean;
import com.library.repository.models.ShopTypeBean;
import com.library.repository.repository.RepositoryFactory;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.enums.PopupPosition;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import okhttp3.Response;

import static android.content.Context.LOCATION_SERVICE;

public class FoodFragment extends BaseMvpFragment<FoodPresenter> implements View.OnClickListener, FoodContract.View {

    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.tv_address)
    TextView mTvAddress;
    @BindView(R.id.rv_content)
    RecyclerView mRvContent;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout mRefreshLayout;
    @BindView(R.id.v_search)
    View mVSearch;
    private WrapContentGridView mGvMenu;
    private WrapContentGridView mGvSecondMenu;
    private CardView mCardView;
    private BannerView mBanner;
    private WrapContentGridView mGvSelectedGoodStore;
    private TextView mTvType;
    private TextView mTvComprehensiveRanking;
    private TextView mTvDistance;

    private GvClassificationAdapter mGvClassificationAdapter;
    private GvSecondMenuAdapter mSecondMenuAdapter;
    private List<ShopTypeBean.ShopTypeListBean> mClassificationList = new ArrayList<>();
    private List<ShopTypeBean.ShopTypeListBean> mSecondMenuList = new ArrayList<>();
    private GvSelectedGoodStoreAdapter mGvSelectedGoodStoreAdapter;
    private List<HotShopBean.ShopListBean> mSelectedShopList;
    private RvStoreAdapter mAdapter;
    private List<HomeShopListBean.ShopListBean> mList = new ArrayList<>();
    private int mPage = 1;

    private String mServiceId = "";
    private String mSecondServiceId = "";

    private boolean mIsFirst = true;
    private FusedLocationProviderClient mClient;
    private LocationRequest mLocationRequest;
    private LocationCallback mLocationCallback;

    private FilterDialog mTypeDialog;
    private List<String> mTypeList = new ArrayList<>();
    private FilterDialog mDialog;
    private String mOrder = "";
    private Drawable mDrawable;
    private Drawable mSelectedDrawable;

    public static FoodFragment newInstance() {
        Bundle args = new Bundle();
        FoodFragment fragment = new FoodFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int layoutId() {
        return R.layout.fragment_food;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mDrawable = ContextCompat.getDrawable(getCurContext(), R.mipmap.icon_arrow_down_black);
        mDrawable.setBounds(0, 0, mDrawable.getMinimumWidth(), mDrawable.getMinimumHeight());
        mSelectedDrawable = ContextCompat.getDrawable(getCurContext(), R.mipmap.icon_arrow_up_red);
        mSelectedDrawable.setBounds(0, 0, mSelectedDrawable.getMinimumWidth(), mSelectedDrawable.getMinimumHeight());
        //设置地址
        String address = SharedPreferencesUtils.getString(getCurContext(), "address", "");
        if (!TextUtils.isEmpty(address)) {
            mTvAddress.setText(address);
        }
        View headView = View.inflate(getCurContext(), R.layout.item_food_header, null);
        mGvMenu = headView.findViewById(R.id.gv_menu);
        mGvSecondMenu = headView.findViewById(R.id.gv_second_menu);
        mCardView = headView.findViewById(R.id.cvBanner);
        mBanner = headView.findViewById(R.id.banner);
        mGvSelectedGoodStore = headView.findViewById(R.id.gv_selected_good_store);
        mTvType = headView.findViewById(R.id.tv_type);
        mTvComprehensiveRanking = headView.findViewById(R.id.tv_comprehensive_ranking);
        mTvDistance = headView.findViewById(R.id.tv_distance);
        //宽高5：1
        CardView.LayoutParams params = (CardView.LayoutParams) mBanner.getLayoutParams();
        int width = DimensionUtil.getScreenWith() - DimensionUtil.dip2px(20);
        params.width = width;
        params.height = width / 4;
        mBanner.setLayoutParams(params);
        //初始化分类
        mGvClassificationAdapter = new GvClassificationAdapter(getCurContext(), mClassificationList);
        mGvMenu.setAdapter(mGvClassificationAdapter);
        mSecondMenuAdapter = new GvSecondMenuAdapter(getCurContext(), mSecondMenuList);
        mGvSecondMenu.setAdapter(mSecondMenuAdapter);
        //初始化优选好店
        mGvSelectedGoodStoreAdapter = new GvSelectedGoodStoreAdapter(getCurContext(), mSelectedShopList);
        mGvSelectedGoodStore.setAdapter(mGvSelectedGoodStoreAdapter);
        //初始化RecyclerView
        mAdapter = new RvStoreAdapter(R.layout.item_rv_store, mList);
        mAdapter.addHeaderView(headView);
        View footerView = View.inflate(getCurContext(), R.layout.item_rv_store_list_footer, null);
        mAdapter.addFooterView(footerView);
        LinearLayoutManager manager = new LinearLayoutManager(getCurContext());
        mRvContent.setLayoutManager(manager);
        mRvContent.setAdapter(mAdapter);
        //分类筛选默认为全部美食
        mTvType.setText(getResources().getString(R.string.all_delicacies));

        requestLocationUpdate();

        mRefreshLayout.setEnableLoadMore(false);
        mPresenter.getShopTypeList("");
        mPresenter.getAd();
        mPresenter.getHotShops();
        //mPresenter.getShopsList(mServiceId, mSecondServiceId, mOrder, mPage);

        setListener();
    }

    private void setListener() {
        mTvAddress.setOnClickListener(this);
        mVSearch.setOnClickListener(this);
        mTvType.setOnClickListener(this);
        mTvComprehensiveRanking.setOnClickListener(this);
        mTvDistance.setOnClickListener(this);
        //分类
        mGvMenu.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(getCurContext(), MenuStoreListActivity.class);
            intent.putExtra("title", mClassificationList.get(position).getName());
            intent.putExtra("serviceId", mClassificationList.get(position).getCId());
            if ("1".equals(mClassificationList.get(position).getCId())) {
                intent.putExtra("menu_data", JsonUtil.moderToString(mSecondMenuList));
            }
            startActivity(intent);
        });
        mGvSecondMenu.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(getCurContext(), MenuStoreListActivity.class);
            intent.putExtra("title", mSecondMenuList.get(position).getName());
            intent.putExtra("serviceId", mSecondMenuList.get(position).getCId());
            intent.putExtra("secondServiceId", mSecondMenuList.get(position).getSecondId());
            startActivity(intent);
        });
        //轮播图
        //优选好店
        mGvSelectedGoodStore.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(getCurContext(), StoreDetailActivity.class);
            intent.putExtra("id", mSelectedShopList.get(position).getSId());
            startActivity(intent);
        });
        //店铺列表Item点击事件
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            Intent intent = new Intent(getCurContext(), StoreDetailActivity.class);
            intent.putExtra("id", mList.get(position).getSId());
            startActivity(intent);
        });
        //下拉刷新、上拉加载更多
        mRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                mPage++;
                mPresenter.getShopsList(mServiceId, mSecondServiceId, mOrder, mPage);
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mPage = 1;
                mPresenter.getShopTypeList("");
                mPresenter.getAd();
                mPresenter.getHotShops();
                mPresenter.getShopsList(mServiceId, mSecondServiceId, mOrder, mPage);
            }
        });
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.iv_back://返回
                break;
            case R.id.tv_address://选择地址
                startActivityForResult(AddressListActivity.newInstance(getCurContext(), true), 9);
                break;
            case R.id.v_search://搜索
                intent = new Intent(getCurContext(), TakeOutFoodSearchActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_type://类型
                if (mTypeDialog == null || !mTypeDialog.isShow()) {
                    showTypeDialog();
                } else {
                    mTypeDialog.dismiss();
                }
                break;
            case R.id.tv_comprehensive_ranking://综合排序
                if (mDialog == null || !mDialog.isShow()) {
                    showDialog();
                } else {
                    mDialog.dismiss();
                }
                break;
            case R.id.tv_distance://距离
                mTvDistance.setTextColor(ContextCompat.getColor(getCurContext(), R.color.colorAccent));
                mTvComprehensiveRanking.setTextColor(ContextCompat.getColor(getCurContext(), R.color.gray_text));
                mTvComprehensiveRanking.setText(getResources().getString(R.string.comprehensive_ranking));
                mOrder = "4";
                if (TextUtils.isEmpty(mAddress)) {
                    mPage = 1;
                    mPresenter.getShopsList(mServiceId, mSecondServiceId, mOrder, mPage);
                }
                break;
        }
    }

    @Override
    protected FoodPresenter createPresenter() {
        return new FoodPresenter(this);
    }

    @Override
    public void getShopTypeListSuccess(List<ShopTypeBean.ShopTypeListBean> data) {
        mClassificationList.clear();
        mSecondMenuList.clear();
        mTypeList.clear();
        for (int i = 0; i < data.size(); i++) {
            if (i < 5) {
                mClassificationList.add(data.get(i));
            } else {
                mTypeList.add(data.get(i).getName());
                mSecondMenuList.add(data.get(i));
            }
        }
        mTypeList.add(0, getResources().getString(R.string.all_delicacies));
        mGvClassificationAdapter.setData(mClassificationList);
        mSecondMenuAdapter.setData(mSecondMenuList);
    }

    @Override
    public void getBannerDataSuccess(List<AdModel> data) {
        if (data == null) {
            return;
        }
        List<String> imageUrl = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            imageUrl.add(data.get(i).getImg());
        }
        mBanner.showImages(imageUrl);
        if (imageUrl.size() == 0) {
            mCardView.setVisibility(View.GONE);
        }
    }

    @Override
    public void getHotShopsSuccess(List<HotShopBean.ShopListBean> data) {
        mSelectedShopList = data;
        mGvSelectedGoodStoreAdapter.setData(mSelectedShopList);
    }

    @Override
    public void getShopListSuccess(List<HomeShopListBean.ShopListBean> data, boolean mHasNext) {
        if (!mHasNext) {
            mRefreshLayout.setEnableLoadMore(false);
        } else {
            mRefreshLayout.setEnableLoadMore(true);
        }
        if (mPage == 1) {
            mList = data;
            if (mRefreshLayout.isRefreshing()) {
                mRefreshLayout.finishRefresh();
            }
        } else {
            mList.addAll(data);
            mRefreshLayout.finishLoadMore();
        }
        mAdapter.setList(mList);
    }

    @Override
    public void getShopListFailure() {
        if (mPage == 1) {
            mRefreshLayout.finishRefresh();
        } else {
            mRefreshLayout.finishLoadMore();
        }
    }

    @Override
    public void getAddressSuccess(String address) {
        SharedPreferencesUtils.setString(BaseApp.getInstance(), "address", address);
        mTvAddress.setText(address);
    }

    @SuppressLint("MissingPermission")
    private void locationAddress() {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = googleApiAvailability.isGooglePlayServicesAvailable(getActivity());

        //获取系统的LocationManager对象
        LocationManager locationManager = (LocationManager) getCurActivity().getSystemService(LOCATION_SERVICE);
        //设置每一秒获取一次location信息
        locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,      //GPS定位提供者
                1000,       //更新数据时间为1秒
                1,      //位置间隔为1米
                //位置监听器
                new LocationListener() {  //GPS定位信息发生改变时触发，用于更新位置信息

                    @Override
                    public void onLocationChanged(Location location) {
                        //GPS信息发生改变时，更新位置
                        locationUpdates(location);
                    }

                    @Override
                    //位置状态发生改变时触发
                    public void onStatusChanged(String provider, int status, Bundle extras) {
                    }

                    @Override
                    //定位提供者启动时触发
                    public void onProviderEnabled(String provider) {
                    }

                    @Override
                    //定位提供者关闭时触发
                    public void onProviderDisabled(String provider) {
                    }
                });
        //从GPS获取最新的定位信息
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        locationUpdates(location);    //将最新的定位信息传递给创建的locationUpdates()方法中


        if (resultCode == ConnectionResult.SUCCESS) {
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

            mClient.requestLocationUpdates(mLocationRequest, mLocationCallback, null);
        }
    }

    public void locationUpdates(Location location) {  //获取指定的查询信息
        //如果location不为空时
        if (location != null) {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();

            RepositoryFactory.getLocalRepository().saveLatitude(String.valueOf(latitude));
            RepositoryFactory.getLocalRepository().saveLongitude(String.valueOf(longitude));

            mPresenter.getShopsList(mServiceId, mSecondServiceId, mOrder, mPage);
        }
    }

    private void showDialog() {
        List<String> list = new ArrayList<>();
        list.add(getResources().getString(R.string.comprehensive_ranking));
        list.add(getResources().getString(R.string.praise_first));
        list.add(getResources().getString(R.string.lowest_starting_price));
        list.add(getResources().getString(R.string.lowest_delivery_fee));
        mDialog = new FilterDialog(getCurActivity(), list);
        mDialog.setOnFilterClickListener(pos -> {
            if (pos == 0) {
                mOrder = "";
            } else {
                mOrder = String.valueOf(pos);
            }
            mTvComprehensiveRanking.setTextColor(ContextCompat.getColor(getCurContext(), R.color.colorAccent));
            mTvComprehensiveRanking.setText(list.get(pos));
            mTvDistance.setTextColor(ContextCompat.getColor(getCurContext(), R.color.gray_text));
            if (TextUtils.isEmpty(mAddress)) {
                mPage = 1;
                mPresenter.getShopsList(mServiceId, mSecondServiceId, mOrder, mPage);
            }
            mDialog.dismiss();
        });
        new XPopup.Builder(getCurContext())
                .atView(mTvComprehensiveRanking)
                .popupPosition(PopupPosition.Bottom)
                .maxWidth(DimensionUtil.getScreenWith())
                .asCustom(mDialog)
                .show();
    }

    private void showTypeDialog() {
        mTypeDialog = new FilterDialog(getCurActivity(), mTypeList);
        mTypeDialog.setOnFilterClickListener(pos -> {
            if (pos == 0) {
                mSecondServiceId = "";
                mTvType.setTextColor(ContextCompat.getColor(getCurContext(), R.color.gray_text));
                mTvType.setText(mTypeList.get(pos));
            } else {
                mSecondServiceId = mSecondMenuList.get(pos).getSecondId();
                mTvType.setTextColor(ContextCompat.getColor(getCurContext(), R.color.colorAccent));
                mTvType.setText(mTypeList.get(pos));
            }
            if (TextUtils.isEmpty(mAddress)) {
                mPage = 1;
                mPresenter.getShopsList(mServiceId, mSecondServiceId, mOrder, mPage);
            }
            mTypeDialog.dismiss();
        });
        new XPopup.Builder(getCurContext())
                .atView(mTvType)
                .popupPosition(PopupPosition.Bottom)
                .maxWidth(DimensionUtil.getScreenWith())
                .asCustom(mTypeDialog)
                .show();
    }

    @SuppressLint("MissingPermission")
    private void requestLocationUpdate() {
        XXPermissions.with(getCurActivity())
                .permission(Permission.Group.LOCATION)
                .request(new OnPermission() {

                    @Override
                    public void hasPermission(List<String> granted, boolean all) {
                        if (all) {
                            locationAddress();
                        } else {
                        }
                    }

                    @Override
                    public void noPermission(List<String> denied, boolean quick) {
                    }
                });
    }

    private void getAddress(String lat, String lon) {
        String language = RepositoryFactory.getLocalRepository().getLangugeType();
        String url = "https://maps.googleapis.com/maps/api/geocode/json?latlng=" + lat + "," + lon + "+"
                + "&key=AIzaSyAOtsgwEAwJ7SjsM1oHDVmp6oLfOS24Rj4&language=" + language;

        new HttpUtil().sendPost(url, new HttpUtil.OnRequestListener() {
            @Override
            public void onSuccess(String json) {
                GoogleMapGeocodeSearchBean data = JsonUtil.strToModel(json, GoogleMapGeocodeSearchBean.class);
                mAddress = data.getResults().get(0).getFormatted_address();
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

    private String mAddress;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            SharedPreferencesUtils.setString(BaseApp.getInstance(), "address", mAddress);
            mTvAddress.setText(mAddress);

            mPresenter.getShopsList(mServiceId, mSecondServiceId, mOrder, mPage);
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 9 && resultCode == 9) {
            mAddress = data.getStringExtra("adr");
            SharedPreferencesUtils.setString(BaseApp.getInstance(), "address", mAddress);
            mTvAddress.setText(mAddress);
            String lng = data.getStringExtra("lng");//经度
            String lat = data.getStringExtra("lat");//纬度
            RepositoryFactory.getLocalRepository().saveLatitude(lat);
            RepositoryFactory.getLocalRepository().saveLongitude(lng);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        GPSUtils.getInstance(getCurActivity()).removeLocationUpdatesListener();
    }
}
