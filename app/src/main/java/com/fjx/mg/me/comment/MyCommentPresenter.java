package com.fjx.mg.me.comment;

import com.fjx.mg.R;
import com.fjx.mg.me.collect.house.HouseResourceFragment;
import com.fjx.mg.me.collect.news.NewsColletFragment;
import com.fjx.mg.me.collect.recruit.RecruitFragment;
import com.fjx.mg.me.comment.fragment.MyCommentFragment;
import com.fjx.mg.me.publish.fragment.MyNearbyCityFragment;
import com.library.common.base.BaseFragment;
import com.tencent.qcloud.uikit.TimConfig;

import java.util.ArrayList;
import java.util.List;

public class MyCommentPresenter extends MyCommentContract.Presenter {

    MyCommentPresenter(MyCommentContract.View view) {
        super(view);
    }

    /**
     * 2-头条 4-房源 5-黄页 6-回答 7--马岛服务
     */
    @Override
    void initData() {
        List<BaseFragment> fragments = new ArrayList<>();
        fragments.add(MyCommentFragment.newInstance(7));
        fragments.add(MyCommentFragment.newInstance(2));
//        fragments.add(MyCommentFragment.newInstance(4));
        fragments.add(MyCommentFragment.newInstance(5));
//        fragments.add(MyCommentFragment.newInstance(6));
//        fragments.add(MyCommentFragment.newInstance(1));
//        fragments.add(MyCommentFragment.newInstance(3));
        String[] titles = new String[]{
                mView.getCurContext().getString(R.string.my_nearby_city),
                mView.getCurContext().getString(R.string.news),
                mView.getCurContext().getString(R.string.yellow_page_c)
        };


        mView.showTabAndFragment(titles, fragments);


    }
}
