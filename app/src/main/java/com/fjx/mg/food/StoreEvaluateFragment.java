package com.fjx.mg.food;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fjx.mg.R;
import com.fjx.mg.food.adapter.GvEvaluateAdapter;
import com.fjx.mg.food.adapter.RvEvaluateAdapter;
import com.fjx.mg.food.contract.StoreEvaluateContract;
import com.fjx.mg.food.presenter.StoreEvaluatePresenter;
import com.fjx.mg.view.WrapContentGridView;
import com.library.common.base.BaseMvpFragment;
import com.library.common.view.materialratingbar.MaterialRatingBar;
import com.library.repository.models.StoreEvaluateBean;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @author yedeman
 * @date 2020/5/25.
 * email：ydmmocoo@gmail.com
 * description：
 */
public class StoreEvaluateFragment extends BaseMvpFragment<StoreEvaluatePresenter> implements StoreEvaluateContract.View {

    @BindView(R.id.rv_evaluate)
    RecyclerView mRvEvaluate;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout mRefreshLayout;
    private TextView mTvScore;
    private MaterialRatingBar mRattingBar;
    private TextView mTvEvaluate;
    private WrapContentGridView mGvTag;

    private RvEvaluateAdapter mAdapter;
    private List<StoreEvaluateBean.EvaluateListBean> mList;
    private GvEvaluateAdapter mEvaluateAdapter;
    private List<String> mTagList = new ArrayList<>();

    private String mId;
    private String mSearchType = "";
    private int mPage = 1;
    private boolean mHasNext;

    @Override
    protected int layoutId() {
        return R.layout.fragment_store_evaluate;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //初始化RecyclerView
        LinearLayoutManager manager = new LinearLayoutManager(getCurContext());
        mRvEvaluate.setLayoutManager(manager);
        View headerView = View.inflate(getCurContext(), R.layout.item_store_evaluate_header, null);
        mTvScore = headerView.findViewById(R.id.tv_score);
        mRattingBar = headerView.findViewById(R.id.ratting_bar);
        mTvEvaluate = headerView.findViewById(R.id.tv_evaluate);
        mGvTag = headerView.findViewById(R.id.gv_tag);
        View footerView = View.inflate(getCurContext(), R.layout.item_rv_store_list_footer, null);
        mAdapter = new RvEvaluateAdapter(R.layout.item_rv_evaluate, mList);
        mAdapter.addHeaderView(headerView);
        mAdapter.addFooterView(footerView);
        mRvEvaluate.setAdapter(mAdapter);
        //初始化评价标签
        mEvaluateAdapter = new GvEvaluateAdapter(getCurContext(), mTagList);
        mGvTag.setAdapter(mEvaluateAdapter);
        //标签点击事件
        mGvTag.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mPage = 1;
                mSearchType = String.valueOf(position + 1);
                mPresenter.getEvaluateList(mId, mSearchType, mPage);
            }
        });

        //设置不能下拉刷新
        mRefreshLayout.setEnableRefresh(false);
        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                if (mHasNext) {
                    mPage++;
                    mPresenter.getEvaluateList(mId, mSearchType, mPage);
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.getEvaluateList(mId, mSearchType, mPage);
    }

    @Override
    public void getEvaluateListSuccess(StoreEvaluateBean data) {
        if (data==null||data.getShopInfo()==null||data.getEvaluateList()==null){
            return;
        }
        //设置评分
        mTvScore.setText(data.getShopInfo().getAvgGlobalScore());
        mRattingBar.setRating(Float.parseFloat(data.getShopInfo().getAvgGlobalScore()));
        //设置评价
        mTvEvaluate.setText(getResources().getString(R.string.flavor)
                .concat(" ")
                .concat(data.getShopInfo().getAvgTasteScore())
                .concat("  ")
                .concat(getResources().getString(R.string.distribution))
                .concat(" ")
                .concat(data.getShopInfo().getAvgPackageScore())
                .concat("  ")
                .concat(getResources().getString(R.string.distribution))
                .concat(data.getShopInfo().getAvgDeliveryScore()));
        //设置标签
        mTagList.clear();
        mTagList.add(getResources().getString(R.string.publish_comments_through_pictures)
                .concat(" ")
                .concat(String.valueOf(data.getShopInfo().getSlideShowCount())));
        mTagList.add(getResources().getString(R.string.favorable_comments)
                .concat(" ")
                .concat(String.valueOf(data.getShopInfo().getGoodsEvaluateCount())));
        mTagList.add(getResources().getString(R.string.negative_comment)
                .concat(" ")
                .concat(String.valueOf(data.getShopInfo().getBadEvaluateCount())));
        mTagList.add(getResources().getString(R.string.good_taste)
                .concat(" ")
                .concat(String.valueOf(data.getShopInfo().getGoodTasteCount())));
        mTagList.add(getResources().getString(R.string.well_packed)
                .concat(" ")
                .concat(String.valueOf(data.getShopInfo().getGoodPackageCount())));
        mTagList.add(getResources().getString(R.string.fast_delivery)
                .concat(" ")
                .concat(String.valueOf(data.getShopInfo().getDeliveryFastCount())));
        mEvaluateAdapter.setData(mTagList);
        mHasNext = data.isHasNext();
        if (mHasNext) {
            mRefreshLayout.setEnableLoadMore(true);
        } else {
            mRefreshLayout.setEnableLoadMore(false);
        }
        if (mPage == 1) {
            mList = data.getEvaluateList();
        } else {
            mList.addAll(data.getEvaluateList());
        }
        mAdapter.setList(mList);
    }

    @Override
    protected StoreEvaluatePresenter createPresenter() {
        return new StoreEvaluatePresenter(this);
    }

    public void setId(String id) {
        mId = id;
    }
}
