//package com.fjx.mg.network;
//
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.BitmapFactory;
//import android.graphics.Color;
//import android.os.Bundle;
//import android.view.MotionEvent;
//import android.view.View;
//
//import com.amap.api.maps.AMap;
//import com.amap.api.maps.CameraUpdateFactory;
//import com.amap.api.maps.MapView;
//import com.amap.api.maps.model.BitmapDescriptorFactory;
//import com.amap.api.maps.model.LatLng;
//import com.amap.api.maps.model.Marker;
//import com.amap.api.maps.model.MarkerOptions;
//import com.amap.api.maps.model.MyLocationStyle;
//import com.fjx.mg.R;
//import com.fjx.mg.network.adapter.MyInfoWindowAdapter;
//import com.fjx.mg.network.fragment.FilterDialogFragment;
//import com.fjx.mg.network.fragment.MapDialogFragment;
//import com.fjx.mg.network.mvp.SalesNetworkContract;
//import com.fjx.mg.network.mvp.SalesNetworkPresenter;
//import com.fjx.mg.utils.MapNaviUtils;
//import com.library.common.base.BaseMvpActivity;
//import com.library.common.constant.IntentConstants;
//import com.library.common.utils.StatusBarManager;
//import com.library.repository.models.ResponseModel;
//import com.library.repository.models.SearchAgentListModel;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import butterknife.BindView;
//import butterknife.OnClick;
//
//import static com.amap.api.maps.model.MyLocationStyle.LOCATION_TYPE_LOCATE;
//
//
///**
// * Author    by hanlz
// * Date      on 2019/10/22.
// * Description：营业网点/店铺列表页
// */
//public class SalesNetworkActivity extends BaseMvpActivity<SalesNetworkPresenter> implements SalesNetworkContract.View {
//
//    public final static String SALES_NETWORK = "4";
//    private ArrayList<SearchAgentListModel> mItems;
//    private String sName;
//    private String lat;
//    private String lng;
//
//    @BindView(R.id.map)
//    MapView mapView;
//    AMap aMap;
//    Marker curShowWindowMarker;
//    private String mMoney;
//
//    public static Intent newIntent(Context context, String type) {
//        return newIntent(context, type, "");
//    }
//
//    /**
//     * @param context
//     * @param type    3:店铺，4:营业网点
//     * @param remark  备注搜索
//     * @return
//     */
//    public static Intent newIntent(Context context, String type, String remark) {
//        Intent intent = new Intent(context, SalesNetworkActivity.class);
//        intent.putExtra(IntentConstants.TYPE, type);
//        intent.putExtra(IntentConstants.REMARK, remark);
//        return intent;
//    }
//
//
//    @Override
//    protected SalesNetworkPresenter createPresenter() {
//        return new SalesNetworkPresenter(this);
//    }
//
//    @Override
//    protected int layoutId() {
//        return R.layout.act_sales_network;
//    }
//
//    @Override
//    protected void initView() {
//        super.initView();
//        StatusBarManager.setLightFontColor(this, R.color.colorAccent);
//        aMap = mapView.getMap();
//        setBluePoint();
//        mPresenter.requestLocationAddress();
//    }
//
//    private void setBluePoint() {
//        // 自定义系统定位小蓝点
//        MyLocationStyle myLocationStyle = new MyLocationStyle();
//        myLocationStyle.interval(2000);
//        myLocationStyle.myLocationType(LOCATION_TYPE_LOCATE);
//        myLocationStyle.strokeColor(Color.argb(0, 0, 0, 0));// 设置圆形的边框颜色
//        myLocationStyle.radiusFillColor(Color.argb(0, 0, 0, 0));// 设置圆形的填充颜色
//        aMap.setMyLocationStyle(myLocationStyle);
//        aMap.moveCamera(CameraUpdateFactory.zoomTo(13));
//        aMap.getUiSettings().setMyLocationButtonEnabled(false);// 设置默认定位按钮是否显示
//        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
//    }
//
//
//    @Override
//    public void responseFailed(ResponseModel data) {
//
//    }
//
//    @Override
//    public void responseAgentList(List<SearchAgentListModel> items, final String lng, final String lat, final String sName) {
//        for (int i = 0; i < items.size(); i++) {
//            final SearchAgentListModel model = items.get(i);
//            this.mItems = (ArrayList<SearchAgentListModel>) items;
//            final LatLng latLng = new LatLng(Double.parseDouble(model.getLat()), Double.parseDouble(model.getLng()));
//            MarkerOptions markerOptions = new MarkerOptions().position(latLng).title(model.getAddress()).snippet(getString(R.string.if_navigation))
//                    .icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
//                            .decodeResource(getResources(), R.drawable.map_location_ic)));
//            Marker marker = aMap.addMarker(markerOptions);
//            marker.setObject(model);
//            aMap.setInfoWindowAdapter(new MyInfoWindowAdapter(getCurActivity(), new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    MapDialogFragment fragment = new MapDialogFragment();
//                    fragment.show(getSupportFragmentManager(), model.getNickName());
//                    fragment.setOnNavigationLinsenter(new MapDialogFragment.OnNavigationLinsenter() {
//                        @Override
//                        public void onGoogleMapNavi() {
//
//                        }
//
//                        @Override
//                        public void onGaodeMapNavi() {
//                            if (MapNaviUtils.isGdMapInstalled()) {
//                                MapNaviUtils.openGaoDeNavi(getCurActivity(), 0d, 0d, sName, Double.parseDouble(model.getLat()), Double.parseDouble(model.getLng()), model.getAddress());
//                            } else {
//                                MapNaviUtils.installGaode(getCurActivity());
//                            }
//                        }
//
//                        @Override
//                        public void onBaiduMapNavi() {
//                            if (MapNaviUtils.isBaiduMapInstalled()) {
//                                MapNaviUtils.openBaiDuNavi(getCurActivity(), Double.parseDouble(lat), Double.parseDouble(lng), sName,
//                                        Double.parseDouble(model.getLat()), Double.parseDouble(model.getLng()), model.getAddress());
//                            } else {
//                                MapNaviUtils.installBaidu(getCurActivity());
//                            }
//                        }
//                    });
//                }
//            }));
//            aMap.setOnMarkerClickListener(new AMap.OnMarkerClickListener() {
//                @Override
//                public boolean onMarkerClick(Marker marker) {
//                    curShowWindowMarker = marker;//保存当前点击的Marker，以便点击地图其他地方设置InfoWindow消失
//                    marker.showInfoWindow();
//                    return true;
//                }
//            });
//
//        }
//    }
//
//    @Override
//    public void responseLocationAddress(String lng, String lat, String sName) {
//        mPresenter.requestAgentList(lng, lat, SALES_NETWORK, "", mMoney, sName);
//        this.lng = lng;
//        this.lat =lat;
//        this.sName = sName;
//    }
//
//    @OnClick({R.id.ivRightIcon, R.id.etSearch, R.id.tvFilter, R.id.tvList})
//    public void onClickView(View view) {
//        switch (view.getId()) {
//            case R.id.ivRightIcon://返回
//                finish();
//                break;
//            case R.id.tvFilter://网店过滤
//                FilterDialogFragment dialogFragment = new FilterDialogFragment();
//                dialogFragment.show(getSupportFragmentManager(), "FilterDialogFragment");
//                dialogFragment.setOnFilterListener(new FilterDialogFragment.OnFilterListener() {
//                    @Override
//                    public void onFilter(String money) {
//                        mMoney = money;
//                        mPresenter.requestLocationAddress();
//                        if (aMap != null) {
//                            aMap.clear();
//                            setBluePoint();
//                        }
//                    }
//                });
//                break;
//            case R.id.tvList://网点列表
//            case R.id.etSearch://搜索
//                startActivity(SalesNetworkSearchActivity.newIntent(getCurContext(), mItems,sName,lat,lng));
//                break;
//            default:
//
//        }
//    }
//
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        if (aMap != null && curShowWindowMarker != null) {
//            if (curShowWindowMarker.isInfoWindowShown()) {
//                curShowWindowMarker.hideInfoWindow();
//                return true;
//            }
//        }
//
//        return super.onTouchEvent(event);
//    }
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        mapView.onCreate(savedInstanceState);// 此方法必须重写
//    }
//
//    /**
//     * 方法必须重写
//     */
//    @Override
//    protected void onResume() {
//        super.onResume();
//        mapView.onResume();
//    }
//
//    /**
//     * 方法必须重写
//     */
//    @Override
//    protected void onPause() {
//        super.onPause();
//        mapView.onPause();
//    }
//
//    /**
//     * 方法必须重写
//     */
//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        mapView.onSaveInstanceState(outState);
//    }
//
//    /**
//     * 方法必须重写
//     */
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        if (mapView != null) {
//            mapView.onDestroy();
//        }
//    }
//
//}
