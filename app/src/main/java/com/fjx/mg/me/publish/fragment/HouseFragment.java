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
import com.fjx.mg.house.detail.HouseDetailActivity;
import com.fjx.mg.house.fragment.HouseLeaseAdapter;
import com.fjx.mg.house.publish.HousePublishActivity;
import com.fjx.mg.job.publish.JobPublicActivity;
import com.library.common.base.BaseFragment;
import com.library.common.base.adapter.decoration.SpacesItemDecoration;
import com.library.common.utils.CommonToast;
import com.library.common.utils.StringUtil;
import com.library.common.view.refresh.CustomRefreshListener;
import com.library.common.view.refresh.CustomRefreshView;
import com.library.repository.core.net.CommonObserver;
import com.library.repository.core.net.RxScheduler;
import com.library.repository.models.HouseDetailModel;
import com.library.repository.models.HouseListModel;
import com.library.repository.models.JobModel;
import com.library.repository.models.ResponseModel;
import com.library.repository.repository.RepositoryFactory;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnSelectListener;

import butterknife.BindView;

public class HouseFragment extends BaseFragment {

    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.refreshView)
    CustomRefreshView refreshView;
    private HouseLeaseAdapter mAdapter;


    public static HouseFragment newInstance() {
        Bundle args = new Bundle();
        HouseFragment fragment = new HouseFragment();
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
        mAdapter = new HouseLeaseAdapter();
        recycler.setAdapter(mAdapter);

        mAdapter.setEmptyView(R.layout.layout_empty);

        refreshView.doRefresh();
        refreshView.setRefreshListener((page, pageSize) -> getHouseList(page));

        mAdapter.setOnItemClickListener((adapter, view1, position) -> startForResult(HouseDetailActivity.newInstance(getCurContext(), mAdapter.getItem(position).getHid(), true), 111));

    }

    private void getHouseList(int page) {
        RepositoryFactory.getRemoteJobApi().myHouseList(page)
                .compose(RxScheduler.<ResponseModel<HouseListModel>>toMain())
                .as(this.<ResponseModel<HouseListModel>>bindAutoDispose())
                .subscribe(new CommonObserver<HouseListModel>() {
                    @Override
                    public void onSuccess(HouseListModel data) {
                        refreshView.noticeAdapterData(mAdapter, data.getHouseList(), data.isHasNext());
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


    private void deleteHouse(String hid, final int position) {
        showLoading();
        RepositoryFactory.getRemoteJobApi().deleteHouse(hid)
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

    private void houseStatus(String hid, final int position) {
        showLoading();
        RepositoryFactory.getRemoteJobApi().setStatus(hid, "1")
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

    private void showActionDialog(final HouseDetailModel model, final int p) {
        new XPopup.Builder(getContext())
                //.maxWidth(600)
                .asCenterList(getString(R.string.select), new String[]{
                                getString(R.string.delete),
                                getString(R.string.edit),
                                getString(R.string.Rented)},
                        new OnSelectListener() {
                            @Override
                            public void onSelect(int position, String text) {
                                if (position == 0) {
                                    deleteHouse(model.getHid(), p);
                                } else if (position == 1) {
                                    startActivity(HousePublishActivity.newInstance(getCurContext(), model.getType(), model.getHid()));
                                } else {
                                    houseStatus(model.getHid(), p);
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
