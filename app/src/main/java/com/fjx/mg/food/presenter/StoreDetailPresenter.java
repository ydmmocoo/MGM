package com.fjx.mg.food.presenter;

import android.util.Log;

import com.fjx.mg.food.contract.StoreDetailContract;
import com.library.common.utils.CommonToast;
import com.library.repository.core.net.CommonObserver;
import com.library.repository.core.net.RxScheduler;
import com.library.repository.models.ResponseModel;
import com.library.repository.models.StoreShopInfoBean;
import com.library.repository.models.ShopingCartBean;
import com.library.repository.repository.RepositoryFactory;

/**
 * @author yedeman
 * @date 2020/5/20.
 * email：ydmmocoo@gmail.com
 * description：
 */
public class StoreDetailPresenter extends StoreDetailContract.Presenter {

    public StoreDetailPresenter(StoreDetailContract.View view) {
        super(view);
    }

    @Override
    public void getShopInfo(String sId) {
        RepositoryFactory.getRemoteFoodApi().getShopInfo(sId)
                .compose(RxScheduler.<ResponseModel<StoreShopInfoBean>>toMain())
                .as(mView.<ResponseModel<StoreShopInfoBean>>bindAutoDispose())
                .subscribe(new CommonObserver<StoreShopInfoBean>() {
                    @Override
                    public void onSuccess(StoreShopInfoBean data) {
                        if (mView != null) {
                            mView.getShopInfoSuccess(data);
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
    public void getShopCartData(String sId) {
        RepositoryFactory.getRemoteFoodApi().getShopingCartData(sId)
                .compose(RxScheduler.<ResponseModel<ShopingCartBean>>toMain())
                .as(mView.<ResponseModel<ShopingCartBean>>bindAutoDispose())
                .subscribe(new CommonObserver<ShopingCartBean>() {
                    @Override
                    public void onSuccess(ShopingCartBean data) {
                        if (mView != null) {
                            mView.getShopCartDataSuccess(data);
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
    public void addShopCart(String sId, String gId, String gName, String seId, String seName, String aIds, String aNames, String price, String num,String img) {
        RepositoryFactory.getRemoteFoodApi().addShopCart(sId,gId,gName,seId,seName,aIds,aNames,price,num,img)
                .compose(RxScheduler.<ResponseModel<Object>>toMain())
                .as(mView.<ResponseModel<Object>>bindAutoDispose())
                .subscribe(new CommonObserver<Object>() {
                    @Override
                    public void onSuccess(Object data) {
                        if (mView != null) {
                            mView.addShopCartSuccess();
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
    public void clearShopCart(String sId) {
        RepositoryFactory.getRemoteFoodApi().clearShopCart(sId)
                .compose(RxScheduler.<ResponseModel<Object>>toMain())
                .as(mView.<ResponseModel<Object>>bindAutoDispose())
                .subscribe(new CommonObserver<Object>() {
                    @Override
                    public void onSuccess(Object data) {
                        if (mView != null) {
                            mView.clearShopCartSuccess();
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
    public void collect(String sId) {
        RepositoryFactory.getRemoteFoodApi().collect(sId)
                .compose(RxScheduler.<ResponseModel<Object>>toMain())
                .as(mView.<ResponseModel<Object>>bindAutoDispose())
                .subscribe(new CommonObserver<Object>() {
                    @Override
                    public void onSuccess(Object data) {
                        if (mView != null) {
                            mView.collectSuccess();
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
    public void cancelCollect(String sId) {
        RepositoryFactory.getRemoteFoodApi().cancelCollect(sId)
                .compose(RxScheduler.<ResponseModel<Object>>toMain())
                .as(mView.<ResponseModel<Object>>bindAutoDispose())
                .subscribe(new CommonObserver<Object>() {
                    @Override
                    public void onSuccess(Object data) {
                        if (mView != null) {
                            mView.cancelCollectSuccess();
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
