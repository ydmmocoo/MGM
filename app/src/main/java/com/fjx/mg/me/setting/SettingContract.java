package com.fjx.mg.me.setting;

import com.library.common.base.BasePresenter;
import com.library.common.base.BaseView;
import com.library.repository.models.UserInfoModel;
import com.library.repository.models.VersionModel;

public interface SettingContract {


    interface View extends BaseView {

        void showUpdateDialog(VersionModel model);

    }

    abstract class Presenter extends BasePresenter<SettingContract.View> {

        Presenter(SettingContract.View view) {
            super(view);
        }

        abstract void logout();

        abstract void checkVersion();


    }
}
