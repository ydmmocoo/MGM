package com.library.common.base.adapter;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.library.common.base.BaseFragment;

import java.util.List;

/**
 * Created by WYiang on 2017/10/21.
 */

public class PagerAdapter extends FragmentPagerAdapter {

    private String[] mTitles;
    private List<BaseFragment> fragments;

    public PagerAdapter(FragmentManager fm, List<BaseFragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    public PagerAdapter(FragmentManager fm, List<BaseFragment> fragments, String[] titles) {
        super(fm);
        this.fragments = fragments;
        mTitles = titles;

    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (mTitles != null)
            return mTitles[position];
        return "";
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
    @Override
    public long getItemId(int position) {
        int hashCode = fragments.get(position).hashCode();
        return hashCode;
    }
}
