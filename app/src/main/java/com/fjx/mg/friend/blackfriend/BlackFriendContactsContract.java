package com.fjx.mg.friend.blackfriend;

import android.widget.EditText;

import com.library.common.base.BasePresenter;
import com.library.common.base.BaseView;
import com.library.repository.models.FriendContactSectionModel;
import com.tencent.imsdk.friendship.TIMFriendPendencyResponse;

import java.util.List;

public interface BlackFriendContactsContract {
    interface View extends BaseView {
        void showContactList(List<FriendContactSectionModel> datas);

        void outblackFriendSuccess();
    }

    abstract class Presenter extends BasePresenter<BlackFriendContactsContract.View> {

        public Presenter(View view) {
            super(view);
        }

        abstract void getAllFriend();

        abstract void OutBlackFriend(final String imUid);


        public abstract void searchWatcher(EditText etSearch);
    }


}
