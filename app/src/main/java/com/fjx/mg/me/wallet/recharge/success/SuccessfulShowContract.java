package com.fjx.mg.me.wallet.recharge.success;

import com.library.common.base.BasePresenter;
import com.library.common.base.BaseView;

public interface SuccessfulShowContract {


    interface View extends BaseView {
        void ShowMessage(String amount, String totalAmount, String poundage, String tip, int payType);
    }

    abstract class Presenter extends BasePresenter<SuccessfulShowContract.View> {
        public Presenter(SuccessfulShowContract.View view) {
            super(view);
        }

        abstract void ShowMessage(String amount, String totalAmount, String poundage, int payType);
    }
}
