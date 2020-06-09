package com.fjx.mg.food.presenter;

import com.fjx.mg.food.contract.ChooseCouponContract;
import com.fjx.mg.food.contract.GoodsSearchContract;
import com.library.common.utils.CommonToast;
import com.library.repository.core.net.CommonObserver;
import com.library.repository.core.net.RxScheduler;
import com.library.repository.models.CouponBean;
import com.library.repository.models.GoodsSearchBean;
import com.library.repository.models.ResponseModel;
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
}
