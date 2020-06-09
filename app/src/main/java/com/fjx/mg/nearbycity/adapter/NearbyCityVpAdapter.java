package com.fjx.mg.nearbycity.adapter;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Author    by hanlz
 * Date      on 2019/10/19.
 * Description：
 */
public class NearbyCityVpAdapter extends FragmentPagerAdapter {
    private List<Fragment> mDatas;

    public NearbyCityVpAdapter(FragmentManager fm, List<Fragment> datas) {
        super(fm);
        mDatas = datas;
    }

    public void setData(List<Fragment> data) {
        mDatas = data;
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int i) {
        return mDatas.get(i);
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }
}
