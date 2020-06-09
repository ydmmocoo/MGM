package com.fjx.mg.food.presenter;

import com.fjx.mg.food.contract.ShoppingInfoContract;
import com.fjx.mg.food.contract.StoreDetailContract;
import com.library.common.utils.CommonToast;
import com.library.repository.core.net.CommonObserver;
import com.library.repository.core.net.RxScheduler;
import com.library.repository.models.CreateOrderBean;
import com.library.repository.models.ResponseModel;
import com.library.repository.models.ShopingCartBean;
import com.library.repository.models.ShoppingInfoBean;
import com.library.repository.models.StoreShopInfoBean;
import com.library.repository.repository.RepositoryFactory;

/**
 * @author yedeman
 * @date 2020/5/20.
 * email：ydmmocoo@gmail.com
 * description：
 */
public class ShoppingInfoPresenter extends ShoppingInfoContract.Presenter {

    public ShoppingInfoPresenter(ShoppingInfoContract.View view) {
        super(view);
    }

    @Override
    public void getShoppingInfo(String sId) {
        RepositoryFactory.getRemoteFoodApi().getShoppingInfo(sId)
                .compose(RxScheduler.<ResponseModel<ShoppingInfoBean>>toMain())
                .as(mView.<ResponseModel<ShoppingInfoBean>>bindAutoDispose())
                .subscribe(new CommonObserver<ShoppingInfoBean>() {
                    @Override
                    public void onSuccess(ShoppingInfoBean data) {
                        if (mView != null) {
                            mView.getShoppingInfoSuccess(data);
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
    public void createOrder(String sId, String type, String addressId, String expectedDeliveryTime, String cId, String remark, String scId, String reservedTelephone) {
        RepositoryFactory.getRemoteFoodApi().createOrder(sId,type,addressId,expectedDeliveryTime,cId,remark,scId,reservedTelephone)
                .compose(RxScheduler.<ResponseModel<CreateOrderBean>>toMain())
                .as(mView.<ResponseModel<CreateOrderBean>>bindAutoDispose())
                .subscribe(new CommonObserver<CreateOrderBean>() {
                    @Override
                    public void onSuccess(CreateOrderBean data) {
                        if (mView != null) {
                            mView.createOrderSuccess(data);
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
