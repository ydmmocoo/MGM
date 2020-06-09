package com.fjx.mg.food;

import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.fjx.mg.R;
import com.fjx.mg.ToolBarManager;
import com.google.android.material.tabs.TabLayout;
import com.library.common.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class OrderActivity extends BaseActivity {

    @BindView(R.id.tab)
    TabLayout mTab;
    @BindView(R.id.view_pager)
    ViewPager mViewPager;


    private List<String> mTitles = new ArrayList<>();
    private List<Fragment> mFragments = new ArrayList<>();

    public static Intent newInstance(Context context,int type) {
        Intent intent=new Intent(context, OrderActivity.class);
        intent.putExtra("type",type);
        return intent;
    }

    @Override
    protected int layoutId() {
        return R.layout.activity_order;
    }

    @Override
    protected void initView() {
        //设置标题
        ToolBarManager.with(this).setTitle(getString(R.string.my_order));
        //初始化Tab
        mTitles.add(getResources().getString(R.string.all));
        mTitles.add(getResources().getString(R.string.to_be_paid));
        //mTitles.add(getResources().getString(R.string.to_be_used));
        mTitles.add(getResources().getString(R.string.to_be_evaluated));
        mTitles.add(getResources().getString(R.string.after_sale_and_refund));
        mFragments.add(OrderFragment.newInstance(1));
        mFragments.add(OrderFragment.newInstance(2));
        //mFragments.add(OrderFragment.newInstance(3));
        mFragments.add(OrderFragment.newInstance(4));
        mFragments.add(OrderFragment.newInstance(5));
        TabFragmentAdapter tabFragmentAdapter = new TabFragmentAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(tabFragmentAdapter);
        mTab.setupWithViewPager(mViewPager);

        int type=getIntent().getIntExtra("type",0);
        mViewPager.setCurrentItem(type);
    }

    class TabFragmentAdapter extends FragmentPagerAdapter {

        public TabFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles.get(position % mTitles.size());
        }
    }
}
