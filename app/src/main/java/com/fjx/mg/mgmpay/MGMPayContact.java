package com.fjx.mg.mgmpay;

import com.library.common.base.BasePresenter;
import com.library.common.base.BaseView;
import com.library.repository.models.PayByBalanceModel;
import com.library.repository.models.PayCheckOrderModel;
import com.library.repository.models.ResponseModel;
import com.library.repository.models.UserInfoModel;

public interface MGMPayContact {
    interface View extends BaseView {

        void responsePayByBalanceFail(ResponseModel data);

        void responsePayByBalanceSuc(PayByBalanceModel model);

        void showPswDialog();

        void showView(PayCheckOrderModel data);

        void noLogin();

        void showUserInfo(UserInfoModel data);
        void showUserInfoFail(ResponseModel data);

    }

    abstract class Presenter extends BasePresenter<MGMPayContact.View> {

        public Presenter(View view) {
            super(view);
        }


        abstract void requestPayByBalance(String appId, String prepayId, String appKey);

        abstract void requestCheckOrder(String orderString);
    }
}
