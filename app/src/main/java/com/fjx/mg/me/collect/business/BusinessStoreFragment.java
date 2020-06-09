package com.fjx.mg.me.collect.business;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fjx.mg.R;
import com.fjx.mg.main.fragment.home.RecommendStoreAdapter;
import com.library.common.base.BaseMvpFragment;
import com.library.common.base.adapter.decoration.SpacesItemDecoration;
import com.library.common.view.refresh.CustomRefreshListener;
import com.library.common.view.refresh.CustomRefreshView;

import java.util.List;

import butterknife.BindView;

public class BusinessStoreFragment extends BaseMvpFragment<BusinessStorePresenter> implements BusinessStoreContract.View {
    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.refreshView)
    CustomRefreshView refreshView;
    private RecommendStoreAdapter adapter;

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
        return R.layout.ac_common_refresh_list;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        adapter = new RecommendStoreAdapter();
        recycler.setLayoutManager(new LinearLayoutManager(getCurContext()));
        recycler.addItemDecoration(new SpacesItemDecoration(0, 10));
        recycler.setAdapter(adapter);




        //adapter.bindToRecyclerView(recycler);




        adapter.setEmptyView(R.layout.layout_empty);
        refreshView.setRefreshListener(new CustomRefreshListener() {
            @Override
            public void onRefreshData(int page, int pageSize) {
                mPresenter.getData();
                recycler.invalidateItemDecorations();
                refreshView.finishLoading();
            }
        });

    }

    @Override
    protected void lazyLoadData() {
        refreshView.autoRefresh();
    }

    @Override
    public void showDatas(List<Object> datas) {
        adapter.setList(datas);
    }
}
