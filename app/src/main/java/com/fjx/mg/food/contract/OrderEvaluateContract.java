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
public interface OrderEvaluateContract {

    interface View extends BaseView {

        void addEvaluateSuccess();

    }

    public abstract class Presenter extends BasePresenter<OrderEvaluateContract.View> {

        public Presenter(OrderEvaluateContract.View view) {
            super(view);
        }

        public abstract void addEvaluate(String oId,String globalScore,String tasteScore,
                                         String packageScore,String deliveryScore,String content,String img);

        public abstract void updateImage(List<String> filePaths,String oId,String globalScore,String tasteScore,
                                         String packageScore,String deliveryScore,String content);
    }
}
