package com.fjx.mg.main.payment.ask.askpay;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.fjx.mg.setting.password.pay.ModifyPayPwdActivity;
import com.library.common.fingerprint.FingerprintIdentify;
import com.library.common.fingerprint.base.BaseFingerprint;
import com.library.common.utils.CommonToast;
import com.library.common.utils.StringUtil;
import com.library.common.view.editdialog.PayFragment;
import com.library.common.view.editdialog.PayPwdView;
import com.library.repository.Constant;
import com.library.repository.core.net.CommonObserver;
import com.library.repository.core.net.RxScheduler;
import com.library.repository.data.UserCenter;
import com.library.repository.db.DBDaoFactory;
import com.library.repository.db.model.FingerprintModel;
import com.library.repository.models.BalanceModel;
import com.library.repository.models.ConvertModel;
import com.library.repository.models.ResponseModel;
import com.library.repository.models.UserInfoModel;
import com.library.repository.repository.RepositoryFactory;

import java.util.Map;

public class AskPayPresenter extends AskPayContract.Presenter {

    private String accountPayMoney;
    private MaterialDialog fingerDialog;
    private boolean waitDataLoading;


    AskPayPresenter(AskPayContract.View view) {
        super(view);
    }


    @Override
    void convertMoney(final Map<String, Object> payMap) {//换算接口
        mView.showLoading();
        final String price = (String) payMap.get("price");
        RepositoryFactory.getRemoteRepository().convert("3", price)
                .compose(RxScheduler.<ResponseModel<ConvertModel>>toMain())
                .as(mView.<ResponseModel<ConvertModel>>bindAutoDispose())
                .subscribe(new CommonObserver<ConvertModel>() {
                    @Override
                    public void onSuccess(ConvertModel data) {
                        mView.hideLoading();
                        String CNYPrice = data.getCNYPrice();
                        String servicePrice = data.getServicePrice();
                        String totalPrice = data.getTotalPrice();
                        accountPayMoney = totalPrice;
                        if (!TextUtils.isEmpty(accountPayMoney) && waitDataLoading) {//如果换算还没完成就点击微信支付或者支付宝支付，等待换算完成直接调动本地支付。
                            showPayCheckDialog(payMap);
                        }

                        mView.convertMoneySuccess(CNYPrice, servicePrice, totalPrice);
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        mView.hideLoading();
                    }

                    @Override
                    public void onNetError(ResponseModel data) {

                    }
                });
    }


    @Override
    void phoneWxRecharge(final Map<String, Object> payMap) {//用微信支付赏金
        String question = (String) payMap.get("question");
        String desc = (String) payMap.get("desc");
        String urls = (String) payMap.get("urls");
        String price = (String) payMap.get("price");
        mView.showLoading();
        RepositoryFactory.getRemotePayRepository().setQuestionPriceByWx(price, question, desc, urls)
                .compose(RxScheduler.<ResponseModel<WXPayModel>>toMain())
                .as(mView.<ResponseModel<WXPayModel>>bindAutoDispose())
                .subscribe(new CommonObserver<WXPayModel>() {
                    @Override
                    public void onSuccess(WXPayModel data) {
                        mView.hideLoading();
//                        setQuestion(data.getOrderId(), payMap, 0, null, data);
                        WXPayUtil.newInstance().doPay(UsagePayMode.question_pay, data);
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        mView.hideLoading();
                        CommonToast.toast(data.getMsg());
                    }

                    @Override
                    public void onNetError(ResponseModel data) {

                    }
                });
    }

    @Override
    void phoneZFBRecharge(final Map<String, Object> payMap) {//用支付宝支付赏金
        String question = (String) payMap.get("question");
        String desc = (String) payMap.get("desc");
        String urls = (String) payMap.get("urls");
        String price = (String) payMap.get("price");
        mView.showLoading();
        RepositoryFactory.getRemotePayRepository().setQuestionPriceByAli(price, question, desc, urls)
                .compose(RxScheduler.<ResponseModel<AliPayModel>>toMain())
                .as(mView.<ResponseModel<AliPayModel>>bindAutoDispose())
                .subscribe(new CommonObserver<AliPayModel>() {
                    @Override
                    public void onSuccess(AliPayModel data) {
                        mView.hideLoading();
                        AliPayUtil.with(mView.getCurActivity()).doPay(UsagePayMode.question_pay, data);
//                        setQuestion(data.getOrderId(), payMap, 1, data, null);

                    }

                    @Override
                    public void onError(ResponseModel data) {
                        mView.hideLoading();
                        CommonToast.toast(data.getMsg());
                    }

                    @Override
                    public void onNetError(ResponseModel data) {

                    }
                });
    }

