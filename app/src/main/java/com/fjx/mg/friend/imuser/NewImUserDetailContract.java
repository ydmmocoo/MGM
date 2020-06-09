package com.fjx.mg.friend.imuser;

import com.library.common.base.BasePresenter;
import com.library.common.base.BaseView;
import com.library.repository.models.MomentsUserInfoModel;
import com.tencent.imsdk.TIMUserProfile;
import com.tencent.imsdk.friendship.TIMFriend;

public interface NewImUserDetailContract {
    interface View extends BaseView {
        void showImUserInfo(TIMUserProfile profile);

        void showImUserInfo(TIMFriend profile);

        void deleteFriendSuccess();

        void blackFriendSuccess();

        void showRemarkDialog();

        void complaintsUser(String imUid);

        void showMommetnsUserInfo(MomentsUserInfoModel model);

        // void editUserRemarkSuccess(TIMUserProfile profile);

        void setNickname(String nickname,boolean isChangeNickname);

    }

    abstract class Presenter extends BasePresenter<NewImUserDetailContract.View> {

        public Presenter(View view) {
            super(view);
        }

        abstract void getAllFriend(TIMUserProfile userProfile);

        abstract void getImUserInfo(String imUserId);

        abstract void editUserRemark(String imUserId, String remark);

        abstract void showRemarkEditDialog(String imUserId, String remark);

        abstract void showSettingDialog(String imUid, android.view.View view);

        abstract void deleteFriend(String imUid, Boolean show);

        abstract void MomentsGetUserInfo(String identifier);

    }


}
