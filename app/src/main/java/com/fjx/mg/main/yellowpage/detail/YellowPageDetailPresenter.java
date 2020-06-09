package com.fjx.mg.main.yellowpage.detail;

import androidx.core.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.fjx.mg.R;
import com.fjx.mg.dialog.CommonDialogHelper;
import com.library.common.utils.CommonToast;
import com.library.common.utils.JsonUtil;
import com.library.common.utils.NetworkUtil;
import com.library.common.utils.StringUtil;
import com.library.common.utils.ViewUtil;
import com.library.repository.Constant;
import com.library.repository.core.net.CommonObserver;
import com.library.repository.core.net.RxScheduler;
import com.library.repository.data.UserCenter;
import com.library.repository.models.AdListModel;
import com.library.repository.models.NewsCommentModel;
import com.library.repository.models.ResponseModel;
import com.library.repository.models.YellowPageDetailModel;
import com.library.repository.repository.RepositoryFactory;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnConfirmListener;
import com.lxj.xpopup.interfaces.OnSelectListener;
import com.tencent.imsdk.TIMUserProfile;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.friendship.TIMFriend;

import java.util.List;

import io.reactivex.functions.Consumer;

import static com.library.repository.db.DBDaoFactory.getYellowPageDetailDao;

class YellowPageDetailPresenter extends YellowPageDetailContract.Presenter {
    private boolean commitComment;

    YellowPageDetailPresenter(YellowPageDetailContract.View view) {
        super(view);
    }

