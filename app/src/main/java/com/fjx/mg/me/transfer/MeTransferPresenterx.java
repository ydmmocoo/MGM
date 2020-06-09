package com.fjx.mg.me.transfer;

import com.library.common.utils.CommonToast;
import com.library.common.utils.StringUtil;
import com.library.repository.core.net.CommonObserver;
import com.library.repository.core.net.RxScheduler;
import com.library.repository.models.ResponseModel;
import com.library.repository.repository.RepositoryFactory;

import java.lang.ref.WeakReference;

public class MeTransferPresenterx extends MeTransferContactx.Presenter {
    WeakReference<MeTransferContactx.View> weakReference;

    public MeTransferPresenterx(MeTransferContactx.View view) {
        super(view);
        weakReference = new WeakReference<>(view);
    }

    @Override
    void checkPrice(String price) {
        final MeTransferContactx.View view = weakReference.get() == null ? mView : weakReference.get();
        if (view == null) return;
        view.createAndShowDialog();
        RepositoryFactory.getRemotePayRepository().checkMoneyLimit("1", price)
                .compose(RxScheduler.<ResponseModel<Object>>toMain())
                .as(mView.<ResponseModel<Object>>bindAutoDispose())
                .subscribe(new CommonObserver<Object>() {
                    @Override
                    public void onSuccess(Object data) {
                        view.checkSuccess();
                        view.destoryAndDismissDialog();
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        view.destoryAndDismissDialog();
                        if (StringUtil.isNotEmpty(data.getMsg())) {
                            CommonToast.toast(data.getMsg());
                        }
                    }

                    @Override
                    public void onNetError(ResponseModel data) {

                    }
                });
    }

    @Override
    void checkSameMoney(String toUid, String money) {
        final MeTransferContactx.View view = weakReference.get() == null ? mView : weakReference.get();
        if (view == null) return;
        view.showLoading();
        RepositoryFactory.getRemotePayRepository().checkSameMoney(toUid, money)
                .compose(RxScheduler.<ResponseModel<Object>>toMain())
                .as(mView.<ResponseModel<Object>>bindAutoDispose())
                .subscribe(new CommonObserver<Object>() {
                    @Override
                    public void onSuccess(Object data) {
                        view.hideLoading();
                        view.sameMoney(false,"");
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        view.hideLoading();
                        view.sameMoney(true,data.getMsg());
//                        if (StringUtil.isNotEmpty(data.getMsg())) {
//                            CommonToast.toast(data.getMsg());
//                        }
                    }

                    @Override
                    public void onNetError(ResponseModel data) {

                    }
                });
    }


}
