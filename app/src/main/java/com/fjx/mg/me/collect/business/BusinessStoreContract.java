package com.fjx.mg.me.collect.business;

import com.fjx.mg.me.collect.MyCollectContract;
import com.library.common.base.BaseFragment;
import com.library.common.base.BasePresenter;
import com.library.common.base.BaseView;

import java.util.List;

public interface BusinessStoreContract {
    interface View extends BaseView {

        void showDatas(List<Object> datas);
    }

    public abstract class Presenter extends BasePresenter<BusinessStoreContract.View> {

        Presenter(BusinessStoreContract.View view) {
            super(view);
        }

        abstract void getData();
    }

}
