package com.fjx.mg.me.collect.news;


import com.library.common.utils.CommonToast;
import com.library.common.utils.StringUtil;
import com.library.repository.core.net.CommonObserver;
import com.library.repository.core.net.RxScheduler;
import com.library.repository.models.NewsItemModel;
import com.library.repository.models.NewsListModel;
import com.library.repository.models.ResponseModel;
import com.library.repository.repository.RepositoryFactory;

import java.util.ArrayList;
import java.util.List;

public class NewsColletPresenter extends NewsColletContract.Presenter {

    NewsColletPresenter(NewsColletContract.View view) {
        super(view);
    }

    @Override
    void initData() {

    }

    @Override
    void getMyCollectNews(int page) {
        RepositoryFactory.getRemoteNewsRepository().myCollect(page)
                .compose(RxScheduler.<ResponseModel<NewsItemModel>>toMain())
                .as(mView.<ResponseModel<NewsItemModel>>bindAutoDispose())
                .subscribe(new CommonObserver<NewsItemModel>() {
                    @Override
                    public void onSuccess(NewsItemModel data) {
                        mView.showDatas(data);

                    }

                    @Override
                    public void onError(ResponseModel data) {
                        if (StringUtil.isNotEmpty(data.getMsg())) {
                            CommonToast.toast(data.getMsg());
                        }

                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                        CommonToast.toast(data.getMsg());
                    }
                });

    }
}
