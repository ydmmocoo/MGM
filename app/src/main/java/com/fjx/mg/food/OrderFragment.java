package com.fjx.mg.food;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fjx.mg.R;
import com.fjx.mg.food.adapter.RvOrderAdapter;
import com.fjx.mg.food.contract.OrderContract;
import com.fjx.mg.food.presenter.OrderPresenter;
import com.fjx.mg.main.fragment.news.tab.NewsTabFragment;
import com.library.common.base.BaseFragment;
import com.library.common.base.BaseMvpFragment;
import com.library.repository.models.OrderBean;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnMultiListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @author yedeman
 * @date 2020/5/28.
 * email：ydmmocoo@gmail.com
 * description：
 */
public class OrderFragment extends BaseMvpFragment<OrderPresenter>
        implements OrderContract.View, RvOrderAdapter.OnBtnClickListener {

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

    public static OrderFragment newInstance(int type) {
        Bundle args = new Bundle();
        args.putInt("type", type);
        OrderFragment fragment = new OrderFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int layoutId() {
        return R.layout.fragment_order;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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
    }

    @Override
    public void onResume() {
        super.onResume();
        mType = getArguments().getInt("type", 1);
        if (mType == 2) {
            mPayStatus = "1";
            mOrderStatus = "";
            mIsRefuse = "";
        } else if (mType == 4) {
            mPayStatus = "";
            mOrderStatus = "1";
            mIsRefuse = "";
        } else if (mType == 5) {
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
