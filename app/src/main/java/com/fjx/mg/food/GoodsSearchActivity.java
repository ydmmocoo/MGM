package com.fjx.mg.food;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fjx.mg.R;
import com.fjx.mg.food.adapter.RvGoodsSearchAdapter;
import com.fjx.mg.food.adapter.RvTakeOutFoodSearchAdapter;
import com.fjx.mg.food.contract.GoodsSearchContract;
import com.fjx.mg.food.presenter.GoodsSearchPresenter;
import com.gyf.immersionbar.ImmersionBar;
import com.library.common.base.BaseActivity;
import com.library.common.base.BaseMvpActivity;
import com.library.repository.models.CouponBean;
import com.library.repository.models.GoodsSearchBean;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class GoodsSearchActivity extends BaseMvpActivity<GoodsSearchPresenter> implements GoodsSearchContract.View {

    @BindView(R.id.et_search)
    EditText mEtSearch;
    @BindView(R.id.rv_content)
    RecyclerView mRvContent;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout mRefreshLayout;

    private RvGoodsSearchAdapter mAdapter;
    private List<GoodsSearchBean.GoodsListBean> mList = new ArrayList<>();
    private String mId;
    private int mPage = 1;
    private String mContent;

    public static Intent newInstance(Context context, String id) {
        Intent intent = new Intent(context, GoodsSearchActivity.class);
        intent.putExtra("id", id);
        return intent;
    }

    @Override
    protected int layoutId() {
        return R.layout.activity_goods_search;
    }

    @Override
    protected void initView() {
        mId = getIntent().getStringExtra("id");
        ImmersionBar.with(this)
                .fitsSystemWindows(false)
                .transparentStatusBar()
                .statusBarDarkFont(true)
                .init();
        //初始化RecyclerView
        LinearLayoutManager manager = new LinearLayoutManager(getCurContext());
        mRvContent.setLayoutManager(manager);
        mAdapter = new RvGoodsSearchAdapter(R.layout.item_store_goods, mList);
        mRvContent.setAdapter(mAdapter);

        //下拉刷新，上拉加载更多
        mRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                mPage++;
                mPresenter.getGoodsList(mId, mContent, mPage);
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mPage = 1;
                mPresenter.getGoodsList(mId, mContent, mPage);
            }
        });
    }

    @OnClick({R.id.iv_back, R.id.tv_search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back://返回
                finish();
                break;
            case R.id.tv_search://搜索
                mContent = mEtSearch.getText().toString();
                mPresenter.getGoodsList(mId, mContent, mPage);
                break;
        }
    }

    @Override
    public void getGoodsListSuccess(List<GoodsSearchBean.GoodsListBean> data, boolean hasNext) {
        if (hasNext) {
            mRefreshLayout.setEnableLoadMore(true);
        } else {
            mRefreshLayout.setEnableLoadMore(false);
        }
        if (mPage == 1) {
            mList = data;
            mRefreshLayout.finishRefresh();
        } else {
            mList.addAll(data);
            mRefreshLayout.finishLoadMore();
        }
        mAdapter.setList(mList);
    }

    @Override
    protected GoodsSearchPresenter createPresenter() {
        return new GoodsSearchPresenter(this);
    }
}