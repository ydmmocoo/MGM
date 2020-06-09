package com.fjx.mg.me.level;

import com.library.common.utils.CommonToast;
import com.library.common.utils.StringUtil;
import com.library.repository.core.net.CommonObserver;
import com.library.repository.core.net.RxScheduler;
import com.library.repository.data.UserCenter;
import com.library.repository.models.LevelHomeModel;
import com.library.repository.models.ResponseModel;
import com.library.repository.models.UserInfoModel;
import com.library.repository.repository.RepositoryFactory;

public class LevelHomePresenter extends LevelHomeContract.Presenter {
    public LevelHomePresenter(LevelHomeContract.IView view) {
        super(view);
    }

    @Override
    void getUserRank() {
        mView.showLoading();
        RepositoryFactory.getRemoteAccountRepository().getUserRank()
                .compose(RxScheduler.<ResponseModel<LevelHomeModel>>toMain())
                .as(mView.<ResponseModel<LevelHomeModel>>bindAutoDispose())
                .subscribe(new CommonObserver<LevelHomeModel>() {
                    @Override
                    public void onSuccess(LevelHomeModel data) {
                        UserInfoModel infoModel = UserCenter.getUserInfo();
                        infoModel.setRank(data.getUserInfo().getRank());
                        UserCenter.saveUserInfo(infoModel);
                        mView.hideLoading();
                        mView.showLevelHomeModel(data);
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        mView.hideLoading();
                        if (StringUtil.isNotEmpty(data.getMsg())) {
                            CommonToast.toast(data.getMsg());
                        }
                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                        mView.hideLoading();
                        if (StringUtil.isNotEmpty(data.getMsg())) {
                            CommonToast.toast(data.getMsg());
                        }
                    }
                });

    }


}
