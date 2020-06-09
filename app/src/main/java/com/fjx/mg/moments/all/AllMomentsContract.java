package com.fjx.mg.moments.all;

import com.library.common.base.BasePresenter;
import com.library.common.base.BaseView;
import com.library.repository.models.MomentsInfoModel;
import com.library.repository.models.MomentsReplyListModel;
import com.library.repository.models.PersonalMomentListModel;

public interface AllMomentsContract {
    interface View extends BaseView {

        void showPersonalMomentList(PersonalMomentListModel model);

        void loadError();

    }

    abstract class Presenter extends BasePresenter<AllMomentsContract.View> {
        public Presenter(View view) {
            super(view);
        }

        abstract void personalMomentList(String identifier, int page);
    }

    interface MessageView extends BaseView {
        void showReplyList(MomentsReplyListModel data);

        void showInfo(MomentsInfoModel data);
    }

    abstract class MessagePersenter extends BasePresenter<AllMomentsContract.MessageView> {
        public MessagePersenter(MessageView view) {
            super(view);
        }

        abstract void MomentsReplyList(String page, String commentId, String isRead);//回复评论列表

        abstract void momentsRead();//读取评论和点赞

        abstract void MomentsInfo(String mId, String lat, String lng, String page);//朋友圈详情
    }
}
