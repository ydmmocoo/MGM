package com.fjx.mg.me.collect.business;

import com.library.common.base.BasePresenter;
import com.library.common.base.BaseView;
import com.library.repository.models.CollectShopsBean;

import java.util.List;

public interface BusinessStoreContract {
    interface View extends BaseView {

        void showDatas(List<CollectShopsBean.ShopListBean> list,boolean hasNext);
    }

    public abstract class Presenter extends BasePresenter<BusinessStoreContract.View> {

        Presenter(BusinessStoreContract.View view) {
            super(view);
        }

        abstract void getData(int page);
    }

}
