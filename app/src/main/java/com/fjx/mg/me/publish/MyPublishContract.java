package com.fjx.mg.me.publish;

import com.library.common.base.BaseFragment;
import com.library.common.base.BasePresenter;
import com.library.common.base.BaseView;

import java.util.List;

public interface MyPublishContract {


    interface View extends BaseView {

        void showTabsAndFragment(String[] title, List<BaseFragment> fragments);


    }

    abstract class Presenter extends BasePresenter<MyPublishContract.View> {

        Presenter(MyPublishContract.View view) {
            super(view);
        }

        abstract void initData();


    }
}
