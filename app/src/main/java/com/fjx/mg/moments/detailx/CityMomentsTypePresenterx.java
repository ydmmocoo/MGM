package com.fjx.mg.moments.detailx;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.fjx.mg.R;
import com.library.common.utils.CommonToast;
import com.library.common.utils.JsonUtil;
import com.library.repository.Constant;
import com.library.repository.core.net.CommonObserver;
import com.library.repository.core.net.RxScheduler;
import com.library.repository.data.UserCenter;
import com.library.repository.models.CityCircleListModel;
import com.library.repository.models.ImUserRelaM;
import com.library.repository.models.OtherUserModel;
import com.library.repository.models.ResponseModel;
import com.library.repository.repository.RepositoryFactory;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnConfirmListener;
import com.tencent.imsdk.TIMUserProfile;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.friendship.TIMFriend;

import java.util.List;

class CityMomentsTypePresenterx extends CityMomentsTypeContractx.Presenter {

    CityMomentsTypePresenterx(CityMomentsTypeContractx.View view) {
        super(view);
    }

    @Override
    void MomentsDel(final String mid) {
        new XPopup.Builder(mView.getCurContext()).asConfirm(mView.getCurContext().getString(R.string.Tips),
                mView.getCurContext().getString(R.string.delete_confirm_message), new OnConfirmListener() {
                    @Override
                    public void onConfirm() {
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
                }).show();

    }

    @Override
    void addReplyMid(String mid, String content, String toUid, String rid) {
        mView.updateEditTextBodyVisible(View.GONE);
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
    void getCityCircleList(int type, String Page) {
        mView.updateEditTextBodyVisible(View.GONE);
        mView.showLoading();
        RepositoryFactory.getRemoteRepository()
                .getCityCircle(Page, type)
                .compose(RxScheduler.<ResponseModel<CityCircleListModel>>toMain())
                .as(mView.<ResponseModel<CityCircleListModel>>bindAutoDispose())
                .subscribe(new CommonObserver<CityCircleListModel>() {
                    @Override
                    public void onSuccess(CityCircleListModel model) {
                        if (model != null && mView != null) {
                            mView.hideLoading();
                            mView.ShowCityCircleList(model);
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
    void ToUserInfo(String identifier, String headimg) {
        mView.ToUserInfo(identifier, headimg);
    }

    @Override
    void BodyVisible(String mId, String rId) {
        mView.BodyVisible(mId, rId);
    }


    @Override
    void toggleCommentPraise(String mid, Boolean isPraised) {
        mView.updateEditTextBodyVisible(View.GONE);
        mView.showLoading();
        if (!isPraised) {
            RepositoryFactory.getRemoteNewsRepository().momentSpraise(mid)
                    .compose(RxScheduler.<ResponseModel<Object>>toMain())
                    .as(mView.<ResponseModel<Object>>bindAutoDispose())
                    .subscribe(new CommonObserver<Object>() {
                        @Override
                        public void onSuccess(Object data) {
                            mView.hideLoading();
                            mView.PraiseSuccess();
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
            RepositoryFactory.getRemoteNewsRepository().momentsCancelPraise(mid)
                    .compose(RxScheduler.<ResponseModel<Object>>toMain())
                    .as(mView.<ResponseModel<Object>>bindAutoDispose())
                    .subscribe(new CommonObserver<Object>() {
                        @Override
                        public void onSuccess(Object data) {
                            mView.hideLoading();
                            mView.PraiseSuccess();
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
        mView.showLoading();
        RepositoryFactory.getRemoteNewsRepository().MomentsDelReply(rId)
                .compose(RxScheduler.<ResponseModel<Object>>toMain())
                .as(mView.<ResponseModel<Object>>bindAutoDispose())
                .subscribe(new CommonObserver<Object>() {
                    @Override
                    public void onSuccess(Object data) {
                        mView.hideLoading();
                        mView.delReplySuccess();
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
