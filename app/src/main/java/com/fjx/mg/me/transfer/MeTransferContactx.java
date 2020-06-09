package com.fjx.mg.me.transfer;

import com.library.common.base.BasePresenter;
import com.library.common.base.BaseView;

public interface MeTransferContactx {
    interface View extends BaseView {

        void checkSuccess();

        void sameMoney(boolean isSame,String msg);
    }

    abstract class Presenter extends BasePresenter<MeTransferContactx.View> {

        public Presenter(View view) {
            super(view);
        }

        abstract void checkPrice(String price);

        abstract void checkSameMoney(String toUid/*对方id*/, String money/*金额*/);

    }
}
