package com.fjx.mg.me.collect.business;


import com.library.common.utils.CommonToast;
import com.library.repository.core.net.CommonObserver;
import com.library.repository.core.net.RxScheduler;
import com.library.repository.models.CollectShopsBean;
import com.library.repository.models.ResponseModel;
import com.library.repository.repository.RepositoryFactory;

public class BusinessStorePresenter extends BusinessStoreContract.Presenter {

    BusinessStorePresenter(BusinessStoreContract.View view) {
        super(view);
    }

    @Override
    void getData(int page) {
        String lng=RepositoryFactory.getLocalRepository().getLongitude();
        String lat=RepositoryFactory.getLocalRepository().getLatitude();
        RepositoryFactory.getRemoteFoodApi().getCollectShops(lng,lat,page)
                .compose(RxScheduler.<ResponseModel<CollectShopsBean>>toMain())
                .as(mView.<ResponseModel<CollectShopsBean>>bindAutoDispose())
                .subscribe(new CommonObserver<CollectShopsBean>() {
                    @Override
                    public void onSuccess(CollectShopsBean data) {
                        if (mView != null) {
                            mView.showDatas(data.getShopList(),data.isHasNext());
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
