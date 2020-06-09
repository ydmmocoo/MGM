package com.fjx.mg.friend.nearby;

import com.library.common.base.BasePresenter;
import com.library.common.base.BaseView;
import com.library.repository.models.NearbyUserModel;
import com.tencent.imsdk.TIMUserProfile;
import com.tencent.imsdk.friendship.TIMFriend;
import com.tencent.imsdk.friendship.TIMFriendPendencyResponse;

import java.util.List;

public interface NearbyContact {
    interface View extends BaseView {

        void showAroundUsers(List<NearbyUserModel> datas);

        void getImUserSuccess(TIMUserProfile profile);

        void getImUserSuccess(TIMFriend friend);
    }

    abstract class Presenter extends BasePresenter<View> {

        public Presenter(View view) {
            super(view);
        }

        abstract void locationAddress();

        abstract void findAround(String lng, String lat);

        abstract void findImUser(String uid);

    }
}
