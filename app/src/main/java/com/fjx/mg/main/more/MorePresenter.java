package com.fjx.mg.main.more;

import com.library.common.utils.CommonToast;
import com.library.repository.core.net.CommonObserver;
import com.library.repository.core.net.RxScheduler;
import com.library.repository.models.RecAppListModel;
import com.library.repository.models.ResponseModel;
import com.library.repository.repository.RepositoryFactory;

public class MorePresenter extends MoreContract.Presenter {
    MorePresenter(MoreContract.View view) {
        super(view);
    }

    @Override
    void recUseApp(final String appId) {
        RepositoryFactory.getRemoteNewsRepository()
                .recUseApp(appId)
                .compose(RxScheduler.<ResponseModel<Object>>toMain())
                .as(mView.<ResponseModel<Object>>bindAutoDispose())
                .subscribe(new CommonObserver<Object>() {
                    @Override
                    public void onSuccess(Object data) {
                        mView.showUsed(appId);
                    }

                    @Override
                    public void onError(ResponseModel data) {
                    }

                    @Override
                    public void onNetError(ResponseModel data) {

                    }
                });
    }

    @Override
    void getRecAppList() {
        RepositoryFactory.getRemoteRepository()
                .recAppList()
                .compose(RxScheduler.<ResponseModel<RecAppListModel>>toMain())
                .as(mView.<ResponseModel<RecAppListModel>>bindAutoDispose())
                .subscribe(new CommonObserver<RecAppListModel>() {
                    @Override
                    public void onSuccess(RecAppListModel model) {
                        mView.showUsed(model);
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        CommonToast.toast(data.getMsg());
                    }

                    @Override
                    public void onNetError(ResponseModel data) {

                    }
                });
    }
}
