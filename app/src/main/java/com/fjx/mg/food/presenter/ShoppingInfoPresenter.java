package com.fjx.mg.food.presenter;

import android.text.TextUtils;

import com.fjx.mg.R;
import com.fjx.mg.food.contract.ShoppingInfoContract;
import com.fjx.mg.food.contract.StoreDetailContract;
import com.library.common.base.BaseApp;
import com.library.common.utils.CommonToast;
import com.library.repository.core.net.CommonObserver;
import com.library.repository.core.net.RxScheduler;
import com.library.repository.models.CheckGoodsBean;
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

    @Override
    public void checkGoods(String sId, String type, String addressId, String expectedDeliveryTime, String cId, String remark, String scId, String reservedTelephone) {
        if("1".equals(type)){
            if (TextUtils.isEmpty(addressId)) {
                CommonToast.toast(BaseApp.getInstance().getResources().getString(R.string.please_add_address_first));
                return;
            }
        }
        RepositoryFactory.getRemoteFoodApi().checkGoods(sId)
                .compose(RxScheduler.<ResponseModel<CheckGoodsBean>>toMain())
                .as(mView.<ResponseModel<CheckGoodsBean>>bindAutoDispose())
                .subscribe(new CommonObserver<CheckGoodsBean>() {
                    @Override
                    public void onSuccess(CheckGoodsBean data) {
                        if (data.getErrors()!=null&&data.getErrors().size()>0) {
                            String s="";
                            for (int i=0;i<data.getErrors().size();i++){
                                if (i==0){
                                    s=data.getErrors().get(i).getGName().concat(data.getErrors().get(i).getTip());
                                }else {
                                    s=s.concat("\n").concat(data.getErrors().get(i).getGName().concat(data.getErrors().get(i).getTip()));
                                }
                            }
                            CommonToast.toast(s);
                        }else {
                            createOrder(sId, type, addressId, expectedDeliveryTime, cId, remark, scId, reservedTelephone);
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
