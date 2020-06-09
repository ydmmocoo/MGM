package com.fjx.mg.me.publish;

import com.fjx.mg.R;
import com.fjx.mg.me.publish.fragment.HouseFragment;
import com.fjx.mg.me.publish.fragment.JobFragment;
import com.fjx.mg.me.publish.fragment.MyNearbyCityFragment;
import com.fjx.mg.me.publish.fragment.YellowPageFragment;
import com.library.common.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

public class MyPublishPresenter extends MyPublishContract.Presenter {
    MyPublishPresenter(MyPublishContract.View view) {
        super(view);
    }

    @Override
    void initData() {
        String[] titles = new String[]{
//                mView.getCurContext().getString(R.string.employment),
//                mView.getCurContext().getString(R.string.housing_resources),
                mView.getCurContext().getString(R.string.my_nearby_city),
                mView.getCurContext().getString(R.string.yellow_page)

        };
        List<BaseFragment> fragments = new ArrayList<>();
//        fragments.add(JobFragment.newInstance());
//        fragments.add(HouseFragment.newInstance());
        fragments.add(MyNearbyCityFragment.newInstance());
        fragments.add(YellowPageFragment.newInstance());
        mView.showTabsAndFragment(titles, fragments);
    }


}
