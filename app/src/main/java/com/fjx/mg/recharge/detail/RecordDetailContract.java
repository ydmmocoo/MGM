package com.fjx.mg.recharge.detail;

import com.library.common.base.BasePresenter;
import com.library.common.base.BaseView;
import com.library.repository.models.RechargeDetailModel;
import com.library.repository.models.RechargePhoneDetailModel;

public interface RecordDetailContract {

    interface View extends BaseView {

        void showPhoneDetail(RechargePhoneDetailModel model);

        void showDetail(RechargeDetailModel model);

    }

    abstract class Presenter extends BasePresenter<RecordDetailContract.View> {

        Presenter(RecordDetailContract.View view) {
            super(view);
        }

        abstract void getPhoneRecordDetail(String id, String type);

        abstract void getRecordDetail(String id, String type);


    }
}
