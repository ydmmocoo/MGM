package com.fjx.mg.me.collect;

import android.content.Context;
import android.content.Intent;

import androidx.viewpager.widget.ViewPager;

import com.fjx.mg.R;
import com.fjx.mg.ToolBarManager;
import com.flyco.tablayout.SlidingTabLayout;
import com.library.common.base.BaseFragment;
import com.library.common.base.BaseMvpActivity;
import com.library.common.base.adapter.PagerAdapter;

import java.util.List;

import butterknife.BindView;

public class MyCollectActivity extends BaseMvpActivity<MyCollectPresenter> implements MyCollectContract.View {

    @BindView(R.id.s_tab)
    SlidingTabLayout sTab;
    @BindView(R.id.viewPager)
    ViewPager viewPager;

    @Override
    protected MyCollectPresenter createPresenter() {
        return new MyCollectPresenter(this);
    }

    public static Intent newInstance(Context context) {
        Intent intent = new Intent(context, MyCollectActivity.class);
        return intent;
    }

    @Override
    protected int layoutId() {
        return R.layout.ac_my_collect;
    }

    @Override
    protected void initView() {
        ToolBarManager.with(this).setTitle(getString(R.string.collect));
        mPresenter.initData();
    }

    @Override
    public void showTabAndFragment(String[] titles, List<BaseFragment> fragments) {
        viewPager.setOffscreenPageLimit(fragments.size());
        viewPager.setAdapter(new PagerAdapter(getSupportFragmentManager(), fragments));
        sTab.setViewPager(viewPager, titles);
    }
}
