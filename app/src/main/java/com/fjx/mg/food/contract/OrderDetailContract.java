package com.fjx.mg.food.contract;

import com.library.common.base.BasePresenter;
import com.library.common.base.BaseView;
import com.library.repository.models.OrderBean;
import com.library.repository.models.OrderDetailBean;

import java.util.List;

/**
 * @author yedeman
 * @date 2020/5/20.
 * email：ydmmocoo@gmail.com
 * description：
 */
public interface OrderDetailContract {

    interface View extends BaseView {

        void getOrderDetailSuccess(OrderDetailBean data);

        void confirmOrderSuccess();

        void cancelOrderSuccess();

        void cancelRefundSuccess();

        void reBuySuccess(String storeId);
    }

    public abstract class Presenter extends BasePresenter<OrderDetailContract.View> {

        public Presenter(OrderDetailContract.View view) {
            super(view);
        }

        public abstract void getOrderDetail(String oId);

        public abstract void confirmOrder(String orderId);

        public abstract void cancelOrder(String orderId);

        public abstract void cancelRefund(String orderId);

        public abstract void reBuy(String orderId,String storeId);
    }
}
