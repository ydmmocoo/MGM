package com.fjx.mg.main.fragment.news.tab;

import com.library.common.utils.CommonToast;
import com.library.repository.core.net.CommonObserver;
import com.library.repository.core.net.RxScheduler;
import com.library.repository.db.DBDaoFactory;
import com.library.repository.models.NewsItemModel;
import com.library.repository.models.NewsListModel;
import com.library.repository.models.ResponseModel;
import com.library.repository.repository.RepositoryFactory;

class NewsTabPresenter extends NewsTabContract.Presenter {


    NewsTabPresenter(NewsTabContract.View view) {
        super(view);
    }


    @Override
    void getNewsList(final int typeId, final int page, String title) {
        RepositoryFactory.getRemoteNewsRepository().newsList(typeId, page, title)
                .compose(RxScheduler.<ResponseModel<NewsItemModel>>toMain())
                .as(mView.<ResponseModel<NewsItemModel>>bindAutoDispose())
                .subscribe(new CommonObserver<NewsItemModel>() {
                    @Override
                    public void onSuccess(NewsItemModel data) {
                        if (mView != null && data != null) {
                            mView.showNewsList(data);
                            try {
                                if (data.getNewsList() != null && data.getNewsList().size() > 0) {
                                    for (NewsListModel item : data.getNewsList()) {
                                        item.setTypeId(typeId);
                                        item.setUniqueId(String.valueOf(typeId).concat("_").concat(item.getNewsId()));

                                    }
                                }
                                if (page == 1) {
                                    DBDaoFactory.getNewsListDao().deleteAll(typeId);
                                    DBDaoFactory.getNewsListDao().insertList(data.getNewsList());
                                }
                            } catch (NullPointerException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        if (mView != null)
                            mView.loadError();
                        CommonToast.toast(data.getMsg());
                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                        if (mView != null)
                            mView.loadError();
                        CommonToast.toast(data.getMsg());
                    }
                });

    }


}
