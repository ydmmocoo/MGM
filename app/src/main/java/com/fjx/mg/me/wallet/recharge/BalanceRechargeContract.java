package com.fjx.mg.me.wallet.recharge;

import android.widget.EditText;

import com.flyco.tablayout.listener.CustomTabEntity;
import com.library.common.base.BasePresenter;
import com.library.common.base.BaseView;
import com.library.repository.models.RechargeModel;
import com.library.repository.models.ResponseModel;

import java.util.ArrayList;
import java.util.List;

public interface BalanceRechargeContract {


    interface View extends BaseView {

        void responseFailed(ResponseModel data);

        void responseSuccess();

        void closeDialog();

        void showTabAndItems(ArrayList<CustomTabEntity> tabEntitys);

        void showDataList(List<RechargeModel> datas);

        void getBalanceSuccess(String balance);

        void ConvertSuccess(String servicePrice, String totalPrice);

    }

    abstract class Presenter extends BasePresenter<BalanceRechargeContract.View> {

        Presenter(BalanceRechargeContract.View view) {
            super(view);
        }

        abstract void QRCollectionCode(String price);


        abstract void TextWatch(EditText amount);

        abstract void playSound();

        abstract void dismiss();

        abstract void Band(String clientId);

        abstract void balancePackage();

        abstract void getUserBalance();


    }
}
