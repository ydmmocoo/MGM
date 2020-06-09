package com.fjx.mg.food.presenter;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.common.paylibrary.AliPayUtil;
import com.common.paylibrary.PayConfig;
import com.common.paylibrary.WXPayUtil;
import com.common.paylibrary.model.AliPayModel;
import com.common.paylibrary.model.PayExtModel;
import com.common.paylibrary.model.UsagePayMode;
import com.common.paylibrary.model.WXPayModel;
import com.fjx.mg.R;
import com.fjx.mg.food.contract.ChooseCouponContract;
import com.fjx.mg.food.contract.OrderPayContract;
import com.fjx.mg.pay.PayPresenter;
import com.fjx.mg.setting.password.pay.ModifyPayPwdActivity;
import com.library.common.fingerprint.FingerprintIdentify;
import com.library.common.fingerprint.base.BaseFingerprint;
import com.library.common.utils.CommonToast;
import com.library.common.utils.StringUtil;
import com.library.common.view.editdialog.PayFragment;
import com.library.common.view.editdialog.PayPwdView;
import com.library.repository.core.net.CommonObserver;
import com.library.repository.core.net.RxScheduler;
import com.library.repository.data.UserCenter;
import com.library.repository.db.DBDaoFactory;
import com.library.repository.db.model.FingerprintModel;
import com.library.repository.models.BalanceModel;
import com.library.repository.models.CouponBean;
import com.library.repository.models.ResponseModel;
import com.library.repository.models.UserInfoModel;
import com.library.repository.repository.RepositoryFactory;

import java.util.Map;

/**
 * @author yedeman
 * @date 2020/5/20.
 * email：ydmmocoo@gmail.com
 * description：
 */
public class OrderPayPresenter extends OrderPayContract.Presenter {

    private MaterialDialog dialog;
    private MaterialDialog fingerDialog;
    private FingerprintIdentify mFingerprintIdentify;
    private boolean waitDataLoading;

    public OrderPayPresenter(OrderPayContract.View view) {
        super(view);
    }

    public void checkOrder(String price,String orderId){
        boolean isFingerEnable = isFingerEnable();
        if (isFingerEnable) {
            showFingerprintDialog(price, orderId);
        } else {
            showPayPasswordDialog(price, orderId);
        }
    }

