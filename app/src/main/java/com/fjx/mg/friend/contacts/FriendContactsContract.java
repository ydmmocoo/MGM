package com.fjx.mg.friend.contacts;

import android.widget.EditText;

import com.library.common.base.BasePresenter;
import com.library.common.base.BaseView;
import com.library.repository.models.FriendContactSectionModel;
import com.tencent.imsdk.friendship.TIMFriendPendencyResponse;

import java.util.List;

public interface FriendContactsContract {
    interface View extends BaseView {
        void showContactList(List<FriendContactSectionModel> datas);

        void getPendencyListSuccess(TIMFriendPendencyResponse response);
    }

    abstract class Presenter extends BasePresenter<FriendContactsContract.View> {

        public Presenter(View view) {
            super(view);
        }

        abstract void getAllFriend();

        /**
         * 获取好友请求未决列表
         */
        abstract void getPendencyList();

        public abstract void searchWatcher(EditText etSearch);
    }


}
