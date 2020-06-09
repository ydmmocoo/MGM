package com.fjx.mg.me.safe_center.check.identity_password;

import com.library.common.base.BasePresenter;
import com.library.common.base.BaseView;

import java.util.Map;

public interface IdentityPasswordAuthContract {


    interface View extends BaseView {


        void checkSuccess();
    }

    abstract class Presenter extends BasePresenter<IdentityPasswordAuthContract.View> {

        Presenter(IdentityPasswordAuthContract.View view) {
            super(view);
        }


        abstract void check(Map<String, Object> map);


    }
}
