package com.fjx.mg.main.yellowpage.detail;

import android.widget.TextView;

import com.library.common.base.BasePresenter;
import com.library.common.base.BaseView;
import com.library.repository.models.AdListModel;
import com.library.repository.models.NewsCommentModel;
import com.library.repository.models.YellowPageDetailModel;
import com.tencent.imsdk.TIMUserProfile;
import com.tencent.imsdk.friendship.TIMFriend;

public interface YellowPageDetailContract {

    interface View extends BaseView {
        void loadError();

        void deleteCommentSuccese(int position);

        void showCommentList(NewsCommentModel model);

        void toggleCommentPraiseSuccess(int position);

        void showCompanyDetail(YellowPageDetailModel data);

        void getImUserSuccess(TIMUserProfile userProfile);

        void getImUserSuccess(TIMFriend friend);

        void editCompanyInfo();

        void deleteCompany();

        void commentSuccess();

        void commentFailed();

        void deleteSuccess();

        void showAds(AdListModel data);

        void share();

//        CompanyDetailModel getCompanyDetailModel();

    }

    abstract class Presenter extends BasePresenter<YellowPageDetailContract.View> {
        Presenter(YellowPageDetailContract.View view) {
            super(view);
        }

        abstract void delete(String id, int position);

        abstract void addComment(String newsId, String content);

        abstract void toggleCommentPraise(int position, NewsCommentModel.CommentListBean item, TextView tvFavNum);

        abstract void deleteComment(String commentId, int position);

        abstract void getCommentList(String newsId, int page);

        abstract void getCompanyDetail(String cId, String change, YellowPageDetailActivity yellowPageDetailActivity);

        abstract void findImUser(String uid);

        abstract void showPublishDialog(android.view.View view);

        abstract void showPublishDialog(android.view.View view, boolean isExpire, boolean isClosed, boolean isLove, boolean isShare);

        abstract void deleteCompany(String cid);

        abstract void updateReadNumCompany(String cid);


        abstract void getAd();

    }

}
