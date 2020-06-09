package com.fjx.mg.main.fragment.news.tab;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.fjx.mg.R;
import com.fjx.mg.news.detail.NewsDetailActivity;
import com.library.common.base.BaseMvpFragment;
import com.library.common.base.adapter.decoration.SpacesItemDecoration;
import com.library.common.utils.DimensionUtil;
import com.library.common.view.refresh.CustomRefreshListener;
import com.library.common.view.refresh.CustomRefreshView;
import com.library.repository.db.DBDaoFactory;
import com.library.repository.models.NewsItemModel;
import com.library.repository.models.NewsListModel;

import java.util.List;

import butterknife.BindView;

public class NewsTabFragment extends BaseMvpFragment<NewsTabPresenter> implements NewsTabContract.View {

    @BindView(R.id.refreshView)
    CustomRefreshView refreshView;
    @BindView(R.id.recycler)
    RecyclerView recyclerView;
    private NewsAdapter adapter;

    private int typeId;

    public static NewsTabFragment newInstance(int typeId) {
        Bundle args = new Bundle();
        args.putInt("typeId", typeId);
        NewsTabFragment fragment = new NewsTabFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        typeId = getArguments().getInt("typeId");

        adapter = new NewsAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getCurContext()));
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new SpacesItemDecoration(1));
        //adapter.bindToRecyclerView(recyclerView);
        adapter.setEmptyView(R.layout.layout_empty);
        refreshView.setRefreshListener(new CustomRefreshListener() {
            @Override
            public void onRefreshData(int page, int pageSize) {
                mPresenter.getNewsList(typeId, page, "");
            }
        });
        refreshView.autoRefresh();
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                startActivity(NewsDetailActivity.newInstance(getCurContext(), ((NewsListModel)adapter.getItem(position)).getNewsId()));
            }
        });
        List<NewsListModel> dataList = DBDaoFactory.getNewsListDao().queryList(typeId);
        if (dataList.isEmpty()) return;
        adapter.setList(dataList);

    }

//    @Override
//    protected void lazyLoadData() {
//        refreshView.autoRefresh();
//    }

    @Override
    protected int layoutId() {
        return R.layout.fragment_news_tab;
    }


    @Override
    protected NewsTabPresenter createPresenter() {
        return new NewsTabPresenter(this);
    }

    @Override
    public void showNewsList(NewsItemModel model) {
        refreshView.noticeAdapterData(adapter, model.getNewsList(), model.isHasNext());
        recyclerView.invalidateItemDecorations();
    }

    @Override
    public void loadError() {
        refreshView.finishLoading();
    }
}
