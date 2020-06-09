package com.fjx.mg.friend.request;

import com.library.common.base.BasePresenter;
import com.library.common.base.BaseView;
import com.tencent.imsdk.TIMUserProfile;
import com.tencent.imsdk.friendship.TIMFriend;

public interface RequestDetailContract {
    interface View extends BaseView {
        void showImUserInfo(TIMUserProfile profile);

        /**
         * 处理好友请求
         *
         * @param isAgree 是否同意
         */
        void doResponseResult(boolean isAgree);

        // void editUserRemarkSuccess(TIMUserProfile profile);
    }

    abstract class Presenter extends BasePresenter<RequestDetailContract.View> {

        public Presenter(View view) {
            super(view);
        }

        abstract void getImUserInfo(String imUserId);


        /**
         * 处理好友请求
         *
         * @param uid
         * @param remark
         */
        abstract void doResponse(String uid, String remark);

        /**
         * 拒绝好友请求
         *
         * @param uid
         * @param remark
         */
        abstract void doResponseReject(String uid, String remark);


        /**
         * 添加好友成功
         *
         * @param phone
         */
        abstract void confirmAddFriend(String phone);


    }


}
