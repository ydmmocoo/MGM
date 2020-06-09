package com.fjx.mg.friend.newfriend;

import com.library.common.base.BasePresenter;
import com.library.common.base.BaseView;
import com.library.repository.db.model.DBFriendRequestModel;
import com.tencent.imsdk.friendship.TIMFriendPendencyResponse;

import java.util.List;

public interface NewFriendRequestContact {
    interface View extends BaseView {

        void showPendencyList(List<DBFriendRequestModel> response);
    }

    abstract class Presenter extends BasePresenter<View> {

        public Presenter(View view) {
            super(view);
        }

        /**
         * 获取好友请求未决列表
         */
        abstract void getPendencyList();


    }
}
