package com.fjx.mg.moments.friends;

import com.library.common.base.BasePresenter;
import com.library.common.base.BaseView;
import com.library.repository.models.CityCircleListModel;
import com.library.repository.models.ImUserRelaM;
import com.library.repository.models.MomentsReplyListModel;
import com.library.repository.models.ResponseModel;
import com.library.repository.models.UnReadCountBean;

import java.util.List;

public interface FriendsMomentsContract {
    interface View extends BaseView {

        void responseFailed(ResponseModel data);

        void ShowCityCircleList(CityCircleListModel data);

        void PraiseSuccess();

        void ToUserInfo(String identifier, String userAvatar);

        void BodyVisible(String mId, String rId, String uid);

        void ShowDetail(int position, List<String> item);

        void ShowCommentDialog(String replyId, String content, boolean showDele);

        void delReplySuccess();

        void GetNewData();

        void replyFailed();

        void updateEditTextBodyVisible(int visibility);

        void showUserInfo(ImUserRelaM userRelaM);

        void MomentsDelSuccess();

        void complaintsUser(String identifier);

        void toDetail(String Mid);

        void friendsMomentsReply(MomentsReplyListModel data);

        void isShowRedPoint(UnReadCountBean data);
    }

    abstract class Presenter extends BasePresenter<FriendsMomentsContract.View> {
        Presenter(FriendsMomentsContract.View view) {
            super(view);
        }

        abstract void toDetail(String Mid);

        abstract void complaintsUser(String identifier);

        abstract void MomentsDel(String mid);

        abstract void addReplyMid(String mid, String content, String toUid, String rid);

        abstract void getCityCircleList(String Page);

        abstract void ToUserInfo(String identifier, String userAvatar);

        abstract void BodyVisible(String mId, String rId, String uid);

        abstract void toggleCommentPraise(String mid, Boolean isPraised);

        abstract void ShowDetail(int position, List<String> item);

        abstract void ShowCommentDialog(String replyId, String content, boolean showDele);

        abstract void delReply(String rId);

        abstract void findUser(String identifier);

        abstract void requestFriendsMomentsReply(String page, String commentId, String isRead);

        abstract void requestIsShowRedPoint();
    }
}
