package com.fjx.mg.friend.notice.sys;

import com.library.repository.core.net.CommonObserver;
import com.library.repository.core.net.RxScheduler;
import com.library.repository.models.IMNoticeListModel;
import com.library.repository.models.ResponseModel;
import com.library.repository.repository.RepositoryFactory;

public class SysNoticePresenter extends SysNoticeContract.Presenter {

    public SysNoticePresenter(SysNoticeContract.View view) {
        super(view);
    }

    @Override
    void getNoticeList(int page) {

        RepositoryFactory.getRemotePayRepository().payNoticeList(page, 1)
                .compose(RxScheduler.<ResponseModel<IMNoticeListModel>>toMain())
                .as(mView.<ResponseModel<IMNoticeListModel>>bindAutoDispose())
                .subscribe(new CommonObserver<IMNoticeListModel>() {
                    @Override
                    public void onSuccess(IMNoticeListModel data) {
                        mView.showNoticeList(data);
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        mView.loadError();
                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                        mView.loadError();
                    }
                });
    }


}
