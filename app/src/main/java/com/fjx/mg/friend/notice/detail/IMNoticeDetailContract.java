package com.fjx.mg.friend.notice.detail;

import com.library.common.base.BasePresenter;
import com.library.common.base.BaseView;
import com.library.repository.models.BillRecordModel;
import com.library.repository.models.IMNoticeModel;

public interface IMNoticeDetailContract {
    interface View extends BaseView {

        void showBillDetail(IMNoticeModel bean);

    }

    abstract class Presenter extends BasePresenter<IMNoticeDetailContract.View> {

        public Presenter(View view) {
            super(view);
        }

        abstract void getBillDetail(String billId);

        abstract void getPayNoticeDetail(String nid);

        abstract void balanceDetail(String id);


    }


}
