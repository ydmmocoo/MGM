package com.fjx.mg.main.fragment.life.Tab;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fjx.mg.R;
import com.fjx.mg.main.fragment.home.RecommendStoreAdapter;
import com.library.common.base.BaseMvpFragment;
import com.library.common.view.refresh.CustomRefreshListener;
import com.library.common.view.refresh.CustomRefreshView;

import java.util.List;

import butterknife.BindView;

public class LifeTabFragment extends BaseMvpFragment<LifeTabPresenter> implements LifeTabContract.View {

    @BindView(R.id.refreshView)
    CustomRefreshView refreshView;

    @BindView(R.id.recycler)
    RecyclerView recycler;
    private RecommendStoreAdapter storeAdapter;

    public static LifeTabFragment newInstance() {
        Bundle args = new Bundle();
        LifeTabFragment fragment = new LifeTabFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected LifeTabPresenter createPresenter() {
        return new LifeTabPresenter(this);
    }

    @Override
    protected int layoutId() {
        return R.layout.fragment_life_tab;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mPresenter.initData();
        refreshView.setEnableRefresh(false);
        refreshView.setRefreshListener(new CustomRefreshListener() {
            @Override
            public void onRefreshData(int page, int pageSize) {
                refreshView.finishLoading();
            }
        });
    }

    @Override
    public void showDataList(List<Object> goodLit) {

        storeAdapter = new RecommendStoreAdapter();
        recycler.setLayoutManager(new LinearLayoutManager(getCurContext()));
        recycler.setAdapter(storeAdapter);
        storeAdapter.setList(goodLit);

    }
}
