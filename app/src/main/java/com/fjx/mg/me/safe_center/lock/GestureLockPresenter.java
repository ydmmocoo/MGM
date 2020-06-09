package com.fjx.mg.me.safe_center.lock;

import android.util.Log;

import com.fjx.mg.login.login.LoginActivity;
import com.fjx.mg.view.lock.GestureLockLayout;
import com.library.common.utils.CommonToast;
import com.library.common.utils.StringUtil;
import com.library.repository.core.net.CommonObserver;
import com.library.repository.core.net.NetCode;
import com.library.repository.core.net.RxScheduler;
import com.library.repository.data.UserCenter;
import com.library.repository.models.ResponseModel;
import com.library.repository.models.UserInfoModel;
import com.library.repository.repository.RepositoryFactory;

import java.util.List;

public class GestureLockPresenter extends GestureLockContract.Presenter {

    GestureLockPresenter(GestureLockContract.View view) {
        super(view);
    }

    @Override
    void setOnLockResetListener(GestureLockLayout lockLayout) {
        lockLayout.setOnLockResetListener(new GestureLockLayout.OnLockResetListener() {
            @Override
            public void onConnectCountUnmatched(int connectCount, int minCount) {

            }

            @Override
            public void onFirstPasswordFinished(List<Integer> answerList) {
                mView.onFirstPasswordFinished(answerList);
            }

            @Override
            public void onSetPasswordFinished(boolean isMatched, List<Integer> answerList) {
                String answer = "";
                for (Integer i : answerList) {
                    answer = answer.concat(String.valueOf(i));
                }
                Log.d("onSetPasswordFinished", answer + "---" + isMatched);
                mView.onSetPasswordFinished(isMatched, answer);
            }
        });
    }

    @Override
    void setOnLockVerifyListener(GestureLockLayout lockLayout) {
        lockLayout.setOnLockVerifyListener(new GestureLockLayout.OnLockVerifyListener() {
            @Override
            public void onGestureSelected(int id) {
                //每选中一个点时调用
            }

            @Override
            public void onGestureFinished(boolean isMatched) {
                //绘制手势解锁完成时调用
                mView.onGestureFinished(isMatched);
            }

            @Override
            public void onGestureTryTimesBoundary() {
                //超出最大尝试次数时调用
                mView.onGestureTryTimesBoundary();
            }
        });
    }

    @Override
    void setGestureCode(String gCode) {
        gCode = StringUtil.getPassword(gCode);
        String uid = "";
        UserInfoModel userinfo = UserCenter.getUserInfo();
        if (userinfo != null) uid = userinfo.getUId();

        mView.showLoading();
        final String finalGCode = gCode;
        RepositoryFactory.getRemoteAccountRepository().setGestureCode(gCode, uid)
                .compose(RxScheduler.<ResponseModel<Object>>toMain())
                .as(mView.<ResponseModel<Object>>bindAutoDispose())
                .subscribe(new CommonObserver<Object>() {
                    @Override
                    public void onSuccess(Object data) {
                        UserInfoModel model = UserCenter.getUserInfo();
                        model.setGestureCode(finalGCode);
                        UserCenter.saveUserInfo(model);
                        mView.hideLoading();
                        mView.setGestureCodeSuccess();
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        if (StringUtil.isNotEmpty(data.getMsg())) {
                            CommonToast.toast(data.getMsg());
                        }
                        mView.getCurActivity().finish();
                        mView.hideLoading();
                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                        mView.hideLoading();
                    }
                });
    }

    @Override
    void refreshToken(String gCode) {

        mView.showLoading();
        String uid = UserCenter.getUserInfo().getUId();
        RepositoryFactory.getRemoteAccountRepository().refreshToken(gCode, uid)
                .compose(RxScheduler.<ResponseModel<UserInfoModel>>toMain())
                .as(mView.<ResponseModel<UserInfoModel>>bindAutoDispose())
                .subscribe(new CommonObserver<UserInfoModel>() {
                    @Override
                    public void onSuccess(UserInfoModel data) {
                        UserCenter.saveUserInfo(data);
//                        UserCenter.loginTim();
                        UserCenter.imLogin();
                        if (mView != null) {
                            mView.hideLoading();
                            mView.getCurActivity().finish();
                            //手势成功后刷新手势密码判断
                            NetCode.isShowGestureLockActivity = false;
                        }
//                        mView.successFinishAtc();
                    }

                    @Override
                    public void onSetPassword() {
                        super.onSetPassword();
                        mView.getCurActivity().startActivity(LoginActivity.newInstance(mView.getCurActivity(), true));
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        CommonToast.toast(data.getMsg());
                        mView.hideLoading();
                        mView.getCurActivity().finish();
                        mView.getCurActivity().startActivity(LoginActivity.newInstance(mView.getCurActivity(), true));
//                        mView.successFinishAtc();
                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                        mView.hideLoading();
                    }
                });
    }
}
