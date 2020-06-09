package com.fjx.mg.scan;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.fjx.mg.R;
import com.fjx.mg.setting.password.pay.ModifyPayPwdActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.library.common.utils.CommonToast;
import com.library.common.utils.StringUtil;
import com.library.common.view.editdialog.PayFragment;
import com.library.common.view.editdialog.PayPwdView;
import com.library.repository.Constant;
import com.library.repository.core.net.CommonObserver;
import com.library.repository.core.net.RxScheduler;
import com.library.repository.data.UserCenter;
import com.library.repository.models.AgentInfoModel;
import com.library.repository.models.ResponseModel;
import com.library.repository.models.UserInfoModel;
import com.library.repository.repository.RepositoryFactory;

import io.reactivex.functions.Consumer;

class ScanSuccessfulPresenter extends ScanSuccessfulContract.Presenter {
    ScanSuccessfulPresenter(ScanSuccessfulContract.View view) {
        super(view);
    }

    @Override
    void chargeUserBalance(final String price, final String servicePrice, final String payCode) {
        mView.createAndShowDialog();
        RepositoryFactory.getRemoteRepository()
                .chargeUserBalance(payCode, price)
                .compose(RxScheduler.<ResponseModel<Object>>toMain())
                .as(mView.<ResponseModel<Object>>bindAutoDispose())
                .subscribe(new CommonObserver<Object>() {
                    @Override
                    public void onSuccess(Object model) {
                        mView.destoryAndDismissDialog();
                        SendUser(price, servicePrice, payCode, Constant.PayStatus.SUCC, true);
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        mView.destoryAndDismissDialog();
                        SendUser(price, servicePrice, payCode, Constant.PayStatus.FAILE, false);
                        CommonToast.toast(data.getMsg());
                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                        mView.destoryAndDismissDialog();
                        SendUser(price, servicePrice, payCode, Constant.PayStatus.FAILE, false);
                        CommonToast.toast(data.getMsg());
                    }
                });
    }

    @Override
    void showPayPasswordDiaog(String price) {//打开密码支付弹窗
        int isSet = UserCenter.getUserInfo().getIsSetPayPsw();
        if (isSet != 1) {
            showSetPayPwdDialog();
            return;
        }
        Bundle bundle = new Bundle();
        if (TextUtils.isEmpty(price)) {
            CommonToast.toast(mView.getCurContext().getString(R.string.loading));
            return;
        }
        bundle.putString(PayFragment.EXTRA_CONTENT, mView.getCurActivity().getString(R.string.whitdrlawal).concat("：") + price);
        final PayFragment fragment = new PayFragment();
        fragment.setArguments(bundle);
        fragment.setPaySuccessCallBack(new PayPwdView.InputCallBack() {
            @Override
            public void onInputFinish(String result) {
                String payPwd = StringUtil.getPassword(result);
                ScanSuccessfulPresenter.this.checkPayPasswoed(payPwd, fragment);
            }
        });
        fragment.show(mView.fragmentManager(), "Pay");
    }

    @Override
    void getAgentInfo(String payCode) {
        mView.createAndShowDialog();
        RepositoryFactory.getRemoteRepository()
                .getAgentInfo(payCode)
                .compose(RxScheduler.<ResponseModel<AgentInfoModel>>toMain())
                .as(mView.<ResponseModel<AgentInfoModel>>bindAutoDispose())
                .subscribe(new CommonObserver<AgentInfoModel>() {
                    @Override
                    public void onSuccess(AgentInfoModel model) {
                        if (mView != null) {
                            mView.destoryAndDismissDialog();
                            mView.ShowAgentInfo(model);
                        }
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        if (mView != null)
                            mView.destoryAndDismissDialog();
                        CommonToast.toast(data.getMsg());
                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                        if (mView != null)
                            mView.destoryAndDismissDialog();
                        CommonToast.toast(data.getMsg());
                    }
                });
    }

    private void showSetPayPwdDialog() {//未设置密码弹窗提示千万设置密码
        MaterialDialog dialog = new MaterialDialog.Builder(mView.getCurActivity())
                .title(R.string.attention)
                .content(R.string.hint_pay_password)
                .cancelable(false)
                .positiveText(R.string.go_set)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog1, @NonNull DialogAction which) {
                        mView.getCurContext().startActivity(ModifyPayPwdActivity.newInstance(mView.getCurContext()));
                        dialog1.dismiss();
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog12, @NonNull DialogAction which) {
                        dialog12.dismiss();
                    }
                })
                .negativeText(R.string.cancel).build();

        dialog.show();
    }

    private void checkPayPasswoed(String password, final PayFragment fragment) {//验证支付密码
        mView.createAndShowDialog();
        RepositoryFactory.getRemotePayRepository().checkPayPassword(password)
                .compose(RxScheduler.<ResponseModel<String>>toMain())
                .as(mView.<ResponseModel<String>>bindAutoDispose())
                .subscribe(new CommonObserver<String>() {
                    @Override
                    public void onSuccess(String data) {
                        if (mView != null) {
                            mView.destoryAndDismissDialog();
                            mView.DoWithDrawal();
                        }
                        fragment.dismiss();
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        if (mView != null)
                            mView.destoryAndDismissDialog();
                        CommonToast.toast(data.getMsg());
                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                        if (mView != null)
                            mView.destoryAndDismissDialog();
                        CommonToast.toast(data.getMsg());
                    }
                });

    }

    @Override
    void SendUser(String price, String servicePrice, String payCode, String status, final Boolean isFinish) {//发送通知
        RepositoryFactory.getRemoteAccountRepository().sendUser(price, servicePrice, payCode, status)
                .compose(RxScheduler.toMain())
                .as(mView.bindAutoDispose())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        Gson gson = new Gson();
                        ResponseModel statusLs = gson.fromJson(o.toString(), new TypeToken<ResponseModel>() {
                        }.getType());
                        if (statusLs.getCode() == 10000 && isFinish) {
                            mView.WorkManSuccess(true);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                        Log.e("accept失败", "" + throwable.toString());
                    }
                });
    }

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
                        mView.showUserInfo(null);
                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                    }
                });

    }
}
