package com.fjx.mg.me.collect;

import com.fjx.mg.R;
import com.fjx.mg.me.collect.business.BusinessStoreFragment;
import com.fjx.mg.me.collect.house.HouseResourceFragment;
import com.fjx.mg.me.collect.news.NewsColletFragment;
import com.fjx.mg.me.collect.recruit.RecruitFragment;
import com.library.common.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

public class MyCollectPresenter extends MyCollectContract.Presenter {

    MyCollectPresenter(MyCollectContract.View view) {
        super(view);
    }

    @Override
    void initData() {
        String[] titles = new String[]{
//                "外卖商家",
                mView.getCurContext().getString(R.string.news)
//                ,
//                mView.getCurContext().getString(R.string.employment),
//                mView.getCurContext().getString(R.string.housing_resources),
        };

        List<BaseFragment> fragments = new ArrayList<>();
//        fragments.add(BusinessStoreFragment.newInstance());
        fragments.add(NewsColletFragment.newInstance());
//        fragments.add(RecruitFragment.newInstance());
//        fragments.add(HouseResourceFragment.newInstance());
        mView.showTabAndFragment(titles, fragments);


    }
}
