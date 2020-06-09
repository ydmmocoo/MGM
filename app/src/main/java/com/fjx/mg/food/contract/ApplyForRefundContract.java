package com.fjx.mg.food.contract;

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
public interface ApplyForRefundContract {

    interface View extends BaseView {

        void refuseOrderSuccess();

    }

    public abstract class Presenter extends BasePresenter<ApplyForRefundContract.View> {

        public Presenter(ApplyForRefundContract.View view) {
            super(view);
        }

        public abstract void refuseOrder(String orderId,String remark);
    }
}
