package com.fjx.mg.me.publish.fragment;

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
import com.fjx.mg.job.detail.JobDetailActivity;
import com.fjx.mg.job.fragment.JobHuntinAdapter;
import com.fjx.mg.job.publish.JobPublicActivity;
import com.library.common.base.BaseFragment;
import com.library.common.base.adapter.decoration.SpacesItemDecoration;
import com.library.common.utils.CommonToast;
import com.library.common.utils.StringUtil;
import com.library.common.view.refresh.CustomRefreshListener;
import com.library.common.view.refresh.CustomRefreshView;
import com.library.repository.core.net.CommonObserver;
import com.library.repository.core.net.RxScheduler;
import com.library.repository.models.JobListModel;
import com.library.repository.models.JobModel;
import com.library.repository.models.ResponseModel;
import com.library.repository.repository.RepositoryFactory;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnSelectListener;

import butterknife.BindView;

public class JobFragment extends BaseFragment {
    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.refreshView)
    CustomRefreshView refreshView;
    private JobHuntinAdapter mAdapter;

    public static JobFragment newInstance() {

        Bundle args = new Bundle();

        JobFragment fragment = new JobFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int layoutId() {
        return R.layout.ac_common_refresh_list;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        recycler.setLayoutManager(new LinearLayoutManager(getCurContext()));
        recycler.addItemDecoration(new SpacesItemDecoration(10));
        mAdapter = new JobHuntinAdapter();
        mAdapter.setSelfPadding(false);
        recycler.setAdapter(mAdapter);



        //mAdapter.bindToRecyclerView(recycler);




        mAdapter.setEmptyView(R.layout.layout_empty);

        refreshView.doRefresh();
        refreshView.setRefreshListener(new CustomRefreshListener() {
            @Override
            public void onRefreshData(int page, int pageSize) {
                getJobList(page);
            }
        });

        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                startForResult(JobDetailActivity.newInstance(getCurContext(),
                        mAdapter.getItem(position).getJobId(),
                        mAdapter.getItem(position).getType() == 1, true), 111);
            }
        });
//        mAdapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
//                showActionDialog(mAdapter.getItem(position), position);
//                return true;
//            }
//        });
    }

    private void getJobList(int page) {
        RepositoryFactory.getRemoteJobApi().MyJobsList(page)
                .compose(RxScheduler.<ResponseModel<JobListModel>>toMain())
                .as(this.<ResponseModel<JobListModel>>bindAutoDispose())
                .subscribe(new CommonObserver<JobListModel>() {
                    @Override
                    public void onSuccess(JobListModel data) {
                        refreshView.noticeAdapterData(mAdapter, data.getJobsList(), data.isHasNext());
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        refreshView.finishLoading();
                        if (StringUtil.isNotEmpty(data.getMsg())) {
                            CommonToast.toast(data.getMsg());
                        }
                    }

                    @Override
                    public void onNetError(ResponseModel data) {

                    }
                });
    }


    private void deleteJob(String jobId, final int position) {
        showLoading();
        RepositoryFactory.getRemoteJobApi().deleteJob(jobId)
                .compose(RxScheduler.<ResponseModel<Object>>toMain())
                .as(this.<ResponseModel<Object>>bindAutoDispose())
                .subscribe(new CommonObserver<Object>() {
                    @Override
                    public void onSuccess(Object data) {
                        hideLoading();
                        mAdapter.remove(position);
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        if (StringUtil.isNotEmpty(data.getMsg())) {
                            CommonToast.toast(data.getMsg());
                        }
                        hideLoading();
                    }

                    @Override
                    public void onNetError(ResponseModel data) {

                    }
                });

    }


    private void houseStatus(String jid, final int position) {
        showLoading();
        RepositoryFactory.getRemoteJobApi().setJobStatus(jid, "1")
                .compose(RxScheduler.<ResponseModel<Object>>toMain())
                .as(this.<ResponseModel<Object>>bindAutoDispose())
                .subscribe(new CommonObserver<Object>() {
                    @Override
                    public void onSuccess(Object data) {
                        hideLoading();
                        mAdapter.getItem(position).setStatus("1");
                        mAdapter.notifyItemChanged(position);
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        if (StringUtil.isNotEmpty(data.getMsg())) {
                            CommonToast.toast(data.getMsg());
                        }
                        hideLoading();
                    }

                    @Override
                    public void onNetError(ResponseModel data) {

                    }
                });

    }

    private void showActionDialog(final JobModel model, final int p) {
        new XPopup.Builder(getContext())
                //.maxWidth(600)
                .asCenterList(getString(R.string.select), new String[]{
                                getString(R.string.delete),
                                getString(R.string.edit),
                                getString(R.string.appointed)},
                        new OnSelectListener() {
                            @Override
                            public void onSelect(int position, String text) {
                                if (position == 0) {
                                    deleteJob(model.getJobId(), p);
                                } else if (position == 1) {
                                    startActivity(JobPublicActivity.newInstance(getCurContext(), model.getType(), model.getJobId()));
                                } else {
                                    houseStatus(model.getJobId(), p);
                                }
                            }
                        })
                .show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == resultCode) refreshView.doRefresh();
    }
}
