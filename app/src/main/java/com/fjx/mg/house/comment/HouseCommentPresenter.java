package com.fjx.mg.house.comment;

import android.text.TextUtils;

import com.fjx.mg.R;
import com.library.common.utils.CommonToast;
import com.library.common.utils.StringUtil;
import com.library.repository.core.net.CommonObserver;
import com.library.repository.core.net.RxScheduler;
import com.library.repository.models.CommentReplyModel;
import com.library.repository.models.NewsCommentModel;
import com.library.repository.models.ResponseModel;
import com.library.repository.repository.RepositoryFactory;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnConfirmListener;

public class HouseCommentPresenter extends HouseCommentContract.Presenter {
    private boolean commitComment;

    public HouseCommentPresenter(HouseCommentContract.View view) {
        super(view);
    }

    @Override
    void addReply(String newsId, String content, String uid, String replyId) {
        if (commitComment) return;
        if (TextUtils.isEmpty(content)) {
            CommonToast.toast(mView.getCurContext().getString(R.string.hint_input_comment));
            return;
        }
        mView.showLoading();
        commitComment = true;
        RepositoryFactory.getRemoteJobApi()
                .addReply(newsId, content, uid, replyId)
                .compose(RxScheduler.<ResponseModel<Object>>toMain())
                .as(mView.<ResponseModel<Object>>bindAutoDispose())
                .subscribe(new CommonObserver<Object>() {
                    @Override
                    public void onSuccess(Object data) {
                        commitComment = false;
                        mView.hideLoading();
                        mView.commentSuccess();
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        commitComment = false;
                        mView.hideLoading();
                    }

                    @Override
                    public void onNetError(ResponseModel data) {

                    }
                });
    }

    @Override
    void getReplyList(String commentId, int page) {
        RepositoryFactory.getRemoteJobApi()
                .replyList(commentId, page)
                .compose(RxScheduler.<ResponseModel<CommentReplyModel>>toMain())
                .as(mView.<ResponseModel<CommentReplyModel>>bindAutoDispose())
                .subscribe(new CommonObserver<CommentReplyModel>() {
                    @Override
                    public void onSuccess(CommentReplyModel data) {
                        if (mView == null) return;
                        mView.showReplyList(data);
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        if (mView == null) return;
                        mView.hideLoading();
                    }

                    @Override
                    public void onNetError(ResponseModel data) {

                    }
                });
    }


    @Override
    void deleteReply(String replyId) {
        RepositoryFactory.getRemoteJobApi().delReply(replyId)
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
                        CommonToast.toast(data.getMsg());
                    }

                    @Override
                    public void onNetError(ResponseModel data) {

                    }
                });
    }

    @Override
    void deleteComment(String commentId) {
        RepositoryFactory.getRemoteJobApi().delComment(commentId)
                .compose(RxScheduler.<ResponseModel<Object>>toMain())
                .as(mView.<ResponseModel<Object>>bindAutoDispose())
                .subscribe(new CommonObserver<Object>() {
                    @Override
                    public void onSuccess(Object data) {
                        mView.hideLoading();
                        mView.deleteCommentSuccese();
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

    @Override
    void delete(final String id, final boolean isReply) {
        new XPopup.Builder(mView.getCurContext()).asConfirm(mView.getCurContext().getString(R.string.Tips),
                mView.getCurContext().getString(R.string.delete_confirm_message), new OnConfirmListener() {
                    @Override
                    public void onConfirm() {
                        if (isReply) {
                            deleteReply(id);
                        } else {
                            deleteComment(id);
                        }
                    }
                }).show();


    }
}
