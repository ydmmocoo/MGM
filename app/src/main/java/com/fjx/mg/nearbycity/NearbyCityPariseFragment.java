package com.fjx.mg.nearbycity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fjx.mg.R;
import com.fjx.mg.nearbycity.adapter.NearbyCityPraiseListAdapter;
import com.fjx.mg.nearbycity.event.PraiseListEvent;
import com.fjx.mg.widget.viewpager.AutoHeightViewPager;
import com.library.common.base.BaseFragment;
import com.library.common.constant.IntentConstants;
import com.library.common.view.refresh.CustomRefreshListener;
import com.library.common.view.refresh.CustomRefreshView;
import com.library.repository.core.net.CommonObserver;
import com.library.repository.core.net.RxScheduler;
import com.library.repository.models.NearbyCityPraiseModel;
import com.library.repository.models.ResponseModel;
import com.library.repository.repository.RepositoryFactory;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;

/**
 * Author    by hanlz
 * Date      on 2019/10/19.
 * Description：同城详情评论列表
 */
@SuppressLint("ValidFragment")
public class NearbyCityPariseFragment extends BaseFragment {

    @BindView(R.id.refreshView)
    CustomRefreshView mRefreshView;
    @BindView(R.id.rvComment)
    RecyclerView mRvComment;
    private NearbyCityPraiseListAdapter mAdapter;

    AutoHeightViewPager viewPager;

    private String mCId;
    private int mPage = 1;

    public static NearbyCityPariseFragment newFragment(AutoHeightViewPager viewPager, String cId) {
        NearbyCityPariseFragment fragment = new NearbyCityPariseFragment(viewPager);
        Bundle bundle = new Bundle();
        bundle.putString(IntentConstants.CID, cId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @SuppressLint("ValidFragment")
    public NearbyCityPariseFragment(AutoHeightViewPager viewPager) {
        this.viewPager = viewPager;
    }

    @Override
    protected int layoutId() {
        return R.layout.fragment_nearby_city_comment_list;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        mCId = getArguments().getString(IntentConstants.CID);

        mRefreshView.autoRefresh();
        mRefreshView.setRefreshListener(new CustomRefreshListener() {
            @Override
            public void onRefreshData(int page, int pageSize) {
                mPage = page;
                requestPraiseList(mCId, "1");
            }
        });

        viewPager.setObjectForPosition(view, 1);//0代表tab的位置 0,1,2,3

        mRvComment.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new NearbyCityPraiseListAdapter();
        mAdapter.addFooterView(LayoutInflater.from(getActivity()).inflate(R.layout.item_placeholder_layout, null));
        mRvComment.setAdapter(mAdapter);
    }

    public void requestPraiseList(String cId, String page) {
        RepositoryFactory.getRemoteNearbyCitysApi()
                .praiseList(cId, page)
                .compose(RxScheduler.<ResponseModel<NearbyCityPraiseModel>>toMain())
                .as(this.<ResponseModel<NearbyCityPraiseModel>>bindAutoDispose())
                .subscribe(new CommonObserver<NearbyCityPraiseModel>() {
                    @Override
                    public void onSuccess(NearbyCityPraiseModel data) {
                        mRefreshView.finishLoading();
                        if (mPage == 1) {//直接替换数据
                            mAdapter.replaceData(data.getParseList());
                        } else {//刷新数据
                            mRefreshView.noticeAdapterData(mAdapter, data.getParseList(), data.isHasNext());//考虑实际，不显示无更多数据，hasNext=true
                            mRvComment.invalidateItemDecorations();
                        }
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        mRefreshView.finishLoading();
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        mRefreshView.finishLoading();
                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                        mRefreshView.finishLoading();
                    }
                });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPraiseEvent(PraiseListEvent event) {
        mRefreshView.autoRefresh();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }
}
