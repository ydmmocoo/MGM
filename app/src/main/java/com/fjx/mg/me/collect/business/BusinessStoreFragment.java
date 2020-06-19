package com.fjx.mg.me.collect.business;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.fjx.mg.R;
import com.fjx.mg.food.StoreDetailActivity;
import com.fjx.mg.main.fragment.home.RecommendStoreAdapter;
import com.library.common.base.BaseMvpFragment;
import com.library.common.base.adapter.decoration.SpacesItemDecoration;
import com.library.common.view.refresh.CustomRefreshListener;
import com.library.common.view.refresh.CustomRefreshView;
import com.library.repository.models.CollectShopsBean;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.List;

import butterknife.BindView;

public class BusinessStoreFragment extends BaseMvpFragment<BusinessStorePresenter> implements BusinessStoreContract.View {

    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.refreshView)
    SmartRefreshLayout refreshView;

    private RecommendStoreAdapter adapter;
    private List<CollectShopsBean.ShopListBean> mList;

    private int mPage=1;

    @Override
    protected BusinessStorePresenter createPresenter() {
        return new BusinessStorePresenter(this);
    }

    public static BusinessStoreFragment newInstance() {
        Bundle args = new Bundle();
        BusinessStoreFragment fragment = new BusinessStoreFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int layoutId() {
        return R.layout.activity_store_collect;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        adapter = new RecommendStoreAdapter();
        recycler.setLayoutManager(new LinearLayoutManager(getCurContext()));
        recycler.addItemDecoration(new SpacesItemDecoration(0, 10));
        recycler.setAdapter(adapter);
        adapter.setEmptyView(R.layout.layout_empty);

        //Item点击事件
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                Intent intent=new Intent(getCurContext(), StoreDetailActivity.class);
                intent.putExtra("id",mList.get(position).getSId());
                startActivity(intent);
            }
        });
        //下拉刷新，上拉加载更多
        refreshView.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                mPage++;
                mPresenter.getData(mPage);
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mPage=1;
                mPresenter.getData(mPage);
            }
        });
    }

    @Override
    protected void lazyLoadData() {
        refreshView.autoRefresh();
    }

    @Override
    public void showDatas(List<CollectShopsBean.ShopListBean> list, boolean hasNext) {
        if (hasNext){
            refreshView.setEnableLoadMore(true);
        }else {
            refreshView.setEnableLoadMore(false);
        }
        if (mPage==1) {
            refreshView.finishRefresh();
            mList=list;
        }else {
            refreshView.finishLoadMore();
            mList.addAll(list);
        }
        adapter.setList(mList);
    }
}
