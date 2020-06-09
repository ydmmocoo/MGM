package com.fjx.mg.main.fragment.news;

import com.library.common.base.BaseFragment;
import com.library.common.base.BasePresenter;
import com.library.common.base.BaseView;
import com.library.repository.models.NewsTypeModel;

import java.util.List;

public interface NewsContract {

    interface View extends BaseView {
        void showTabsAndFragment(List<NewsTypeModel> typeList, String[] titles, List<BaseFragment> fragments);

    }

    abstract class Presenter extends BasePresenter<NewsContract.View> {

        Presenter(NewsContract.View view) {
            super(view);
        }

         abstract void initData(List<NewsTypeModel> typeList);

        abstract void getNewsTypes();
    }

}
