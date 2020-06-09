package com.fjx.mg.friend.search;

import com.library.common.base.BasePresenter;
import com.library.common.base.BaseView;
import com.library.repository.models.SearchTimFriendModel;
import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.ext.group.TIMGroupBaseInfo;
import com.tencent.imsdk.friendship.TIMFriend;

import java.util.List;

public interface FriendSearchContract {

    interface View extends BaseView {

        void showFriends(List<SearchTimFriendModel> friendList);

        void showGroups(List<TIMGroupBaseInfo> mTimGroupBaseInfos);

        void showConverser(List<SearchTimFriendModel> timConversations);
    }


    abstract class Presenter extends BasePresenter<FriendSearchContract.View> {

        public Presenter(FriendSearchContract.View view) {
            super(view);
        }

        abstract void searchFriend(String content);


    }
}
