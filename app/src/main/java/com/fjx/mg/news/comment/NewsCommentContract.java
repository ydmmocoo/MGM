package com.fjx.mg.news.comment;

import com.library.common.base.BasePresenter;
import com.library.common.base.BaseView;
import com.library.repository.models.CommentReplyModel;
import com.library.repository.models.NewsCommentModel;

public interface NewsCommentContract {

    interface View extends BaseView {

        void commentSuccess();

        void commentFailed();

        void responseFailed();

        void showReplyList(CommentReplyModel data);

        void showTopicDetail(NewsCommentModel.CommentListBean model);

        void toggleCommentPraiseSuccess();

        void toggleReplyPraiseSuccess(int position);

        void deleteReplySuccess();

        void deleteCommentSuccese();

    }

    abstract class Presenter extends BasePresenter<NewsCommentContract.View> {

        public Presenter(View view) {
            super(view);
        }

        abstract void addReply(String newsId, String content, String uid, String replyId);

        abstract void getReplyList(String commentId, int page);

        abstract void topicDetail(String commentId);


        abstract void toggleCommentPraise( NewsCommentModel.CommentListBean item);

        abstract void toggleReplyPraise(int position, CommentReplyModel.ReplyListBean item);

        abstract void deleteReply(String replyId);

        abstract void deleteComment(String commentId);

        abstract void delete( String id,  boolean isReply);


    }

}
