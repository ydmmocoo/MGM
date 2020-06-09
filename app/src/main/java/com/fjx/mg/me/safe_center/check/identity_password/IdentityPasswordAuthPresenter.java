package com.fjx.mg.me.safe_center.check.identity_password;

import com.library.common.utils.CommonToast;
import com.library.repository.core.net.CommonObserver;
import com.library.repository.core.net.RxScheduler;
import com.library.repository.models.ResponseModel;
import com.library.repository.repository.RepositoryFactory;

import java.util.Map;

public class IdentityPasswordAuthPresenter extends IdentityPasswordAuthContract.Presenter {


    IdentityPasswordAuthPresenter(IdentityPasswordAuthContract.View view) {
        super(view);
    }


    @Override
    void check(Map<String, Object> map) {
        mView.showLoading();
        RepositoryFactory.getRemoteAccountRepository().check(map)
                .compose(RxScheduler.<ResponseModel<Object>>toMain())
                .as(mView.<ResponseModel<Object>>bindAutoDispose())
                .subscribe(new CommonObserver<Object>() {
                    @Override
                    public void onSuccess(Object data) {
                        mView.hideLoading();
                        mView.checkSuccess();

                    }

                    @Override
                    public void onError(ResponseModel data) {
                        mView.hideLoading();
                        CommonToast.toast(data.getMsg());
                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                        mView.hideLoading();
                        CommonToast.toast(data.getMsg());
                    }
                });
    }

}
