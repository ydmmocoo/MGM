package com.fjx.mg.moments.city;

import android.content.DialogInterface;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AlertDialog;

import com.fjx.mg.R;
import com.fjx.mg.moments.CityCache;
import com.library.common.utils.CommonToast;
import com.library.common.utils.JsonUtil;
import com.library.common.utils.NetworkUtil;
import com.library.common.utils.StringUtil;
import com.library.repository.Constant;
import com.library.repository.core.net.CommonObserver;
import com.library.repository.core.net.RxScheduler;
import com.library.repository.data.UserCenter;
import com.library.repository.models.CityCircleListModel;
import com.library.repository.models.ImUserRelaM;
import com.library.repository.models.OtherUserModel;
import com.library.repository.models.ResponseModel;
import com.library.repository.models.TypeListModel;
import com.library.repository.models.UnReadCountBean;
import com.library.repository.repository.RepositoryFactory;
import com.tencent.imsdk.TIMUserProfile;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.friendship.TIMFriend;

import java.util.List;

class CityMomentsPresenter extends CityMomentsContract.Presenter {

    CityMomentsPresenter(CityMomentsContract.View view) {
        super(view);
    }

    @Override
    void toDetail(String Mid) {
        mView.toDetail(Mid);
    }

    @Override
    void complaintsUser(String identifier) {
        mView.complaintsUser(identifier);
    }

    @Override
    void MomentsDel(final String mid) {
        mView.createAndShowDialog();
        AlertDialog dialog = new AlertDialog.Builder(mView.getCurContext())
                .setMessage(mView.getCurContext().getString(R.string.is_delete_review))
                .setPositiveButton(mView.getCurContext().getString(R.string.delete), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mView.createAndShowDialog();
                        RepositoryFactory.getRemoteNewsRepository().MomentsDel(mid)
                                .compose(RxScheduler.<ResponseModel<Object>>toMain())
                                .as(mView.<ResponseModel<Object>>bindAutoDispose())
                                .subscribe(new CommonObserver<Object>() {
                                    @Override
                                    public void onSuccess(Object data) {
                                        if (mView != null) {
                                            mView.destoryAndDismissDialog();
                                            mView.MomentsDelSuccess();
                                        }
                                    }

                                    @Override
                                    public void onError(ResponseModel data) {
                                        if (mView != null)
                                            mView.destoryAndDismissDialog();
                                        CommonToast.toast(data.getMsg());
                                    }

                                    @Override
                                    public void onNetError(ResponseModel data) {
                                        if (mView != null)
                                            mView.destoryAndDismissDialog();
                                        CommonToast.toast(data.getMsg());
                                    }
                                });
                        dialogInterface.dismiss();
                    }
                })
                .setNegativeButton(mView.getCurContext().getString(R.string.think_about_it), null)
                .create();
        if (!dialog.isShowing()) {
            dialog.show();
        }
