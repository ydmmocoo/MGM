package com.fjx.mg.nearbycity.pay;

import androidx.fragment.app.FragmentManager;

import com.common.paylibrary.model.UsagePayMode;
import com.library.common.base.BasePresenter;
import com.library.common.base.BaseView;
import com.library.common.view.editdialog.PayFragment;

import java.util.Map;


public interface NearbyCityPayContract {

    interface View extends BaseView {
        FragmentManager fragmentManager();

        void convertMoneySuccess(String price, String servicePrice, String totalPrice);

        void getBalanceSuccess(String balance);

        void responsetMoney(String money,UsagePayMode type);

    }

    abstract class Presenter extends BasePresenter<NearbyCityPayContract.View> {
        Presenter(NearbyCityPayContract.View view) {
            super(view);
        }

        abstract void convertMoney(Map<String, Object> payMap);

        abstract void phoneWxRecharge(Map<String, Object> payMap);

        abstract void phoneZFBRecharge(Map<String, Object> payMap);

        abstract void phoneBalanceRecharge(Map<String, Object> payMap);

        abstract void setNearbyCityPriceBy(int payType, Map<String, Object> payMap);

        abstract void checkPayPasswoed(String password, PayFragment fragment, Map<String, Object> payMap);

        abstract void getUserBalance();

        abstract void requestMoney(String sId, UsagePayMode type);

    }
}
