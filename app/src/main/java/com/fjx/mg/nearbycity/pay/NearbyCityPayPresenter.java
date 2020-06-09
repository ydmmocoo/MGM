package com.fjx.mg.nearbycity.pay;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

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
import com.fjx.mg.utils.HttpUtil;
import com.library.common.constant.IntentConstants;
import com.library.common.fingerprint.FingerprintIdentify;
import com.library.common.fingerprint.base.BaseFingerprint;
import com.library.common.utils.CommonToast;
import com.library.common.utils.ContextManager;
import com.library.common.utils.StringUtil;
import com.library.common.view.editdialog.PayFragment;
import com.library.common.view.editdialog.PayPwdView;
import com.library.repository.Constant;
import com.library.repository.core.net.CommonObserver;
import com.library.repository.core.net.RxScheduler;
import com.library.repository.data.UserCenter;
import com.library.repository.db.DBDaoFactory;
import com.library.repository.db.model.FingerprintModel;
import com.library.repository.models.AddByBalanceModel;
import com.library.repository.models.BalanceModel;
import com.library.repository.models.ConvertModel;
import com.library.repository.models.NearbyCityGetMoneyModel;
import com.library.repository.models.ResponseModel;
import com.library.repository.models.UserInfoModel;
import com.library.repository.repository.RepositoryFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class NearbyCityPayPresenter extends NearbyCityPayContract.Presenter {

    private String accountPayMoney;
    private MaterialDialog fingerDialog;
    private boolean waitDataLoading;


    NearbyCityPayPresenter(NearbyCityPayContract.View view) {
        super(view);
    }


    @Override
    void convertMoney(final Map<String, Object> payMap) {//换算接口
        mView.showLoading();
        final String price = (String) payMap.get(IntentConstants.NERBY_CITY_TOLPRICE);
        RepositoryFactory.getRemoteRepository().convert("1", price)
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
//        accountPayMoney = (String) payMap.get(IntentConstants.NERBY_CITY_TOLPRICE);
    }


    ThreadPoolExecutor executor = new ThreadPoolExecutor(3, 5, 30, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(10));
    @SuppressLint("HandlerLeak")
    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                if (msg.obj != null) {
                    AliPayModel payModel = (AliPayModel) msg.obj;
                    AliPayUtil.with(mView.getCurActivity()).doPay(UsagePayMode.nearby_city_pay, payModel);
                }
            } else {
                if (msg.obj != null) {
                    String data = (String) msg.obj;
                    CommonToast.toast(data);
                }
            }
        }
    };

    @Override
    void phoneWxRecharge(final Map<String, Object> payMap) {//用微信支付赏金
        final String cId = (String) payMap.get(IntentConstants.NERBY_CITY_CID);
        final String content = (String) payMap.get(IntentConstants.NERBY_CITY_CONTENT);
        final String images = (String) payMap.get(IntentConstants.NERBY_CITY_IMAGES);
        final String eId = (String) payMap.get(IntentConstants.NERBY_CITY_EID);
        final String topDays = (String) payMap.get(IntentConstants.NERBY_CITY_TOPDAYS);
        final String perPrice = (String) payMap.get(IntentConstants.NERBY_CITY_PERPRICE);
        mView.showLoading();
        if (StringUtil.isNotEmpty(content) || StringUtil.isNotEmpty(images) || StringUtil.isNotEmpty(eId)) {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    Map<String, String> map = new HashMap<>();
                    map.put("cId", cId);
                    map.put("content", content);
                    map.put("images", images);
                    map.put("eId", eId);
                    map.put("topDays", topDays == null ? "" : topDays);
                    map.put("perPrice", perPrice);
                    new HttpUtil().sendPost(Constant.HOST.concat("CityCircle/addByWx"), map, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            if (e instanceof SocketTimeoutException || e instanceof ConnectException) {
                                //连接超时
                                Message msg = Message.obtain();
                                msg.obj = ContextManager.getContext().getString(R.string.error_socket_fail);
                                msg.what = 2;
                                handler.sendMessage(msg);
                            }
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            if (mView != null)
                                mView.hideLoading();
                            try {
                                String string = response.body().string();
                                JSONObject object = new JSONObject(string);
                                int code = object.optInt("code");
                                String msg = object.optString("msg");
                                if (10000 == code) {
                                    //成功
                                    JSONObject data = object.optJSONObject("data");
                                    WXPayModel model = new WXPayModel();
                                    model.setAppid(data.optString("appid"));
                                    model.setPartnerid(data.optString("partnerid"));
                                    model.setPrepayid(data.optString("prepayid"));
                                    model.setPackageX(data.optString("package"));
                                    model.setNoncestr(data.optString("noncestr"));
                                    model.setTimestamp(data.optString("timestamp"));
                                    model.setSign(data.optString("sign"));
                                    model.setTransferId(object.optString("sId"));
                                    WXPayUtil.newInstance().doPay(UsagePayMode.nearby_city_pay, model);
                                } else {
                                    //失败
                                    Message m = Message.obtain();
                                    m.obj = msg;
                                    m.what = 2;
                                    handler.sendMessage(m);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            });
        } else {
            RepositoryFactory.getRemoteNearbyCitysApi()
                    .setTopByWx(cId, topDays, Integer.valueOf(perPrice))
                    .compose(RxScheduler.<ResponseModel<WXPayModel>>toMain())
                    .as(mView.<ResponseModel<WXPayModel>>bindAutoDispose())
                    .subscribe(new CommonObserver<WXPayModel>() {
                        @Override
                        public void onSuccess(WXPayModel data) {
                            mView.hideLoading();
//                        setQuestion(data.getOrderId(), payMap, 0, null, data);
                            WXPayUtil.newInstance().doPay(UsagePayMode.nearby_city_set_top_pay, data);
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

    @Override
    void phoneZFBRecharge(final Map<String, Object> payMap) {//用支付宝支付赏金

        final String cId = (String) payMap.get(IntentConstants.NERBY_CITY_CID);
        final String content = (String) payMap.get(IntentConstants.NERBY_CITY_CONTENT);
        final String images = (String) payMap.get(IntentConstants.NERBY_CITY_IMAGES);
        final String eId = (String) payMap.get(IntentConstants.NERBY_CITY_EID);
        final String topDays = (String) payMap.get(IntentConstants.NERBY_CITY_TOPDAYS);
        final String perPrice = (String) payMap.get(IntentConstants.NERBY_CITY_PERPRICE);
        mView.showLoading();
        if (StringUtil.isNotEmpty(content) || StringUtil.isNotEmpty(images) || StringUtil.isNotEmpty(eId)) {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    Map<String, String> map = new HashMap<>();
                    map.put("cId", cId);
                    map.put("content", content);
                    map.put("images", images);
                    map.put("eId", eId);
                    map.put("topDays", topDays == null ? "" : topDays);
                    map.put("perPrice", perPrice);
                    new HttpUtil().sendPost(Constant.HOST.concat("CityCircle/addByAli"), map, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            if (e instanceof SocketTimeoutException || e instanceof ConnectException) {
                                //连接超时
                                Message m = Message.obtain();
                                m.obj = ContextManager.getContext().getString(R.string.error_socket_fail);
                                m.what = 2;
                                handler.sendMessage(m);
                            }
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            if (mView != null)
                                mView.hideLoading();
                            try {
                                String string = response.body().string();
                                JSONObject object = new JSONObject(string);
                                int code = object.optInt("code");
                                String msg = object.optString("msg");
                                String sId = object.optString("sId");
                                String data = object.optString("data");
                                if (10000 == code) {
                                    //成功
                                    mView.hideLoading();
                                    AliPayModel payModel = new AliPayModel();
                                    payModel.setUrl(data);
                                    payModel.setTransferId(sId);
                                    Message m = Message.obtain();
                                    m.what = 1;
                                    m.obj = payModel;
                                    handler.sendMessage(m);
                                } else {
                                    //失败
                                    Message m = Message.obtain();
                                    m.obj = msg;
                                    m.what = 2;
                                    handler.sendMessage(m);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            });
        } else {
            RepositoryFactory.getRemoteNearbyCitysApi()
                    .setTopByAli(cId, topDays, Integer.valueOf(perPrice))
                    .compose(RxScheduler.<ResponseModel<String>>toMain())
                    .as(mView.<ResponseModel<String>>bindAutoDispose())
                    .subscribe(new CommonObserver<String>() {
                        @Override
                        public void onSuccess(String data) {
                            mView.hideLoading();
                            AliPayModel payModel = new AliPayModel();
                            payModel.setUrl(data);
                            AliPayUtil.with(mView.getCurActivity()).doPay(UsagePayMode.nearby_city_set_top_pay, payModel);
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
                            CommonToast.toast(data.getMsg());
                        }
                    });
        }

    }

    @Override
    void phoneBalanceRecharge(final Map<String, Object> payMap) {//用余额支付赏金

        String cId = (String) payMap.get(IntentConstants.NERBY_CITY_CID);
        String content = (String) payMap.get(IntentConstants.NERBY_CITY_CONTENT);
        String images = (String) payMap.get(IntentConstants.NERBY_CITY_IMAGES);
        final String eId = (String) payMap.get(IntentConstants.NERBY_CITY_EID);
        String topDays = (String) payMap.get(IntentConstants.NERBY_CITY_TOPDAYS);
        String perPrice = (String) payMap.get(IntentConstants.NERBY_CITY_PERPRICE);

        mView.showLoading();
        if (StringUtil.isNotEmpty(content) || StringUtil.isNotEmpty(images) || StringUtil.isNotEmpty(eId)) {
            RepositoryFactory.getRemoteNearbyCitysApi()
                    .addByBalance(cId, content, images, eId, topDays, Integer.valueOf(perPrice))
                    .compose(RxScheduler.<ResponseModel<AddByBalanceModel>>toMain())
                    .as(mView.<ResponseModel<AddByBalanceModel>>bindAutoDispose())
                    .subscribe(new CommonObserver<AddByBalanceModel>() {
                        @Override
                        public void onSuccess(AddByBalanceModel data) {
                            mView.hideLoading();
                            PayExtModel extModel = new PayExtModel(UsagePayMode.nearby_city_pay);
                            extModel.setMessage(data.getsId());
                            PayConfig.sendPayReceiver(extModel.toString());
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
                            if (mView != null)
                                mView.hideLoading();
                            CommonToast.toast(data.getMsg());
                        }
                    });
        } else {
            RepositoryFactory.getRemoteNearbyCitysApi()
                    .setTopByBalance(cId, topDays, Integer.valueOf(perPrice))
                    .compose(RxScheduler.<ResponseModel<Object>>toMain())
                    .as(mView.<ResponseModel<Object>>bindAutoDispose())
                    .subscribe(new CommonObserver<Object>() {
                        @Override
                        public void onSuccess(Object data) {
                            mView.hideLoading();
                            PayExtModel extModel = new PayExtModel(UsagePayMode.nearby_city_set_top_pay);
                            PayConfig.sendPayReceiver(extModel.toString());
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


    @Override
    void setNearbyCityPriceBy(int payType, Map<String, Object> payMap) {//正常支付流程调用
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
                        mView.hideLoading();
                        fragment.dismiss();
                        phoneBalanceRecharge(payMap);
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

    @Override
    void requestMoney(String sId, final UsagePayMode type) {
        RepositoryFactory.getRemoteNearbyCitysApi()
                .getMoney(sId)
                .compose(RxScheduler.<ResponseModel<NearbyCityGetMoneyModel>>toMain())
                .as(mView.<ResponseModel<NearbyCityGetMoneyModel>>bindAutoDispose())
                .subscribe(new CommonObserver<NearbyCityGetMoneyModel>() {
                    @Override
                    public void onSuccess(NearbyCityGetMoneyModel data) {
                        if (mView != null) {
                            mView.responsetMoney(data.getMoney(), type);
                        }
                    }

                    @Override
                    public void onError(ResponseModel data) {

                    }

                    @Override
                    public void onNetError(ResponseModel data) {

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
        bundle.putString(PayFragment.EXTRA_CONTENT, "支付：" + accountPayMoney);
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
