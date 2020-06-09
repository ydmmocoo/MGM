package com.fjx.mg.food.contract;

import com.library.common.base.BasePresenter;
import com.library.common.base.BaseView;
import com.library.repository.models.CouponBean;
import com.library.repository.models.HomeShopListBean;

import java.util.List;

/**
 * @author yedeman
 * @date 2020/5/20.
 * email：ydmmocoo@gmail.com
 * description：
 */
public interface MenuStoreListContract {

    interface View extends BaseView {

        void getShopsListSuccess(List<HomeShopListBean.ShopListBean> data, boolean hasNext);

    }

    public abstract class Presenter extends BasePresenter<MenuStoreListContract.View> {

        public Presenter(MenuStoreListContract.View view) {
            super(view);
        }

        public abstract void getShopsList(String serviceId,String secondServiceId,
                                          String order,int page);
    }
}
