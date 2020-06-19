package com.fjx.mg.food.contract;

import com.library.common.base.BasePresenter;
import com.library.common.base.BaseView;
import com.library.repository.models.CreateOrderBean;
import com.library.repository.models.ShoppingInfoBean;

/**
 * @author yedeman
 * @date 2020/5/20.
 * email：ydmmocoo@gmail.com
 * description：
 */
public interface ShoppingInfoContract {

    interface View extends BaseView {

        void getShoppingInfoSuccess(ShoppingInfoBean data);

        void createOrderSuccess(CreateOrderBean data);

    }

    public abstract class Presenter extends BasePresenter<ShoppingInfoContract.View> {

        public Presenter(ShoppingInfoContract.View view) {
            super(view);
        }

        public abstract void getShoppingInfo(String sId);

        public abstract void createOrder(String sId,String type,String addressId,
                                         String expectedDeliveryTime,String cId,String remark,
                                         String scId,String reservedTelephone);

        public abstract void checkGoods(String sId, String type, String addressId, String expectedDeliveryTime, String cId, String remark, String scId, String reservedTelephone);

    }
}
