package com.fjx.mg.main.fragment.me;


import android.text.TextUtils;
import android.util.Log;

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
import com.library.repository.models.UserInfoModel;
import com.library.repository.repository.RepositoryFactory;
import com.tencent.imsdk.TIMUserProfile;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.friendship.TIMFriend;

import java.util.List;

public class MePresenter extends MeContract.Presenter {


    MePresenter(MeContract.View view) {
        super(view);
    }

    @Override
    void getUserProfile() {
        if (mView == null) return;
        if (!UserCenter.hasLogin()) return;
        RepositoryFactory.getRemoteAccountRepository().getUserProfile()
                .compose(RxScheduler.<ResponseModel<UserInfoModel>>toMain())
                .as(mView.<ResponseModel<UserInfoModel>>bindAutoDispose())
                .subscribe(new CommonObserver<UserInfoModel>() {
                    @Override
                    public void onSuccess(UserInfoModel data) {
                        UserInfoModel model = UserCenter.getUserInfo();
                        data.setToken(model.getToken());
                        data.setUseRig(model.getUseRig());
                        UserCenter.saveUserInfo(data);
                        if (mView != null)
                            mView.showUserInfo(data);
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

    }


    @Override
    void getAllFriend(TIMUserProfile userProfile) {
//        TIMFriend friend = UserCenter.getFriend(userProfile.getIdentifier());
        if (mView != null)
            mView.isMe(new ImUserRelaM(userProfile, false));
    }

    void get(String page) {
        RepositoryFactory.getRemoteRepository()
                .getCityCircle("1")
                .compose(RxScheduler.<ResponseModel<CityCircleListModel>>toMain())
                .as(mView.<ResponseModel<CityCircleListModel>>bindAutoDispose())
                .subscribe(new CommonObserver<CityCircleListModel>() {
                    @Override
                    public void onSuccess(CityCircleListModel model) {
                        if (model != null && mView != null) {
                            mView.requestIde(model);
                        }
                    }

                    @Override
                    public void onError(ResponseModel data) {
//                        CommonToast.toast(data.getMsg());
                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                        mView.hideLoading();
                    }
                });
    }

    @Override
    void findUser(String identifier) {
        if (identifier.contains("mgm") || identifier.contains("MGM")) {
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
                            if (mView != null)
                                mView.hideLoading();
                            CommonToast.toast(data.getMsg());
                        }

                        @Override
                        public void onNetError(ResponseModel data) {
                            if (mView != null)
                                mView.hideLoading();
                            CommonToast.toast(data.getMsg());
                        }
                    });
        }
    }
}
