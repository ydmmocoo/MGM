
package com.fjx.mg.main.payment.questionceter.Fragment;

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
import com.fjx.mg.main.payment.detail.AskDetailActivity;
import com.library.common.base.BaseMvpFragment;
import com.library.common.base.adapter.decoration.SpacesItemDecoration;
import com.library.common.view.refresh.CustomRefreshListener;
import com.library.common.view.refresh.CustomRefreshView;
import com.library.repository.models.MyQuestionListModel;

import butterknife.BindView;


public class CenterFragment extends BaseMvpFragment<CenterPresenter> implements CenterContract.View {

    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.refreshView)
    CustomRefreshView refreshView;


    private CenterAdapter mAdapter;


    public static CenterFragment newInstance(int type) {
        Bundle args = new Bundle();
        args.putInt("type", type);
        CenterFragment fragment = new CenterFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected CenterPresenter createPresenter() {
        return new CenterPresenter(this);
    }

    @Override
    protected int layoutId() {
        return R.layout.fragment_center;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        final int type = getArguments().getInt("type");
        mAdapter = new CenterAdapter();
        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                startActivity(AskDetailActivity.newInstance(getCurContext(), mAdapter.getItem(position).getQId()));
            }
        });


        recycler.setLayoutManager(new LinearLayoutManager(getCurContext()));
        recycler.addItemDecoration(new SpacesItemDecoration(1));
        recycler.setAdapter(mAdapter);

        refreshView.setRefreshListener(new CustomRefreshListener() {
            @Override
            public void onRefreshData(int page, int pageSize) {
                mPresenter.getQuestionList("" + type, page);
            }
        });
        refreshView.autoRefresh();
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshView.autoRefresh();
    }

    @Override
    public void showQuestionListModel(MyQuestionListModel data) {
        int type = getArguments().getInt("type");
        for (int i = 0; i < data.getQuestionList().size(); i++) {
            data.getQuestionList().get(i).setType(type);
        }
        refreshView.noticeAdapterData(mAdapter, data.getQuestionList(), data.isHasNext());
    }

    @Override
    public void loadError() {
        refreshView.finishLoading();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        refreshView.autoRefresh();
    }
}
