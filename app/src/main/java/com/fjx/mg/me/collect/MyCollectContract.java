package com.fjx.mg.me.collect;

import com.library.common.base.BaseFragment;
import com.library.common.base.BasePresenter;
import com.library.common.base.BaseView;

import java.util.List;

public interface MyCollectContract {


    interface View extends BaseView {

        void showTabAndFragment(String[] titles, List<BaseFragment> fragments);
    }

    abstract class Presenter extends BasePresenter<MyCollectContract.View> {

        Presenter(MyCollectContract.View view) {
            super(view);
        }

        abstract void initData();


    }
}
