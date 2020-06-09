package com.fjx.mg.main.fragment.life.Tab;

import com.library.common.base.BasePresenter;
import com.library.common.base.BaseView;

import java.util.List;

public interface LifeTabContract {

    interface View extends BaseView {
        void showDataList(List<Object> goodLit);

    }

    abstract class Presenter extends BasePresenter<LifeTabContract.View> {

        Presenter(LifeTabContract.View view) {
            super(view);
        }

        abstract void initData();
    }

}
