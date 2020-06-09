package com.fjx.mg.main.fragment.news.tab;

import com.library.common.base.BasePresenter;
import com.library.common.base.BaseView;
import com.library.repository.models.NewsItemModel;

public interface NewsTabContract {

    interface View extends BaseView {
        void showNewsList(NewsItemModel model);

        void loadError();

    }

    abstract class Presenter extends BasePresenter<NewsTabContract.View> {

        Presenter(NewsTabContract.View view) {
            super(view);
        }


        abstract void getNewsList(int typeId, int page, String title);
    }

}
