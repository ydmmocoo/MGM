package com.fjx.mg.me.comment;

import com.library.common.base.BaseFragment;
import com.library.common.base.BasePresenter;
import com.library.common.base.BaseView;

import java.util.List;

public interface MyCommentContract {


    interface View extends BaseView {

        void showTabAndFragment(String[] titles, List<BaseFragment> fragments);
    }

    abstract class Presenter extends BasePresenter<MyCommentContract.View> {

        Presenter(MyCommentContract.View view) {
            super(view);
        }

        abstract void initData();


    }
}
