package com.fjx.mg.nearbycity.mvp;

import com.library.common.base.BasePresenter;
import com.library.common.base.BaseView;
import com.library.repository.models.CommentReplyModel;
import com.library.repository.models.NearbyCityCommentListModel;
import com.library.repository.models.ResponseModel;

public interface NearbyCityCommentContract {

    interface View extends BaseView {

        void commentSuccess();

        void commentFailed();

        void showReplyList(CommentReplyModel data);

        void showTopicDetail(NearbyCityCommentListModel model);

        void toggleCommentPraiseSuccess();

        void toggleReplyPraiseSuccess(int position);

        void deleteReplySuccess();

        void deleteCommentSuccese();

        void responseFailed(ResponseModel model);

    }

    abstract class Presenter extends BasePresenter<NearbyCityCommentContract.View> {

        public Presenter(View view) {
            super(view);
        }

        public abstract void addReply(String newsId, String content, String uid, String replyId);

        public abstract void getReplyList(String commentId, int page);

        public abstract void topicDetail(String commentId);


        public abstract void toggleCommentPraise(NearbyCityCommentListModel item, String cId);

        public abstract void toggleReplyPraise(int position, CommentReplyModel.ReplyListBean item);

        public abstract void deleteReply(String replyId);

        public abstract void deleteComment(String commentId);

        public abstract void delete(String id, boolean isReply);

    }

}
