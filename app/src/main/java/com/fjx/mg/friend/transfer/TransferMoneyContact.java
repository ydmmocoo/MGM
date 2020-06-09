package com.fjx.mg.friend.transfer;

import com.common.paylibrary.model.WXPayModel;
import com.library.common.base.BasePresenter;
import com.library.common.base.BaseView;
import com.tencent.imsdk.TIMUserProfile;
import com.tencent.imsdk.friendship.TIMFriend;

public interface TransferMoneyContact {
    interface View extends BaseView {

        void getWXOrderSuccess(WXPayModel payModel);

        void showUserInfo(TIMUserProfile userProfile);

        void showUserInfo(TIMFriend friend);
        void checkSuccess();
    }

    abstract class Presenter extends BasePresenter<TransferMoneyContact.View> {

        public Presenter(View view) {
            super(view);
        }

        /**
         * 调取后台支付，生成预账单
         *
         * @param receiveUserId
         * @param amount
         * @param instruction
         */
        abstract void transMoneyByWX(String receiveUserId, String amount, String instruction);

        /**
         * 客户端微信支付
         *
         * @param payModel
         */
        abstract void payWX(WXPayModel payModel);

        abstract void findUser(String imUid);
        abstract void checkPrice(String price);

    }
}
