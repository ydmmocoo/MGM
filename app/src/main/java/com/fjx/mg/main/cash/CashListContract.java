package com.fjx.mg.main.cash;

import com.library.common.base.BasePresenter;
import com.library.common.base.BaseView;
import com.library.repository.models.CashListModel;

public interface CashListContract {

    interface View extends BaseView {


        void showCashList(CashListModel data);

        void showReciveCashResult(String balance, int position);

        void showBatchReciveCashResult(String balance);

        void loadError();

    }

    abstract class Presenter extends BasePresenter<CashListContract.View> {

        Presenter(CashListContract.View view) {
            super(view);
        }

        abstract void getCashList(int page,int statues);

        abstract void reciveCash(String id, int position);

        abstract void batchReciveCash();


    }

}
