package com.fjx.mg.food.presenter;

import com.fjx.mg.food.contract.OrderContract;
import com.library.common.utils.CommonToast;
import com.library.repository.core.net.CommonObserver;
import com.library.repository.core.net.RxScheduler;
import com.library.repository.models.OrderBean;
import com.library.repository.models.ResponseModel;
import com.library.repository.repository.RepositoryFactory;

/**
 * @author yedeman
 * @date 2020/5/20.
 * email：ydmmocoo@gmail.com
 * description：
 */
public class OrderPresenter extends OrderContract.Presenter {

    public OrderPresenter(OrderContract.View view) {
        super(view);
    }

    @Override
    public void getOrderList(String payStatus, String orderStatus, String isRefuse, int page) {
        RepositoryFactory.getRemoteFoodApi().getOrderList(payStatus, orderStatus, isRefuse, page)
                .compose(RxScheduler.<ResponseModel<OrderBean>>toMain())
                .as(mView.<ResponseModel<OrderBean>>bindAutoDispose())
                .subscribe(new CommonObserver<OrderBean>() {
                    @Override
                    public void onSuccess(OrderBean data) {
                        if (mView != null) {
                            mView.getOrderListSuccess(data.getOrderList(), data.isHasNext());
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

    @Override
    public void confirmOrder(String orderId) {
        RepositoryFactory.getRemoteFoodApi().confirmOrder(orderId)
                .compose(RxScheduler.<ResponseModel<Object>>toMain())
                .as(mView.<ResponseModel<Object>>bindAutoDispose())
                .subscribe(new CommonObserver<Object>() {
                    @Override
                    public void onSuccess(Object data) {
                        if (mView != null) {
                            mView.confirmOrderSuccess();
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

    @Override
    public void cancelOrder(String orderId) {
        RepositoryFactory.getRemoteFoodApi().cancelOrder(orderId)
                .compose(RxScheduler.<ResponseModel<Object>>toMain())
                .as(mView.<ResponseModel<Object>>bindAutoDispose())
                .subscribe(new CommonObserver<Object>() {
                    @Override
                    public void onSuccess(Object data) {
                        if (mView != null) {
                            mView.cancelOrderSuccess();
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

    @Override
    public void cancelRefund(String orderId) {
        RepositoryFactory.getRemoteFoodApi().cancelRefuse(orderId)
                .compose(RxScheduler.<ResponseModel<Object>>toMain())
                .as(mView.<ResponseModel<Object>>bindAutoDispose())
                .subscribe(new CommonObserver<Object>() {
                    @Override
                    public void onSuccess(Object data) {
                        if (mView != null) {
                            mView.cancelOrderSuccess();
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

    @Override
    public void reBuy(String orderId,String storeId) {
        RepositoryFactory.getRemoteFoodApi().reBuy(orderId)
                .compose(RxScheduler.<ResponseModel<Object>>toMain())
                .as(mView.<ResponseModel<Object>>bindAutoDispose())
                .subscribe(new CommonObserver<Object>() {
                    @Override
                    public void onSuccess(Object data) {
                        if (mView != null) {
                            mView.reBuySuccess(storeId);
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
