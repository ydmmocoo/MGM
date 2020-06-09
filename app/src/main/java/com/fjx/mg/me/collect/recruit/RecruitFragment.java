package com.fjx.mg.me.collect.recruit;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.fjx.mg.R;
import com.fjx.mg.job.detail.JobDetailActivity;
import com.fjx.mg.job.fragment.JobHuntinAdapter;
import com.fjx.mg.main.fragment.news.tab.NewsAdapter;
import com.library.common.base.BaseMvpFragment;
import com.library.common.base.adapter.decoration.SpacesItemDecoration;
import com.library.common.utils.DimensionUtil;
import com.library.common.view.refresh.CustomRefreshListener;
import com.library.common.view.refresh.CustomRefreshView;
import com.library.repository.models.JobListModel;
import com.library.repository.models.JobModel;

import java.util.List;

import butterknife.BindView;

public class RecruitFragment extends BaseMvpFragment<RecruitPresenter> implements RecruitContract.View {

    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.refreshView)
    CustomRefreshView refreshView;
    private JobHuntinAdapter adapter;

    @Override
    protected RecruitPresenter createPresenter() {
        return new RecruitPresenter(this);
    }

    public static RecruitFragment newInstance() {
        Bundle args = new Bundle();
        RecruitFragment fragment = new RecruitFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int layoutId() {
        return R.layout.ac_common_refresh_list;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        adapter = new JobHuntinAdapter();
        adapter.setSelfPadding(false);
        recycler.setLayoutManager(new LinearLayoutManager(getCurContext()));
        recycler.setAdapter(adapter);
        recycler.addItemDecoration(new SpacesItemDecoration(0, 10));
        refreshView.setRefreshListener(new CustomRefreshListener() {
            @Override
            public void onRefreshData(int page, int pageSize) {
                mPresenter.getMyCollectJob(page);
            }
        });


        //adapter.bindToRecyclerView(recycler);


        adapter.setEmptyView(R.layout.layout_empty);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                startActivity(JobDetailActivity.newInstance(getCurContext(),
                        ((JobModel)adapter.getItem(position)).getJobId(), ((JobModel)adapter.getItem(position)).getType() == 1));
            }
        });
    }

    @Override
    protected void lazyLoadData() {
        refreshView.autoRefresh();
    }

    @Override
    public void showCollectData(JobListModel model) {
        refreshView.noticeAdapterData(adapter, model.getJobsList(), model.isHasNext());
    }

    @Override
    public void loadError() {
        refreshView.finishLoading();
    }
}
