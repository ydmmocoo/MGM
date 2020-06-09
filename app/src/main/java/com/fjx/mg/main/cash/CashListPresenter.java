package com.fjx.mg.main.cash;


import androidx.annotation.NonNull;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.fjx.mg.R;
import com.fjx.mg.me.invite.InviteActivity;
import com.google.gson.JsonObject;
import com.library.common.utils.CommonToast;
import com.library.repository.core.net.CommonObserver;
import com.library.repository.core.net.RxScheduler;
import com.library.repository.models.CashListModel;
import com.library.repository.models.ResponseModel;
import com.library.repository.repository.RepositoryFactory;

class CashListPresenter extends CashListContract.Presenter {

    private MaterialDialog dialog;

    CashListPresenter(CashListContract.View view) {
        super(view);
    }

    @Override
    void getCashList(int page, int statues) {
        RepositoryFactory.getRemoteAccountRepository().getCashList(page, statues)
                .compose(RxScheduler.<ResponseModel<CashListModel>>toMain())
                .as(mView.<ResponseModel<CashListModel>>bindAutoDispose())
                .subscribe(new CommonObserver<CashListModel>() {
                    @Override
                    public void onSuccess(CashListModel data) {
                        mView.showCashList(data);

                    }

                    @Override
                    public void onError(ResponseModel data) {
                        CommonToast.toast(data.getMsg());
                        mView.loadError();
                    }

                    @Override
                    public void onNetError(ResponseModel data) {

                    }
                });
    }

    @Override
    void reciveCash(String id, final int position) {
        mView.showLoading();
        RepositoryFactory.getRemoteAccountRepository().reciveCash(id)
                .compose(RxScheduler.<ResponseModel<JsonObject>>toMain())
                .as(mView.<ResponseModel<JsonObject>>bindAutoDispose())
                .subscribe(new CommonObserver<JsonObject>() {
                    @Override
                    public void onSuccess(JsonObject data) {
                        mView.hideLoading();
                        String balance = data.get("balance").getAsString();
                        mView.showReciveCashResult(balance, position);

                    }

                    @Override
                    public void onError(ResponseModel data) {
                        CommonToast.toast(data.getMsg());
                        mView.hideLoading();
                    }

                    @Override
                    public void onNetError(ResponseModel data) {

                    }
                });
    }

    @Override
    void batchReciveCash() {
        mView.showLoading();
        RepositoryFactory.getRemoteAccountRepository().batchReciveCash()
                .compose(RxScheduler.<ResponseModel<JsonObject>>toMain())
                .as(mView.<ResponseModel<JsonObject>>bindAutoDispose())
                .subscribe(new CommonObserver<JsonObject>() {
                    @Override
                    public void onSuccess(JsonObject data) {
                        String balance = data.get("balance").getAsString();
                        mView.showBatchReciveCashResult(balance);
                        mView.hideLoading();
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        mView.hideLoading();
                        if (data.getCode() == 10005) {
                            showDialog();
                            return;
                        }
                        CommonToast.toast(data.getMsg());
                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                        mView.hideLoading();
                        CommonToast.toast(data.getMsg());
                    }
                });
    }

    private void showDialog() {

        dialog = new MaterialDialog.Builder(mView.getCurActivity())
                .title(R.string.attention)
                .content(R.string.hint_invite_user)
                .cancelable(false)
                .positiveText(R.string.go_invite)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        mView.getCurContext().startActivity(InviteActivity.newInstance(mView.getCurContext()));
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
