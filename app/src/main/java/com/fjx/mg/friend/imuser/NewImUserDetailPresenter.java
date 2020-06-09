package com.fjx.mg.friend.imuser;

import android.app.Dialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import com.fjx.mg.R;
import com.fjx.mg.sharetoapp.FriendsSelectActivity;
import com.library.common.utils.CommonToast;
import com.library.common.utils.JsonUtil;
import com.library.common.utils.StringUtil;
import com.library.repository.core.net.CommonObserver;
import com.library.repository.core.net.RxScheduler;
import com.library.repository.data.UserCenter;
import com.library.repository.db.DBDaoFactory;
import com.library.repository.db.model.DBFriendRequestModel;
import com.library.repository.models.MomentsUserInfoModel;
import com.library.repository.models.ResponseModel;
import com.library.repository.repository.RepositoryFactory;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnSelectListener;
import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMFriendshipManager;
import com.tencent.imsdk.TIMUserProfile;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.friendship.TIMFriend;
import com.tencent.qcloud.uikit.business.chat.model.ElemExtModel;
import com.tencent.qcloud.uikit.common.utils.ScreenUtil;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Consumer;

public class NewImUserDetailPresenter extends NewImUserDetailContract.Presenter {

    WeakReference<NewImUserDetailContract.View> weakReference;

    public NewImUserDetailPresenter(NewImUserDetailContract.View view) {
        super(view);
        weakReference = new WeakReference<>(view);
    }

    @Override
    void getImUserInfo(String imUserId) {
        final NewImUserDetailContract.View view = weakReference.get();
        if (view == null) {
            return;
        }
        mView.createAndShowDialog();
        RepositoryFactory.getChatRepository().getUsersProfile(imUserId, true, new TIMValueCallBack<List<TIMUserProfile>>() {
            @Override
            public void onError(int i, String s) {
                Log.d("getUsersProfile", "code" + i + "error result=" + s);
                if (view != null) {
                    mView.destoryAndDismissDialog();
                }
            }

            @Override
            public void onSuccess(List<TIMUserProfile> timUserProfiles) {
                Log.d("getUsersProfile", JsonUtil.moderToString(timUserProfiles));
//                getAllFriend(timUserProfiles.get(0));
                if (view != null) {
                    view.destoryAndDismissDialog();
                    view.showImUserInfo(timUserProfiles.get(0));
                }
            }
        });
    }

    @Override
    void getAllFriend(final TIMUserProfile userProfile) {
        Log.d("getAllFriend", JsonUtil.moderToString(userProfile));
        RepositoryFactory.getChatRepository().getAllFriend(new TIMValueCallBack<List<TIMFriend>>() {

            @Override
            public void onError(int i, String s) {
                Log.d("getAllFriend", s);
                if (mView != null) {
                    mView.destoryAndDismissDialog();
                }
            }

            @Override
            public void onSuccess(List<TIMFriend> timFriends) {
                if (mView != null) {
                    mView.destoryAndDismissDialog();
                    RepositoryFactory.getLocalRepository().saveUserAvatar(timFriends);
                    for (TIMFriend friend : timFriends) {
                        Log.d("getAllFriend2", JsonUtil.moderToString(friend));
                        if (TextUtils.equals(friend.getIdentifier(), userProfile.getIdentifier())) {
                            mView.showImUserInfo(friend);
                            return;
                        }
                    }
                    mView.showImUserInfo(userProfile);
                }
            }
        });
    }

    @Override
    void editUserRemark(final String imUserId, String remark) {
        mView.createAndShowDialog();
        RepositoryFactory.getChatRepository().modifyFriend(imUserId, remark, new TIMCallBack() {
            @Override
            public void onError(int i, String s) {
                Log.e("getUsersProfile", "code" + i + "  error result = " + s);
                if (mView != null) {
                    mView.destoryAndDismissDialog();
                }
            }

            @Override
            public void onSuccess() {
                if (mView != null) {
                    mView.hideLoading();
                    Log.d("getUsersProfile", "modifyFriend onSuccess");
                    UserCenter.getAllFriend();
//                    getImUserInfo(imUserId);
                    getFriendLis(imUserId, true);
                }
            }
        });
    }

