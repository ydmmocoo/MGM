package com.fjx.mg.main.yellowpage;

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
import com.fjx.mg.main.yellowpage.detail.YellowPageDetailActivity;
import com.library.common.base.BaseFragment;
import com.library.common.base.adapter.decoration.SpacesItemDecoration;
import com.library.common.view.refresh.CustomRefreshListener;
import com.library.common.view.refresh.CustomRefreshView;
import com.library.repository.core.net.CommonObserver;
import com.library.repository.core.net.RxScheduler;
import com.library.repository.db.DBDaoFactory;
import com.library.repository.models.CmpanydetaisModel;
import com.library.repository.models.CompanyDetailModel;
import com.library.repository.models.CompanyListModel;
import com.library.repository.models.ResponseModel;
import com.library.repository.repository.RepositoryFactory;

import java.util.List;

import butterknife.BindView;

public class YellowPageTabFragment extends BaseFragment {
    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.refreshView)
    CustomRefreshView refreshView;

    private String mType;

    private YellowPageAdapter mAdapter;

    public static YellowPageTabFragment newInstance(String type) {

        Bundle args = new Bundle();
        args.putString("type", type);
        YellowPageTabFragment fragment = new YellowPageTabFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected int layoutId() {
        return R.layout.ac_common_refresh_list;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mType = getArguments().getString("type");

        refreshView.autoRefresh();
        refreshView.setRefreshListener(new CustomRefreshListener() {
            @Override
            public void onRefreshData(int page, int pageSize) {
                String title = ((YellowPageActivity) getActivity()).getSearchTitle();
                getCompanyList(page, title);
            }
        });

        mAdapter = new YellowPageAdapter();
        recycler.setLayoutManager(new LinearLayoutManager(getCurContext()));
        recycler.addItemDecoration(new SpacesItemDecoration(1));
        recycler.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                startActivity(YellowPageDetailActivity.newInstance(getCurContext(),
                        mAdapter.getItem(position).getCId(),"1",false,
                        mAdapter.getItem(position).getUId()));
            }
        });


        List<CmpanydetaisModel> dataList = DBDaoFactory.getCompanyListDaos().queryList(mType);
        mAdapter.setNewInstance(dataList);
    }


    void getCompanyList(final int page, String title) {
        RepositoryFactory.getRemoteCompanyApi().companyList(page, title, mType)
                .compose(RxScheduler.<ResponseModel<CompanyListModel>>toMain())
                .as(this.<ResponseModel<CompanyListModel>>bindAutoDispose())
                .subscribe(new CommonObserver<CompanyListModel>() {
                    @Override
                    public void onSuccess(CompanyListModel data) {
                        if (page == 1) {
                            DBDaoFactory.getCompanyListDaos().deleteAll();
                        }
                        for (CmpanydetaisModel m : data.getCompanyList()) {
                            m.setType(mType);
                        }
                        DBDaoFactory.getCompanyListDaos().insertList(data.getCompanyList());
                        if (mView == null) return;
                        refreshView.noticeAdapterData(mAdapter, data.getCompanyList(), data.isHasNext());
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        if (mView == null) return;
                        refreshView.finishLoading();
                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                        if (mView == null) return;
                        refreshView.finishLoading();
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        refreshView.autoRefresh();
    }

    public void doRefresh() {
        refreshView.doRefresh();
    }
}
