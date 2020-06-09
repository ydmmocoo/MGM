package com.fjx.mg.friend.addfriend;

import com.library.common.base.BasePresenter;
import com.library.common.base.BaseView;
import com.library.repository.models.FriendContactSectionModel;
import com.library.repository.models.ImUserRelaM;
import com.library.repository.models.InviteModel;
import com.library.repository.models.MomentsUserInfoModel;
import com.tencent.imsdk.TIMUserProfile;

import java.util.List;

public interface SearchFriendContract {
    interface View extends BaseView {

        void showUserInfo(ImUserRelaM userRelaM);

        void showRemark(String remark);

        void outblackFriendSuccess();

        void showInviteModel(InviteModel model);

        void showContactLists(Boolean noblack);

        void showContactList(List<FriendContactSectionModel> datas);

        void showMommetnsUserInfo(MomentsUserInfoModel model);

    }

    abstract class Presenter extends BasePresenter<SearchFriendContract.View> {
        abstract void getAllBlackFriend();

        public Presenter(View view) {
            super(view);
        }

        abstract void getAllFriend(TIMUserProfile userProfile);

        abstract void findUser(String imUid);

        /**
         * @param timUserId  tim即时通讯商的用户id
         * @param remark     好友备注
         * @param addWording 添加好友说明信息
         */
        abstract void addFriend(String timUserId, String remark, String addWording, Boolean black);

        abstract void addFriend(String timUserId, String addWording);


        abstract void showRemarkEditDialog();

        abstract void getInviteCode();

        abstract void outblackFriendSuccess();

        abstract void MomentsGetUserInfo(String identifier);
    }



}
