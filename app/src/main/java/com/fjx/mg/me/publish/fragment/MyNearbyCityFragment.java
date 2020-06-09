package com.fjx.mg.me.publish.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.fjx.mg.R;
import com.fjx.mg.nearbycity.NearbyCityDetailActivity;
import com.fjx.mg.nearbycity.SetTopActivity;
import com.fjx.mg.nearbycity.adapter.MyNearbyCityAdapter;
import com.fjx.mg.nearbycity.dialog.MyNearbyCItyDialogFragment;
import com.fjx.mg.widget.recyclerview.ItemDecoration;
import com.library.common.base.BaseFragment;
import com.library.common.utils.CommonToast;
import com.library.common.utils.StringUtil;
import com.library.common.utils.ViewUtil;
import com.library.common.view.refresh.CustomRefreshListener;
import com.library.common.view.refresh.CustomRefreshView;
import com.library.repository.core.net.CommonObserver;
import com.library.repository.core.net.RxScheduler;
import com.library.repository.models.MyCityCircleListModel;
import com.library.repository.models.NearbyCityInfoModel;
import com.library.repository.models.ResponseModel;
import com.library.repository.models.TotalCityCircleListModel;
import com.library.repository.repository.RepositoryFactory;
import com.library.repository.util.LogUtil;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Author    by hanlz
 * Date      on 2019/10/18.
 * Description：我的同城
 */
public class MyNearbyCityFragment extends BaseFragment implements OnItemClickListener, MyNearbyCItyDialogFragment.OnSwitchListener, OnItemChildClickListener {

    @BindView(R.id.refreshView)
    CustomRefreshView mRefreshView;
    @BindView(R.id.recycler)
    RecyclerView mRecycler;

    MyNearbyCityAdapter mAdapter;

    private int mPage = 1;

    public static MyNearbyCityFragment newInstance() {
        Bundle args = new Bundle();
        MyNearbyCityFragment fragment = new MyNearbyCityFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int layoutId() {
        return R.layout.ac_common_refresh_list;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecycler.addItemDecoration(new ItemDecoration(getActivity(), ItemDecoration.VERTICAL_LIST));
        mAdapter = new MyNearbyCityAdapter();
        mRecycler.setAdapter(mAdapter);
        mAdapter.setEmptyView(R.layout.empty_all_moments_message);
        mRefreshView.autoRefresh();
        mRefreshView.setRefreshListener(new CustomRefreshListener() {
            @Override
            public void onRefreshData(int page, int pageSize) {
                mPage = page;
                requestMyCityCircleList("" + page, "", "");
            }
        });
        mAdapter.setOnItemChildClickListener(this);
        mAdapter.setOnItemClickListener(this);
    }

    public void requestMyCityCircleList(String page, String k, String typeId) {
        RepositoryFactory.getRemoteNearbyCitysApi()
                .myCityCircleList(page, k, typeId)
                .compose(RxScheduler.<ResponseModel<NearbyCityInfoModel>>toMain())
                .as(this.<ResponseModel<NearbyCityInfoModel>>bindAutoDispose())
                .subscribe(new CommonObserver<NearbyCityInfoModel>() {
                    @Override
                    public void onSuccess(NearbyCityInfoModel data) {
                        mRefreshView.finishLoading();
                        if (mPage == 1) {//直接替换数据
                            mAdapter.setList(data.getCityCircleList());
                        } else {//刷新数据
                            mRefreshView.noticeAdapterData(mAdapter, data.getCityCircleList(), data.getHasNext());//考虑实际，不显示无更多数据，hasNext=true
                            mRecycler.invalidateItemDecorations();
                        }
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        mRefreshView.finishLoading();
                        CommonToast.toast(data.getMsg());
                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                        mRefreshView.finishLoading();
                        CommonToast.toast(data.getMsg());
                    }
                });
    }


    private void requestsetStatus(String cId, String status) {
        RepositoryFactory.getRemoteNearbyCitysApi()
                .setStatus(cId, status)
                .compose(RxScheduler.<ResponseModel<Object>>toMain())
                .as(this.<ResponseModel<Object>>bindAutoDispose())
                .subscribe(new CommonObserver<Object>() {
                    @Override
                    public void onSuccess(Object data) {
                        CommonToast.toast(R.string.successfully);
                        hideLoading();
                        mRefreshView.autoRefresh();
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        CommonToast.toast(data.getMsg());
                        hideLoading();
                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                        hideLoading();
                        CommonToast.toast(data.getMsg());
                    }
                });
    }


    @Override
    public void onItemChildClick(@NotNull BaseQuickAdapter adapter, View view, int position) {
        if (view.getId() == R.id.ivPop) {
            MyCityCircleListModel item = (MyCityCircleListModel) adapter.getItem(position);
            MyNearbyCItyDialogFragment instance = MyNearbyCItyDialogFragment.getInstance(item);
            instance.show(getFragmentManager(), "MyNearbyCItyDialogFragment");
            instance.setOnSwitchListener(this);
        } else if (view.getId() == R.id.tvLike) {
            TextView tvLike = (TextView) view;
            MyCityCircleListModel item = (MyCityCircleListModel) adapter.getItem(position);
//            boolean isLike = StringUtil.equals("0", item.getLikeNum()) ? false : true;
            boolean isLike = item.isLike();
            if (isLike) {
                item.setLikeNum(StringUtil.add(item.getLikeNum(), "1"));
                ViewUtil.setDrawableLeft(tvLike, R.drawable.like_gray);
                tvLike.setText(item.getLikeNum());
                cancelPraise(item.getcId());
                item.setLike(false);
            } else {
                item.setLikeNum(StringUtil.subtract(item.getLikeNum(), "1", 0L));
                ViewUtil.setDrawableLeft(tvLike, R.drawable.like_red);
                tvLike.setText(item.getLikeNum());
                praise(item.getcId());
                item.setLike(true);
            }
        }
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        MyCityCircleListModel model = (MyCityCircleListModel) adapter.getItem(position);
        view.getContext().startActivity(NearbyCityDetailActivity.newIntent(view.getContext(), model.getcId()));
    }

    @Override
    public void onSwitch(MyCityCircleListModel model, String status) {
        showLoading();
        requestsetStatus(model.getcId(), status);
    }

    @Override
    public void onSetTop(String cId) {//修改同城置顶

        startActivity(SetTopActivity.newIntent(getActivity(), cId));
    }

    private void praise(String cId) {
        //点赞
        RepositoryFactory.getRemoteNearbyCitysApi()
                .praise("", "", cId)
                .compose(RxScheduler.<ResponseModel<Object>>toMain())
                .subscribe(new CommonObserver<Object>() {
                    @Override
                    public void onSuccess(Object data) {
//                        mRefreshView.autoRefresh();
//                        CommonToast.toast(getActivity().getString(R.string.praise_success));
                    }

                    @Override
                    public void onError(ResponseModel data) {

                    }

                    @Override
                    public void onNetError(ResponseModel data) {

                    }
                });
    }

    private void cancelPraise(String cId) {
        //取消点赞
        RepositoryFactory.getRemoteNearbyCitysApi()
                .cancelPraise("", "", cId)
                .compose(RxScheduler.<ResponseModel<Object>>toMain())
                .subscribe(new CommonObserver<Object>() {
                    @Override
                    public void onSuccess(Object data) {
//                        mRefreshView.autoRefresh();
//                        CommonToast.toast(getActivity().getString(R.string.cancelPraise));
                    }

                    @Override
                    public void onError(ResponseModel data) {

                    }

                    @Override
                    public void onNetError(ResponseModel data) {

                    }
                });
    }

}
