package com.fjx.mg.food;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.fjx.mg.R;
import com.fjx.mg.ToolBarManager;
import com.fjx.mg.food.adapter.RvOrderAdapter;
import com.fjx.mg.food.contract.OrderContract;
import com.fjx.mg.food.presenter.OrderPresenter;
import com.google.android.material.tabs.TabLayout;
import com.library.common.base.BaseActivity;
import com.library.common.base.BaseMvpActivity;
import com.library.common.base.BaseMvpFragment;
import com.library.repository.models.OrderBean;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class OrderActivity extends BaseMvpActivity<OrderPresenter>
        implements OrderContract.View, RvOrderAdapter.OnBtnClickListener {

    @BindView(R.id.tab)
    TabLayout mTab;
    @BindView(R.id.rv_order)
    RecyclerView mRvOrder;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout mRefreshLayout;

    private RvOrderAdapter mAdapter;
    private List<OrderBean.OrderListBean> mList = new ArrayList<>();

    private int mType = 1;
    private boolean mHasNext;
    private int mPage = 1;

    private String mPayStatus, mOrderStatus, mIsRefuse;

    private List<String> mTitles = new ArrayList<>();

    public static Intent newInstance(Context context,int type) {
        Intent intent=new Intent(context, OrderActivity.class);
        intent.putExtra("type",type);
        return intent;
    }

    @Override
    protected int layoutId() {
        return R.layout.activity_order;
    }

    @Override
    protected void initView() {
        mType = getIntent().getIntExtra("type", 0);
        if (mType == 1) {
            mPayStatus = "1";
            mOrderStatus = "";
            mIsRefuse = "";
        } else if (mType == 2) {
            mPayStatus = "";
            mOrderStatus = "1";
            mIsRefuse = "";
        } else if (mType == 3) {
            mPayStatus = "";
            mOrderStatus = "";
            mIsRefuse = "1";
        } else {
            mPayStatus = "";
            mOrderStatus = "";
            mIsRefuse = "";
        }
        //设置标题
        ToolBarManager.with(this).setTitle(getString(R.string.my_order));
        //初始化Tab
        mTitles.add(getResources().getString(R.string.all));
        mTitles.add(getResources().getString(R.string.to_be_paid));
        //mTitles.add(getResources().getString(R.string.to_be_used));
        mTitles.add(getResources().getString(R.string.to_be_evaluated));
        mTitles.add(getResources().getString(R.string.after_sale_and_refund));
        for (int i=0;i<mTitles.size();i++) {
            if (mType==i) {
                mTab.addTab(mTab.newTab().setText(mTitles.get(i)),true);
            }else {
                mTab.addTab(mTab.newTab().setText(mTitles.get(i)),false);
            }
        }

        //初始化RecyclerView
        LinearLayoutManager manager = new LinearLayoutManager(getCurContext());
        mRvOrder.setLayoutManager(manager);
        mAdapter = new RvOrderAdapter(R.layout.item_rv_order, mList);
        mAdapter.setOnBtnClickListener(this);
        mRvOrder.setAdapter(mAdapter);

        //点击Item跳转订单详情
        mAdapter.setOnItemClickListener((adapter, view1, position) -> {
            if ("2".equals(mList.get(position).getPayStatus()) &&
                    !"6".equals(mList.get(position).getOrderStatus())) {
                Intent intent = new Intent(getCurContext(), UnpaidOrderDetailActivity.class);
                intent.putExtra("id", mList.get(position).getOId());
                intent.putExtra("store_id", mList.get(position).getSId());
                startActivity(intent);
            } else {
                Intent intent = new Intent(getCurContext(), OrderDetailActivity.class);
                intent.putExtra("id", mList.get(position).getOId());
                intent.putExtra("store_id", mList.get(position).getSId());
                startActivity(intent);
            }
        });

        //监听下拉刷新、上拉加载更多
        mRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                if (mHasNext) {
                    mPage++;
                    mPresenter.getOrderList(mPayStatus, mOrderStatus, mIsRefuse, mPage);
                }
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mPage = 1;
                mPresenter.getOrderList(mPayStatus, mOrderStatus, mIsRefuse, mPage);
            }
        });

        //监听Tab切换
        mTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mType=tab.getPosition();
                if (mType == 1) {
                    mPayStatus = "1";
                    mOrderStatus = "";
                    mIsRefuse = "";
                } else if (mType == 2) {
                    mPayStatus = "";
                    mOrderStatus = "1";
                    mIsRefuse = "";
                } else if (mType == 3) {
                    mPayStatus = "";
                    mOrderStatus = "";
                    mIsRefuse = "1";
                } else {
                    mPayStatus = "";
                    mOrderStatus = "";
                    mIsRefuse = "";
                }
                mPage = 1;
                mPresenter.getOrderList(mPayStatus, mOrderStatus, mIsRefuse, mPage);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mPage = 1;
        mPresenter.getOrderList(mPayStatus, mOrderStatus, mIsRefuse, mPage);
    }

    @Override
    public void getOrderListSuccess(List<OrderBean.OrderListBean> data, boolean hasNext) {
        mHasNext = hasNext;
        if (!mHasNext) {
            mRefreshLayout.setEnableLoadMore(false);
            mRefreshLayout.setNoMoreData(true);
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
    public void confirmOrderSuccess() {
        mPage = 1;
        mPresenter.getOrderList(mPayStatus, mOrderStatus, mIsRefuse, mPage);
    }

    @Override
    public void cancelOrderSuccess() {
        mPage = 1;
        mPresenter.getOrderList(mPayStatus, mOrderStatus, mIsRefuse, mPage);
    }

    @Override
    public void cancelRefundSuccess() {
        mPage = 1;
        mPresenter.getOrderList(mPayStatus, mOrderStatus, mIsRefuse, mPage);
    }

    @Override
    public void reBuySuccess(String storeId) {
        Intent intent = new Intent(getCurContext(), StoreDetailActivity.class);
        intent.putExtra("id", storeId);
        startActivity(intent);
    }

    @Override
    protected OrderPresenter createPresenter() {
        return new OrderPresenter(this);
    }

    @Override
    public void addAnotherOne(String orderId, String storeId) {
        mPresenter.reBuy(orderId, storeId);
    }

    @Override
    public void confirmReceipt(String orderId) {
        mPresenter.confirmOrder(orderId);
    }

    @Override
    public void cancelOrder(String orderId) {
        mPresenter.cancelOrder(orderId);
    }

    @Override
    public void cancelRefund(String orderId) {
        mPresenter.cancelRefund(orderId);
    }
}
