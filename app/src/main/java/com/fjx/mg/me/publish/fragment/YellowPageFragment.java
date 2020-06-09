package com.fjx.mg.me.publish.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.listener.OnItemLongClickListener;
import com.fjx.mg.R;
import com.fjx.mg.main.yellowpage.YellowPageAdapter;
import com.fjx.mg.main.yellowpage.detail.YellowPageDetailActivity;
import com.fjx.mg.main.yellowpage.publish.YellowPagePublicActivity;
import com.fjx.mg.utils.DialogUtil;
import com.library.common.base.BaseFragment;
import com.library.common.base.adapter.decoration.SpacesItemDecoration;
import com.library.common.utils.CommonToast;
import com.library.common.utils.StringUtil;
import com.library.common.view.refresh.CustomRefreshListener;
import com.library.common.view.refresh.CustomRefreshView;
import com.library.repository.core.net.CommonObserver;
import com.library.repository.core.net.RxScheduler;
import com.library.repository.data.UserCenter;
import com.library.repository.models.CmpanydetaisModel;
import com.library.repository.models.CompanyDetailModel;
import com.library.repository.models.CompanyListModel;
import com.library.repository.models.ResponseModel;
import com.library.repository.repository.RepositoryFactory;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnSelectListener;

import butterknife.BindView;

public class YellowPageFragment extends BaseFragment {

    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.refreshView)
    CustomRefreshView refreshView;
    private YellowPageAdapter mAdapter;

    public static YellowPageFragment newInstance() {

        Bundle args = new Bundle();

        YellowPageFragment fragment = new YellowPageFragment();
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
        mAdapter = new YellowPageAdapter();
        recycler.setAdapter(mAdapter);



        //mAdapter.bindToRecyclerView(recycler);





        mAdapter.setEmptyView(R.layout.layout_empty);

        refreshView.doRefresh();
        refreshView.setRefreshListener(new CustomRefreshListener() {
            @Override
            public void onRefreshData(int page, int pageSize) {
                getCompanyList(page);
            }
        });

        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                startForResult(YellowPageDetailActivity.newInstance(getCurContext(),
                        mAdapter.getItem(position).getCId(), "",true,
                        mAdapter.getItem(position).getUId()), 111);
            }
        });

        mAdapter.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
                showActionDialog(mAdapter.getItem(position), position);
                return true;
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == resultCode) refreshView.doRefresh();
        refreshView.doRefresh();
    }


    private void getCompanyList(int page) {
        RepositoryFactory.getRemoteCompanyApi().myCompanyList(page, "")
                .compose(RxScheduler.<ResponseModel<CompanyListModel>>toMain())
                .as(this.<ResponseModel<CompanyListModel>>bindAutoDispose())
                .subscribe(new CommonObserver<CompanyListModel>() {
                    @Override
                    public void onSuccess(CompanyListModel data) {
                        refreshView.noticeAdapterData(mAdapter, data.getCompanyList(), data.isHasNext());
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        if (StringUtil.isNotEmpty(data.getMsg())) {
                            CommonToast.toast(data.getMsg());
                        }
                    }

                    @Override
                    public void onNetError(ResponseModel data) {

                    }
                });

    }

    private void showActionDialog(final CmpanydetaisModel model, final int p) {
        new XPopup.Builder(getContext())
                //.maxWidth(600)
                .asCenterList(getString(R.string.select), new String[]{
                                getString(R.string.delete),
                                getString(R.string.edit)},
                        new OnSelectListener() {
                            @Override
                            public void onSelect(int position, String text) {
                                if (position == 0) {
                                    deleteCompany(model.getCId(), p);
                                } else if (position == 1) {
                                    if (UserCenter.hasLogin()) {
                                        startActivityForResult(YellowPagePublicActivity.newInstance(getCurContext(), model.getCId()), 11);
                                    } else {
                                        new DialogUtil().showAlertDialog(getCurActivity(), R.string.tips, R.string.not_login_forward_login, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                                UserCenter.goLoginActivity();
                                            }
                                        });
                                    }
                                }
                            }
                        })
                .show();
    }

    private void deleteCompany(String jobId, final int p) {
        showLoading();
        RepositoryFactory.getRemoteCompanyApi().deleteCompany(jobId)
                .compose(RxScheduler.<ResponseModel<Object>>toMain())
                .as(this.<ResponseModel<Object>>bindAutoDispose())
                .subscribe(new CommonObserver<Object>() {
                    @Override
                    public void onSuccess(Object data) {
                        hideLoading();
                        mAdapter.remove(p);
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        hideLoading();
                        if (StringUtil.isNotEmpty(data.getMsg())) {
                            CommonToast.toast(data.getMsg());
                        }

                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                        hideLoading();
                        if (StringUtil.isNotEmpty(data.getMsg())) {
                            CommonToast.toast(data.getMsg());
                        }
                    }
                });
    }
}