    @Override
    void phoneBalanceRecharge(final Map<String, Object> payMap) {//用余额支付赏金

        String question = (String) payMap.get("question");
        String desc = (String) payMap.get("desc");
        String urls = (String) payMap.get("urls");
        String price = (String) payMap.get("price");


        mView.showLoading();
        RepositoryFactory.getRemotePayRepository().setQuestionPriceByBalance(price, question, desc, urls)
                .compose(RxScheduler.<ResponseModel<Object>>toMain())
                .as(mView.<ResponseModel<Object>>bindAutoDispose())
                .subscribe(new CommonObserver<Object>() {
                    @Override
                    public void onSuccess(Object data) {
                        mView.hideLoading();
                        PayExtModel extModel = new PayExtModel(UsagePayMode.question_pay);
                        PayConfig.sendPayReceiver(extModel.toString());
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        mView.hideLoading();
                        CommonToast.toast(data.getMsg());

                    }

                    @Override
                    public void onNetError(ResponseModel data) {

                    }
                });

    }

    private void setQuestion(String orderId, Map<String, Object> payMap, final int from, final AliPayModel alipaydata, final WXPayModel wxpdata) {
        String question = (String) payMap.get("question");
        String desc = (String) payMap.get("desc");
        String urls = (String) payMap.get("urls");
        RepositoryFactory.getRemoteRepository().setQuestion(orderId, question, desc, urls)
                .compose(RxScheduler.<ResponseModel<Object>>toMain())
                .as(mView.<ResponseModel<Object>>bindAutoDispose())
                .subscribe(new CommonObserver<Object>() {
                    @Override
                    public void onSuccess(Object data) {
                        mView.hideLoading();
                        switch (from) {
                            case 0:
                                WXPayUtil.newInstance().doPay(UsagePayMode.question_pay, wxpdata);
                                break;
                            case 1:
                                AliPayUtil.with(mView.getCurActivity()).doPay(UsagePayMode.question_pay, alipaydata);
                                break;

                            case 2:
                                PayExtModel extModel = new PayExtModel(UsagePayMode.question_pay);
                                PayConfig.sendPayReceiver(extModel.toString());
                                break;
                        }
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        mView.hideLoading();
                        CommonToast.toast(data.getMsg());
                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                        mView.hideLoading();
                    }
                });
    }

    @Override
    void setQuestionPriceBy(int payType, Map<String, Object> payMap) {//正常支付流程调用
        switch (payType) {
            case Constant.PayType.ACCOUNT:
                showPayCheckDialog(payMap);
                break;
            case Constant.PayType.ZFB:
                phoneZFBRecharge(payMap);
                break;
            case Constant.PayType.WX:
                phoneWxRecharge(payMap);
                break;
        }
    }


    @Override
    void checkPayPasswoed(String password, final PayFragment fragment, final Map<String, Object> payMap) {//验证支付密码
        mView.showLoading();
        RepositoryFactory.getRemotePayRepository().checkPayPassword(password)
                .compose(RxScheduler.<ResponseModel<String>>toMain())
                .as(mView.<ResponseModel<String>>bindAutoDispose())
                .subscribe(new CommonObserver<String>() {
                    @Override
                    public void onSuccess(String data) {
                        if (mView != null)
                            mView.hideLoading();
                        fragment.dismiss();
                        phoneBalanceRecharge(payMap);
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        if (mView != null)
                            mView.hideLoading();
                        CommonToast.toast(data.getMsg());
                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                        if (mView != null)
                            mView.hideLoading();
                        CommonToast.toast(data.getMsg());
                    }
                });

    }


