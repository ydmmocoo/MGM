package com.fjx.mg.main.fragment.news;

import android.text.TextUtils;

import com.fjx.mg.main.fragment.news.tab.NewsTabFragment;
import com.fjx.mg.main.fragment.news.tab.NewsWebFragment;
import com.library.common.base.BaseFragment;
import com.library.common.utils.CommonToast;
import com.library.repository.core.net.CommonObserver;
import com.library.repository.core.net.RxScheduler;
import com.library.repository.db.DBDaoFactory;
import com.library.repository.models.NewsTypeModel;
import com.library.repository.models.ResponseModel;
import com.library.repository.repository.RepositoryFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class NewsPresenter extends NewsContract.Presenter {


    NewsPresenter(NewsContract.View view) {
        super(view);
    }

    @Override
    void initData(List<NewsTypeModel> typeList) {
        String[] titles = new String[typeList.size()];
        List<BaseFragment> fragments = new ArrayList<>();
        for (int i = 0; i < typeList.size(); i++) {
            titles[i] = typeList.get(i).getTypeName();
            if (TextUtils.equals("1", typeList.get(i).getJumpType())) {//1---新闻列表
                fragments.add(NewsTabFragment.newInstance(typeList.get(i).getTypeId()));
            } else if (TextUtils.equals("2", typeList.get(i).getJumpType())) {//2---webView
                fragments.add(NewsWebFragment.getInstance(typeList.get(i).getUrl()));
            }
        }
        if (mView != null&&fragments.size()==titles.length) {
            mView.showTabsAndFragment(typeList, titles, fragments);
        }

    }

    @Override
    void getNewsTypes() {

        final List<NewsTypeModel> dataList = DBDaoFactory.getNewsTypeDao().queryList();
        if (!dataList.isEmpty()) {
            initData(dataList);
        }
        RepositoryFactory.getRemoteNewsRepository().newsTypeList()
                .compose(RxScheduler.<ResponseModel<List<NewsTypeModel>>>toMain())
                .as(mView.<ResponseModel<List<NewsTypeModel>>>bindAutoDispose())
                .subscribe(new CommonObserver<List<NewsTypeModel>>() {
                    @Override
                    public void onSuccess(List<NewsTypeModel> data) {
                        initData(data);
                        if (dataList.isEmpty()) {
                            DBDaoFactory.getNewsTypeDao().insertList(data);
                        } else {
                            //每次创建重新加载tab分类
                            DBDaoFactory.getNewsTypeDao().deleteList(dataList);
                            DBDaoFactory.getNewsTypeDao().insertList(data);
                        }

                    }

                    @Override
                    public void onError(ResponseModel data) {
                        CommonToast.toast(data.getMsg());
                        if (mView != null) {
                            mView.hideLoading();
                        }
                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                        CommonToast.toast(data.getMsg());
                        if (mView != null) {
                            mView.hideLoading();
                        }
                    }
                });
//            initData(new ArrayList<NewsTypeModel>());
    }


}
