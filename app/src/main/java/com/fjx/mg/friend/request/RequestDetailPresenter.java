package com.fjx.mg.friend.request;

import android.util.Log;
import android.widget.Toast;

import com.library.common.utils.CommonToast;
import com.library.common.utils.JsonUtil;
import com.library.repository.core.net.CommonObserver;
import com.library.repository.core.net.RxScheduler;
import com.library.repository.data.UserCenter;
import com.library.repository.db.DBDaoFactory;
import com.library.repository.db.model.DBFriendRequestModel;
import com.library.repository.models.ResponseModel;
import com.library.repository.repository.RepositoryFactory;
import com.tencent.imsdk.TIMUserProfile;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.friendship.TIMFriendResult;

import java.util.List;

public class RequestDetailPresenter extends RequestDetailContract.Presenter {

    public RequestDetailPresenter(RequestDetailContract.View view) {
        super(view);
    }

    @Override
    void getImUserInfo(String imUserId) {
        mView.showLoading();
        RepositoryFactory.getChatRepository().getUsersProfile(imUserId, true, new TIMValueCallBack<List<TIMUserProfile>>() {
            @Override
            public void onError(int i, String s) {
                Log.d("getUsersProfile", s);
                mView.hideLoading();
            }

            @Override
            public void onSuccess(List<TIMUserProfile> timUserProfiles) {
                Log.d("getUsersProfile", JsonUtil.moderToString(timUserProfiles));
                mView.hideLoading();
                mView.showImUserInfo(timUserProfiles.get(0));
            }
        });
    }

    @Override
    void doResponse(final String uid, String remark) {
        mView.showLoading();
        RepositoryFactory.getChatRepository().doResponse(uid, remark, new TIMValueCallBack<TIMFriendResult>() {
            @Override
            public void onError(int i, String s) {
                Log.d("doResponse", s);
                mView.hideLoading();
            }

            @Override
            public void onSuccess(TIMFriendResult timFriendResult) {
                UserCenter.getAllFriend();
                Log.d("doResponse", JsonUtil.moderToString(timFriendResult));
                confirmAddFriend(uid);
                mView.hideLoading();
                mView.doResponseResult(true);
                DBFriendRequestModel model = DBDaoFactory.getFriendRequestDao().queryModel(uid);
                if (model != null) {
                    model.setStatus(1);
                    DBDaoFactory.getFriendRequestDao().insertModel(model);
                }

            }
        });
    }

    @Override
    void doResponseReject(final String uid, String remark) {
        mView.showLoading();
        RepositoryFactory.getChatRepository().doResponseReject(uid, remark, new TIMValueCallBack<TIMFriendResult>() {
            @Override
            public void onError(int i, String s) {
                Log.d("doResponse", s);
                mView.hideLoading();
            }

            @Override
            public void onSuccess(TIMFriendResult timFriendResult) {
                Log.d("doResponse", JsonUtil.moderToString(timFriendResult));
                mView.hideLoading();
                mView.doResponseResult(false);
                DBFriendRequestModel model = DBDaoFactory.getFriendRequestDao().queryModel(uid);
                if (model != null) {
                    model.setStatus(2);
                    DBDaoFactory.getFriendRequestDao().insertModel(model);
                }

            }
        });
    }

    @Override
    void confirmAddFriend(String phone) {
        RepositoryFactory.getRemoteRepository().confirmFriend(phone)
                .compose(RxScheduler.<ResponseModel<Object>>toMain())
                .as(mView.<ResponseModel<Object>>bindAutoDispose())
                .subscribe(new CommonObserver<Object>() {
                    @Override
                    public void onSuccess(Object data) {

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

}
