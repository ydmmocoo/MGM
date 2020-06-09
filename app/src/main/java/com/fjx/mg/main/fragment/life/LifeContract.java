package com.fjx.mg.main.fragment.life;

import com.library.common.base.BaseFragment;
import com.library.common.base.BasePresenter;
import com.library.common.base.BaseView;

import java.util.List;

public interface LifeContract {

    interface View extends BaseView {
        void showRecommendGoods(List<Object> goodLit);

        void showTabFragments(String[] titles, List<BaseFragment> fragments);

        void showBanner(List<String> urlList);
    }

    abstract class Presenter extends BasePresenter<LifeContract.View> {

        Presenter(LifeContract.View view) {
            super(view);
        }

        abstract void initData();


    }

}
