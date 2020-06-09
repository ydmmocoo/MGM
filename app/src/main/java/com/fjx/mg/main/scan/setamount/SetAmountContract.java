package com.fjx.mg.main.scan.setamount;

import com.library.common.base.BasePresenter;
import com.library.common.base.BaseView;

public interface SetAmountContract {
    interface View extends BaseView {

    }

    abstract class Presenter extends BasePresenter<SetAmountContract.View> {

        Presenter(SetAmountContract.View view) {
            super(view);
        }


    }
}