    @Override
    void getUserBalance() {//获取用户余额
        mView.showLoading();
        RepositoryFactory.getRemoteAccountRepository().getUserBalance()
                .compose(RxScheduler.<ResponseModel<BalanceModel>>toMain())
                .as(mView.<ResponseModel<BalanceModel>>bindAutoDispose())
                .subscribe(new CommonObserver<BalanceModel>() {
                    @Override
                    public void onSuccess(BalanceModel data) {
                        String balance = data.getBalance();
                        if (TextUtils.isEmpty(balance)) balance = "0";
                        mView.getBalanceSuccess(balance);
                        mView.hideLoading();
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


    private void showPayCheckDialog(final Map<String, Object> payMap) {//选择默认支付方式
        boolean isFingerEnable = isFingerEnable();
        if (isFingerEnable) {
            showFingerprintDialog(payMap);
        } else {
            showPayPasswordDiaog(payMap);
        }
    }

    private boolean isFingerEnable() {//检测是否开启指纹支付
        UserInfoModel infoModel = UserCenter.getUserInfo();
        if (infoModel == null) return false;
        String uid = infoModel.getUId();
        FingerprintModel model = DBDaoFactory.getFingerprintDao().queryModel(uid);
        if (model == null) return false;
        return model.getFingerEnable();
    }

    private void showPayPasswordDiaog(final Map<String, Object> payMap) {//打开密码支付弹窗
        int isSet = UserCenter.getUserInfo().getIsSetPayPsw();
        if (isSet != 1) {
            showSetPayPwdDialog();
            return;
        }
        Bundle bundle = new Bundle();
        if (TextUtils.isEmpty(accountPayMoney)) {
            CommonToast.toast(mView.getCurContext().getString(R.string.loading));
            waitDataLoading = true;
            return;
        }
        bundle.putString(PayFragment.EXTRA_CONTENT, mView.getCurActivity().getString(R.string.pay).concat("：") + accountPayMoney);
        final PayFragment fragment = new PayFragment();
        fragment.setArguments(bundle);
        fragment.setPaySuccessCallBack(new PayPwdView.InputCallBack() {
            @Override
            public void onInputFinish(String result) {
                String payPwd = StringUtil.getPassword(result);
                checkPayPasswoed(payPwd, fragment, payMap);
            }
        });
        fragment.show(mView.fragmentManager(), "Pay");
        waitDataLoading = false;
    }


    /**
     * 指纹识别认证
     */
    private void showFingerprintDialog(final Map<String, Object> payMap) {//指纹支付弹窗
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
        tvPrice.setText(accountPayMoney);
        if (TextUtils.isEmpty(accountPayMoney)) {
            CommonToast.toast(mView.getCurContext().getString(R.string.loading));
            waitDataLoading = true;
            return;
        }
        tvPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fingerDialog.dismiss();
                showPayPasswordDiaog(payMap);
            }
        });

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fingerDialog.dismiss();
            }
        });
        initFingerpring(payMap);
        fingerDialog.show();
        waitDataLoading = false;
    }

    private void showSetPayPwdDialog() {//未设置密码弹窗提示千万设置密码
        MaterialDialog dialog = new MaterialDialog.Builder(mView.getCurActivity())
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

    private void initFingerpring(final Map<String, Object> payMap) {//指纹验证回调处理，余额支付才会触发。
        FingerprintIdentify mFingerprintIdentify = new FingerprintIdentify(mView.getCurContext().getApplicationContext());
        mFingerprintIdentify.setSupportAndroidL(true);
        mFingerprintIdentify.setExceptionListener(new BaseFingerprint.ExceptionListener() {
            @Override
            public void onCatchException(Throwable exception) {
            }
        });
        mFingerprintIdentify.init();
        mFingerprintIdentify.startIdentify(10, new BaseFingerprint.IdentifyListener() {
            @Override
            public void onSucceed() {// 验证成功，自动结束指纹识别
                CommonToast.toast(mView.getCurContext().getString(R.string.auth_access));
                if (fingerDialog != null) fingerDialog.dismiss();
                fingerDialog.dismiss();
                phoneBalanceRecharge(payMap);
            }

            @Override
            public void onNotMatch(int availableTimes) {// 指纹不匹配，并返回可用剩余次数并自动继续验证
                CommonToast.toast(mView.getCurContext().getString(R.string.auth_no_access));
            }

            @Override
            public void onFailed(boolean isDeviceLocked) {// 错误次数达到上限或者API报错停止了验证，自动结束指纹识别
                CommonToast.toast(mView.getCurContext().getString(R.string.busy_try_later));
                showPayPasswordDiaog(payMap);
            }

            @Override
            public void onStartFailedByDeviceLocked() {// 第一次调用startIdentify失败，因为设备被暂时锁定
                CommonToast.toast(mView.getCurContext().getString(R.string.busy_try_later));
                showPayPasswordDiaog(payMap);
            }
        });
    }


}
