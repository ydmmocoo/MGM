package com.fjx.mg.nearbycity.mvp;

import android.text.TextUtils;

import com.fjx.mg.R;
import com.library.common.utils.CommonToast;
import com.library.common.utils.NetworkUtil;
import com.library.common.utils.StringUtil;
import com.library.repository.core.net.CommonObserver;
import com.library.repository.core.net.RxScheduler;
import com.library.repository.models.CommentReplyModel;
import com.library.repository.models.NearbyCityCommentListModel;
import com.library.repository.models.ResponseModel;
import com.library.repository.repository.RepositoryFactory;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnConfirmListener;

public class NearbyCityCommentPresenter extends NearbyCityCommentContract.Presenter {
    private boolean commitComment;

    public NearbyCityCommentPresenter(NearbyCityCommentContract.View view) {
        super(view);
    }

    @Override
    public void addReply(String newsId, String content, String uid, String replyId) {
        if (commitComment) return;
        if (TextUtils.isEmpty(content)) {
            CommonToast.toast(mView.getCurContext().getString(R.string.hint_input_comment));
            return;
        }
        commitComment = true;
        RepositoryFactory.getRemoteNearbyCitysApi()
                .addReply(newsId, content, uid, replyId)
                .compose(RxScheduler.<ResponseModel<Object>>toMain())
                .as(mView.<ResponseModel<Object>>bindAutoDispose())
                .subscribe(new CommonObserver<Object>() {
                    @Override
                    public void onSuccess(Object data) {
                        commitComment = false;
                        if (mView != null) {
                            mView.commentSuccess();
                        }
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        commitComment = false;
                        CommonToast.toast(data.getMsg());
                        if (mView != null) {
                            if (new NetworkUtil().isNetworkConnected(mView.getCurContext()))
                                mView.commentFailed();
                        }
                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                        if (mView != null) {
                            if (new NetworkUtil().isNetworkConnected(mView.getCurContext()))
                                mView.commentFailed();
                        }
                    }
                });
    }

    @Override
    public void getReplyList(String commentId, int page) {
        RepositoryFactory.getRemoteNearbyCitysApi()
                .replyList(commentId, page)
                .compose(RxScheduler.<ResponseModel<CommentReplyModel>>toMain())
                .as(mView.<ResponseModel<CommentReplyModel>>bindAutoDispose())
                .subscribe(new CommonObserver<CommentReplyModel>() {
                    @Override
                    public void onSuccess(CommentReplyModel data) {
                        mView.showReplyList(data);
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
    public void topicDetail(String commentId) {
        mView.showLoading();
        RepositoryFactory.getRemoteNearbyCitysApi()
                .topicDetail(commentId)
                .compose(RxScheduler.<ResponseModel<NearbyCityCommentListModel>>toMain())
                .as(mView.<ResponseModel<NearbyCityCommentListModel>>bindAutoDispose())
                .subscribe(new CommonObserver<NearbyCityCommentListModel>() {
                    @Override
                    public void onSuccess(NearbyCityCommentListModel data) {
                        mView.showTopicDetail(data);
                        mView.hideLoading();
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


    @Override
    public void toggleCommentPraise(final NearbyCityCommentListModel item, String cId) {
        mView.showLoading();
        String commentId = item.getComentId();
        if (item.isLike()) {
            RepositoryFactory.getRemoteNearbyCitysApi().cancelPraise(commentId, "", cId)
                    .compose(RxScheduler.<ResponseModel<Object>>toMain())
                    .as(mView.<ResponseModel<Object>>bindAutoDispose())
                    .subscribe(new CommonObserver<Object>() {
                        @Override
                        public void onSuccess(Object data) {
                            mView.hideLoading();
                            CommonToast.toast(mView.getCurContext().getString(R.string.cancelPraise));
                            item.setLike(false);
                            String likeNum = item.getLikeNum();
                            if (StringUtil.isNum(likeNum)) {
                                int num = Integer.parseInt(likeNum);
                                num--;
                                item.setLikeNum(String.valueOf(num));
                            }
                            mView.toggleCommentPraiseSuccess();
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
        } else {
            RepositoryFactory.getRemoteNearbyCitysApi().praise(commentId, "", cId)
                    .compose(RxScheduler.<ResponseModel<Object>>toMain())
                    .as(mView.<ResponseModel<Object>>bindAutoDispose())
                    .subscribe(new CommonObserver<Object>() {
                        @Override
                        public void onSuccess(Object data) {
                            mView.hideLoading();
                            CommonToast.toast(mView.getCurContext().getString(R.string.praise_success));
                            item.setLike(true);
                            String likeNum = item.getLikeNum();
                            if (StringUtil.isNum(likeNum)) {
                                int num = Integer.parseInt(likeNum);
                                num++;
                                item.setLikeNum(String.valueOf(num));
                            }
                            mView.toggleCommentPraiseSuccess();
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

    @Override
    public void toggleReplyPraise(final int position, final CommentReplyModel.ReplyListBean item) {
        mView.showLoading();
        String replyId = item.getReplyId();
        if (item.isLike()) {
            RepositoryFactory.getRemoteNearbyCitysApi().cancelPraise("", replyId, "")
                    .compose(RxScheduler.<ResponseModel<Object>>toMain())
                    .as(mView.<ResponseModel<Object>>bindAutoDispose())
                    .subscribe(new CommonObserver<Object>() {
                        @Override
                        public void onSuccess(Object data) {
                            mView.hideLoading();
                            CommonToast.toast(mView.getCurContext().getString(R.string.cancelPraise));
                            item.setLike(false);

                            String likeNum = item.getLikeNum();
                            if (StringUtil.isNum(likeNum)) {
                                int num = Integer.parseInt(likeNum);
                                num--;
                                item.setLikeNum(String.valueOf(num));
                            }
                            mView.toggleReplyPraiseSuccess(position);
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
        } else {
            RepositoryFactory.getRemoteNearbyCitysApi().praise("", replyId, "")
                    .compose(RxScheduler.<ResponseModel<Object>>toMain())
                    .as(mView.<ResponseModel<Object>>bindAutoDispose())
                    .subscribe(new CommonObserver<Object>() {
                        @Override
                        public void onSuccess(Object data) {
                            mView.hideLoading();
                            CommonToast.toast(mView.getCurContext().getString(R.string.praise_success));
                            item.setLike(true);
                            String likeNum = item.getLikeNum();
                            if (StringUtil.isNum(likeNum)) {
                                int num = Integer.parseInt(likeNum);
                                num++;
                                item.setLikeNum(String.valueOf(num));
                            }
                            mView.toggleReplyPraiseSuccess(position);
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

    @Override
    public void deleteReply(String replyId) {
        RepositoryFactory.getRemoteNewsRepository().delReply(replyId)
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
                        mView.hideLoading();
                        CommonToast.toast(data.getMsg());
                    }
                });
    }

    @Override
    public void deleteComment(String commentId) {
        RepositoryFactory.getRemoteNearbyCitysApi().delComment(commentId)
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
                        mView.hideLoading();
                        CommonToast.toast(data.getMsg());
                    }
                });
    }

    @Override
    public void delete(final String id, final boolean isReply) {
        new XPopup.Builder(mView.getCurContext()).asConfirm(mView.getCurActivity().getString(R.string.tips),
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
