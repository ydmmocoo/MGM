package com.fjx.mg.food;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.fjx.mg.R;
import com.fjx.mg.food.adapter.RvTakeOutFoodSearchAdapter;
import com.fjx.mg.food.contract.TakeOutFoodSearchContract;
import com.fjx.mg.food.model.SearchEntity;
import com.fjx.mg.food.presenter.TakeOutFoodSearchPresenter;
import com.gyf.immersionbar.ImmersionBar;
import com.library.common.base.BaseMvpActivity;
import com.library.common.utils.CommonToast;
import com.library.common.utils.SoftInputUtil;
import com.library.repository.models.HomeShopListBean;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 外卖搜索
 */
public class TakeOutFoodSearchActivity extends BaseMvpActivity<TakeOutFoodSearchPresenter> implements TakeOutFoodSearchContract.View {

    @BindView(R.id.et_search)
    EditText mEtSearch;
    @BindView(R.id.rv_content)
    RecyclerView mRvContent;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout mRefreshLayout;

    private RvTakeOutFoodSearchAdapter mAdapter;
    private List<SearchEntity> mList = new ArrayList<>();

    private String mContent;
    private int mPage = 1;

    @Override
    protected int layoutId() {
        return R.layout.activity_take_out_food_search;
    }

    @Override
    protected void initView() {
        ImmersionBar.with(this)
                .fitsSystemWindows(false)
                .transparentStatusBar()
                .statusBarDarkFont(true)
                .init();
        //初始化RecyclerView
        LinearLayoutManager manager = new LinearLayoutManager(getCurContext());
        mRvContent.setLayoutManager(manager);
        mAdapter = new RvTakeOutFoodSearchAdapter(mList);
        mRvContent.setAdapter(mAdapter);

        setListener();
    }

    private void setListener() {
        //点击Item跳转店铺详情
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            if (mList.get(position).getItemType() == SearchEntity.TYPE_TAKE_OUT_FOOD) {
                Intent intent = new Intent(getCurContext(), StoreDetailActivity.class);
                intent.putExtra("id", mList.get(position).getData().getSId());
                startActivity(intent);
            }
        });
        //下拉刷新、上拉加载更多
        mRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                mPage++;
                mPresenter.getShopsList(mContent, mPage);
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mPage = 1;
                mPresenter.getShopsList(mContent, mPage);
            }
        });
    }

    @Override
    public void getShopListSuccess(List<HomeShopListBean.ShopListBean> data, boolean hasNext) {
        if (hasNext) {
            mRefreshLayout.setEnableLoadMore(true);
        } else {
            mRefreshLayout.setEnableLoadMore(false);
        }
        for (int i = 0; i < data.size(); i++) {
            if (i == 0) {
                mList.add(new SearchEntity(getResources().getString(R.string.take_out), SearchEntity.TYPE_HEADER));
            }
            mList.add(new SearchEntity(getResources().getString(R.string.take_out), data.get(i), SearchEntity.TYPE_TAKE_OUT_FOOD));
            if (i == data.size() - 1) {
                mList.add(new SearchEntity(SearchEntity.TYPE_FOOTER));
            }
        }
        if (mPage == 1) {
            mRefreshLayout.finishRefresh();
        } else {
            mRefreshLayout.finishLoadMore();
        }
        mAdapter.setList(mList);
    }

    @Override
    protected TakeOutFoodSearchPresenter createPresenter() {
        return new TakeOutFoodSearchPresenter(this);
    }

    @OnClick({R.id.iv_back, R.id.tv_search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back://返回
                finish();
                break;
            case R.id.tv_search://搜索
                mContent = mEtSearch.getText().toString();
                if (TextUtils.isEmpty(mContent)) {
                    CommonToast.toast(getString(R.string.hint_input_search_content));
                    return;
                }
                SoftInputUtil.hideSoftInput(getCurActivity());
                mPresenter.getShopsList(mContent, mPage);
                break;
        }
    }
}
