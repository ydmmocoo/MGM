package com.fjx.mg.me.safe_center.check.identity_question;

import com.library.common.utils.CommonToast;
import com.library.repository.core.net.CommonObserver;
import com.library.repository.core.net.RxScheduler;
import com.library.repository.data.UserCenter;
import com.library.repository.models.AuthQuestionModel;
import com.library.repository.models.ResponseModel;
import com.library.repository.repository.RepositoryFactory;

import java.util.Map;

public class IdentityQuestionAuthPresenter extends IdentityQuestionAuthContract.Presenter {


    IdentityQuestionAuthPresenter(IdentityQuestionAuthContract.View view) {
        super(view);
    }

    @Override
    void getSecurityIssue() {
        mView.showLoading();
        RepositoryFactory.getRemoteAccountRepository().getSecurityIssue()
                .compose(RxScheduler.<ResponseModel<AuthQuestionModel>>toMain())
                .as(mView.<ResponseModel<AuthQuestionModel>>bindAutoDispose())
                .subscribe(new CommonObserver<AuthQuestionModel>() {
                    @Override
                    public void onSuccess(AuthQuestionModel data) {
                        mView.hideLoading();
                        mView.showAuthQuestionModel(data);
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
    void check(Map<String, Object> map) {
        mView.showLoading();
        RepositoryFactory.getRemoteAccountRepository().check(map)
                .compose(RxScheduler.<ResponseModel<Object>>toMain())
                .as(mView.<ResponseModel<Object>>bindAutoDispose())
                .subscribe(new CommonObserver<Object>() {
                    @Override
                    public void onSuccess(Object data) {
                        mView.hideLoading();
                        mView.checkSuccess();

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
    void bindDevice() {
        mView.showLoading();
        String phone = UserCenter.getUserInfo().getPhone();
        RepositoryFactory.getRemoteAccountRepository().bindDevice(phone)
                .compose(RxScheduler.<ResponseModel<Object>>toMain())
                .as(mView.<ResponseModel<Object>>bindAutoDispose())
                .subscribe(new CommonObserver<Object>() {
                    @Override
                    public void onSuccess(Object data) {
                        mView.hideLoading();
                        mView.bindDeviceSuccess();
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
