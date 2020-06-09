package com.fjx.mg.friend.imuser;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.fjx.mg.R;
import com.fjx.mg.me.transfer.MeTransferActivity;
import com.fjx.mg.setting.feedback.FeedBackActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.library.common.utils.CommonToast;
import com.library.common.utils.JsonUtil;
import com.library.repository.core.net.CommonObserver;
import com.library.repository.core.net.RxScheduler;
import com.library.repository.data.UserCenter;
import com.library.repository.db.DBDaoFactory;
import com.library.repository.db.model.DBFriendRequestModel;
import com.library.repository.models.FriendContactSectionModel;
import com.library.repository.models.ResponseModel;
import com.library.repository.repository.RepositoryFactory;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnInputConfirmListener;
import com.lxj.xpopup.interfaces.OnSelectListener;
import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMUserProfile;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.friendship.TIMFriend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.functions.Consumer;

public class ImUserDetailPresenter extends ImUserDetailContract.Presenter {

    public ImUserDetailPresenter(ImUserDetailContract.View view) {
        super(view);
    }

    @Override
    void getImUserInfo(String imUserId) {
        mView.createAndShowDialog();
        RepositoryFactory.getChatRepository().getUsersProfile(imUserId, true, new TIMValueCallBack<List<TIMUserProfile>>() {
            @Override
            public void onError(int i, String s) {
                Log.d("getUsersProfile", s);
                mView.destoryAndDismissDialog();
            }

            @Override
            public void onSuccess(List<TIMUserProfile> timUserProfiles) {
                Log.d("getUsersProfile", JsonUtil.moderToString(timUserProfiles));
                getAllFriend(timUserProfiles.get(0));
            }
        });
    }

    @Override
    void getAllFriend(final TIMUserProfile userProfile) {
        RepositoryFactory.getChatRepository().getAllFriend(new TIMValueCallBack<List<TIMFriend>>() {

            @Override
            public void onError(int i, String s) {
                Log.d("getAllFriend", s);
                mView.destoryAndDismissDialog();
            }

            @Override
            public void onSuccess(List<TIMFriend> timFriends) {
                mView.destoryAndDismissDialog();
                RepositoryFactory.getLocalRepository().saveUserAvatar(timFriends);
                for (TIMFriend friend : timFriends) {

                    if (TextUtils.equals(friend.getIdentifier(), userProfile.getIdentifier())) {
                        mView.showImUserInfo(friend);
                        return;
                    }
                }
                mView.showImUserInfo(userProfile);
            }
        });
    }

    @Override
    void editUserRemark(final String imUserId, String remark) {
        mView.showLoading();
        RepositoryFactory.getChatRepository().modifyFriend(imUserId, remark, new TIMCallBack() {
            @Override
            public void onError(int i, String s) {
                Log.d("getUsersProfile", s);
                mView.destoryAndDismissDialog();

            }

            @Override
            public void onSuccess() {
                mView.hideLoading();
                Log.d("getUsersProfile", "modifyFriend onSuccess");
                UserCenter.getAllFriend();
                getImUserInfo(imUserId);
            }
        });
    }

    @Override
    void showRemarkEditDialog(final String imUserId, String remark) {
        new XPopup.Builder(mView.getCurContext()).asInputConfirm(mView.getCurContext().getString(R.string.edit_remark), remark,
                new OnInputConfirmListener() {
                    @Override
                    public void onConfirm(String text) {
                        editUserRemark(imUserId, text);
                    }
                })
                .show();
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
                        Log.e("deleteFriendonSuccess:", "" + data.toString());
                        mView.destoryAndDismissDialog();
                        if (show) {
                            mView.deleteFriendSuccess();
                        }
                        DBFriendRequestModel model = DBDaoFactory.getFriendRequestDao().queryModel(imUid);
                        DBDaoFactory.getFriendRequestDao().deleteModel(model);
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        Log.e("deleteFriendononError:", "" + data.toString());
                        if (data.getCode() == 10004)
                            deleteIM(imUid);
                        else {
                            CommonToast.toast(data.getMsg());
                            mView.destoryAndDismissDialog();
                        }

                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                        mView.destoryAndDismissDialog();
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
                mView.destoryAndDismissDialog();
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
                mView.destoryAndDismissDialog();
                mView.blackFriendSuccess();
                deleteFriends(imUid, false);
            }
        });
    }

    private void deleteFriends(final String imUid, final Boolean show) {
        Log.e("imUid:" + imUid, "Boolean:" + show);
        mView.createAndShowDialog();
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
                        mView.destoryAndDismissDialog();
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
}
