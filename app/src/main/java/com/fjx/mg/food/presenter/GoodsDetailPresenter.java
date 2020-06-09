package com.fjx.mg.food.presenter;

import com.fjx.mg.food.contract.GoodsDetailContract;
import com.library.common.utils.CommonToast;
import com.library.repository.core.net.CommonObserver;
import com.library.repository.core.net.RxScheduler;
import com.library.repository.models.GoodsDetailBean;
import com.library.repository.models.ResponseModel;
import com.library.repository.models.ShopingCartBean;
import com.library.repository.models.StoreEvaluateBean;
import com.library.repository.repository.RepositoryFactory;

/**
 * @author yedeman
 * @date 2020/5/20.
 * email：ydmmocoo@gmail.com
 * description：
 */
public class GoodsDetailPresenter extends GoodsDetailContract.Presenter {

    public GoodsDetailPresenter(GoodsDetailContract.View view) {
        super(view);
    }

    @Override
    public void getGoodsInfo(String gId) {
        RepositoryFactory.getRemoteFoodApi().getGoodsDetail(gId)
                .compose(RxScheduler.<ResponseModel<GoodsDetailBean>>toMain())
                .as(mView.<ResponseModel<GoodsDetailBean>>bindAutoDispose())
                .subscribe(new CommonObserver<GoodsDetailBean>() {
                    @Override
                    public void onSuccess(GoodsDetailBean data) {
                        if (mView != null) {
                            mView.getGoodsInfoSuccess(data);
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
    public void getEvaluateList(String sId, String searchType, int page) {
        RepositoryFactory.getRemoteFoodApi().getStoreEvaluateList(sId, searchType, page)
                .compose(RxScheduler.<ResponseModel<StoreEvaluateBean>>toMain())
                .as(mView.<ResponseModel<StoreEvaluateBean>>bindAutoDispose())
                .subscribe(new CommonObserver<StoreEvaluateBean>() {
                    @Override
                    public void onSuccess(StoreEvaluateBean data) {
                        if (mView != null) {
                            mView.getEvaluateListSuccess(data);
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
