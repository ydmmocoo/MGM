package com.fjx.mg.friend.imuser;

import com.library.common.base.BasePresenter;
import com.library.common.base.BaseView;
import com.tencent.imsdk.TIMUserProfile;
import com.tencent.imsdk.friendship.TIMFriend;

public interface ImUserDetailContract {
    interface View extends BaseView {
        void showImUserInfo(TIMUserProfile profile);

        void showImUserInfo(TIMFriend profile);

        void deleteFriendSuccess();

        void blackFriendSuccess();

        void showRemarkDialog();

        void complaintsUser(String imUid);

        // void editUserRemarkSuccess(TIMUserProfile profile);
    }

    abstract class Presenter extends BasePresenter<ImUserDetailContract.View> {

        public Presenter(View view) {
            super(view);
        }

        abstract void getAllFriend(TIMUserProfile userProfile);

        abstract void getImUserInfo(String imUserId);

        abstract void editUserRemark(String imUserId, String remark);

        abstract void showRemarkEditDialog(String imUserId, String remark);

        abstract void showSettingDialog(String imUid, android.view.View view);

        abstract void deleteFriend(String imUid,Boolean show);

    }


}
