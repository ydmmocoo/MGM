package com.fjx.mg.nearbycity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.fjx.mg.R;
import com.fjx.mg.nearbycity.adapter.TopTypeDetailAdapter;
import com.fjx.mg.nearbycity.mvp.TopTypeDetailContract;
import com.fjx.mg.nearbycity.mvp.TopTypeDetailPresenter;
import com.fjx.mg.widget.recyclerview.ItemDecoration;
import com.library.common.base.BaseMvpActivity;
import com.library.common.utils.CommonToast;
import com.library.common.utils.StringUtil;
import com.library.common.view.refresh.CustomRefreshListener;
import com.library.common.view.refresh.CustomRefreshView;
import com.library.repository.models.NearbyCItyGetListModel;
import com.library.repository.models.ResponseModel;
import com.library.repository.models.TotalCityCircleListModel;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Author    by hanlz
 * Date      on 2019/10/20.
 * Description：
 */
public class NearbyCitySearchActivity extends BaseMvpActivity<TopTypeDetailPresenter> implements TopTypeDetailContract.View {


    private int mPage = 1;

    @BindView(R.id.rvTopType)
    RecyclerView mRvTopType;
    private TopTypeDetailAdapter mAdapter;
    @BindView(R.id.refreshView)
    CustomRefreshView mRefreshView;
    @BindView(R.id.etSearch)
    EditText mEtSearch;

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, NearbyCitySearchActivity.class);
        return intent;
    }


    @Override
    protected int layoutId() {
        return R.layout.act_nearby_city_search;
    }

    @Override
    protected void initView() {
        super.initView();
        mRefreshView.setRefreshListener(new CustomRefreshListener() {
            @Override
            public void onRefreshData(int page, int pageSize) {
                mPage = page;
                mPresenter.requestNearbyCityList(page + "", mEtSearch.getText().toString().trim(), "");
            }
        });
        mAdapter = new TopTypeDetailAdapter();
        mRvTopType.addItemDecoration(new ItemDecoration(this, ItemDecoration.VERTICAL_LIST));
        mRvTopType.setLayoutManager(new LinearLayoutManager(getCurContext()));
        mRvTopType.setAdapter(mAdapter);

        mAdapter.setEmptyView(R.layout.empty_all_moments_message);

        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                TotalCityCircleListModel item = (TotalCityCircleListModel) adapter.getItem(position);
                startActivity(NearbyCityDetailActivity.newIntent(getCurContext(), item.getcId()));
            }
        });
        mAdapter.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
                TotalCityCircleListModel item = (TotalCityCircleListModel) adapter.getItem(position);
                if (view.getId() == R.id.tvLike) {

                    if (item.isLike()) {
                        mPresenter.requestCancelPraise("", "", item.getcId());
                    } else {
                        mPresenter.requestPraise("", "", item.getcId());
                    }
                }
            }
        });
    }


    @OnClick({R.id.ivBack, R.id.tvSearchGo})
    public void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack://返回
                finish();
                break;
            case R.id.tvSearchGo://搜索
                search();
                break;
            default:
        }
    }

    private void search() {
        if (StringUtil.isEmpty(mEtSearch.getText().toString().trim())) {
            CommonToast.toast(getString(R.string.hint_input_search_content));
            return;
        }
        mRefreshView.autoRefresh();
    }

    @Override
    protected TopTypeDetailPresenter createPresenter() {
        return new TopTypeDetailPresenter(this);
    }


    @Override
    public void responseNearbyCityList(NearbyCItyGetListModel model) {
        mRefreshView.finishLoading();
        if (mPage == 1) {//直接替换数据
            mAdapter.replaceData(model.getCityCircleList());
        } else {//刷新数据
            mRefreshView.noticeAdapterData(mAdapter, model.getCityCircleList(), model.isHasNext());//考虑实际，不显示无更多数据，hasNext=true
            mRvTopType.invalidateItemDecorations();
        }
    }

    @Override
    public void responsePraise() {
        mRefreshView.autoRefresh();
        CommonToast.toast(getString(R.string.praise_success));
    }

    @Override
    public void responseCancelPraise() {
        mRefreshView.autoRefresh();
        CommonToast.toast(getString(R.string.cancelPraise));
    }

    @Override
    public void responseFailed(ResponseModel model) {
        mRefreshView.finishLoading();
    }
}
