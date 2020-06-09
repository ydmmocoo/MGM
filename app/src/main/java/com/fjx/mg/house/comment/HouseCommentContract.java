package com.fjx.mg.house.comment;

import com.library.common.base.BasePresenter;
import com.library.common.base.BaseView;
import com.library.repository.models.CommentReplyModel;
import com.library.repository.models.NewsCommentModel;

public interface HouseCommentContract {

    interface View extends BaseView {

        void commentSuccess();

        void showReplyList(CommentReplyModel data);

        void showTopicDetail(NewsCommentModel.CommentListBean model);


        void deleteReplySuccess();

        void deleteCommentSuccese();

    }

    abstract class Presenter extends BasePresenter<HouseCommentContract.View> {

        public Presenter(View view) {
            super(view);
        }

        abstract void addReply(String newsId, String content, String uid, String replyId);

        abstract void getReplyList(String commentId, int page);


        abstract void deleteReply(String replyId);

        abstract void deleteComment(String commentId);

        abstract void delete( String id,  boolean isReply);


    }

}
