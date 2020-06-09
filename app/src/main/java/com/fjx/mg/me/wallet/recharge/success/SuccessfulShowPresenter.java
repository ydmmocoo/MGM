package com.fjx.mg.me.wallet.recharge.success;

import com.fjx.mg.R;
import com.library.repository.Constant;

class SuccessfulShowPresenter extends SuccessfulShowContract.Presenter {


    SuccessfulShowPresenter(SuccessfulShowContract.View view) {
        super(view);
    }

    @Override
    void ShowMessage(String amount, String totalAmount, String poundage, int payType) {
        String pou = mView.getCurContext().getString(R.string.order_poundage) + poundage;
        String tot = (payType == Constant.SuccessfullShowType.WD ? mView.getCurContext().getString(R.string.withdrawal_amount) : mView.getCurContext().getString(R.string.recharge_success_pay)) + totalAmount;
        String tip = payType == Constant.SuccessfullShowType.WD ? mView.getCurContext().getString(R.string.whitdrawal_success_tip) : mView.getCurContext().getString(R.string.recharge_success_tip);
        mView.ShowMessage(amount, tot, pou, tip, payType);
    }


}
