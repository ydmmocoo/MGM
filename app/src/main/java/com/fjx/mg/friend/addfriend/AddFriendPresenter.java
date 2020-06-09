package com.fjx.mg.friend.addfriend;

import android.text.TextUtils;
import android.util.Log;

import com.fjx.mg.R;
import com.fjx.mg.friend.chat.ChatActivity;
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

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class AddFriendPresenter extends AddFriendContract.Presenter {
    private WeakReference<AddFriendContract.View> weakReference;

    @Override
    void getAllBlackFriend() {

    }

    public AddFriendPresenter(AddFriendContract.View view) {
        super(view);
        weakReference = new WeakReference<>(view);
    }

    void getUser(String imUid) {
        String p = "";
        String id = "";
        if (imUid.contains("mgm") || imUid.contains("fjx")||
                imUid.contains("MGM")) {
            id = imUid;
        } else {
            p = imUid;
        }
        final AddFriendContract.View view = weakReference.get() == null ? mView : weakReference.get();
        if (view == null) return;
        RepositoryFactory.getRemoteAccountRepository()
                .getUserInfo(p, id, "")
                .compose(RxScheduler.<ResponseModel<OtherUserModel>>toMain())
                .as(view.<ResponseModel<OtherUserModel>>bindAutoDispose())
                .subscribe(new CommonObserver<OtherUserModel>() {
                    @Override
                    public void onSuccess(OtherUserModel data) {
                        String userId = data.getIdentifier();
                        if (!TextUtils.isEmpty(userId)) {
                            findUser(userId);
                        } else {
                            CommonToast.toast(mView.getCurContext().getString(R.string.no_user));
                        }
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        CommonToast.toast(data.getMsg());
                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                    }

                    @Override
                    public void onUserFailed(ResponseModel data) {
                        CommonToast.toast(mView.getCurContext().getString(R.string.no_user));
                    }
                });
    }

    @Override
    void findUser(String imUid) {
        final AddFriendContract.View view = weakReference.get() == null ? mView : weakReference.get();
        if (view == null) return;
        if (imUid.contains("mgm") || imUid.contains("MGM")) {
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
        } else if (imUid.contains("fjx") || imUid.contains("FJX")) {
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
                    .as(view.<ResponseModel<OtherUserModel>>bindAutoDispose())
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
                            if (view != null)
                                view.hideLoading();
                            CommonToast.toast(data.getMsg());
                        }

                        @Override
                        public void onNetError(ResponseModel data) {
                            if (view != null)
                                view.hideLoading();
                            CommonToast.toast(data.getMsg());
                        }
                    });
        }
    }


    @Override
    void getAllFriend(final TIMUserProfile userProfile) {
        final AddFriendContract.View view = weakReference.get() == null ? mView : weakReference.get();
        TIMFriend friend = UserCenter.getFriend(userProfile.getIdentifier());
        if (friend == null) {
            if (view != null)
                view.showUserInfo(new ImUserRelaM(userProfile, false));
        } else {
            if (view != null)
                view.showUserInfo(new ImUserRelaM(userProfile, true));
        }
    }

    @Override
    void addFriend(final String timUserId, final String addWorld, final String remark, Boolean black) {
        mView.createAndShowDialog();
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
                    mView.destoryAndDismissDialog();
                }
            });
        } else {
            RepositoryFactory.getRemoteRepository().addFriend(timUserId, addWorld, remark)
                    .compose(RxScheduler.<ResponseModel<AddFriendModel>>toMain())
                    .as(mView.<ResponseModel<AddFriendModel>>bindAutoDispose())
                    .subscribe(new CommonObserver<AddFriendModel>() {
                        @Override
                        public void onSuccess(AddFriendModel data) {
                            if (data == null) {
                                ChatActivity.startC2CChat(mView.getCurActivity(), timUserId, remark, true);
                                mView.chatact(timUserId);
//                                MessageInfo messageInfo = MessageInfoUtil.buildTextMessage(mView.getCurActivity().getString(R.string.we_are_friend_we_can_chat));
//                                TIMConversation conversation = TIMManager.getInstance().getConversation(TIMConversationType.C2C, timUserId);
//                                try {
//                                    Thread.sleep(200);
//                                } catch (InterruptedException e) {
//                                    e.printStackTrace();
//                                }
//
//                                conversation.sendMessage(messageInfo.getTIMMessage(), new TIMValueCallBack<TIMMessage>() {
//                                    @Override
//                                    public void onError(int i, String s) {
//
//                                    }
//
//                                    @Override
//                                    public void onSuccess(TIMMessage timMessage) {
//                                        mView.getCurActivity().finish();
//                                        mView.hideLoading();
//
//                                    }
//                                });
                            } else {
                                if (1 == data.getNeedConfirm()) {
                                    CommonToast.toast(mView.getCurContext().getString(R.string.request_had_send));
                                    Log.d(Constant.TIM_LOG, JsonUtil.moderToString(data));
                                    mView.getCurActivity().finish();
                                    mView.destoryAndDismissDialog();
                                } else {
                                    ChatActivity.startC2CChat(mView.getCurActivity(), timUserId, remark, true);
                                    mView.chatact(timUserId);
                                }
                            }
                        }

                        @Override
                        public void onError(ResponseModel data) {
                            mView.destoryAndDismissDialog();
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
                CommonToast.toast(R.string.remove_backlist_need_check);
            }
        });
    }

    @Override
    void addFriend(String timUserId, String addWording) {
        mView.createAndShowDialog();
        RepositoryFactory.getRemoteRepository().addFriend(timUserId, addWording, "")
                .compose(RxScheduler.<ResponseModel<AddFriendModel>>toMain())
                .as(mView.<ResponseModel<AddFriendModel>>bindAutoDispose())
                .subscribe(new CommonObserver<AddFriendModel>() {
                    @Override
                    public void onSuccess(AddFriendModel data) {
                        CommonToast.toast(mView.getCurContext().getString(R.string.request_had_send));
                        Log.d(Constant.TIM_LOG, JsonUtil.moderToString(data));
                        mView.destoryAndDismissDialog();
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        mView.destoryAndDismissDialog();
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
        mView.createAndShowDialog();
        RepositoryFactory.getRemoteRepository()
                .getUserInfo(identifier)
                .compose(RxScheduler.<ResponseModel<MomentsUserInfoModel>>toMain())
                .as(mView.<ResponseModel<MomentsUserInfoModel>>bindAutoDispose())
                .subscribe(new CommonObserver<MomentsUserInfoModel>() {
                    @Override
                    public void onSuccess(MomentsUserInfoModel data) {
                        mView.destoryAndDismissDialog();
                        if (mView != null && data != null) {
                            mView.showMommetnsUserInfo(data);
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
                        if (mView != null) {
                            mView.destoryAndDismissDialog();
                            CommonToast.toast(data.getMsg());
                        }
                    }

                    @Override
                    public void onSetPassword() {
                        super.onSetPassword();
                        if (mView != null)
                            mView.destoryAndDismissDialog();
                    }
                });
    }

    public void getFriendRelation(final String identity) {
        if (mView == null) return;
        RepositoryFactory.getChatRepository().getAllFriend(new TIMValueCallBack<List<TIMFriend>>() {
            @Override
            public void onError(int i, String s) {
                mView.friendRelation(false);
            }

            @Override
            public void onSuccess(List<TIMFriend> list) {
                RepositoryFactory.getLocalRepository().saveUserAvatar(list);
                RepositoryFactory.getLocalRepository().saveAllFriend(list);
                for (int i = 0; i < list.size(); i++) {
                    if (TextUtils.equals(identity, list.get(i).getIdentifier())) {
                        mView.friendRelation(true);
                    } else {
                        mView.friendRelation(false);
                    }
                }
            }
        });
    }

}
