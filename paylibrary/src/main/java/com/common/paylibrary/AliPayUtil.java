package com.common.paylibrary;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.alipay.sdk.app.AuthTask;
import com.alipay.sdk.app.PayTask;
import com.common.paylibrary.listener.ThirdLoginListener;
import com.common.paylibrary.model.AliPayModel;
import com.common.paylibrary.model.PayExtModel;
import com.common.paylibrary.model.UsagePayMode;
import com.library.common.utils.CommonToast;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by WYiang on 2018/1/29.
 */

public class AliPayUtil {

    private static final int SDK_PAY_FLAG = 1;
    private static final int FLAG_ALIPAY_LOGIN = 2;
    private static final int FOOD_PAY_FLAG = 3;
    private Activity activity;
    private PayExtModel payExtModel;

    private ThirdLoginListener loginListener;

    private AliPayUtil(Activity activity) {
        this.activity = activity;
    }


    public static AliPayUtil with(Activity activity) {
        return new AliPayUtil(activity);
    }


    /**
     * 发起支付
     *
     * @param usagePayMode 支付种类 {@link com.common.paylibrary.model.UsagePayMode}
     * @param payModel     内部处理支付相关信息
     */
    public void doPay(UsagePayMode usagePayMode, AliPayModel payModel) {
        if (payModel == null) return;
        payExtModel = new PayExtModel(usagePayMode, payModel.getTransferId());
        final String orderInfo = payModel.getUrl();
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                PayTask alipay = new PayTask(activity);
                Map<String, String> result = alipay.payV2(orderInfo, true);
                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    /**
     * 美食支付
     */
    public void doPay(UsagePayMode usagePayMode,String payInfo) {
        payExtModel = new PayExtModel(usagePayMode, "");
        Runnable payRunnable = () -> {
            PayTask alipay = new PayTask(activity);
            Map<String, String> result = alipay.payV2(payInfo, true);
            Message msg = new Message();
            msg.what = FOOD_PAY_FLAG;
            msg.obj = result;
            mHandler.sendMessage(msg);
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    /**
     * 发起支付
     *
     * @param usagePayMode 支付种类 {@link com.common.paylibrary.model.UsagePayMode}
     * @param payModel     内部处理支付相关信息
     * @param money        判断是否有发布红包
     */
    public void doPay(UsagePayMode usagePayMode, AliPayModel payModel, String money) {
        if (payModel == null) return;
        payExtModel = new PayExtModel(usagePayMode, payModel.getTransferId());
        payExtModel.setObj(money);
        final String orderInfo = payModel.getUrl();
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                PayTask alipay = new PayTask(activity);
                Map<String, String> result = alipay.payV2(orderInfo, true);
                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    public void doLogin(final String info, ThirdLoginListener listener) {
        loginListener = listener;
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                // 构造AuthTask 对象
                AuthTask authTask = new AuthTask(activity);
                // 调用授权接口，获取授权结果
                Map<String, String> result = authTask.authV2(info, true);
                Message msg = new Message();
                msg.what = FLAG_ALIPAY_LOGIN;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }


    /**
     * 支付结果
     */
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            int what = msg.what;
            Map<String, String> result = (Map<String, String>) msg.obj;
            String resultStatus = result.get("resultStatus");
            if (what == FLAG_ALIPAY_LOGIN) {
                //登录
                if (TextUtils.equals(resultStatus, "9000")) {
                    String re = result.get("result");
                    Map<String, Object> map = getMap(re);
                    String openId = (String) map.get("alipay_open_id");
                    String authToken = (String) map.get("auth_code");
                    loginListener.onSuccess(openId, authToken);
                }
            } else if (what == FOOD_PAY_FLAG){
                if (TextUtils.equals(resultStatus, "9000")) {
                    String message = payExtModel.toString();
                    PayConfig.sendPayReceiver(message);
                } else {
                    CommonToast.toast("支付失败");
                }
            }else {
                //支付
                if (TextUtils.equals(resultStatus, "9000")) {
                    String message;
                    message = payExtModel.toString();
                    PayConfig.sendPayReceiver(message);
                    Log.d("hanlz","支付成功");
                } else {
                    CommonToast.toast("支付失败");
                }
            }
        }
    };

    private Map<String, Object> getMap(String result) {
        String[] strs = result.split("&");
        Map<String, Object> map = new HashMap<>();
        for (String s : strs) {
            String[] ss = s.split("=");
            map.put(ss[0], ss[1]);
        }
        return map;
    }


}
