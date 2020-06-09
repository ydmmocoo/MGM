package com.fjx.mg.main.payment.questionceter;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.fjx.mg.R;
import com.fjx.mg.ToolBarManager;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.library.common.base.BaseFragment;
import com.library.common.base.BaseMvpActivity;
import com.library.common.base.adapter.PagerAdapter;
import com.library.common.utils.StatusBarManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class QuestionCenterActivity extends BaseMvpActivity<QuestionCenterPresenter> implements QuestionCenterContract.View {

    @BindView(R.id.s_tab)
    CommonTabLayout sTab;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    private List<BaseFragment> fragments;


    public static Intent newInstance(Context context) {
        return new Intent(context, QuestionCenterActivity.class);
    }

    @Override
    protected int layoutId() {
        return R.layout.activity_question_center;
    }


    @Override
    protected void initView() {
        StatusBarManager.setLightFontColor(this, R.color.colorAccent);
        ToolBarManager.with(this).setTitle(getString(R.string.my_answer));

        mPresenter.initData();
    }


    @Override
    protected QuestionCenterPresenter createPresenter() {
        return new QuestionCenterPresenter(this);
    }

    @Override
    public void showTabAndItems(final List<BaseFragment> fragments, ArrayList<CustomTabEntity> mTabEntities) {
        this.fragments = fragments;
        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager(), fragments);
        viewPager.setAdapter(pagerAdapter);
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
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

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





