package com.fjx.mg.job;

import com.flyco.tablayout.listener.CustomTabEntity;
import com.library.common.base.BaseFragment;
import com.library.common.base.BasePresenter;
import com.library.common.base.BaseView;

import java.util.ArrayList;
import java.util.List;

public interface JobHomeContract {

    interface View extends BaseView {

        void showTabAndItems(List<BaseFragment> fragments, ArrayList<CustomTabEntity> mTabEntities);

    }

    abstract class Presenter extends BasePresenter<JobHomeContract.View> {

        Presenter(JobHomeContract.View view) {
            super(view);
        }

        abstract void initData();


    }

}
