package com.fjx.mg.main.fragment.life;

import com.fjx.mg.R;
import com.fjx.mg.main.fragment.life.Tab.LifeTabFragment;
import com.fjx.mg.main.fragment.news.tab.NewsTabFragment;
import com.library.common.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

class LifePresenter extends LifeContract.Presenter {


    LifePresenter(LifeContract.View view) {
        super(view);
    }

    @Override
    void initData() {
        List<Object> list = new ArrayList<>();
        list.add(new Object());
        list.add(new Object());
        list.add(new Object());
        list.add(new Object());
        list.add(new Object());
        mView.showRecommendGoods(list);


        String[] titles = new String[]{
                mView.getCurActivity().getString(R.string.recommend),
                mView.getCurActivity().getString(R.string.hot),
                mView.getCurActivity().getString(R.string.finance),
                mView.getCurActivity().getString(R.string.recreation),
                mView.getCurActivity().getString(R.string.life),
                mView.getCurActivity().getString(R.string.recommend),
                mView.getCurActivity().getString(R.string.hot),
                mView.getCurActivity().getString(R.string.finance),
                mView.getCurActivity().getString(R.string.recreation),
                mView.getCurActivity().getString(R.string.life),
        };
        List<BaseFragment> fragments = new ArrayList<>();
        fragments.add(LifeTabFragment.newInstance());
        fragments.add(LifeTabFragment.newInstance());
        fragments.add(LifeTabFragment.newInstance());
        fragments.add(LifeTabFragment.newInstance());
        fragments.add(LifeTabFragment.newInstance());
        fragments.add(LifeTabFragment.newInstance());
        fragments.add(LifeTabFragment.newInstance());
        fragments.add(LifeTabFragment.newInstance());
        fragments.add(LifeTabFragment.newInstance());
        fragments.add(LifeTabFragment.newInstance());
        mView.showTabFragments(titles, fragments);

        List<String> urlList = new ArrayList<>();
        urlList.add("http://img5.imgtn.bdimg.com/it/u=3300305952,1328708913&fm=26&gp=0.jpg");
        urlList.add("http://k.zol-img.com.cn/sjbbs/7692/a7691515_s.jpg");
        mView.showBanner(urlList);


    }


}
