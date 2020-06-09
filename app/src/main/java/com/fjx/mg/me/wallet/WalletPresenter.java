package com.fjx.mg.me.wallet;

import com.library.common.utils.CommonToast;
import com.library.common.utils.StringUtil;
import com.library.repository.core.net.CommonObserver;
import com.library.repository.core.net.RxScheduler;
import com.library.repository.data.UserCenter;
import com.library.repository.models.ResponseModel;
import com.library.repository.models.UserInfoModel;
import com.library.repository.repository.RepositoryFactory;

public class WalletPresenter extends WalletContract.Presenter {

    WalletPresenter(WalletContract.View view) {
        super(view);
    }


    @Override
    void getUserProfile() {
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
                        mView.showUserInfo(data);
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        if (StringUtil.isNotEmpty(data.getMsg())) {
                            CommonToast.toast(data.getMsg());
                        }
                    }

                    @Override
                    public void onNetError(ResponseModel data) {

                    }
                });

    }

}
