package com.fjx.mg.login.bind;

import com.common.paylibrary.model.AliUserModel;
import com.library.common.base.BasePresenter;
import com.library.common.base.BaseView;
import com.library.repository.models.UserInfoModel;

public interface BindMobileContract {

    interface View extends BaseView {
        void setMobile(String text);

        void bindSuccess(UserInfoModel data);

        void showTimeCount(String text);

        void aliLoginSuccess(AliUserModel userModel);

        void showUser(String info);

        void sethasPass(Boolean hasPass);

        String getSignPassword();

    }

    abstract class Presenter extends BasePresenter<BindMobileContract.View> {

        Presenter(BindMobileContract.View view) {
            super(view);
        }


        abstract void bind(String nickName, String sn, String phone, String smsCode, String psw, String openId, String sex, String avatar, String loginType, String confirmPwd);

        abstract void getAliUserInfo(String code);

        abstract void sendSmsCode(String mobile, String areaCode);

        abstract void getWeixinUserInfo(String code);

        abstract void releaseTimer();

        abstract void getUser(String phone);

//        abstract void updateImUserImage(String imageUrl, String nickName, String sex);


    }

}
