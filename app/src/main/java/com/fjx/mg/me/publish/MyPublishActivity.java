package com.fjx.mg.me.publish;

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

public class MyPublishActivity extends BaseMvpActivity<MyPublishPresenter> implements MyPublishContract.View {

    @BindView(R.id.s_tab)
    SlidingTabLayout sTab;
    @BindView(R.id.viewPager)
    ViewPager viewPager;

    @Override
    protected MyPublishPresenter createPresenter() {
        return new MyPublishPresenter(this);
    }

    public static Intent newInstance(Context context) {
        Intent intent = new Intent(context, MyPublishActivity.class);
        return intent;
    }

    @Override
    protected void initView() {
        ToolBarManager.with(this).setTitle(getString(R.string.my_publish));
        mPresenter.initData();
    }

    @Override
    protected int layoutId() {
        return R.layout.ac_my_publish;
    }


    @Override
    public void showTabsAndFragment(String[] title, List<BaseFragment> fragments) {
        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager(), fragments);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(3);
        sTab.setViewPager(viewPager, title);
    }
}
