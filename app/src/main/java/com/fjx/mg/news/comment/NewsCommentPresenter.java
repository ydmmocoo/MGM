package com.fjx.mg.news.comment;

import android.text.TextUtils;

import com.fjx.mg.R;
import com.library.common.utils.CommonToast;
import com.library.common.utils.NetworkUtil;
import com.library.common.utils.StringUtil;
import com.library.repository.core.net.CommonObserver;
import com.library.repository.core.net.RxScheduler;
import com.library.repository.models.CommentReplyModel;
import com.library.repository.models.NewsCommentModel;
import com.library.repository.models.ResponseModel;
import com.library.repository.repository.RepositoryFactory;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnConfirmListener;

public class NewsCommentPresenter extends NewsCommentContract.Presenter {
    private boolean commitComment;

    public NewsCommentPresenter(NewsCommentContract.View view) {
        super(view);
    }

    @Override
    void addReply(String newsId, String content, String uid, String replyId) {
        if (commitComment) return;
        if (TextUtils.isEmpty(content)) {
            CommonToast.toast(mView.getCurContext().getString(R.string.hint_input_comment));
            return;
        }
        commitComment = true;
        RepositoryFactory.getRemoteNewsRepository()
                .addReply(newsId, content, uid, replyId)
                .compose(RxScheduler.<ResponseModel<Object>>toMain())
                .as(mView.<ResponseModel<Object>>bindAutoDispose())
                .subscribe(new CommonObserver<Object>() {
                    @Override
                    public void onSuccess(Object data) {
                        commitComment = false;
                        if (mView != null)
                            mView.commentSuccess();
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        commitComment = false;
                        if (mView != null) {
                            if (new NetworkUtil().isNetworkConnected(mView.getCurContext()))
                                mView.commentFailed();
                        }
                        CommonToast.toast(data.getMsg());
                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                        if (mView != null) {
                            if (new NetworkUtil().isNetworkConnected(mView.getCurContext()))
                                mView.commentFailed();
                        }
                        CommonToast.toast(data.getMsg());
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        if (mView != null) {
                            mView.responseFailed();
                            if (new NetworkUtil().isNetworkConnected(mView.getCurContext()))
                                mView.commentFailed();
                        }
                    }
                });
    }

    @Override
    void getReplyList(String commentId, int page) {
        RepositoryFactory.getRemoteNewsRepository()
                .replyList(commentId, page)
                .compose(RxScheduler.<ResponseModel<CommentReplyModel>>toMain())
                .as(mView.<ResponseModel<CommentReplyModel>>bindAutoDispose())
                .subscribe(new CommonObserver<CommentReplyModel>() {
                    @Override
                    public void onSuccess(CommentReplyModel data) {
                        mView.showReplyList(data);
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

    @Override
    void topicDetail(String commentId) {
        mView.showLoading();
        RepositoryFactory.getRemoteNewsRepository()
                .topicDetail(commentId)
                .compose(RxScheduler.<ResponseModel<NewsCommentModel.CommentListBean>>toMain())
                .as(mView.<ResponseModel<NewsCommentModel.CommentListBean>>bindAutoDispose())
                .subscribe(new CommonObserver<NewsCommentModel.CommentListBean>() {
                    @Override
                    public void onSuccess(NewsCommentModel.CommentListBean data) {
                        mView.showTopicDetail(data);
                        mView.hideLoading();
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
                        if (StringUtil.isNotEmpty(data.getMsg())) {
                            CommonToast.toast(data.getMsg());
                        }
                    }
                });
    }


    @Override
    void toggleCommentPraise(final NewsCommentModel.CommentListBean item) {
        mView.showLoading();
        String commentId = item.getComentId();
        if (item.isLike()) {
            RepositoryFactory.getRemoteNewsRepository().cancelPraise(commentId, "")
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
                            if (StringUtil.isNotEmpty(data.getMsg())) {
                                CommonToast.toast(data.getMsg());
                            }
                        }

                        @Override
                        public void onNetError(ResponseModel data) {
                            mView.hideLoading();
                            if (StringUtil.isNotEmpty(data.getMsg())) {
                                CommonToast.toast(data.getMsg());
                            }
                        }
                    });
        } else {
            RepositoryFactory.getRemoteNewsRepository().praiseComment(commentId, "")
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
                            if (StringUtil.isNotEmpty(data.getMsg())) {
                                CommonToast.toast(data.getMsg());
                            }
                        }

                        @Override
                        public void onNetError(ResponseModel data) {
                            mView.hideLoading();
                            if (StringUtil.isNotEmpty(data.getMsg())) {
                                CommonToast.toast(data.getMsg());
                            }
                        }
                    });
        }
    }

    @Override
    void toggleReplyPraise(final int position, final CommentReplyModel.ReplyListBean item) {
        String replyId = item.getReplyId();
        if (item.isLike()) {
            RepositoryFactory.getRemoteNewsRepository().cancelPraise("", replyId)
                    .compose(RxScheduler.<ResponseModel<Object>>toMain())
                    .as(mView.<ResponseModel<Object>>bindAutoDispose())
                    .subscribe(new CommonObserver<Object>() {
                        @Override
                        public void onSuccess(Object data) {
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
                            CommonToast.toast(data.getMsg());
                        }

                        @Override
                        public void onNetError(ResponseModel data) {
                            CommonToast.toast(data.getMsg());
                        }
                    });
        } else {
            RepositoryFactory.getRemoteNewsRepository().praiseComment("", replyId)
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
                        }
                    });
        }
    }

    @Override
    void deleteReply(String replyId) {
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
                    }
                });
    }

    @Override
    void deleteComment(String commentId) {
        RepositoryFactory.getRemoteNewsRepository().delComment(commentId)
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
    void delete(final String id, final boolean isReply) {
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
