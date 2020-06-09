package com.fjx.mg.me.comment.fragment;

import com.fjx.mg.R;
import com.library.common.utils.CommonToast;
import com.library.common.utils.StringUtil;
import com.library.repository.core.net.CommonObserver;
import com.library.repository.core.net.RxScheduler;
import com.library.repository.models.MyCommentModel;
import com.library.repository.models.MyCompanyCommentModel;
import com.library.repository.models.MyHouseCommentModel;
import com.library.repository.models.MyNewsCommentModel;
import com.library.repository.models.MyReplyListCommentModel;
import com.library.repository.models.ResponseModel;
import com.library.repository.repository.RepositoryFactory;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnConfirmListener;

public class MyCommentFragmentPresenter extends MyCommentFragmentContract.Presenter {

    MyCommentFragmentPresenter(MyCommentFragmentContract.View view) {
        super(view);
    }

    @Override
    void getMyHouseComment(int page) {

        RepositoryFactory.getRemoteJobApi().myHouseComment(page)
                .compose(RxScheduler.<ResponseModel<MyHouseCommentModel>>toMain())
                .as(mView.<ResponseModel<MyHouseCommentModel>>bindAutoDispose())
                .subscribe(new CommonObserver<MyHouseCommentModel>() {
                    @Override
                    public void onSuccess(MyHouseCommentModel data) {
                        mView.showMyHouseCommentModel(data);
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

    @Override
    void getMyNewsComment(int page) {

        RepositoryFactory.getRemoteNewsRepository().myNewsComment(page)
                .compose(RxScheduler.<ResponseModel<MyNewsCommentModel>>toMain())
                .as(mView.<ResponseModel<MyNewsCommentModel>>bindAutoDispose())
                .subscribe(new CommonObserver<MyNewsCommentModel>() {
                    @Override
                    public void onSuccess(MyNewsCommentModel data) {
                        mView.showMyNewsCommentModel(data);

                    }

                    @Override
                    public void onError(ResponseModel data) {
                        mView.loadError();
                    }

                    @Override
                    public void onNetError(ResponseModel data) {

                    }
                });

    }

    @Override
    void deletY(final String id, final boolean isReply) {
        new XPopup.Builder(mView.getCurContext()).asConfirm(mView.getCurActivity().getString(R.string.tips),
                mView.getCurContext().getString(R.string.delete_confirm_message), new OnConfirmListener() {
                    @Override
                    public void onConfirm() {
                        deleteReplyY(id);

                    }
                }).show();


    }

    @Override
    void deletQ(final String id) {
        new XPopup.Builder(mView.getCurContext()).asConfirm(mView.getCurActivity().getString(R.string.tips),
                mView.getCurContext().getString(R.string.delete_confirm_message), new OnConfirmListener() {
                    @Override
                    public void onConfirm() {
                        deleteReplyQ(id);

                    }
                }).show();


    }

    @Override
    void deleteh(final String id, final boolean isReply) {
        new XPopup.Builder(mView.getCurContext()).asConfirm(mView.getCurContext().getString(R.string.Tips),
                mView.getCurContext().getString(R.string.delete_confirm_message), new OnConfirmListener() {
                    @Override
                    public void onConfirm() {
                        deleteReply(id);

                    }
                }).show();


    }

    @Override
    void deleteN(final String id, final boolean isReply) {
        new XPopup.Builder(mView.getCurContext()).asConfirm(mView.getCurActivity().getString(R.string.tips),
                mView.getCurContext().getString(R.string.delete_confirm_message), new OnConfirmListener() {
                    @Override
                    public void onConfirm() {
                        deleteReplyN(id);

                    }
                }).show();


    }

    void deleteReplyY(String replyId) {//删除黄页回复
        RepositoryFactory.getRemoteNewsRepository().companydelComment(replyId)
                .compose(RxScheduler.<ResponseModel<Object>>toMain())
                .as(mView.<ResponseModel<Object>>bindAutoDispose())
                .subscribe(new CommonObserver<Object>() {
                    @Override
                    public void onSuccess(Object data) {
                        mView.hideLoading();
                        mView.deleteReplySuccess();
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        mView.hideLoading();
                        if (StringUtil.isNotEmpty(data.getMsg())) {
                            CommonToast.toast(data.getMsg());
                        }
                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                        mView.hideLoading();
                        CommonToast.toast(data.getMsg());
                    }
                });
    }

    private void deleteReplyQ(String replyId) {//删除有偿问答回复
        RepositoryFactory.getRemoteNewsRepository().delQuestionReply(replyId)
                .compose(RxScheduler.<ResponseModel<Object>>toMain())
                .as(mView.<ResponseModel<Object>>bindAutoDispose())
                .subscribe(new CommonObserver<Object>() {
                    @Override
                    public void onSuccess(Object data) {
                        mView.hideLoading();
                        mView.deleteReplySuccess();
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        mView.hideLoading();
                        if (StringUtil.isNotEmpty(data.getMsg())) {
                            CommonToast.toast(data.getMsg());
                        }
                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                        CommonToast.toast(data.getMsg());
                        if (mView != null) {
                            mView.hideLoading();
                        }
                    }
                });
    }

    void deleteReplyN(String replyId) {
        RepositoryFactory.getRemoteNewsRepository().delComment(replyId)
                .compose(RxScheduler.<ResponseModel<Object>>toMain())
                .as(mView.<ResponseModel<Object>>bindAutoDispose())
                .subscribe(new CommonObserver<Object>() {
                    @Override
                    public void onSuccess(Object data) {
                        mView.hideLoading();
                        mView.deleteReplySuccess();
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        mView.hideLoading();
                        if (StringUtil.isNotEmpty(data.getMsg())) {
                            CommonToast.toast(data.getMsg());
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

    private void deleteReply(String replyId) {
        RepositoryFactory.getRemoteJobApi().delComment(replyId)
                .compose(RxScheduler.<ResponseModel<Object>>toMain())
                .as(mView.<ResponseModel<Object>>bindAutoDispose())
                .subscribe(new CommonObserver<Object>() {
                    @Override
                    public void onSuccess(Object data) {
                        mView.hideLoading();
                        mView.deleteReplySuccess();
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        mView.hideLoading();
                        if (StringUtil.isNotEmpty(data.getMsg())) {
                            CommonToast.toast(data.getMsg());
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
    void getMyCompanyComment(int page) {

        RepositoryFactory.getRemoteJobApi().myConmpanyComment(page)
                .compose(RxScheduler.<ResponseModel<MyCompanyCommentModel>>toMain())
                .as(mView.<ResponseModel<MyCompanyCommentModel>>bindAutoDispose())
                .subscribe(new CommonObserver<MyCompanyCommentModel>() {
                    @Override
                    public void onSuccess(MyCompanyCommentModel data) {
                        mView.ShowMyCompanyComment(data);

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

    @Override
    void getMyQuestionComment(int page) {

        RepositoryFactory.getRemoteJobApi().myReplyList(page)
                .compose(RxScheduler.<ResponseModel<MyReplyListCommentModel>>toMain())
                .as(mView.<ResponseModel<MyReplyListCommentModel>>bindAutoDispose())
                .subscribe(new CommonObserver<MyReplyListCommentModel>() {
                    @Override
                    public void onSuccess(MyReplyListCommentModel data) {
                        mView.ShowMyReplyListComment(data);
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


    public void requestMyCommentList(String page) {
        RepositoryFactory.getRemoteNearbyCitysApi()
                .myComment(page)
                .compose(RxScheduler.<ResponseModel<MyCommentModel>>toMain())
                .as(mView.<ResponseModel<MyCommentModel>>bindAutoDispose())
                .subscribe(new CommonObserver<MyCommentModel>() {
                    @Override
                    public void onSuccess(MyCommentModel data) {
                        if (mView != null) {
                            mView.responseMyCommentList(data);
                        }
                    }

                    @Override
                    public void onError(ResponseModel data) {

                    }

                    @Override
                    public void onNetError(ResponseModel data) {

                    }
                });
    }

    public void delComment(String commentId) {
        RepositoryFactory.getRemoteNearbyCitysApi()
                .delComment(commentId)
                .compose(RxScheduler.<ResponseModel<Object>>toMain())
                .as(mView.<ResponseModel<Object>>bindAutoDispose())
                .subscribe(new CommonObserver<Object>() {
                    @Override
                    public void onSuccess(Object data) {
                        requestMyCommentList("1");
                    }

                    @Override
                    public void onError(ResponseModel data) {
                    }

                    @Override
                    public void onNetError(ResponseModel data) {

                    }
                });
    }

}
