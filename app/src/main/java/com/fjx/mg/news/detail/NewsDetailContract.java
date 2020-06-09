package com.fjx.mg.news.detail;

import android.widget.TextView;

import com.library.common.base.BasePresenter;
import com.library.common.base.BaseView;
import com.library.repository.models.AdListModel;
import com.library.repository.models.NewsCommentModel;
import com.library.repository.models.NewsDetailModel;
import com.library.repository.models.NewsRecommendReadModel;

public interface NewsDetailContract {

    interface View extends BaseView {

        void loadError();

        void showNewsDetail(NewsDetailModel model);

        void commentSuccess();

        void commentFail();

        void showCommentList(NewsCommentModel model);

        void toggleCollectSuccess(boolean collect);

        void toggleCommentPraiseSuccess(int position);

        void deleteCommentSuccese(int position);

        void selectSettingPosition(int position);

        void showBanners(AdListModel data);

        void responseRecommendNews(NewsRecommendReadModel model);
    }

    abstract class Presenter extends BasePresenter<NewsDetailContract.View> {

        public Presenter(View view) {
            super(view);
        }

        abstract void getNewsDetail(String newsId);

        abstract void getCommentList(String newsId, int page);

        abstract void addComment(String newsId, String content);

        abstract void toggleCollectNews(String newsId, boolean hasCollect);

        abstract void toggleCommentPraise(int position, NewsCommentModel.CommentListBean item, TextView tvFavNum);


        abstract void deleteComment(String commentId, int position);

        abstract void delete(String id, int position);

        abstract void updateReadNum(String newsId);

        abstract void showSettingDialog(android.view.View view, boolean hasCollect);

        abstract void getAd();

        /**
         * 新闻推荐
         *
         * @param id 当前浏览的新闻记录id
         */
        abstract void requestRecommendNews(String id);
    }

}
