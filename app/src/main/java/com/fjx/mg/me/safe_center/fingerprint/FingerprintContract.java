package com.fjx.mg.me.safe_center.fingerprint;

import com.library.common.base.BasePresenter;
import com.library.common.base.BaseView;

public interface FingerprintContract {


    interface View extends BaseView {

    }

    abstract class Presenter extends BasePresenter<FingerprintContract.View> {

        Presenter(FingerprintContract.View view) {
            super(view);
        }

        abstract void showAuthDialog();
    }



}
