package com.fjx.mg.me.score;

import android.content.Context;
import android.content.Intent;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fjx.mg.R;
import com.fjx.mg.ToolBarManager;
import com.library.common.base.BaseMvpActivity;
import com.library.common.base.adapter.decoration.SpacesItemDecoration;
import com.library.common.view.refresh.CustomRefreshListener;
import com.library.common.view.refresh.CustomRefreshView;
import com.library.repository.models.ScoreListModel;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ScoreActivity extends BaseMvpActivity<ScorePresenter> implements ScoreContract.View {


    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.refreshView)
    CustomRefreshView refreshView;

    private ScoreAdapter scoreAdapter;

    @Override
    protected ScorePresenter createPresenter() {
        return new ScorePresenter(this);
    }

    public static Intent newInstance(Context context) {
        Intent intent = new Intent(context, ScoreActivity.class);
        return intent;
    }


    @Override
    protected int layoutId() {
        return R.layout.ac_common_refresh_list_toolbar;
    }

    @Override
    protected void initView() {
        ToolBarManager.with(this).setTitle(getString(R.string.point_detail));

        scoreAdapter = new ScoreAdapter();
        recycler.setLayoutManager(new LinearLayoutManager(getCurContext()));
        recycler.addItemDecoration(new SpacesItemDecoration(1));
        recycler.setAdapter(scoreAdapter);
        refreshView.autoRefresh();
        refreshView.setRefreshListener(new CustomRefreshListener() {
            @Override
            public void onRefreshData(int page, int pageSize) {
                mPresenter.getScoreList(page);
            }
        });

    }

    @Override
    public void showScoreList(ScoreListModel model) {
        refreshView.noticeAdapterData(scoreAdapter, model.getScoreList(), model.isHasNext());
    }

    @Override
    public void loadError() {
        refreshView.finishLoading();
    }
}
