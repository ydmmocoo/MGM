package com.fjx.mg.friend.redpacker;

import com.library.common.base.BasePresenter;
import com.library.common.base.BaseView;

public interface RedPacketContact {
    interface View extends BaseView {
        void checkSuccess();
    }

    abstract class Presenter extends BasePresenter<RedPacketContact.View> {

        public Presenter(View view) {
            super(view);
        }

        abstract void checkPrice(String price);
    }
}
