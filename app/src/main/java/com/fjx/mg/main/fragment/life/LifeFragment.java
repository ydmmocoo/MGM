package com.fjx.mg.main.fragment.life;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.fjx.mg.R;
import com.flyco.tablayout.SlidingTabLayout;
import com.library.common.base.BaseFragment;
import com.library.common.base.BaseMvpFragment;
import com.library.common.base.adapter.PagerAdapter;
import com.library.common.base.adapter.decoration.SpacesItemDecoration;
import com.library.common.view.BannerView;
import com.library.common.view.refresh.CustomRefreshListener;
import com.library.common.view.refresh.CustomRefreshView;

import java.util.List;

import butterknife.BindView;

public class LifeFragment extends BaseMvpFragment<LifePresenter> implements LifeContract.View {

    @BindView(R.id.s_tab)
    SlidingTabLayout slidingTabLayout;

    @BindView(R.id.bannseView)
    BannerView bannerView;


    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.refreshView)
    CustomRefreshView refreshView;

    @BindView(R.id.recommendRecycler)
    RecyclerView recommendRecycler;

    private RecommendGoodAdapter goodAdapter;


    public static LifeFragment newInstance() {
        Bundle args = new Bundle();
        LifeFragment fragment = new LifeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int layoutId() {
        return R.layout.fragment_life;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        goodAdapter = new RecommendGoodAdapter();
        recommendRecycler.setLayoutManager(new LinearLayoutManager(getCurContext(), RecyclerView.HORIZONTAL, false));
        recommendRecycler.setAdapter(goodAdapter);
        recommendRecycler.addItemDecoration(new SpacesItemDecoration(10, 0));
        mPresenter.initData();

        refreshView.setEnableLoadMore(false);
        //goodAdapter.openLoadAnimation();
        refreshView.setRefreshListener(new CustomRefreshListener() {
            @Override
            public void onRefreshData(int page, int pageSize) {

                refreshView.finishLoading();
            }
        });
    }


    @Override
    protected LifePresenter createPresenter() {
        return new LifePresenter(this);
    }

    @Override
    public void showRecommendGoods(List<Object> goodLit) {
//        goodAdapter.setList(goodLit);
        refreshView.noticeAdapterData(goodAdapter, goodLit);
//        recommendRecycler.invalidateItemDecorations();
    }

    @Override
    public void showTabFragments(String[] titles, List<BaseFragment> fragments) {
        viewPager.setOffscreenPageLimit(fragments.size());
        viewPager.setAdapter(new PagerAdapter(getChildFragmentManager(), fragments));
        slidingTabLayout.setViewPager(viewPager, titles);
    }

    @Override
    public void showBanner(List<String> urlList) {
        bannerView.showImages(urlList);
    }
}
