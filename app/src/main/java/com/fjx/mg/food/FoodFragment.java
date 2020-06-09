package com.fjx.mg.food;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
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

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.fjx.mg.R;
import com.fjx.mg.dialog.FilterDialog;
import com.fjx.mg.food.adapter.GvClassificationAdapter;
import com.fjx.mg.food.adapter.GvSecondMenuAdapter;
import com.fjx.mg.food.adapter.GvSelectedGoodStoreAdapter;
import com.fjx.mg.food.adapter.RvStoreAdapter;
import com.fjx.mg.food.contract.FoodContract;
import com.fjx.mg.food.presenter.FoodPresenter;
import com.fjx.mg.setting.address.list.AddressListActivity;
import com.fjx.mg.utils.SharedPreferencesUtils;
import com.fjx.mg.view.WrapContentGridView;
import com.library.common.base.BaseApp;
import com.library.common.base.BaseMvpFragment;
import com.library.common.utils.DimensionUtil;
import com.library.common.utils.JsonUtil;
import com.library.common.view.BannerView;
import com.library.repository.models.AdModel;
import com.library.repository.models.HomeShopListBean;
import com.library.repository.models.HotShopBean;
import com.library.repository.models.ShopTypeBean;
import com.library.repository.repository.RepositoryFactory;
import com.lxj.xpopup.XPopup;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

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

    //声明mlocationClient对象
    private AMapLocationClient mlocationClient;
    //声明mLocationOption对象
    private AMapLocationClientOption mLocationOption = null;

    private FilterDialog mTypeDialog;
    private List<String> mTypeList=new ArrayList<>();
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
        mDrawable= ContextCompat.getDrawable(getCurContext(),R.mipmap.icon_arrow_down_black);
        mDrawable.setBounds(0,0,mDrawable.getMinimumWidth(),mDrawable.getMinimumHeight());
        mSelectedDrawable= ContextCompat.getDrawable(getCurContext(),R.mipmap.icon_arrow_up_red);
        mSelectedDrawable.setBounds(0,0,mSelectedDrawable.getMinimumWidth(),mSelectedDrawable.getMinimumHeight());
        //设置地址
        String address = SharedPreferencesUtils.getString(getCurContext(), "address", "");
        mTvAddress.setText(address);
        View headView = View.inflate(getCurContext(), R.layout.item_food_header, null);
        mGvMenu = headView.findViewById(R.id.gv_menu);
        mGvSecondMenu = headView.findViewById(R.id.gv_second_menu);
        mCardView = headView.findViewById(R.id.cvBanner);
        mBanner = headView.findViewById(R.id.banner);
        mGvSelectedGoodStore = headView.findViewById(R.id.gv_selected_good_store);
        mTvType = headView.findViewById(R.id.tv_type);
        mTvComprehensiveRanking = headView.findViewById(R.id.tv_comprehensive_ranking);
        mTvDistance = headView.findViewById(R.id.tv_distance);
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
        View footerView=View.inflate(getCurContext(),R.layout.item_rv_store_list_footer,null);
        mAdapter.addFooterView(footerView);
        LinearLayoutManager manager = new LinearLayoutManager(getCurContext());
        mRvContent.setLayoutManager(manager);
        mRvContent.setAdapter(mAdapter);

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
                intent = new Intent(getCurContext(), AddressListActivity.class);
                startActivity(intent);
                break;
            case R.id.v_search://搜索
                intent = new Intent(getCurContext(), TakeOutFoodSearchActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_type://类型
                if (mTypeDialog==null||!mTypeDialog.isShow()) {
                    showTypeDialog();
                }else {
                    mTypeDialog.dismiss();
                }
                break;
            case R.id.tv_comprehensive_ranking://综合排序
                if (mDialog==null||!mDialog.isShow()) {
                    showDialog();
                }else {
                    mDialog.dismiss();
                }
                break;
            case R.id.tv_distance://距离
                mOrder = "4";
                mPage = 1;
                mPresenter.getShopsList(mServiceId, mSecondServiceId, mOrder, mPage);
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
        mTypeList.add(0,getResources().getString(R.string.all));
        mGvClassificationAdapter.setData(mClassificationList);
        mSecondMenuAdapter.setData(mSecondMenuList);
    }

    @Override
    public void getBannerDataSuccess(List<AdModel> data) {
        if (data==null){
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
            mRefreshLayout.setNoMoreData(true);
        } else {
            mRefreshLayout.setEnableLoadMore(true);
            mRefreshLayout.setNoMoreData(false);
        }
        if (mPage == 1) {
            mList = data;
            mRefreshLayout.finishRefresh();
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
    public void onResume() {
        super.onResume();
        locationAddress();
    }

    private void locationAddress() {
        mlocationClient = new AMapLocationClient(getCurActivity());
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
                    SharedPreferencesUtils.setString(BaseApp.getInstance(), "address", address);
                    RepositoryFactory.getLocalRepository().saveLatitude(String.valueOf(amapLocation.getLatitude()));
                    RepositoryFactory.getLocalRepository().saveLongitude(String.valueOf(amapLocation.getLongitude()));

                    mPage = 1;
                    mPresenter.getShopTypeList("");
                    mPresenter.getAd();
                    mPresenter.getHotShops();
                    mPresenter.getShopsList(mServiceId, mSecondServiceId, mOrder, mPage);
                } else {
                    //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                }
            }
        }
    };

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
                mTvComprehensiveRanking.setTextColor(ContextCompat.getColor(getCurContext(),R.color.gray_text));
                mTvComprehensiveRanking.setText(list.get(pos));
            } else {
                mOrder = String.valueOf(pos);
                mTvComprehensiveRanking.setTextColor(ContextCompat.getColor(getCurContext(),R.color.gray_text));
                mTvComprehensiveRanking.setText(list.get(pos));
            }
            mPage = 1;
            mPresenter.getShopsList(mServiceId, mSecondServiceId, mOrder, mPage);
            mDialog.dismiss();
        });
        new XPopup.Builder(getCurContext())
                .atView(mTvComprehensiveRanking)
                .maxWidth(DimensionUtil.getScreenWith())
                .asCustom(mDialog)
                .show();
    }

    private void showTypeDialog() {
        mTypeDialog = new FilterDialog(getCurActivity(), mTypeList);
        mTypeDialog.setOnFilterClickListener(pos -> {
            if (pos==0){
                mSecondServiceId="";
                mTvType.setTextColor(ContextCompat.getColor(getCurContext(),R.color.gray_text));
                mTvType.setText(mTypeList.get(pos));
            }else {
                mSecondServiceId = mSecondMenuList.get(pos).getSecondId();
                mTvType.setTextColor(ContextCompat.getColor(getCurContext(),R.color.colorAccent));
                mTvType.setText(mTypeList.get(pos));
            }
            mPresenter.getShopsList(mServiceId, mSecondServiceId, mOrder, mPage);
            mTypeDialog.dismiss();
        });
        new XPopup.Builder(getCurContext())
                .atView(mTvType)
                .maxWidth(DimensionUtil.getScreenWith())
                .asCustom(mTypeDialog)
                .show();
    }
}
