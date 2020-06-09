package com.fjx.mg.food.contract;

import com.library.common.base.BasePresenter;
import com.library.common.base.BaseView;
import com.library.repository.models.CouponBean;
import com.library.repository.models.HomeShopListBean;

import java.util.List;

/**
 * @author yedeman
 * @date 2020/6/1.
 * email：ydmmocoo@gmail.com
 * description：
 */
public interface TakeOutFoodSearchContract {

    interface View extends BaseView {

        void getShopListSuccess(List<HomeShopListBean.ShopListBean> data, boolean hasNext);

    }

    public abstract class Presenter extends BasePresenter<TakeOutFoodSearchContract.View> {

        public Presenter(TakeOutFoodSearchContract.View view) {
            super(view);
        }

        public abstract void getShopsList(String title, int page);
    }
}
