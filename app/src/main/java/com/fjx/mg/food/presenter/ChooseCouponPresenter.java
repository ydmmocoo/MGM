package com.fjx.mg.food.presenter;

import com.fjx.mg.food.contract.ChooseCouponContract;
import com.fjx.mg.food.contract.ShoppingInfoContract;
import com.library.common.utils.CommonToast;
import com.library.repository.core.net.CommonObserver;
import com.library.repository.core.net.RxScheduler;
import com.library.repository.models.CouponBean;
import com.library.repository.models.ResponseModel;
import com.library.repository.models.ShoppingInfoBean;
import com.library.repository.repository.RepositoryFactory;

/**
 * @author yedeman
 * @date 2020/5/20.
 * email：ydmmocoo@gmail.com
 * description：
 */
public class ChooseCouponPresenter extends ChooseCouponContract.Presenter {

    public ChooseCouponPresenter(ChooseCouponContract.View view) {
        super(view);
    }

    @Override
    public void getCouponList(String price, String phone) {
        RepositoryFactory.getRemoteFoodApi().getCouponList(price, "1", phone)
                .compose(RxScheduler.<ResponseModel<CouponBean>>toMain())
                .as(mView.<ResponseModel<CouponBean>>bindAutoDispose())
                .subscribe(new CommonObserver<CouponBean>() {
                    @Override
                    public void onSuccess(CouponBean data) {
                        if (mView != null) {
                            mView.getCouponListSuccess(data.getCouponList());
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
