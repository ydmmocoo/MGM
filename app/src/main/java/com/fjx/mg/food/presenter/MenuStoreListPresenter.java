package com.fjx.mg.food.presenter;

import com.fjx.mg.food.contract.MenuStoreListContract;
import com.library.common.utils.CommonToast;
import com.library.repository.core.net.CommonObserver;
import com.library.repository.core.net.RxScheduler;
import com.library.repository.models.HomeShopListBean;
import com.library.repository.models.ResponseModel;
import com.library.repository.repository.RepositoryFactory;

/**
 * @author yedeman
 * @date 2020/5/20.
 * email：ydmmocoo@gmail.com
 * description：
 */
public class MenuStoreListPresenter extends MenuStoreListContract.Presenter {

    public MenuStoreListPresenter(MenuStoreListContract.View view) {
        super(view);
    }

    @Override
    public void getShopsList(String serviceId, String secondServiceId, String order, int page) {
        String lng=RepositoryFactory.getLocalRepository().getLongitude();
        String lat=RepositoryFactory.getLocalRepository().getLatitude();
        RepositoryFactory.getRemoteFoodApi().getShopList(lng,lat,0,serviceId,secondServiceId,
                order,1,0,"",page)
                .compose(RxScheduler.<ResponseModel<HomeShopListBean>>toMain())
                .as(mView.<ResponseModel<HomeShopListBean>>bindAutoDispose())
                .subscribe(new CommonObserver<HomeShopListBean>() {
                    @Override
                    public void onSuccess(HomeShopListBean data) {
                        if (mView != null) {
                            mView.getShopsListSuccess(data.getShopList(),data.isHaxNext());
                        }
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        CommonToast.toast(data.getMsg());
                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                        if (mView != null)
                            mView.hideLoading();
                        CommonToast.toast(data.getMsg());
                    }
                });
    }
}
