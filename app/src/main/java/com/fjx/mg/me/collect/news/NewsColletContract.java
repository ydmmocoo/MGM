package com.fjx.mg.me.collect.news;

import com.library.common.base.BasePresenter;
import com.library.common.base.BaseView;
import com.library.repository.models.NewsItemModel;
import com.library.repository.models.NewsListModel;

import java.util.List;

public interface NewsColletContract {
    interface View extends BaseView {
        void showDatas(NewsItemModel list);

    }

    abstract class Presenter extends BasePresenter<NewsColletContract.View> {

        Presenter(NewsColletContract.View view) {
            super(view);
        }

        abstract void initData();

        abstract void getMyCollectNews(int page);
    }

}
