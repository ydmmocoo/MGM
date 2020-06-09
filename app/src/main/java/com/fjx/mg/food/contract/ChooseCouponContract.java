package com.fjx.mg.food.contract;

import com.library.common.base.BasePresenter;
import com.library.common.base.BaseView;
import com.library.repository.models.CouponBean;
import com.library.repository.models.ShoppingInfoBean;

import java.util.List;

/**
 * @author yedeman
 * @date 2020/5/20.
 * email：ydmmocoo@gmail.com
 * description：
 */
public interface ChooseCouponContract {

    interface View extends BaseView {

        void getCouponListSuccess(List<CouponBean.CouponListBean> data);

    }

    public abstract class Presenter extends BasePresenter<ChooseCouponContract.View> {

        public Presenter(ChooseCouponContract.View view) {
            super(view);
        }

        public abstract void getCouponList(String price,String phone);
    }
}
