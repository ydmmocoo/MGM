package com.fjx.mg.food.presenter;

import android.widget.ImageView;

import com.fjx.mg.food.contract.GoodsSearchContract;
import com.library.common.utils.CommonToast;
import com.library.repository.core.net.CommonObserver;
import com.library.repository.core.net.RxScheduler;
import com.library.repository.models.GoodsSearchBean;
import com.library.repository.models.ResponseModel;
import com.library.repository.models.ShopingCartBean;
import com.library.repository.repository.RepositoryFactory;

/**
 * @author yedeman
 * @date 2020/5/20.
 * email：ydmmocoo@gmail.com
 * description：
 */
public class GoodsSearchPresenter extends GoodsSearchContract.Presenter {

    public GoodsSearchPresenter(GoodsSearchContract.View view) {
        super(view);
    }

    @Override
    public void getGoodsList(String sId, String name,int page) {
        RepositoryFactory.getRemoteFoodApi().getGoodsList(sId, "", "",name,page)
                .compose(RxScheduler.<ResponseModel<GoodsSearchBean>>toMain())
                .as(mView.<ResponseModel<GoodsSearchBean>>bindAutoDispose())
                .subscribe(new CommonObserver<GoodsSearchBean>() {
                    @Override
                    public void onSuccess(GoodsSearchBean data) {
                        if (mView != null) {
                            mView.getGoodsListSuccess(data.getGoodsList(),data.isHasNext());
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
    public void addShopCart(String sId, String gId, String gName, String seId, String seName, String aIds, String aNames, String price, String num, String img, ImageView ivAdd) {
        RepositoryFactory.getRemoteFoodApi().addShopCart(sId,gId,gName,seId,seName,aIds,aNames,price,num,img)
                .compose(RxScheduler.<ResponseModel<Object>>toMain())
                .as(mView.<ResponseModel<Object>>bindAutoDispose())
                .subscribe(new CommonObserver<Object>() {
                    @Override
                    public void onSuccess(Object data) {
                        if (mView != null) {
                            mView.addShopCartSuccess(ivAdd);
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
}
