package com.fjx.mg.me.safe_center.auth;

import com.library.common.base.BasePresenter;
import com.library.common.base.BaseView;

public interface AuthCenterContract {


    interface View extends BaseView {


    }

    abstract class Presenter extends BasePresenter<AuthCenterContract.View> {

        Presenter(AuthCenterContract.View view) {
            super(view);
        }

        abstract boolean canEdit();

        abstract void change(int type);

        abstract void reset(int type);

        abstract void showAuthDialog(int type, boolean isReset);


    }
}
