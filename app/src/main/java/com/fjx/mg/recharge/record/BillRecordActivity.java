package com.fjx.mg.recharge.record;

import android.content.Context;
import android.content.Intent;

import androidx.viewpager.widget.ViewPager;

import com.fjx.mg.R;
import com.fjx.mg.ToolBarManager;
import com.google.android.material.tabs.TabLayout;
import com.library.common.base.BaseFragment;
import com.library.common.base.BaseMvpActivity;
import com.library.common.base.adapter.PagerAdapter;

import java.util.List;

import butterknife.BindView;

public class BillRecordActivity extends BaseMvpActivity<BillRecordPresenter> implements BillRecordContract.View {
    @BindView(R.id.s_tab)
    TabLayout slidingTabLayout;

    @BindView(R.id.viewPager)
    ViewPager viewPager;

    public static Intent newInstance(Context context) {
        Intent intent = new Intent(context, BillRecordActivity.class);
        return intent;
    }

    @Override
    protected BillRecordPresenter createPresenter() {
        return new BillRecordPresenter(this);
    }

    @Override
    protected int layoutId() {
        return R.layout.ac_bill_record;
    }

    @Override
    protected void initView() {
        ToolBarManager.with(this).setTitle(getString(R.string.payment_record));
        mPresenter.initData();
    }

    @Override
    public void showTabsAndFragment(String[] titles, List<BaseFragment> fragments) {
        viewPager.setOffscreenPageLimit(fragments.size());
        viewPager.setAdapter(new PagerAdapter(getSupportFragmentManager(), fragments));
        for (int i = 0; i < titles.length; i++) {
            slidingTabLayout.addTab(slidingTabLayout.newTab().setText(titles[i]));
        }
        slidingTabLayout.setupWithViewPager(viewPager, true);
        for (int i = 0; i < slidingTabLayout.getTabCount(); i++) {
            slidingTabLayout.getTabAt(i).setText(titles[i]);
        }
    }
}
