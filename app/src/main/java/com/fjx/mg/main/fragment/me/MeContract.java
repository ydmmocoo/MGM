package com.fjx.mg.main.fragment.me;

import com.library.common.base.BasePresenter;
import com.library.common.base.BaseView;
import com.library.repository.models.CityCircleListModel;
import com.library.repository.models.ImUserRelaM;
import com.library.repository.models.UserInfoModel;
import com.tencent.imsdk.TIMUserProfile;

public interface MeContract {

    interface View extends BaseView {

        void showUserInfo(UserInfoModel data);

        void showContactUsDialog();

        void isMe(ImUserRelaM userRelaM);
        void requestIde(CityCircleListModel model);
    }

    abstract class Presenter extends BasePresenter<MeContract.View> {

        Presenter(MeContract.View view) {
            super(view);
        }

        abstract void getUserProfile();

        abstract void getAllFriend(final TIMUserProfile userProfile);
        abstract void findUser(String identifier);
    }
}
