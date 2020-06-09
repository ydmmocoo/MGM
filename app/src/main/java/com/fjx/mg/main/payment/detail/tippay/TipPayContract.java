package com.fjx.mg.main.payment.detail.tippay;

import androidx.fragment.app.FragmentManager;

import com.library.common.base.BasePresenter;
import com.library.common.base.BaseView;
import com.library.common.view.editdialog.PayFragment;

import java.util.Map;


public interface TipPayContract {

    interface View extends BaseView {
        FragmentManager fragmentManager();

        void convertMoneySuccess(String price, String servicePrice, String totalPrice);

        void getBalanceSuccess(String balance);

    }

    abstract class Presenter extends BasePresenter<TipPayContract.View> {
        Presenter(TipPayContract.View view) {
            super(view);
        }

        abstract void convertMoney(Map<String, Object> payMap);

        abstract void phoneWxRecharge(Map<String, Object> payMap);

        abstract void phoneZFBRecharge(Map<String, Object> payMap);

        abstract void phoneBalanceRecharge(Map<String, Object> payMap);

        abstract void setQuestionPriceBy(int payType, Map<String, Object> payMap);

        abstract void checkPayPasswoed(String password, PayFragment fragment, Map<String, Object> payMap);

        abstract void getUserBalance();
    }
}
