package com.fjx.mg.me.collect.house;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.fjx.mg.R;
import com.fjx.mg.house.detail.HouseDetailActivity;
import com.fjx.mg.house.fragment.HouseLeaseAdapter;
import com.library.common.base.BaseMvpFragment;
import com.library.common.base.adapter.decoration.SpacesItemDecoration;
import com.library.common.view.refresh.CustomRefreshListener;
import com.library.common.view.refresh.CustomRefreshView;
import com.library.repository.models.HouseDetailModel;
import com.library.repository.models.HouseListModel;

import java.util.List;

import butterknife.BindView;

public class HouseResourceFragment extends BaseMvpFragment<HouseResourcePresenter> implements HouseResourceContract.View {
    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.refreshView)
    CustomRefreshView refreshView;
    private HouseLeaseAdapter adapter;

    @Override
    protected HouseResourcePresenter createPresenter() {
        return new HouseResourcePresenter(this);
    }

    public static HouseResourceFragment newInstance() {
        Bundle args = new Bundle();
        HouseResourceFragment fragment = new HouseResourceFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int layoutId() {
        return R.layout.ac_common_refresh_list;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        adapter = new HouseLeaseAdapter();
        recycler.setLayoutManager(new LinearLayoutManager(getCurContext()));
        recycler.addItemDecoration(new SpacesItemDecoration(0, 10));
        recycler.setAdapter(adapter);


        //adapter.bindToRecyclerView(recycler);


        adapter.setEmptyView(R.layout.layout_empty);
        refreshView.setRefreshListener(new CustomRefreshListener() {
            @Override
            public void onRefreshData(int page, int pageSize) {
                mPresenter.getMyCollectHouse(page);
            }
        });
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                startActivity(HouseDetailActivity.newInstance(getCurContext(), ((HouseDetailModel) adapter.getItem(position)).getHid()));
            }
        });

    }

    @Override
    protected void lazyLoadData() {
        refreshView.autoRefresh();
    }

    @Override
    public void showHouseList(HouseListModel houseListModel) {
        refreshView.noticeAdapterData(adapter, houseListModel.getHouseList(), houseListModel.isHasNext());
    }

    @Override
    public void loadError() {
        refreshView.finishLoading();

    }
}
