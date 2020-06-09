package com.fjx.mg.me.transfer;

import com.library.common.utils.CommonToast;
import com.library.common.utils.StringUtil;
import com.library.repository.core.net.CommonObserver;
import com.library.repository.core.net.RxScheduler;
import com.library.repository.models.ResponseModel;
import com.library.repository.repository.RepositoryFactory;

public class MeTransferPresenter extends MeTransferContact.Presenter {

    public MeTransferPresenter(MeTransferContact.View view) {
        super(view);
    }

    @Override
    void checkPrice(String price) {
        mView.createAndShowDialog();
        RepositoryFactory.getRemotePayRepository().checkMoneyLimit("1", price)
                .compose(RxScheduler.<ResponseModel<Object>>toMain())
                .as(mView.<ResponseModel<Object>>bindAutoDispose())
                .subscribe(new CommonObserver<Object>() {
                    @Override
                    public void onSuccess(Object data) {
                        mView.checkSuccess();
                        mView.destoryAndDismissDialog();
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        mView.destoryAndDismissDialog();
                        if (StringUtil.isNotEmpty(data.getMsg())) {
                            CommonToast.toast(data.getMsg());
                        }
                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                        mView.destoryAndDismissDialog();
                        CommonToast.toast(data.getMsg());
                    }
                });
    }


}
