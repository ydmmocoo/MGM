package com.fjx.mg.food.contract;

import androidx.fragment.app.FragmentManager;

import com.library.common.base.BasePresenter;
import com.library.common.base.BaseView;
import com.library.repository.models.CouponBean;

import java.util.List;

/**
 * @author yedeman
 * @date 2020/5/20.
 * email：ydmmocoo@gmail.com
 * description：
 */
public interface OrderPayContract {

    interface View extends BaseView {

        FragmentManager fragmentManager();

        void getBalanceSuccess(String balance);

        void balancePaySuccess();

    }

    public abstract class Presenter extends BasePresenter<OrderPayContract.View> {

        public Presenter(OrderPayContract.View view) {
            super(view);
        }

        public abstract void getUserBalance();

        public abstract void orderByBalance(String orderId);

        public abstract void orderByAlipay(String orderId);

        public abstract void orderByWechat(String orderId);
    }
}
