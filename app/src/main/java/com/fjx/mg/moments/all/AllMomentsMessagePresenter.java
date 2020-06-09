package com.fjx.mg.moments.all;

import com.library.repository.core.net.CommonObserver;
import com.library.repository.core.net.RxScheduler;
import com.library.repository.models.MomentsInfoModel;
import com.library.repository.models.MomentsReplyListModel;
import com.library.repository.models.ResponseModel;
import com.library.repository.repository.RepositoryFactory;

/**
 * create by hanlz
 * 2019/9/23
 * Describe:用户朋友圈动态回复的消息
 */
public class AllMomentsMessagePresenter extends AllMomentsContract.MessagePersenter {

    public AllMomentsMessagePresenter(AllMomentsContract.MessageView view) {
        super(view);
    }

    @Override
    void MomentsReplyList(String page, String commentId, String isRead) {
//        mView.showLoading();
        RepositoryFactory.getRemoteRepository()
                .MomentsReplyList(page, commentId, isRead)
                .compose(RxScheduler.<ResponseModel<MomentsReplyListModel>>toMain())
                .as(mView.<ResponseModel<MomentsReplyListModel>>bindAutoDispose())
                .subscribe(new CommonObserver<MomentsReplyListModel>() {
                    @Override
                    public void onSuccess(MomentsReplyListModel data) {
                        mView.hideLoading();
                        if (mView != null && data != null) {
                            mView.showReplyList(data);
                        }
                    }

                    @Override
                    public void onError(ResponseModel data) {
//                        mView.hideLoading();
                    }

                    @Override
                    public void onNetError(ResponseModel data) {

                    }
                });
    }

    @Override
    void momentsRead() {
        RepositoryFactory.getRemoteRepository()
                .momentsRead()
                .compose(RxScheduler.<ResponseModel>toMain())
                .as(mView.<ResponseModel>bindAutoDispose())
                .subscribe();
    }

    @Override
    void MomentsInfo(final String mId, String lat, String lng, final String page) {
        mView.createAndShowDialog();
        RepositoryFactory.getRemoteRepository()
                .MomentsInfo(mId, lat, lng)
                .compose(RxScheduler.<ResponseModel<MomentsInfoModel>>toMain())
                .as(mView.<ResponseModel<MomentsInfoModel>>bindAutoDispose())
                .subscribe(new CommonObserver<MomentsInfoModel>() {
                    @Override
                    public void onSuccess(MomentsInfoModel data) {
                        mView.destoryAndDismissDialog();
                        if (mView != null && data != null) {
                            mView.showInfo(data);
                            MomentsReplyList(page, "", "2");
                        }
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        mView.hideLoading();
                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                        mView.hideLoading();
                    }
                });
    }


}
