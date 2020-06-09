package com.fjx.mg.me.level.list;

import androidx.annotation.NonNull;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.fjx.mg.R;
import com.fjx.mg.me.invite.InviteActivity;
import com.library.common.utils.CommonToast;
import com.library.common.utils.StringUtil;
import com.library.repository.core.net.CommonObserver;
import com.library.repository.core.net.RxScheduler;
import com.library.repository.models.InviteListModel;
import com.library.repository.models.ResponseModel;
import com.library.repository.repository.RepositoryFactory;

public class InviteListPresenter extends InviteListContract.Presenter {

    private MaterialDialog dialog;
    private boolean hasShowDialog;

    public InviteListPresenter(InviteListContract.IView view) {
        super(view);
    }

    @Override
    void myInvite(int page) {
        RepositoryFactory.getRemoteAccountRepository().myInvite(page)
                .compose(RxScheduler.<ResponseModel<InviteListModel>>toMain())
                .as(mView.<ResponseModel<InviteListModel>>bindAutoDispose())
                .subscribe(new CommonObserver<InviteListModel>() {
                    @Override
                    public void onSuccess(InviteListModel data) {
                        mView.showInviteList(data);
                        showInviteNumDialog(data.getInviteNum());
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        if (StringUtil.isNotEmpty(data.getMsg())) {
                            CommonToast.toast(data.getMsg());
                        }
                        mView.loadError();
                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                        mView.loadError();
                        CommonToast.toast(data.getMsg());
                    }
                });

    }

    @Override
    void showInviteNumDialog(String num) {
        if (hasShowDialog) return;


        String content = mView.getCurContext().getString(R.string.cur_invite_num).concat(num)
                .concat(mView.getCurContext().getString(R.string.invite_or_not));
        hasShowDialog = true;
        dialog = new MaterialDialog.Builder(mView.getCurActivity())
                .title(com.library.common.R.string.att)
                .content(content)
                .cancelable(false)
                .positiveText(R.string.go_invite)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        mView.getCurActivity().startActivity(InviteActivity.newInstance(mView.getCurContext()));
                        dialog.dismiss();
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                })
                .negativeText(R.string.cancel).build();

        dialog.show();
    }


}
