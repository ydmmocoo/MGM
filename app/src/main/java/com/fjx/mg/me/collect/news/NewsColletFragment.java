package com.fjx.mg.me.collect.news;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.fjx.mg.R;
import com.fjx.mg.main.fragment.news.tab.NewsAdapter;
import com.fjx.mg.news.detail.NewsDetailActivity;
import com.library.common.base.BaseMvpFragment;
import com.library.common.base.adapter.decoration.SpacesItemDecoration;
import com.library.common.utils.DimensionUtil;
import com.library.common.view.refresh.CustomRefreshListener;
import com.library.common.view.refresh.CustomRefreshView;
import com.library.repository.models.NewsItemModel;
import com.library.repository.models.NewsListModel;

import butterknife.BindView;

public class NewsColletFragment extends BaseMvpFragment<NewsColletPresenter> implements NewsColletContract.View {
    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.refreshView)
    CustomRefreshView refreshView;
    private NewsAdapter adapter;

    @Override
    protected NewsColletPresenter createPresenter() {
        return new NewsColletPresenter(this);
    }

    public static NewsColletFragment newInstance() {
        Bundle args = new Bundle();
        NewsColletFragment fragment = new NewsColletFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int layoutId() {
        return R.layout.ac_common_refresh_list;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        adapter = new NewsAdapter();
        recycler.setLayoutManager(new LinearLayoutManager(getCurContext()));
        recycler.setAdapter(adapter);
        recycler.addItemDecoration(new SpacesItemDecoration(0, 10));
        refreshView.setRefreshListener(new CustomRefreshListener() {
            @Override
            public void onRefreshData(int page, int pageSize) {
                mPresenter.getMyCollectNews(page);
            }
        });



        //adapter.bindToRecyclerView(recycler);






        adapter.setEmptyView(R.layout.layout_empty);

        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                startActivity(NewsDetailActivity.newInstance(getCurContext(), ((NewsListModel)adapter.getItem(position)).getNewsId()));
            }
        });
    }

    @Override
    protected void lazyLoadData() {
        refreshView.autoRefresh();
    }


    @Override
    public void showDatas(NewsItemModel model) {
        refreshView.noticeAdapterData(adapter, model.getNewsList(), model.isHasNext());
    }
}
