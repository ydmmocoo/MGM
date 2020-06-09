package com.fjx.mg.food.presenter;

import com.fjx.mg.food.contract.ApplyForRefundContract;
import com.fjx.mg.food.contract.ChooseCouponContract;
import com.library.common.utils.CommonToast;
import com.library.repository.core.net.CommonObserver;
import com.library.repository.core.net.RxScheduler;
import com.library.repository.models.CouponBean;
import com.library.repository.models.ResponseModel;
import com.library.repository.repository.RepositoryFactory;

/**
 * @author yedeman
 * @date 2020/5/20.
 * email：ydmmocoo@gmail.com
 * description：
 */
public class ApplyForRefundPresenter extends ApplyForRefundContract.Presenter {

    public ApplyForRefundPresenter(ApplyForRefundContract.View view) {
        super(view);
    }

    @Override
    public void refuseOrder(String orderId, String remark) {
        RepositoryFactory.getRemoteFoodApi().refuseOrder(orderId, remark)
                .compose(RxScheduler.<ResponseModel<Object>>toMain())
                .as(mView.<ResponseModel<Object>>bindAutoDispose())
                .subscribe(new CommonObserver<Object>() {
                    @Override
                    public void onSuccess(Object data) {
                        if (mView != null) {
                            mView.refuseOrderSuccess();
                        }
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        CommonToast.toast(data.getMsg());
                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                        CommonToast.toast(data.getMsg());
                    }
                });
    }
}
