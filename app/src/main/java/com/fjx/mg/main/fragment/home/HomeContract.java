package com.fjx.mg.main.fragment.home;

import android.widget.TextView;

import com.flyco.tablayout.listener.CustomTabEntity;
import com.library.common.base.BaseFragment;
import com.library.common.base.BasePresenter;
import com.library.common.base.BaseView;
import com.library.repository.models.AdListModel;
import com.library.repository.models.AdModel;
import com.library.repository.models.NewsItemModel;
import com.library.repository.models.NewsListModel;
import com.library.repository.models.ResponseModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

public interface HomeContract {

    interface View extends BaseView {

        /**
         * 用于请求失败关闭刷新动画和处理一些请求失败逻辑
         *
         * @param data 接口返回的code、msg等信息用于提示用户或者程序做一些失败逻辑
         */
        void responseFailed(ResponseModel data);

        void showRecommendStore();

        void showMarqueeView(List<String> datas);

        void showNewsList(NewsItemModel newsListModel);

        void showCheckLanguage(int position, String text);

        void showBanners(AdListModel data);
    }

    abstract class Presenter extends BasePresenter<HomeContract.View> {

        Presenter(HomeContract.View view) {
            super(view);
        }

        abstract void getRecommendStore();

        abstract void getNewsList(int page);

        abstract void recUseApp(String appId);

        abstract void showLanguageDialog(TextView view);

        abstract void getAd();

    }

}
