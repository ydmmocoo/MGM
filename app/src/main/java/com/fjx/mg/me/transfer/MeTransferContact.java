package com.fjx.mg.me.transfer;

import com.library.common.base.BasePresenter;
import com.library.common.base.BaseView;

public interface MeTransferContact {
    interface View extends BaseView {

        void checkSuccess();
    }

    abstract class Presenter extends BasePresenter<MeTransferContact.View> {

        public Presenter(View view) {
            super(view);
        }

        abstract void checkPrice(String price);


    }
}
