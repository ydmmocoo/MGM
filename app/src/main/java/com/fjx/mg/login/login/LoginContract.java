package com.fjx.mg.login.login;

import com.library.common.base.BasePresenter;
import com.library.common.base.BaseView;
import com.library.repository.models.UserInfoModel;

public interface LoginContract {

    interface View extends BaseView {
        void loginSuccess(UserInfoModel data);

        void loginFalied();

        void setMobile(String text);
        void showTimeCount(String text);


        void userNoRegister(String openId, String nickName, String loginType, String avatar, String sex);


    }

    abstract class Presenter extends BasePresenter<LoginContract.View> {

        Presenter(LoginContract.View view) {
            super(view);
        }

        abstract void loginPwd(String mobile, String pwd);

        abstract void loginAuth(String openId, String nickName, String type, String avatar, String sex);

        abstract void loginFacebook();

        abstract void loginWx();

        abstract void loginAli();

        abstract void loginCode(String mobile, String code);

        abstract void sendSmsCode(String mobile, String areaCode);

        abstract void releaseTimer();


        abstract void getAliUserInfo(String openid, String code);


        /**
         * 根据手机好获取token,忘记密码用
         */
        abstract void getUser(String phone);

    }

}
