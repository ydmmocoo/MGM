package com.fjx.mg.me.record;

import com.library.common.base.BasePresenter;
import com.library.common.base.BaseView;
import com.library.repository.models.BillRecordModel;

import java.util.List;

public interface BillRecord2Contract {


    interface View extends BaseView {

        void showBillRecore(BillRecordModel recordModel, int page);

        void loadDataerror();

        void onSelectType(String typeId, String typeName, boolean isOther);

        void onDialogDismiss();

    }

    abstract class Presenter extends BasePresenter<BillRecord2Contract.View> {

        Presenter(BillRecord2Contract.View view) {
            super(view);
        }

        abstract void getBillRecord(int page, String billType, String accountType);

        abstract void showSelectDialog(android.view.View view, List<BillRecordModel.AccountTypeBean> accountTypeList,
                                       List<BillRecordModel.BillTypeBean> billTypeList);
    }
}
