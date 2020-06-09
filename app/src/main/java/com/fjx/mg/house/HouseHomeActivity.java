package com.fjx.mg.house;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.fjx.mg.R;
import com.fjx.mg.house.publish.HousePublishActivity;
import com.fjx.mg.utils.RankPermissionHelper;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.library.common.base.BaseFragment;
import com.library.common.base.BaseMvpActivity;
import com.library.common.base.adapter.PagerAdapter;
import com.library.repository.data.UserCenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class HouseHomeActivity extends BaseMvpActivity<HouseHomePresenter> implements HouseHomeContract.View {


    @BindView(R.id.s_tab)
    CommonTabLayout sTab;
    @BindView(R.id.viewPager)
    ViewPager viewPager;

    private int type;

    private List<BaseFragment> fragments;

    public static Intent newInstance(Context context) {
        Intent intent = new Intent(context, HouseHomeActivity.class);
        return intent;
    }

    @Override
    protected int layoutId() {
        return R.layout.ac_job_home;
    }


    @Override
    protected void initView() {
        mPresenter.initData();
    }


    @Override
    protected HouseHomePresenter createPresenter() {
        return new HouseHomePresenter(this);
    }

    @Override
    public void showTabAndItems(final List<BaseFragment> f, ArrayList<CustomTabEntity> mTabEntities) {
        this.fragments = f;
        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager(), fragments);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(2);
        sTab.setTabData(mTabEntities);
        sTab.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                viewPager.setCurrentItem(position, true);
            }

            @Override
            public void onTabReselect(int position) {

            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                sTab.setCurrentTab(i);
                type = i;
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

    }

    @OnClick({R.id.ivBack, R.id.tvRithtImage})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                onBackPressed();
                break;
            case R.id.tvRithtImage:
                if (UserCenter.needLogin()) return;
                if (RankPermissionHelper.NoPrivileges(2)) return;
                startActivityForResult(HousePublishActivity.newInstance(getCurContext(), type), 11);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == resultCode) {
            if (fragments == null) return;
            for (BaseFragment fragment : fragments) {
                fragment.onActivityResult(requestCode, resultCode, data);
            }
        }
    }
}
