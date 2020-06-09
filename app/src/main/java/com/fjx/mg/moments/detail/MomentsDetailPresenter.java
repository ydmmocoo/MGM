package com.fjx.mg.moments.detail;

import android.text.TextUtils;
import android.util.Log;

import com.fjx.mg.R;
import com.library.common.utils.CommonToast;
import com.library.common.utils.JsonUtil;
import com.library.repository.Constant;
import com.library.repository.core.net.CommonObserver;
import com.library.repository.core.net.RxScheduler;
import com.library.repository.data.UserCenter;
import com.library.repository.models.ImUserRelaM;
import com.library.repository.models.MomentsInfoModel;
import com.library.repository.models.MomentsReplyListModel;
import com.library.repository.models.OtherUserModel;
import com.library.repository.models.ResponseModel;
import com.library.repository.repository.RepositoryFactory;
import com.tencent.imsdk.TIMUserProfile;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.friendship.TIMFriend;

import java.util.List;

class MomentsDetailPresenter extends MomentsDetailContract.Presenter {

    MomentsDetailPresenter(MomentsDetailContract.View view) {
        super(view);
    }

    @Override
    void complaintsUser(String identifier) {
        mView.complaintsUser(identifier);
    }

    @Override
    void addReplyMid(String mid, String content, String toUid, String rid) {
        mView.showLoading();
        RepositoryFactory.getRemoteRepository()
                .addReplyMid(mid, content, toUid, rid)
                .compose(RxScheduler.<ResponseModel<Object>>toMain())
                .as(mView.<ResponseModel<Object>>bindAutoDispose())
                .subscribe(new CommonObserver<Object>() {
                    @Override
                    public void onSuccess(Object model) {
                        if (mView != null) {
                            {
                                mView.hideLoading();
                                mView.GetNewData();
                            }
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
    void MomentsReplyList(String page, String commentId, String isRead) {
        RepositoryFactory.getRemoteRepository()
                .MomentsReplyList(page, commentId, isRead)
                .compose(RxScheduler.<ResponseModel<MomentsReplyListModel>>toMain())
                .as(mView.<ResponseModel<MomentsReplyListModel>>bindAutoDispose())
                .subscribe(new CommonObserver<MomentsReplyListModel>() {
                    @Override
                    public void onSuccess(MomentsReplyListModel data) {
                        if (mView != null && data != null) {
                            mView.showReplyList(data);
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
    void MomentsInfo(final String mId, String lat, String lng, final String page) {
        RepositoryFactory.getRemoteRepository()
                .MomentsInfo(mId, lat, lng)
                .compose(RxScheduler.<ResponseModel<MomentsInfoModel>>toMain())
                .as(mView.<ResponseModel<MomentsInfoModel>>bindAutoDispose())
                .subscribe(new CommonObserver<MomentsInfoModel>() {
                    @Override
                    public void onSuccess(MomentsInfoModel data) {
                        if (mView != null && data != null) {
                            mView.showInfo(data);
                            MomentsReplyList(page, mId, "");
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
    void Praise(String mid) {
        mView.showLoading();
        RepositoryFactory.getRemoteNewsRepository().momentSpraise(mid)
                .compose(RxScheduler.<ResponseModel<Object>>toMain())
                .as(mView.<ResponseModel<Object>>bindAutoDispose())
                .subscribe(new CommonObserver<Object>() {
                    @Override
                    public void onSuccess(Object data) {
                        mView.hideLoading();
                        mView.GetNewData();
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
    void MomentsDel(String mid) {
        mView.showLoading();
        RepositoryFactory.getRemoteNewsRepository().MomentsDel(mid)
                .compose(RxScheduler.<ResponseModel<Object>>toMain())
                .as(mView.<ResponseModel<Object>>bindAutoDispose())
                .subscribe(new CommonObserver<Object>() {
                    @Override
                    public void onSuccess(Object data) {
                        mView.hideLoading();
                        mView.MomentsDelSuccess();
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
    void delReply(String rId) {
        mView.showLoading();
        RepositoryFactory.getRemoteNewsRepository().MomentsDelReply(rId)
                .compose(RxScheduler.<ResponseModel<Object>>toMain())
                .as(mView.<ResponseModel<Object>>bindAutoDispose())
                .subscribe(new CommonObserver<Object>() {
                    @Override
                    public void onSuccess(Object data) {
                        mView.hideLoading();
                        mView.GetNewData();
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
    void CancelPraise(String mid) {
        mView.showLoading();
        RepositoryFactory.getRemoteNewsRepository().momentsCancelPraise(mid)
                .compose(RxScheduler.<ResponseModel<Object>>toMain())
                .as(mView.<ResponseModel<Object>>bindAutoDispose())
                .subscribe(new CommonObserver<Object>() {
                    @Override
                    public void onSuccess(Object data) {
                        mView.hideLoading();
                        mView.GetNewData();
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
    void findUser(String identifier) {
        if (identifier.contains("mg")) {
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


    void getAllFriend(final TIMUserProfile userProfile) {
        TIMFriend friend = UserCenter.getFriend(userProfile.getIdentifier());
        if (friend == null) {
            mView.showUserInfo(new ImUserRelaM(userProfile, false));
        } else {
            mView.showUserInfo(new ImUserRelaM(userProfile, true));
        }
    }
}
