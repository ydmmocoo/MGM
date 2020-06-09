package com.fjx.mg.food.contract;

import com.library.common.base.BasePresenter;
import com.library.common.base.BaseView;
import com.library.repository.models.CouponBean;
import com.library.repository.models.OrderBean;

import java.util.List;

/**
 * @author yedeman
 * @date 2020/5/20.
 * email：ydmmocoo@gmail.com
 * description：
 */
public interface OrderContract {

    interface View extends BaseView {

        void getOrderListSuccess(List<OrderBean.OrderListBean> data,boolean hasNext);

        void confirmOrderSuccess();

        void cancelOrderSuccess();

        void cancelRefundSuccess();

        void reBuySuccess(String storeId);
    }

    public abstract class Presenter extends BasePresenter<OrderContract.View> {

        public Presenter(OrderContract.View view) {
            super(view);
        }

        public abstract void getOrderList(String payStatus,String orderStatus,String isRefuse,int page);

        public abstract void confirmOrder(String orderId);

        public abstract void cancelOrder(String orderId);

        public abstract void cancelRefund(String orderId);

        public abstract void reBuy(String orderId,String storeId);
    }
}
