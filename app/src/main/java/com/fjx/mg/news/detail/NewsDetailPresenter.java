package com.fjx.mg.news.detail;

import androidx.core.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.fjx.mg.R;
import com.library.common.utils.CommonToast;
import com.library.common.utils.NetworkUtil;
import com.library.common.utils.StringUtil;
import com.library.common.utils.ViewUtil;
import com.library.repository.core.net.CommonObserver;
import com.library.repository.core.net.RxScheduler;
import com.library.repository.data.UserCenter;
import com.library.repository.db.DBDaoFactory;
import com.library.repository.models.AdListModel;
import com.library.repository.models.NewsCommentModel;
import com.library.repository.models.NewsDetailModel;
import com.library.repository.models.NewsRecommendReadModel;
import com.library.repository.models.ResponseModel;
import com.library.repository.repository.RepositoryFactory;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.enums.PopupAnimation;
import com.lxj.xpopup.interfaces.OnConfirmListener;
import com.lxj.xpopup.interfaces.OnSelectListener;

public class NewsDetailPresenter extends NewsDetailContract.Presenter {
    private boolean commitComment;

    public NewsDetailPresenter(NewsDetailContract.View view) {
        super(view);
    }

    @Override
    void getNewsDetail(String newsId) {
        String userId = "";
        if (UserCenter.hasLogin()) {
            userId = UserCenter.getUserInfo().getUId();
        }
        if (mView == null) return;
        RepositoryFactory.getRemoteNewsRepository()
                .newsDetail(newsId, userId)
                .compose(RxScheduler.<ResponseModel<NewsDetailModel>>toMain())
                .as(mView.<ResponseModel<NewsDetailModel>>bindAutoDispose())
                .subscribe(new CommonObserver<NewsDetailModel>() {
                    @Override
                    public void onSuccess(NewsDetailModel data) {
                        if (data == null) return;
                        DBDaoFactory.getNewsDetailDao().insertModel(data);
                        if (data != null && mView != null) {
                            mView.showNewsDetail(data);
                        }
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
    void getCommentList(String newsId, int page) {
        if (mView == null) {
            return;
        }
        RepositoryFactory.getRemoteNewsRepository()
                .commentList(newsId, page)
                .compose(RxScheduler.<ResponseModel<NewsCommentModel>>toMain())
                .as(mView.<ResponseModel<NewsCommentModel>>bindAutoDispose())
                .subscribe(new CommonObserver<NewsCommentModel>() {
                    @Override
                    public void onSuccess(NewsCommentModel data) {
                        if (mView == null) return;
                        mView.showCommentList(data);
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        if (mView != null) {
                            mView.loadError();
                            CommonToast.toast(data.getMsg());
                        }
                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                        if (mView != null) {
                            CommonToast.toast(data.getMsg());
                            mView.loadError();
                        }
                    }
                });
    }

    @Override
    void addComment(String newsId, String content) {
        if (commitComment) return;
        if (TextUtils.isEmpty(content)) {
            CommonToast.toast(mView.getCurContext().getString(R.string.hint_input_comment));
            return;
        }
        if (mView==null)return;
        commitComment = true;
        RepositoryFactory.getRemoteNewsRepository()
                .addComment(newsId, content)
                .compose(RxScheduler.<ResponseModel<Object>>toMain())
                .as(mView.<ResponseModel<Object>>bindAutoDispose())
                .subscribe(new CommonObserver<Object>() {
                    @Override
                    public void onSuccess(Object data) {
                        commitComment = false;
                        mView.commentSuccess();
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        commitComment = false;
                        CommonToast.toast(data.getMsg());
                        if (mView != null) {
                            if (new NetworkUtil().isNetworkConnected(mView.getCurActivity())) {
                                mView.commentFail();
                            }
                        }
                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                        CommonToast.toast(data.getMsg());
                        if (mView != null) {
                            if (new NetworkUtil().isNetworkConnected(mView.getCurActivity())) {
                                mView.commentFail();
                            }
                        }
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        if (mView != null) {
                            mView.loadError();
                            if (new NetworkUtil().isNetworkConnected(mView.getCurActivity())) {
                                mView.commentFail();
                            }
                        }
                    }
                });
    }

    @Override
    void toggleCollectNews(String newsId, boolean hasCollect) {
        if (mView==null)return;
        mView.createAndShowDialog();
        if (hasCollect) {
            RepositoryFactory.getRemoteNewsRepository().cancelCollectNews(newsId)
                    .compose(RxScheduler.<ResponseModel<Object>>toMain())
                    .as(mView.<ResponseModel<Object>>bindAutoDispose())
                    .subscribe(new CommonObserver<Object>() {
                        @Override
                        public void onSuccess(Object data) {
                            mView.destoryAndDismissDialog();
                            CommonToast.toast(mView.getCurContext().getString(R.string.cancel_collect));
                            mView.toggleCollectSuccess(false);
                        }

                        @Override
                        public void onError(ResponseModel data) {
                            mView.destoryAndDismissDialog();
                        }

                        @Override
                        public void onNetError(ResponseModel data) {
                            mView.destoryAndDismissDialog();
                        }
                    });
        } else {
            RepositoryFactory.getRemoteNewsRepository().collectNews(newsId)
                    .compose(RxScheduler.<ResponseModel<Object>>toMain())
                    .as(mView.<ResponseModel<Object>>bindAutoDispose())
                    .subscribe(new CommonObserver<Object>() {
                        @Override
                        public void onSuccess(Object data) {
                            mView.destoryAndDismissDialog();
                            CommonToast.toast(mView.getCurContext().getString(R.string.collect_success));
                            mView.toggleCollectSuccess(true);
                        }

                        @Override
                        public void onError(ResponseModel data) {
                            mView.destoryAndDismissDialog();
                        }

                        @Override
                        public void onNetError(ResponseModel data) {
                            mView.destoryAndDismissDialog();
                        }
                    });
        }


    }

    @Override
    void toggleCommentPraise(final int position, final NewsCommentModel.CommentListBean item, TextView tvFavNum) {
//        mView.createAndShowDialog();
        if (mView==null)return;
        String commentId = item.getComentId();
        String num = tvFavNum.getText().toString().trim();
        if (item.isLike()) {
            ViewUtil.setDrawableLeft(tvFavNum, R.drawable.news_fav0);
            tvFavNum.setText((Integer.valueOf(num) - 1) + "");
            RepositoryFactory.getRemoteNewsRepository().cancelPraise(commentId, "")
                    .compose(RxScheduler.<ResponseModel<Object>>toMain())
                    .as(mView.<ResponseModel<Object>>bindAutoDispose())
                    .subscribe(new CommonObserver<Object>() {
                        @Override
                        public void onSuccess(Object data) {
//                            mView.destoryAndDismissDialog();
//                            CommonToast.toast(mView.getCurContext().getString(R.string.cancelPraise));
                            item.setLike(false);

                            String likeNum = item.getLikeNum();
                            if (StringUtil.isNum(likeNum)) {
                                int num = Integer.parseInt(likeNum);
                                num--;
                                item.setLikeNum(String.valueOf(num));
                            }
                            mView.toggleCommentPraiseSuccess(position);
                        }

                        @Override
                        public void onError(ResponseModel data) {
//                            mView.destoryAndDismissDialog();
                        }

                        @Override
                        public void onNetError(ResponseModel data) {

                        }
                    });
        } else {
            tvFavNum.setText((Integer.valueOf(num) + 1) + "");
            ViewUtil.setDrawableLeft(tvFavNum, R.drawable.news_fav1);
            RepositoryFactory.getRemoteNewsRepository().praiseComment(commentId, "")
                    .compose(RxScheduler.<ResponseModel<Object>>toMain())
                    .as(mView.<ResponseModel<Object>>bindAutoDispose())
                    .subscribe(new CommonObserver<Object>() {
                        @Override
                        public void onSuccess(Object data) {
//                            mView.hideLoading();
//                            CommonToast.toast(mView.getCurContext().getString(R.string.praise_success));
                            item.setLike(true);
                            String likeNum = item.getLikeNum();
                            if (StringUtil.isNum(likeNum)) {
                                int num = Integer.parseInt(likeNum);
                                num++;
                                item.setLikeNum(String.valueOf(num));
                            }
                            mView.toggleCommentPraiseSuccess(position);
                        }

                        @Override
                        public void onError(ResponseModel data) {
//                            mView.destoryAndDismissDialog();
                        }

                        @Override
                        public void onNetError(ResponseModel data) {

                        }
                    });
        }
    }

    @Override
    void deleteComment(String commentId, final int position) {
        if (mView==null)return;
        RepositoryFactory.getRemoteNewsRepository().delComment(commentId)
                .compose(RxScheduler.<ResponseModel<Object>>toMain())
                .as(mView.<ResponseModel<Object>>bindAutoDispose())
                .subscribe(new CommonObserver<Object>() {
                    @Override
                    public void onSuccess(Object data) {
                        mView.hideLoading();
                        mView.deleteCommentSuccese(position);
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        mView.destoryAndDismissDialog();
                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                        mView.destoryAndDismissDialog();
                    }
                });
    }

    @Override
    void delete(final String id, final int position) {
        XPopup.setPrimaryColor(ContextCompat.getColor(mView.getCurContext(), R.color.colorAccent));
        new XPopup.Builder(mView.getCurContext()).asConfirm(mView.getCurActivity().getString(R.string.tips), mView.getCurContext().getString(R.string.delete_confirm_message), new OnConfirmListener() {
            @Override
            public void onConfirm() {
                deleteComment(id, position);
            }
        }).show();


    }

    @Override
    void updateReadNum(String newsId) {
        if (mView==null)return;
        RepositoryFactory.getRemoteNewsRepository().updateReadNum(newsId)
                .compose(RxScheduler.<ResponseModel<Object>>toMain())
                .as(mView.<ResponseModel<Object>>bindAutoDispose())
                .subscribe(new CommonObserver<Object>() {
                    @Override
                    public void onSuccess(Object data) {

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
    void showSettingDialog(View view, boolean hasCollect) {
        String[] texts = {mView.getCurActivity().getString(R.string.shared), hasCollect ? mView.getCurActivity().getString(R.string.cancel_collection) : mView.getCurActivity().getString(R.string.collect)};
        new XPopup.Builder(mView.getCurActivity())
                .atView(view)// 依附于所点击的View，内部会自动判断在上方或者下方显示
                .hasShadowBg(false)
                .popupAnimation(PopupAnimation.ScrollAlphaFromLeftTop)
                .asAttachList(texts, new int[]{},
                        new OnSelectListener() {
                            @Override
                            public void onSelect(int position, String text) {
                                mView.selectSettingPosition(position);
                            }
                        })
                .show();
    }

    @Override
    void getAd() {
        if (mView==null)return;
        RepositoryFactory.getRemoteRepository().getAd("2")
                .compose(RxScheduler.<ResponseModel<AdListModel>>toMain())
                .as(mView.<ResponseModel<AdListModel>>bindAutoDispose())
                .subscribe(new CommonObserver<AdListModel>() {
                    @Override
                    public void onSuccess(AdListModel data) {
                        if (mView == null) return;
                        mView.showBanners(data);
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
    void requestRecommendNews(String id) {
        if (mView==null)return;
        RepositoryFactory.getRemoteRepository()
                .requestRecommendNews(id)
                .compose(RxScheduler.<ResponseModel<NewsRecommendReadModel>>toMain())
                .as(mView.<ResponseModel<NewsRecommendReadModel>>bindAutoDispose())
                .subscribe(new CommonObserver<NewsRecommendReadModel>() {
                    @Override
                    public void onSuccess(NewsRecommendReadModel data) {
                        if (mView != null) {
                            if (data != null) {
                                mView.responseRecommendNews(data);
                            }
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

}