    @Override
    public void getUserBalance() {
        RepositoryFactory.getRemoteAccountRepository().getUserBalance()
                .compose(RxScheduler.<ResponseModel<BalanceModel>>toMain())
                .as(mView.<ResponseModel<BalanceModel>>bindAutoDispose())
                .subscribe(new CommonObserver<BalanceModel>() {
                    @Override
                    public void onSuccess(BalanceModel data) {
                        String balance = data.getBalance();
                        if (TextUtils.isEmpty(balance)) balance = "0";
                        mView.getBalanceSuccess(balance);
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
    public void orderByBalance(String orderId) {
        RepositoryFactory.getRemoteFoodApi().payByBalance(orderId)
                .compose(RxScheduler.<ResponseModel<Object>>toMain())
                .as(mView.<ResponseModel<Object>>bindAutoDispose())
                .subscribe(new CommonObserver<Object>() {
                    @Override
                    public void onSuccess(Object data) {
                        if (mView != null) {
                            mView.balancePaySuccess();
                        }
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
    public void orderByAlipay(String orderId) {
        RepositoryFactory.getRemoteFoodApi().payByAlipay(orderId)
                .compose(RxScheduler.<ResponseModel<String>>toMain())
                .as(mView.<ResponseModel<String>>bindAutoDispose())
                .subscribe(new CommonObserver<String>() {
                    @Override
                    public void onSuccess(String data) {
                        AliPayModel aliPayModel = new AliPayModel();
                        aliPayModel.setUrl(data);
                        AliPayUtil.with(mView.getCurActivity()).doPay(UsagePayMode.food_pay,data);
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
    public void orderByWechat(String orderId) {
        RepositoryFactory.getRemoteFoodApi().payByWechat(orderId)
                .compose(RxScheduler.<ResponseModel<WXPayModel>>toMain())
                .as(mView.<ResponseModel<WXPayModel>>bindAutoDispose())
                .subscribe(new CommonObserver<WXPayModel>() {
                    @Override
                    public void onSuccess(WXPayModel data) {
                        WXPayUtil.newInstance().doPay(data);
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

    private boolean isFingerEnable() {
        UserInfoModel infoModel = UserCenter.getUserInfo();
        if (infoModel == null) return false;
        String uid = infoModel.getUId();
        FingerprintModel model = DBDaoFactory.getFingerprintDao().queryModel(uid);
        if (model == null) return false;
        return model.getFingerEnable();
    }

    private void showPayPasswordDialog(String price,String orderId) {
        int isSet = UserCenter.getUserInfo().getIsSetPayPsw();
        if (isSet != 1) {
            showSetPayPwdDialog();
            return;
        }
        Bundle bundle = new Bundle();
        if (TextUtils.isEmpty(price)) {
            waitDataLoading = true;
            return;
        }
        bundle.putString(PayFragment.EXTRA_CONTENT, mView.getCurActivity().getString(R.string.pay).concat("：") + price);
        final PayFragment fragment = new PayFragment();
        fragment.setArguments(bundle);
        fragment.setPaySuccessCallBack(result -> {
            String payPwd = StringUtil.getPassword(result);
            checkPayPassword(payPwd, fragment,orderId);
        });
        fragment.show(mView.fragmentManager(), "Pay");
        waitDataLoading = false;
    }


    /**
     * 指纹识别认证
     */
    private void showFingerprintDialog(String price,String orderId) {
        fingerDialog = new MaterialDialog.Builder(mView.getCurActivity())
                .customView(R.layout.dialog_finger, true)
                .backgroundColor(ContextCompat.getColor(mView.getCurContext(), R.color.trans))
                .build();
        fingerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View view = fingerDialog.getCustomView();
        if (view == null) return;
        TextView tvPrice = view.findViewById(R.id.tvPrice);
        TextView tvPassword = view.findViewById(R.id.tvPassword);
        ImageView ivClose = view.findViewById(R.id.ivClose);
        tvPrice.setText(price);
        if (TextUtils.isEmpty(price)) {
            CommonToast.toast(mView.getCurContext().getString(R.string.loading));
            waitDataLoading = true;
            return;
        }
        tvPassword.setOnClickListener(v -> {
            fingerDialog.dismiss();
            showPayPasswordDialog(price, orderId);
        });

        ivClose.setOnClickListener(v -> fingerDialog.dismiss());

        initFingerpring(price, orderId);
        fingerDialog.show();
        waitDataLoading = false;
    }


    private void showSetPayPwdDialog() {
        dialog = new MaterialDialog.Builder(mView.getCurActivity())
                .title(R.string.attention)
                .content(R.string.hint_pay_password)
                .cancelable(false)
                .positiveText(R.string.go_set)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        mView.getCurContext().startActivity(ModifyPayPwdActivity.newInstance(mView.getCurContext()));
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

    public void checkPayPassword(String password, final PayFragment fragment,String orderId) {
        if (mView == null) return;
        mView.createAndShowDialog();
        RepositoryFactory.getRemotePayRepository().checkPayPassword(password)
                .compose(RxScheduler.<ResponseModel<String>>toMain())
                .as(mView.<ResponseModel<String>>bindAutoDispose())
                .subscribe(new CommonObserver<String>() {
                    @Override
                    public void onSuccess(String data) {
                        mView.destoryAndDismissDialog();
                        fragment.dismiss();
                        orderByBalance(orderId);
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        mView.destoryAndDismissDialog();
                        CommonToast.toast(data.getMsg());
                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                        mView.destoryAndDismissDialog();
                        CommonToast.toast(data.getMsg());
                    }
                });
    }

    private void initFingerpring(String price,String orderId) {
        mFingerprintIdentify = new FingerprintIdentify(mView.getCurContext().getApplicationContext());
        mFingerprintIdentify.setSupportAndroidL(true);
        mFingerprintIdentify.setExceptionListener(new BaseFingerprint.ExceptionListener() {
            @Override
            public void onCatchException(Throwable exception) {
                Log.d("FingerprintIdentify", exception.getLocalizedMessage());
            }
        });
        mFingerprintIdentify.init();
        mFingerprintIdentify.startIdentify(10, new BaseFingerprint.IdentifyListener() {
            @Override
            public void onSucceed() {
                // 验证成功，自动结束指纹识别
                CommonToast.toast(mView.getCurContext().getString(R.string.auth_access));
                if (fingerDialog != null) fingerDialog.dismiss();
                fingerDialog.dismiss();
                orderByBalance(orderId);
            }

            @Override
            public void onNotMatch(int availableTimes) {
                // 指纹不匹配，并返回可用剩余次数并自动继续验证
                CommonToast.toast(mView.getCurContext().getString(R.string.auth_no_access));
            }

            @Override
            public void onFailed(boolean isDeviceLocked) {
                // 错误次数达到上限或者API报错停止了验证，自动结束指纹识别
                CommonToast.toast(mView.getCurContext().getString(R.string.busy_try_later));
                showPayPasswordDialog(price, orderId);
            }

            @Override
            public void onStartFailedByDeviceLocked() {
                // 第一次调用startIdentify失败，因为设备被暂时锁定
                CommonToast.toast(mView.getCurContext().getString(R.string.busy_try_later));
                showPayPasswordDialog(price, orderId);
            }
        });
    }
}