    @Override
    void deleteComment(String commentId, final int position) {
        RepositoryFactory.getRemoteNewsRepository().companydelComment(commentId)
                .compose(RxScheduler.<ResponseModel<Object>>toMain())
                .as(mView.<ResponseModel<Object>>bindAutoDispose())
                .subscribe(new CommonObserver<Object>() {
                    @Override
                    public void onSuccess(Object data) {
                        if (mView != null) {
                            mView.hideLoading();
                            mView.deleteCommentSuccese(position);
                        }

                    }

                    @Override
                    public void onError(ResponseModel data) {
                        if (mView != null) {
                            mView.hideLoading();
                        }
                        if (StringUtil.isNotEmpty(data.getMsg())) {
                            CommonToast.toast(data.getMsg());
                        }
                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                        if (mView != null) {
                            mView.hideLoading();
                        }
                        if (StringUtil.isNotEmpty(data.getMsg())) {
                            CommonToast.toast(data.getMsg());
                        }
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
    void toggleCommentPraise(final int position, final NewsCommentModel.CommentListBean item, TextView tvFavNum) {//点赞或取消
//        mView.createAndShowDialog();
        String commentId = item.getComentId();
        String num = tvFavNum.getText().toString().trim();
        if (item.isLike()) {
            ViewUtil.setDrawableLeft(tvFavNum, R.drawable.news_fav0);
            tvFavNum.setText((Integer.valueOf(num) - 1) + "");
            RepositoryFactory.getRemoteNewsRepository().companycancelPraise(commentId, "")
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
//                            if (StringUtil.isNotEmpty(data.getMsg())) {
//                                CommonToast.toast(data.getMsg());
//                            }
                        }

                        @Override
                        public void onNetError(ResponseModel data) {

                        }
                    });
        } else {
            tvFavNum.setText((Integer.valueOf(num) + 1) + "");
            ViewUtil.setDrawableLeft(tvFavNum, R.drawable.news_fav1);
            RepositoryFactory.getRemoteNewsRepository().companypraiseComment(commentId, "")
                    .compose(RxScheduler.<ResponseModel<Object>>toMain())
                    .as(mView.<ResponseModel<Object>>bindAutoDispose())
                    .subscribe(new CommonObserver<Object>() {
                        @Override
                        public void onSuccess(Object data) {
                            if (mView != null) {
//                                mView.destoryAndDismissDialog();
//                                CommonToast.toast(mView.getCurContext().getString(R.string.praise_success));
                                item.setLike(true);
                                String likeNum = item.getLikeNum();
                                if (StringUtil.isNum(likeNum)) {
                                    int num = Integer.parseInt(likeNum);
                                    num++;
                                    item.setLikeNum(String.valueOf(num));
                                }
                                mView.toggleCommentPraiseSuccess(position);
                            }
                        }

                        @Override
                        public void onError(ResponseModel data) {
//                            if (mView != null) {
//                                mView.destoryAndDismissDialog();
//                            }
//                            if (StringUtil.isNotEmpty(data.getMsg())) {
//                                CommonToast.toast(data.getMsg());
//                            }
                        }

                        @Override
                        public void onNetError(ResponseModel data) {

                        }
                    });
        }
    }

    @Override
    void addComment(String newsId, String content) {//添加评论
        if (commitComment) return;
        if (TextUtils.isEmpty(content)) {
            CommonToast.toast(mView.getCurContext().getString(R.string.hint_input_comment));
            return;
        }
        commitComment = true;
        RepositoryFactory.getRemoteNewsRepository()
                .addCompanyComment(newsId, content)
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
                        if (mView!=null){
                            if (new NetworkUtil().isNetworkConnected(mView.getCurActivity())) {
                                mView.commentFailed();
                            }
                        }
                        CommonToast.toast(data.getMsg());
                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                        if (mView!=null){
                            if (new NetworkUtil().isNetworkConnected(mView.getCurActivity())) {
                                mView.commentFailed();
                            }
                        }
                        CommonToast.toast(data.getMsg());
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        if (mView!=null){
                            mView.loadError();
                            if (new NetworkUtil().isNetworkConnected(mView.getCurActivity())) {
                                mView.commentFailed();
                            }
                        }
                    }
                });
    }

    @Override
    void getCommentList(String newsId, int page) {//企业黄页评论列表
        RepositoryFactory.getRemoteNewsRepository()
                .companycommentList(newsId, page)
                .compose(RxScheduler.<ResponseModel<NewsCommentModel>>toMain())
                .as(mView.<ResponseModel<NewsCommentModel>>bindAutoDispose())
                .subscribe(new CommonObserver<NewsCommentModel>() {
                    @Override
                    public void onSuccess(NewsCommentModel data) {
                        if (mView != null)
                            mView.showCommentList(data);
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        CommonToast.toast(data.getMsg());
                        if (mView != null)
                            mView.loadError();
                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                        CommonToast.toast(data.getMsg());
                        if (mView != null)
                            mView.loadError();
                    }
                });
    }

    @Override
    void getCompanyDetail(final String cId, final String change, final YellowPageDetailActivity yellowPageDetailActivity) {
        YellowPageDetailModel model = getYellowPageDetailDao().queryModel(cId);
        if (model != null) {
            mView.showCompanyDetail(model);
        }

        RepositoryFactory.getRemoteCompanyApi().YellowPageDetail(cId)
                .compose(RxScheduler.<ResponseModel<YellowPageDetailModel>>toMain())
                .as(mView.<ResponseModel<YellowPageDetailModel>>bindAutoDispose())
                .subscribe(new CommonObserver<YellowPageDetailModel>() {
                    @Override
                    public void onSuccess(YellowPageDetailModel data) {
                        try {
                            getYellowPageDetailDao().insertModel(data);
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (mView != null) {
                            mView.showCompanyDetail(data);
                        }
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

    @Override
    void findImUser(String uid) {
        RepositoryFactory.getChatRepository().getUsersProfile(uid, true,
                new TIMValueCallBack<List<TIMUserProfile>>() {

                    @Override
                    public void onError(int i, String s) {
                        if (TextUtils.equals(s, "Err_Profile_Invalid_Account")) {
                            CommonToast.toast(mView.getCurContext().getString(R.string.no_user));
                        }
                    }

                    @Override
                    public void onSuccess(List<TIMUserProfile> timUserProfiles) {
                        Log.d(Constant.TIM_LOG, JsonUtil.moderToString(timUserProfiles));
                        if (timUserProfiles.size() == 0) return;
                        getAllFriend(timUserProfiles.get(0));
                    }
                });
    }

    @Override
    void showPublishDialog(View view) {
        CommonDialogHelper.showPublishDialog(mView.getCurActivity(), view, new OnSelectListener() {
            @Override
            public void onSelect(int position, String text) {
                settingOptions(position);
            }
        });
    }

    @Override
    void showPublishDialog(View view, boolean isExpire, boolean isClosed, boolean isLove, boolean isShare) {
        CommonDialogHelper.showNewPublishDialog(mView.getCurActivity(), view, (position, text) -> settingOptions(position));
    }

    @Override
    void deleteCompany(String cid) {
        mView.showLoading();
        RepositoryFactory.getRemoteCompanyApi().deleteCompany(cid)
                .compose(RxScheduler.<ResponseModel<Object>>toMain())
                .as(mView.<ResponseModel<Object>>bindAutoDispose())
                .subscribe(new CommonObserver<Object>() {
                    @Override
                    public void onSuccess(Object data) {
                        if (mView != null) {
                            mView.hideLoading();
                            mView.deleteSuccess();
                        }
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        if (mView != null) {
                            mView.hideLoading();
                            if (StringUtil.isNotEmpty(data.getMsg())) {
                                CommonToast.toast(data.getMsg());
                            }
                        }

                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                        if (mView != null) {
                            mView.hideLoading();
                            if (StringUtil.isNotEmpty(data.getMsg())) {
                                CommonToast.toast(data.getMsg());
                            }
                        }
                    }
                });
    }

    @Override
    void updateReadNumCompany(String cid) {
        RepositoryFactory.getRemoteCompanyApi().updateReadNumCompany(cid)
                .compose(RxScheduler.toMain())
                .as(mView.bindAutoDispose())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) {
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                        Log.e("accept失败", "" + throwable.toString());
                    }
                });
    }

    @Override
    void getAd() {
        RepositoryFactory.getRemoteRepository().getAd("6")
                .compose(RxScheduler.<ResponseModel<AdListModel>>toMain())
                .as(mView.<ResponseModel<AdListModel>>bindAutoDispose())
                .subscribe(new CommonObserver<AdListModel>() {
                    @Override
                    public void onSuccess(AdListModel data) {
                        if (mView != null & data != null) {
                            mView.showAds(data);
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

    private void getAllFriend(final TIMUserProfile userProfile) {
        TIMFriend friend = UserCenter.getFriend(userProfile.getIdentifier());
        if (friend == null) {
            mView.getImUserSuccess(userProfile);
        } else {
            mView.getImUserSuccess(friend);
        }
    }

    private void settingOptions(int position) {
        switch (position) {
            case 1:
                mView.editCompanyInfo();
                break;
            case 2:
                mView.deleteCompany();
                break;
            case 4://分享
                mView.share();
                break;
            default:
                break;
        }
    }
}