    @Override
    void showRemarkEditDialog(final String imUserId, String remark) {
        final Dialog dialog = new Dialog(mView.getCurContext());
        View view = LayoutInflater.from(mView.getCurContext()).inflate(R.layout.dialog_modify_remark, null);
        final EditText content = view.findViewById(R.id.etModifyRemark);
        if (StringUtil.isNotEmpty(remark)) {
            content.setText(remark);
        }
        view.findViewById(R.id.tvOk).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = content.getText().toString().trim();
                dialog.dismiss();
                editUserRemark(imUserId, name);
            }
        });
        view.findViewById(R.id.tvCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

            }
        });
        dialog.setContentView(view);
        dialog.show();
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        int w = ScreenUtil.getScreenWidth(mView.getCurActivity());
        int h = ScreenUtil.getScreenHeight(mView.getCurActivity());
        params.width = w / 5 * 4;
        params.height = h / 3;
        dialog.getWindow().setAttributes(params);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }

    @Override
    void showSettingDialog(final String imUid, View view) {
        new XPopup.Builder(mView.getCurContext())
                .atView(view)  // 依附于所点击的View，内部会自动判断在上方或者下方显示
                .asAttachList(new String[]{
                                mView.getCurContext().getString(R.string.delete),
                                mView.getCurContext().getString(R.string.edit_remark),
                                mView.getCurContext().getString(R.string.complaints),
                                mView.getCurContext().getString(R.string.shielding_friends)
                        },
                        new int[]{},
                        new OnSelectListener() {
                            @Override
                            public void onSelect(int position, String text) {
                                if (position == 0) {
                                    deleteFriend(imUid, true);
                                } else if (position == 1) {
                                    mView.showRemarkDialog();
                                } else if (position == 2) {
                                    mView.complaintsUser(imUid);
                                } else {
                                    ShieldingFriend(imUid);

                                }

                            }
                        })
                .show();
    }

    void showSettingDialog2(final String imUid, final View view) {
        new XPopup.Builder(mView.getCurContext())
                .atView(view)  // 依附于所点击的View，内部会自动判断在上方或者下方显示
                .asAttachList(new String[]{
                                mView.getCurContext().getString(R.string.delete),
                                mView.getCurContext().getString(R.string.edit_remark),
                                mView.getCurContext().getString(R.string.complaints),
                                mView.getCurContext().getString(R.string.shielding_friends),
                                mView.getCurContext().getString(R.string.tim_psersonal_info2)
                        },
                        new int[]{},
                        new OnSelectListener() {
                            @Override
                            public void onSelect(int position, String text) {
                                if (position == 0) {
                                    deleteFriend(imUid, true);
                                } else if (position == 1) {
                                    mView.showRemarkDialog();
                                } else if (position == 2) {
                                    mView.complaintsUser(imUid);
                                } else if (position == 3) {
                                    ShieldingFriend(imUid);
                                } else {
                                    //推荐个人名片
                                    sendFriendsPersonalInfo(imUid, view);
                                }

                            }
                        })
                .show();
    }


    private void sendFriendsPersonalInfo(String imUid, View view) {
        ElemExtModel model = new ElemExtModel();
        TIMUserProfile timUserProfile;
        if (UserCenter.getUserInfo().getIdentifier().equals(imUid)) {
            //自己
            timUserProfile = TIMFriendshipManager.getInstance().querySelfProfile();
        } else {
            //好友
            timUserProfile = TIMFriendshipManager.getInstance().queryUserProfile(imUid);
        }
        model.setMessageType(ElemExtModel.SHARE_PERSONAL_CARD);
        model.setName(timUserProfile.getNickName());
        model.setRemark(timUserProfile.getNickName());
        model.setIdentifier(timUserProfile.getIdentifier());
        model.setFaceUrl(timUserProfile.getFaceUrl());
        model.settIMUserProfile(JsonUtil.moderToString(timUserProfile));
        mView.getCurActivity().startActivity(FriendsSelectActivity.newIntent(view.getContext(), JsonUtil.moderToString(model), ElemExtModel.SHARE_PERSONAL_CARD, "1"));
    }

    private void ShieldingFriend(final String imUid) {
        addBlackListIM(imUid);
    }

    @Override
    void deleteFriend(final String imUid, final Boolean show) {
        Log.e("imUid:" + imUid, "Boolean:" + show);
        mView.showLoading();
        RepositoryFactory.getRemoteRepository().delFriend(imUid)
                .compose(RxScheduler.<ResponseModel<Object>>toMain())
                .as(mView.<ResponseModel<Object>>bindAutoDispose())
                .subscribe(new CommonObserver<Object>() {
                    @Override
                    public void onSuccess(Object data) {
//                        Log.e("deleteFriendonSuccess:", "" + data.toString());
                        mView.hideLoading();
                        if (show) {
                            mView.deleteFriendSuccess();
                        }
                        DBFriendRequestModel model = DBDaoFactory.getFriendRequestDao().queryModel(imUid);
                        DBDaoFactory.getFriendRequestDao().deleteModel(model);
                    }

                    @Override
                    public void onError(ResponseModel data) {
//                        Log.e("deleteFriendononError:", "" + data.toString());
                        if (data.getCode() == 10004)
                            deleteIM(imUid);
                        else {
                            CommonToast.toast(data.getMsg());
                            mView.hideLoading();
                        }

                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                        mView.hideLoading();
                    }
                });

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
                        if (mView != null)
                            mView.hideLoading();
                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                        if (mView != null)
                            mView.hideLoading();
                    }
                });
    }

    private void deleteIM(final String imUid) {
        RepositoryFactory.getChatRepository().deleteFriend(imUid, true, new TIMValueCallBack() {
            @Override
            public void onError(int i, String s) {
                mView.hideLoading();
            }

            @Override
            public void onSuccess(Object o) {
                DBFriendRequestModel model = DBDaoFactory.getFriendRequestDao().queryModel(imUid);
                DBDaoFactory.getFriendRequestDao().deleteModel(model);
                mView.hideLoading();
                mView.deleteFriendSuccess();
            }
        });
    }

    private void addBlackListIM(final String imUid) {
        List<String> id = new ArrayList<>();
        id.add(imUid);
        RepositoryFactory.getChatRepository().addBlackList(id, new TIMValueCallBack() {
            @Override
            public void onError(int i, String s) {
                Log.e("addBlackListIM:" + imUid, "addBlackListIM:" + imUid);
                mView.hideLoading();
            }

            @Override
            public void onSuccess(Object o) {
                mView.hideLoading();
                mView.blackFriendSuccess();
                deleteFriends(imUid, false);
            }
        });
    }

    private void deleteFriends(final String imUid, final Boolean show) {
        Log.e("imUid:" + imUid, "Boolean:" + show);
        mView.showLoading();
        RepositoryFactory.getRemoteRepository().delFriends(imUid)
//                .compose(RxScheduler.<ResponseModel<Object>>toMain())
//                .as(mView.<ResponseModel<Object>>bindAutoDispose())
//                .subscribe(new CommonObserver<Object>() {
//                    @Override
//                    public void onSuccess(Object data) {
//                        Log.e("deleteFriendsonSuccess:",""+data.toString());
//                        mView.hideLoading();
//                        if (show) {
//                            mView.deleteFriendSuccess();
//                        }
//                        DBFriendRequestModel model = DBDaoFactory.getFriendRequestDao().queryModel(imUid);
//                        DBDaoFactory.getFriendRequestDao().deleteModel(model);
//                    }
//
//                    @Override
//                    public void onError(ResponseModel data) {
//                        Log.e("deleteFriendsononError:",""+data.toString());
//                        if (data.getCode() == 10004)
//                            deleteIM(imUid);
//                        else {
//                            CommonToast.toast(data.getMsg());
//                            mView.hideLoading();
//                        }
//
//                    }
//                });
                .compose(RxScheduler.toMain())
                .as(mView.bindAutoDispose())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) {
                        Log.e("deleteFriendsonSuccess:", "" + o.toString());
                        mView.hideLoading();
                        if (show) {
                            mView.deleteFriendSuccess();
                        }
                        DBFriendRequestModel model = DBDaoFactory.getFriendRequestDao().queryModel(imUid);
                        DBDaoFactory.getFriendRequestDao().deleteModel(model);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                        Log.e("deleteFriendsononError", "" + throwable.toString());
                    }
                });

    }

    public void refreshUserSig(String id) {
        RepositoryFactory.getRemoteRepository().refreshUserSig(id)
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


    public void getFriendLis(final String imUserId, final boolean isChangeNickname) {
        TIMFriendshipManager.getInstance().getFriendList(new TIMValueCallBack<List<TIMFriend>>() {
            @Override
            public void onError(int i, String s) {
                if (mView != null) {
                    mView.destoryAndDismissDialog();
                }
                CommonToast.toast("code：".concat(String.valueOf(i)).concat(" error：".concat(s)));
            }

            @Override
            public void onSuccess(List<TIMFriend> timFriends) {
                for (TIMFriend friend : timFriends) {
                    if (TextUtils.equals(friend.getIdentifier(), imUserId)) {
                        //获取用户昵称
                        if (StringUtil.isNotEmpty(friend.getRemark())) {
                            String nickname = friend.getRemark();
                            if (mView != null) {
                                mView.setNickname(nickname, isChangeNickname);
                                mView.destoryAndDismissDialog();
                            }
                        } else {
                            if (StringUtil.isNotEmpty(friend.getTimUserProfile().getNickName())) {
                                String nickname = friend.getTimUserProfile().getNickName();
                                if (mView != null) {
                                    mView.setNickname(nickname, isChangeNickname);
                                    mView.destoryAndDismissDialog();
                                }
                            }
                        }

                    }
                }
            }
        });
    }
}
