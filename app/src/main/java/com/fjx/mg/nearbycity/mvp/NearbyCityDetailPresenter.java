package com.fjx.mg.nearbycity.mvp;

import com.fjx.mg.R;
import com.library.common.utils.CommonToast;
import com.library.common.utils.StringUtil;
import com.library.repository.core.net.CommonObserver;
import com.library.repository.core.net.RxScheduler;
import com.library.repository.data.UserCenter;
import com.library.repository.models.NearbyCityInfoDetailModel;
import com.library.repository.models.ResponseModel;
import com.library.repository.repository.RepositoryFactory;

public class NearbyCityDetailPresenter extends NearbyCityDetailContract.Presenter {

    public NearbyCityDetailPresenter(NearbyCityDetailContract.View view) {
        super(view);
    }

    @Override
    public void requestDetails(String cId) {
        RepositoryFactory.getRemoteNearbyCitysApi()
                .getInfo(cId)
                .compose(RxScheduler.<ResponseModel<NearbyCityInfoDetailModel>>toMain())
                .as(mView.<ResponseModel<NearbyCityInfoDetailModel>>bindAutoDispose())
                .subscribe(new CommonObserver<NearbyCityInfoDetailModel>() {
                    @Override
                    public void onSuccess(NearbyCityInfoDetailModel data) {
                        if (mView != null) {
                            if (data != null) {
                                mView.responseDetails(data);
                            }
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
                        if (mView != null) {
                            mView.responseFailed(data);
                        }
                    }
                });
    }


    @Override
    public void addComment(String cId, String content) {
        if (StringUtil.isEmpty(content)) {
            CommonToast.toast(mView.getCurActivity().getString(R.string.hint_input_comment));
            return;
        }
        RepositoryFactory.getRemoteNearbyCitysApi()
                .addComment(cId, content)
                .compose(RxScheduler.<ResponseModel<Object>>toMain())
                .as(mView.<ResponseModel<Object>>bindAutoDispose())
                .subscribe(new CommonObserver<Object>() {
                    @Override
                    public void onSuccess(Object data) {
                        if (mView != null) {
                            mView.addCommentSuccess();
                        }
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        if (mView != null) {
                            mView.responseFailed(data);
                            mView.addCommentFailed();
                        }
                        CommonToast.toast(data.getMsg());
                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                        if (mView != null) {
                            mView.responseFailed(data);
                            mView.addCommentFailed();
                        }
                        CommonToast.toast(data.getMsg());
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        if (mView!=null){
                            mView.addCommentFailed();
                        }
                    }
                });
    }

    @Override
    public void requestPraise(String commentId, String replyId, String cId) {
        if (mView != null) {
            mView.showLoading();
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
                            mView.hideLoading();
                        }
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        if (mView != null) {
                            mView.hideLoading();
                            mView.responseFailed(data);
                        }
                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                        if (mView != null) {
                            mView.hideLoading();
                            mView.responseFailed(data);
                        }
                    }
                });
    }

    @Override
    public void requestCancelPraise(String commentId, String replyId, String cId) {
        RepositoryFactory.getRemoteNearbyCitysApi()
                .cancelPraise(commentId, replyId, cId)
                .compose(RxScheduler.<ResponseModel<Object>>toMain())
                .subscribe(new CommonObserver<Object>() {
                    @Override
                    public void onSuccess(Object data) {
                        if (mView != null) {
                            mView.hideLoading();
                            mView.responseCancelPraise();
                        }


                    }

                    @Override
                    public void onError(ResponseModel data) {
                        if (mView != null) {
                            mView.hideLoading();
                        }
                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                        if (mView != null) {
                            mView.hideLoading();
                        }
                    }
                });
    }

    @Override
    public void requestUpdateReadNum(String cId) {
        RepositoryFactory.getRemoteNearbyCitysApi()
                .updateReadNum(cId)
                .compose(RxScheduler.<ResponseModel<Object>>toMain())
                .subscribe();
    }


}
