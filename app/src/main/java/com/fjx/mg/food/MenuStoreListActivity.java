package com.fjx.mg.food;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fjx.mg.R;
import com.fjx.mg.dialog.FilterDialog;
import com.fjx.mg.food.adapter.RvMenuStoreListAdapter;
import com.fjx.mg.food.contract.MenuStoreListContract;
import com.fjx.mg.food.presenter.MenuStoreListPresenter;
import com.google.android.material.tabs.TabLayout;
import com.gyf.immersionbar.ImmersionBar;
import com.library.common.base.BaseMvpActivity;
import com.library.common.utils.DimensionUtil;
import com.library.common.utils.JsonUtil;
import com.library.repository.models.HomeShopListBean;
import com.library.repository.models.ShopTypeBean;
import com.lxj.xpopup.XPopup;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class MenuStoreListActivity extends BaseMvpActivity<MenuStoreListPresenter> implements MenuStoreListContract.View {

    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.tab)
    TabLayout mTab;
    @BindView(R.id.tv_comprehensive_ranking)
    TextView mTvComprehensiveRanking;
    @BindView(R.id.tv_distance)
    TextView mTvDistance;
    @BindView(R.id.rv_store_list)
    RecyclerView mRvStoreList;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout mRefreshLayout;
    @BindView(R.id.v_line)
    View mVLine;
    private String mServiceId;
    private String mSecondServiceId;

    private RvMenuStoreListAdapter mAdapter;
    private List<HomeShopListBean.ShopListBean> mList;
    private List<ShopTypeBean.ShopTypeListBean> mSecondMenuList;
    private int mPage = 1;
    private String mOrder = "";

    private FilterDialog mDialog;
    private Drawable mDrawable;
    private Drawable mSelectedDrawable;

    @Override
    protected int layoutId() {
        return R.layout.activity_menu_store_list;
    }

    @Override
    protected void initView() {
        ImmersionBar.with(this)
                .fitsSystemWindows(false)
                .transparentStatusBar()
                .statusBarDarkFont(true)
                .init();
        String title = getIntent().getStringExtra("title");
        mTvTitle.setText(title);
        mServiceId = getIntent().getStringExtra("serviceId");
        String data = getIntent().getStringExtra("menu_data");
        if (!TextUtils.isEmpty(data)) {
            mSecondMenuList = JsonUtil.jsonToList(data, ShopTypeBean.ShopTypeListBean.class);
            mTab.setVisibility(View.VISIBLE);
            ShopTypeBean.ShopTypeListBean item=new ShopTypeBean.ShopTypeListBean();
            item.setName(getResources().getString(R.string.all));
            item.setSecondId("");
            mSecondMenuList.add(0,item);
            for (int i = 0; i < mSecondMenuList.size(); i++) {
                mTab.addTab(mTab.newTab().setText(mSecondMenuList.get(i).getName()));
            }
        }
        mSecondServiceId = getIntent().getStringExtra("secondServiceId");

        mDrawable= ContextCompat.getDrawable(getCurContext(),R.mipmap.icon_arrow_down_black);
        mDrawable.setBounds(0,0,mDrawable.getMinimumWidth(),mDrawable.getMinimumHeight());
        mSelectedDrawable= ContextCompat.getDrawable(getCurContext(),R.mipmap.icon_arrow_up_red);
        mSelectedDrawable.setBounds(0,0,mSelectedDrawable.getMinimumWidth(),mSelectedDrawable.getMinimumHeight());

        //初始化RecyclerView
        LinearLayoutManager manager = new LinearLayoutManager(this);
        mRvStoreList.setLayoutManager(manager);
        mAdapter = new RvMenuStoreListAdapter(R.layout.item_rv_menu_store_list, mList);
        mRvStoreList.setAdapter(mAdapter);

        //点击Item跳转店铺详情
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            Intent intent=new Intent(getCurContext(),StoreDetailActivity.class);
            intent.putExtra("id",mList.get(position).getSId());
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
                mPresenter.getShopsList(mServiceId, mSecondServiceId, mOrder, mPage);
            }
        });
        //Tab点击事件
        mTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mPage = 1;
                mSecondServiceId=mSecondMenuList.get(tab.getPosition()).getSecondId();
                mPresenter.getShopsList(mServiceId, mSecondServiceId, mOrder, mPage);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    @OnClick({R.id.tv_title, R.id.iv_search, R.id.tv_comprehensive_ranking, R.id.tv_distance})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_title://返回
                finish();
                break;
            case R.id.iv_search://搜索
                Intent intent = new Intent(getCurContext(), TakeOutFoodSearchActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_comprehensive_ranking://综合排序
                if (mDialog==null||!mDialog.isShow()) {
                    mTvComprehensiveRanking.setCompoundDrawables(null,null,mSelectedDrawable,null);
                    showDialog();
                }else {
                    mTvComprehensiveRanking.setCompoundDrawables(null,null,mDrawable,null);
                    mDialog.dismiss();
                }
                break;
            case R.id.tv_distance://距离
                mTvDistance.setTextColor(ContextCompat.getColor(getCurContext(),R.color.colorAccent));
                mTvComprehensiveRanking.setTextColor(ContextCompat.getColor(getCurContext(),R.color.gray_text));
                mTvComprehensiveRanking.setText(getResources().getString(R.string.comprehensive_ranking));
                mOrder = "4";
                mPresenter.getShopsList(mServiceId, mSecondServiceId, mOrder, mPage);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mRefreshLayout.autoRefresh();
    }

    @Override
    public void getShopsListSuccess(List<HomeShopListBean.ShopListBean> data, boolean hasNext) {
        if (hasNext) {
            mRefreshLayout.setEnableLoadMore(true);
        } else {
            mRefreshLayout.setEnableLoadMore(false);
        }
        if (mPage == 1) {
            mRefreshLayout.finishRefresh();
            mList = data;
        } else {
            mRefreshLayout.finishLoadMore();
            mList.addAll(data);
        }
        mAdapter.setList(mList);
    }

    @Override
    protected MenuStoreListPresenter createPresenter() {
        return new MenuStoreListPresenter(this);
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
                mTvComprehensiveRanking.setTextColor(ContextCompat.getColor(getCurContext(),R.color.gray_text));
                mTvComprehensiveRanking.setText(list.get(pos));
            } else {
                mOrder = String.valueOf(pos);
                mTvComprehensiveRanking.setTextColor(ContextCompat.getColor(getCurContext(),R.color.colorAccent));
                mTvComprehensiveRanking.setText(list.get(pos));
                mTvDistance.setTextColor(ContextCompat.getColor(getCurContext(),R.color.gray_text));
            }
            mPage = 1;
            mPresenter.getShopsList(mServiceId, mSecondServiceId, mOrder, mPage);
            mDialog.dismiss();
            mTvComprehensiveRanking.setCompoundDrawables(null,null,mDrawable,null);
        });
        new XPopup.Builder(getCurContext())
                .atView(mVLine)
                .maxWidth(DimensionUtil.getScreenWith())
                .asCustom(mDialog)
                .show();
    }
}