package com.fjx.mg.recharge.record;

import com.library.common.base.BaseFragment;
import com.library.common.base.BasePresenter;
import com.library.common.base.BaseView;

import java.util.List;

public interface BillRecordContract {

    interface View extends BaseView {
        void showTabsAndFragment(String[] titles, List<BaseFragment> fragments);
    }

    abstract class Presenter extends BasePresenter<BillRecordContract.View> {

        Presenter(BillRecordContract.View view) {
            super(view);
        }

        abstract void initData();


    }
}