//        new XPopup.Builder(mView.getCurContext()).asConfirm(mView.getCurContext().getString(R.string.Tips),
//                mView.getCurContext().getString(R.string.delete_confirm_message), new OnConfirmListener() {
//                    @Override
//                    public void onConfirm() {
//
//                    }
//                }).show();

    }

    @Override
    void getTypeList() {
        mView.updateEditTextBodyVisible(View.GONE);
        RepositoryFactory.getRemoteRepository()
                .getType()
                .compose(RxScheduler.<ResponseModel<TypeListModel>>toMain())
                .as(mView.<ResponseModel<TypeListModel>>bindAutoDispose())
                .subscribe(new CommonObserver<TypeListModel>() {
                    @Override
                    public void onSuccess(TypeListModel model) {
                        if (model.getTypeList() != null && mView != null) {
                            mView.ShowTypeList(model.getTypeList());
                        }
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        if (mView != null) {
                            mView.responseFailed(data);
                        }
                        CommonToast.toast(data.getMsg());
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
    void addReplyMid(String mid, String content, String toUid, String rid) {
        mView.updateEditTextBodyVisible(View.GONE);
        if (StringUtil.isEmpty(content)){
            CommonToast.toast(mView.getCurContext().getString(R.string.hint_input_comment));
            return;
        }
        RepositoryFactory.getRemoteRepository()
                .addReplyMid(mid, content, toUid, rid)
                .compose(RxScheduler.<ResponseModel<Object>>toMain())
                .as(mView.<ResponseModel<Object>>bindAutoDispose())
                .subscribe(new CommonObserver<Object>() {
                    @Override
                    public void onSuccess(Object model) {
                        if (mView != null) {
                            {
                                mView.GetNewData();
                            }
                        }
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        if (mView != null) {
                            mView.responseFailed(data);
                            if (new NetworkUtil().isNetworkConnected(mView.getCurContext())){
                                mView.replyFailed();
                            }
                        }
                        //CommonToast.toast(data.getMsg());

                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                        if (mView != null) {
                            mView.responseFailed(data);
                            if (new NetworkUtil().isNetworkConnected(mView.getCurContext())){
                                mView.replyFailed();
                            }
                        }
                    }
                });
    }

    @Override
    void getCityCircleList(String Page) {
        mView.updateEditTextBodyVisible(View.GONE);
        RepositoryFactory.getRemoteRepository()
                .getCityCircle(Page)
                .compose(RxScheduler.<ResponseModel<CityCircleListModel>>toMain())
                .as(mView.<ResponseModel<CityCircleListModel>>bindAutoDispose())
                .subscribe(new CommonObserver<CityCircleListModel>() {
                    @Override
                    public void onSuccess(CityCircleListModel model) {
                        if (model != null && mView != null) {
                            mView.ShowCityCircleList(model);
                            CityCache.getInstance().putModel(model);
//                            DaoSession daoSession = DBHelper.getInstance().getDaoSession();
//                            CityCircleListModelDao dao = daoSession.getCityCircleListModelDao();
//                            if (dao.loadAll().isEmpty()) {
//                                dao.insertOrReplace(model);
//                            } else {
//                                dao.deleteAll();
//                                dao.insertOrReplace(model);
//                            }
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
                        CommonToast.toast(data.getMsg());
                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                        if (mView != null) {
                            mView.responseFailed(data);
                        }
                        CommonToast.toast(data.getMsg());
                    }
                });
    }

    @Override
    void ToUserInfo(String identifier, String headimg) {
        mView.ToUserInfo(identifier, headimg);
    }

    @Override
    void BodyVisible(String mId, String rId, String uid) {
        mView.BodyVisible(mId, rId, uid);
    }


    @Override
    void toggleCommentPraise(String mid, Boolean isPraised) {
        mView.updateEditTextBodyVisible(View.GONE);
        if (!isPraised) {
            RepositoryFactory.getRemoteNewsRepository().momentSpraise(mid)
                    .compose(RxScheduler.<ResponseModel<Object>>toMain())
                    .as(mView.<ResponseModel<Object>>bindAutoDispose())
                    .subscribe(new CommonObserver<Object>() {
                        @Override
                        public void onSuccess(Object data) {
                            mView.PraiseSuccess();
                        }

                        @Override
                        public void onError(ResponseModel data) {
                        }

                        @Override
                        public void onNetError(ResponseModel data) {

                        }
                    });
        } else {
            RepositoryFactory.getRemoteNewsRepository().momentsCancelPraise(mid)
                    .compose(RxScheduler.<ResponseModel<Object>>toMain())
                    .as(mView.<ResponseModel<Object>>bindAutoDispose())
                    .subscribe(new CommonObserver<Object>() {
                        @Override
                        public void onSuccess(Object data) {
                            mView.PraiseSuccess();
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

    @Override
    void ShowDetail(int position, List<String> item) {
        mView.ShowDetail(position, item);
    }

    @Override
    void ShowCommentDialog(String replyId, String content, boolean showDele) {
        mView.ShowCommentDialog(replyId, content, showDele);
    }

    @Override
    void delReply(String rId) {
        mView.updateEditTextBodyVisible(View.GONE);
        mView.createAndShowDialog();
        RepositoryFactory.getRemoteNewsRepository().MomentsDelReply(rId)
                .compose(RxScheduler.<ResponseModel<Object>>toMain())
                .as(mView.<ResponseModel<Object>>bindAutoDispose())
                .subscribe(new CommonObserver<Object>() {
                    @Override
                    public void onSuccess(Object data) {
                        mView.destoryAndDismissDialog();
                        mView.delReplySuccess();
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        mView.destoryAndDismissDialog();
                        CommonToast.toast(data.getMsg());
                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                        mView.destoryAndDismissDialog();
                    }
                });
    }

    @Override
    void findUser(String identifier) {
        if (identifier.contains("mg") || identifier.contains("MGM")) {
            RepositoryFactory.getChatRepository().getUsersProfile(identifier, true,
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
        } else if (identifier.contains("fjx")) {
            RepositoryFactory.getChatRepository().getUsersProfile(identifier, true,
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
        } else {
            RepositoryFactory.getRemoteAccountRepository().getUserInfo(identifier, "")
                    .compose(RxScheduler.<ResponseModel<OtherUserModel>>toMain())
                    .as(mView.<ResponseModel<OtherUserModel>>bindAutoDispose())
                    .subscribe(new CommonObserver<OtherUserModel>() {
                        @Override
                        public void onSuccess(OtherUserModel data) {
                            RepositoryFactory.getChatRepository().getUsersProfile(data.getIdentifier(), true,
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
                        public void onError(ResponseModel data) {
                            mView.hideLoading();
                            //CommonToast.toast(data.getMsg());
                        }

                        @Override
                        public void onNetError(ResponseModel data) {
                            mView.hideLoading();
                        }
                    });
        }
    }

    @Override
    void requestIsShowRedPoint() {
        if (!UserCenter.hasLogin()){
            return;
        }
        RepositoryFactory.getRemoteFriendsApi().unReadCount()
                .compose(RxScheduler.<ResponseModel<UnReadCountBean>>toMain())
                .as(mView.<ResponseModel<UnReadCountBean>>bindAutoDispose())
                .subscribe(new CommonObserver<UnReadCountBean>() {
                    @Override
                    public void onSuccess(UnReadCountBean data) {
                        mView.isShowRedPoint(data);
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        if (StringUtil.isNotEmpty(data.getMsg())) {
                            CommonToast.toast(data.getMsg());
                        }
                    }

                    @Override
                    public void onNetError(ResponseModel data) {

                    }
                });
    }

    void getAllFriend(final TIMUserProfile userProfile) {
        TIMFriend friend = UserCenter.getFriend(userProfile.getIdentifier());
        if (friend == null) {
            mView.showUserInfo(new ImUserRelaM(userProfile, false));
        } else {
            mView.showUserInfo(new ImUserRelaM(userProfile, true));
        }
    }

}
