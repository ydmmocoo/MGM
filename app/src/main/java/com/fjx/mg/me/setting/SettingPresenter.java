package com.fjx.mg.me.setting;

import android.content.DialogInterface;
import android.text.TextUtils;

import com.fjx.mg.R;
import com.fjx.mg.utils.DialogUtil;
import com.library.common.utils.CommonToast;
import com.library.common.utils.StringUtil;
import com.library.repository.core.net.CommonObserver;
import com.library.repository.core.net.RxScheduler;
import com.library.repository.data.UserCenter;
import com.library.repository.models.ResponseModel;
import com.library.repository.models.VersionModel;
import com.library.repository.repository.RepositoryFactory;

public class SettingPresenter extends SettingContract.Presenter {

    SettingPresenter(SettingContract.View view) {
        super(view);
    }

    @Override
    void logout() {
        if (UserCenter.hasLogin()) {
            new DialogUtil().showAlertDialog(mView.getCurContext(), R.string.tips, R.string.confitm_logout, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    exit();
                }
            });
        } else {
            UserCenter.logout();
        }
    }

    public void exit() {
        UserCenter.logout2Settings();
    }

    @Override
    void checkVersion() {
        mView.createAndShowDialog();
        RepositoryFactory.getRemoteRepository().checkVersion("2")
                .compose(RxScheduler.<ResponseModel<VersionModel>>toMain())
                .as(mView.<ResponseModel<VersionModel>>bindAutoDispose())
                .subscribe(new CommonObserver<VersionModel>() {
                    @Override
                    public void onSuccess(VersionModel data) {
                        mView.destoryAndDismissDialog();
                        if (TextUtils.isEmpty(data.getDownUrl())) {
                            CommonToast.toast(mView.getCurContext().getString(R.string.newest_version));
                            return;
                        }
                        mView.showUpdateDialog(data);
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        mView.destoryAndDismissDialog();
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
