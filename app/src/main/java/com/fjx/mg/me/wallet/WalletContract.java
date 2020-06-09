package com.fjx.mg.me.wallet;

import com.library.common.base.BasePresenter;
import com.library.common.base.BaseView;
import com.library.repository.models.UserInfoModel;

public interface WalletContract {


    interface View extends BaseView {


        void showUserInfo(UserInfoModel data);
    }

    abstract class Presenter extends BasePresenter<WalletContract.View> {

        Presenter(WalletContract.View view) {
            super(view);
        }


        abstract void getUserProfile();
    }
}
