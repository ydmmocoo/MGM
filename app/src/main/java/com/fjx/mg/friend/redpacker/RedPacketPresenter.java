package com.fjx.mg.friend.redpacker;

import com.library.common.utils.CommonToast;
import com.library.repository.core.net.CommonObserver;
import com.library.repository.core.net.RxScheduler;
import com.library.repository.models.ResponseModel;
import com.library.repository.repository.RepositoryFactory;

public class RedPacketPresenter extends RedPacketContact.Presenter {

    public RedPacketPresenter(RedPacketContact.View view) {
        super(view);
    }

    @Override
    void checkPrice(String price) {
        mView.showLoading();
        RepositoryFactory.getRemotePayRepository().checkMoneyLimit("1", price)
                .compose(RxScheduler.<ResponseModel<Object>>toMain())
                .as(mView.<ResponseModel<Object>>bindAutoDispose())
                .subscribe(new CommonObserver<Object>() {
                    @Override
                    public void onSuccess(Object data) {
                        mView.checkSuccess();
                        mView.hideLoading();
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        mView.hideLoading();
                        CommonToast.toast(data.getMsg());
                    }

                    @Override
                    public void onNetError(ResponseModel data) {

                    }
                });
    }
}
