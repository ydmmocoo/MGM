package com.fjx.mg.moments.city.type;

import com.library.common.base.BasePresenter;
import com.library.common.base.BaseView;
import com.library.repository.models.CityCircleListModel;
import com.library.repository.models.ImUserRelaM;

import java.util.List;

public interface CityMomentsTypeContract {
    interface View extends BaseView {
        void ShowCityCircleList(CityCircleListModel data);

        void PraiseSuccess();

        void ToUserInfo(String identifier, String userAvatar);

        void BodyVisible(String mId, String rId, String uid);

        void ShowDetail(int position, List<String> item);

        void ShowCommentDialog(String replyId, String content, boolean showDele);

        void delReplySuccess();

        void GetNewData();

        void updateEditTextBodyVisible(int visibility);

        void showUserInfo(ImUserRelaM userRelaM);

        void MomentsDelSuccess();

        void complaintsUser(String identifier);

        void toDetail(String Mid);
    }

    abstract class Presenter extends BasePresenter<CityMomentsTypeContract.View> {
        Presenter(CityMomentsTypeContract.View view) {
            super(view);
        }

        abstract void toDetail(String Mid);

        abstract void complaintsUser(String identifier);

        abstract void MomentsDel(String mid);

        abstract void addReplyMid(String mid, String content, String toUid, String rid);

        abstract void getCityCircleList(int type, String Page);

        abstract void ToUserInfo(String identifier, String userAvatar);

        abstract void BodyVisible(String mId, String rId, String uid);

        abstract void toggleCommentPraise(String mid, Boolean isPraised);

        abstract void ShowDetail(int position, List<String> item);

        abstract void ShowCommentDialog(String replyId, String content, boolean showDele);

        abstract void delReply(String rId);

        abstract void findUser(String identifier);
    }
}
