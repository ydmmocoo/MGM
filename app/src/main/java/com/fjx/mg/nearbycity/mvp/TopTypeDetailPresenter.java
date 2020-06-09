package com.fjx.mg.nearbycity.mvp;

import com.library.repository.core.net.CommonObserver;
import com.library.repository.core.net.RxScheduler;
import com.library.repository.models.NearbyCItyGetListModel;
import com.library.repository.models.ResponseModel;
import com.library.repository.repository.RepositoryFactory;

/**
 * Author    by hanlz
 * Date      on 2019/10/18.
 * Description：
 */
public class TopTypeDetailPresenter extends TopTypeDetailContract.Presenter {

    public TopTypeDetailPresenter(TopTypeDetailContract.View view) {
        super(view);
    }


    @Override
    public void requestNearbyCityList(String page, String k, String typeId) {
        RepositoryFactory.getRemoteNearbyCitysApi()
                .getList(page, k, typeId)
                .compose(RxScheduler.<ResponseModel<NearbyCItyGetListModel>>toMain())
                .as(mView.<ResponseModel<NearbyCItyGetListModel>>bindAutoDispose())
                .subscribe(new CommonObserver<NearbyCItyGetListModel>() {
                    @Override
                    public void onSuccess(NearbyCItyGetListModel data) {
                        if (mView != null) {
                            mView.responseNearbyCityList(data);
                        }
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        if (mView != null) {
                            mView.responseFailed(null);
                        }
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        if (mView != null) {
                            mView.responseFailed(data);
                        }
                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                        if (mView!=null){
                            mView.responseFailed(data);
                        }
                    }
                });
    }


    public void requestPraise(String commentId, String replyId, String cId) {
        if (mView != null) {
            mView.createAndShowDialog();
        }
        RepositoryFactory.getRemoteNearbyCitysApi()
                .praise(commentId, replyId, cId)
                .compose(RxScheduler.<ResponseModel<Object>>toMain())
                .as(mView.<ResponseModel<Object>>bindAutoDispose())
                .subscribe(new CommonObserver<Object>() {
                    @Override
                    public void onSuccess(Object data) {
                        if (mView != null) {
                            mView.responsePraise();
                            mView.destoryAndDismissDialog();
                        }
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        if (mView != null) {
                            mView.destoryAndDismissDialog();
                            mView.responseFailed(data);
                        }
                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                        if (mView != null) {
                            mView.destoryAndDismissDialog();
                            mView.responseFailed(data);
                        }
                    }
                });
    }

    @Override
    public void requestCancelPraise(String commentId, String replyId, String cId) {
        if (mView != null) {
            mView.createAndShowDialog();
        }
        RepositoryFactory.getRemoteNearbyCitysApi()
                .cancelPraise(commentId, replyId, cId)
                .compose(RxScheduler.<ResponseModel<Object>>toMain())
                .as(mView.<ResponseModel<Object>>bindAutoDispose())
                .subscribe(new CommonObserver<Object>() {
                    @Override
                    public void onSuccess(Object data) {
                        if (mView != null) {
                            mView.responseCancelPraise();
                            mView.destoryAndDismissDialog();
                        }
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        if (mView != null) {
                            mView.destoryAndDismissDialog();
                            mView.responseFailed(data);
                        }
                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                        if (mView != null) {
                            mView.destoryAndDismissDialog();
                            mView.responseFailed(data);
                        }
                    }
                });
    }


}
