package com.fjx.mg.me.userinfo;

import com.library.common.base.BasePresenter;
import com.library.common.base.BaseView;
import com.library.repository.models.UserInfoModel;

import java.util.List;

public interface UserInfoContract {


    interface View extends BaseView {

        void showPickPhotoDialog(List<String> avatars);

        void showUserInfo(UserInfoModel userInfoModel);

        void upateSuccess(boolean isImage);

        void selecrtAddress(String countryName, String cityName, String countryId, String cityId);
    }

    abstract class Presenter extends BasePresenter<UserInfoContract.View> {

        Presenter(UserInfoContract.View view) {
            super(view);
        }

        abstract void getUserInfo();

        abstract void updateProfile(String filePath, String nickName, String sex, String address, String inviteCode);

        abstract void updateImage(String filePath, String nickName, String sex, String address, String inviteCode);

        abstract void doUpdate(String filePath, String nickName, String sex, String address, String inviteCode);

        abstract void doUpdateImage(String filePath);

        abstract void updateImUserImage(String imageUrl, String nickName, String sex, String address, String inviteCode);


        abstract void getConfig();

        abstract void getDefaultAvatar();

        abstract void showAddressDialog();
    }
}
