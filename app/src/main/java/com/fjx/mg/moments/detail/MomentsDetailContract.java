package com.fjx.mg.moments.detail;

import com.library.common.base.BasePresenter;
import com.library.common.base.BaseView;
import com.library.repository.models.ImUserRelaM;
import com.library.repository.models.MomentsInfoModel;
import com.library.repository.models.MomentsReplyListModel;

public interface MomentsDetailContract {
    interface View extends BaseView {

        void showInfo(MomentsInfoModel model);//展示朋友圈详情数据

        void showReplyList(MomentsReplyListModel model);//展示回复列表数据

        void MomentsDelSuccess();//删除朋友圈成功

        void loadError();//获取数据异常

        void showUserInfo(ImUserRelaM userRelaM);//到个人页面

        void GetNewData();//刷新数据

        void complaintsUser(String identifier);
    }

    abstract class Presenter extends BasePresenter<MomentsDetailContract.View> {
        public Presenter(View view) {
            super(view);
        }

        abstract void complaintsUser(String identifier);

        abstract void addReplyMid(String mid, String content, String toUid, String rid);//评论朋友圈

        abstract void MomentsReplyList(String page, String commentId, String isRead);//回复评论列表

        abstract void MomentsInfo(String mId, String lat, String lng, String page);//朋友圈详情

        abstract void Praise(String mid);//朋友圈点赞

        abstract void MomentsDel(String mid);//删除朋友圈

        abstract void delReply(String rId);//删除评论

        abstract void CancelPraise(String mid);//朋友圈去赞

        abstract void findUser(String identifier);//拉取某个用户的信息
    }
}
