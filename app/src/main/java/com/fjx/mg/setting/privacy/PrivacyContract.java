package com.fjx.mg.setting.privacy;

import com.library.common.base.BasePresenter;
import com.library.common.base.BaseView;

public interface PrivacyContract {


    interface View extends BaseView {
        void showUserInfo(String momentAccessForFriend, String momentAccessForStranger);

        void setSecretSuccess();
    }

    abstract class Presenter extends BasePresenter<PrivacyContract.View> {

        Presenter(PrivacyContract.View view) {
            super(view);
        }

        abstract void getUserInfo();

        abstract void setSecret(String frinendAccess, String strangeAccess);
    }
}
