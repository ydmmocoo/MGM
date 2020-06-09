package com.fjx.mg.friend.addfriend;

import android.text.TextUtils;
import android.util.Log;

import com.fjx.mg.R;
import com.library.common.utils.CommonToast;
import com.library.common.utils.JsonUtil;
import com.library.repository.Constant;
import com.library.repository.core.net.CommonObserver;
import com.library.repository.core.net.RxScheduler;
import com.library.repository.data.UserCenter;
import com.library.repository.models.AddFriendModel;
import com.library.repository.models.ImUserRelaM;
import com.library.repository.models.InviteModel;
import com.library.repository.models.MomentsUserInfoModel;
import com.library.repository.models.OtherUserModel;
import com.library.repository.models.ResponseModel;
import com.library.repository.repository.RepositoryFactory;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnInputConfirmListener;
import com.tencent.imsdk.TIMUserProfile;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.friendship.TIMFriend;

import java.util.ArrayList;
import java.util.List;

public class SearchFriendPresenter extends SearchFriendContract.Presenter {

    @Override
    void getAllBlackFriend() {

    }

    public SearchFriendPresenter(SearchFriendContract.View view) {
        super(view);
    }

    @Override
    void findUser(String imUid) {
        if (imUid.contains("mg")) {
            RepositoryFactory.getChatRepository().getUsersProfile(imUid, true,
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
        } else if (imUid.contains("fjx")) {
            RepositoryFactory.getChatRepository().getUsersProfile(imUid, true,
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
            RepositoryFactory.getRemoteAccountRepository().getUserInfo(imUid, "")
                    .compose(RxScheduler.<ResponseModel<OtherUserModel>>toMain())
                    .as(mView.<ResponseModel<OtherUserModel>>bindAutoDispose())
                    .subscribe(new CommonObserver<OtherUserModel>() {
                        @Override
                        public void onSuccess(final OtherUserModel data) {
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
                                            getAllFriend(timUserProfiles.get(0), data.getIdentifier());
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

    void getAllFriend(final TIMUserProfile userProfile, String identifier) {
        TIMFriend friend = UserCenter.getFriend(userProfile.getIdentifier());
        if (friend == null) {
            mView.showUserInfo(new ImUserRelaM(userProfile, false));
        } else {
            mView.showUserInfo(new ImUserRelaM(userProfile, true));
        }
    }

    @Override
    void getAllFriend(final TIMUserProfile userProfile) {
        TIMFriend friend = UserCenter.getFriend(userProfile.getIdentifier());
        if (friend == null) {
            mView.showUserInfo(new ImUserRelaM(userProfile, false));
        } else {
            mView.showUserInfo(new ImUserRelaM(userProfile, true));
        }
    }

    @Override
    void addFriend(final String timUserId, final String addWorld, final String remark, Boolean black) {
        mView.showLoading();
//        RepositoryFactory.getChatRepository().addFriend(timUserId, remark, addWorld, new TIMValueCallBack<TIMFriendResult>() {
//            @Override
//            public void onError(int i, String stroke) {
//                Log.d(Constant.TIM_LOG, stroke);
//                CommonToast.toast(mView.getCurContext().getString(R.string.add_fail));
//            }
//
//            @Override
//            public void onSuccess(TIMFriendResult timFriendResult) {
//                CommonToast.toast(mView.getCurContext().getString(R.string.request_had_send));
//                Log.d(Constant.TIM_LOG, JsonUtil.moderToString(timFriendResult));
//                mView.hideLoading();
//
//            }
//        });
//        mView.showLoading();
        Log.e("Boolean", "" + black);
        if (black) {
            List<String> id = new ArrayList<>();
            id.add(timUserId);
            RepositoryFactory.getChatRepository().deleteBlackList(id, new TIMValueCallBack() {
                @Override
                public void onError(int i, String s) {
                    mView.hideLoading();
                }

                @Override
                public void onSuccess(Object o) {
                    addnew(timUserId, addWorld, remark);
                    mView.hideLoading();
                }
            });
        } else {
            RepositoryFactory.getRemoteRepository().addFriend(timUserId, addWorld, remark)
                    .compose(RxScheduler.<ResponseModel<AddFriendModel>>toMain())
                    .as(mView.<ResponseModel<AddFriendModel>>bindAutoDispose())
                    .subscribe(new CommonObserver<AddFriendModel>() {
                        @Override
                        public void onSuccess(AddFriendModel data) {
                            CommonToast.toast(mView.getCurContext().getString(R.string.request_had_send));
                            Log.d(Constant.TIM_LOG, JsonUtil.moderToString(data));
                            mView.getCurActivity().finish();
                            mView.hideLoading();
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
    }

    private void addnew(String timUserId, String addWorld, String remark) {
        RepositoryFactory.getChatRepository().addFriend(timUserId, remark, addWorld, new TIMValueCallBack() {
            @Override
            public void onError(int i, String s) {
                Log.e("移除黑名单重新添加IM好友onError:", "" + s);
//                mView.hideLoading();
            }

            @Override
            public void onSuccess(Object o) {
                Log.e("移出黑名单成功,需要等对方验证通过", "" + o.toString());
//                mView.hideLoading();
                CommonToast.toast(mView.getCurActivity().getString(R.string.remove_backlist_need_check));
            }
        });
    }

    @Override
    void addFriend(String timUserId, String addWording) {
        mView.showLoading();
        RepositoryFactory.getRemoteRepository().addFriend(timUserId, addWording, "")
                .compose(RxScheduler.<ResponseModel<AddFriendModel>>toMain())
                .as(mView.<ResponseModel<AddFriendModel>>bindAutoDispose())
                .subscribe(new CommonObserver<AddFriendModel>() {
                    @Override
                    public void onSuccess(AddFriendModel data) {
                        CommonToast.toast(mView.getCurContext().getString(R.string.request_had_send));
                        Log.d(Constant.TIM_LOG, JsonUtil.moderToString(data));
                        mView.hideLoading();
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        mView.hideLoading();
                        CommonToast.toast(data.getMsg());
                        mView.getCurActivity().finish();

                    }

                    @Override
                    public void onNetError(ResponseModel data) {

                    }
                });
    }

    @Override
    void showRemarkEditDialog() {
        new XPopup.Builder(mView.getCurContext()).asInputConfirm(mView.getCurContext().getString(R.string.edit_remark), mView.getCurContext()
                        .getString(R.string.hint_input_remark),
                new OnInputConfirmListener() {
                    @Override
                    public void onConfirm(String text) {
                        mView.showRemark(text);
                    }
                })
                .show();
    }

    @Override
    void getInviteCode() {
        RepositoryFactory.getRemoteAccountRepository().getInviteCode()
                .compose(RxScheduler.<ResponseModel<InviteModel>>toMain())
                .as(mView.<ResponseModel<InviteModel>>bindAutoDispose())
                .subscribe(new CommonObserver<InviteModel>() {
                    @Override
                    public void onSuccess(InviteModel data) {
                        mView.showInviteModel(data);
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
    void outblackFriendSuccess() {

    }

    @Override
    void MomentsGetUserInfo(String identifier) {
        mView.showLoading();
        RepositoryFactory.getRemoteRepository()
                .getUserInfo(identifier)
                .compose(RxScheduler.<ResponseModel<MomentsUserInfoModel>>toMain())
                .as(mView.<ResponseModel<MomentsUserInfoModel>>bindAutoDispose())
                .subscribe(new CommonObserver<MomentsUserInfoModel>() {
                    @Override
                    public void onSuccess(MomentsUserInfoModel data) {
                        mView.hideLoading();
                        if (mView != null && data != null) {
                            mView.showMommetnsUserInfo(data);
                        }
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        mView.hideLoading();
                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                        mView.hideLoading();
                        CommonToast.toast(data.getMsg());
                    }
                });
    }

}
