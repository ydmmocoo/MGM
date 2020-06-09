package com.fjx.mg.recharge.ewnbill;

import com.library.common.base.BasePresenter;
import com.library.common.base.BaseView;
import com.library.repository.models.ServiceModel;

public interface BillContract {

    interface View extends BaseView {

        void selectService(ServiceModel serviceModel);

    }

    abstract class Presenter extends BasePresenter<BillContract.View> {

        Presenter(BillContract.View view) {
            super(view);
        }

        abstract void getServiceType(int type);

        abstract void showServiceTypeDialog();


    }
}
