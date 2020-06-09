package com.fjx.mg.moments.detailx;

import com.library.common.base.BasePresenter;
import com.library.common.base.BaseView;
import com.library.repository.models.CityCircleListModel;
import com.library.repository.models.ImUserRelaM;

import java.util.List;

public interface CityMomentsTypeContractx {
    interface View extends BaseView {
        void ShowCityCircleList(CityCircleListModel data);

        void PraiseSuccess();

        void ToUserInfo(String identifier, String userAvatar);

        void BodyVisible(String mId, String rId);

        void ShowDetail(int position, List<String> item);

        void ShowCommentDialog(String replyId, String content, boolean showDele);

        void delReplySuccess();

        void GetNewData();

        void updateEditTextBodyVisible(int visibility);

        void showUserInfo(ImUserRelaM userRelaM);

        void MomentsDelSuccess();
    }

    abstract class Presenter extends BasePresenter<CityMomentsTypeContractx.View> {
        Presenter(CityMomentsTypeContractx.View view) {
            super(view);
        }

        abstract void MomentsDel(String mid);

        abstract void addReplyMid(String mid, String content, String toUid, String rid);

        abstract void getCityCircleList(int type, String Page);

        abstract void ToUserInfo(String identifier, String userAvatar);

        abstract void BodyVisible(String mId, String rId);

        abstract void toggleCommentPraise(String mid, Boolean isPraised);

        abstract void ShowDetail(int position, List<String> item);

        abstract void ShowCommentDialog(String replyId, String content, boolean showDele);

        abstract void delReply(String rId);

        abstract void findUser(String identifier);
    }
}
