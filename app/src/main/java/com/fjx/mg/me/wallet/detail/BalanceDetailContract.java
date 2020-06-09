package com.fjx.mg.me.wallet.detail;

import com.library.common.base.BasePresenter;
import com.library.common.base.BaseView;
import com.library.repository.models.BalanceDetailModel;

public interface BalanceDetailContract {


    interface View extends BaseView {
        void showBalanceDetail(BalanceDetailModel detailModel, int page);

        void loadBalanceError();


    }

    abstract class Presenter extends BasePresenter<BalanceDetailContract.View> {

        Presenter(BalanceDetailContract.View view) {
            super(view);
        }

        abstract void getBalanceList(int page);


    }
}
