package com.fjx.mg.main.payment.questionceter;

import com.flyco.tablayout.listener.CustomTabEntity;
import com.library.common.base.BaseFragment;
import com.library.common.base.BasePresenter;
import com.library.common.base.BaseView;

import java.util.ArrayList;
import java.util.List;

public interface QuestionCenterContract {

    interface View extends BaseView {

        void showTabAndItems(List<BaseFragment> fragments, ArrayList<CustomTabEntity> mTabEntities);

    }

    abstract class Presenter extends BasePresenter<QuestionCenterContract.View> {

        Presenter(QuestionCenterContract.View view) {
            super(view);
        }

        abstract void initData();


    }

}
