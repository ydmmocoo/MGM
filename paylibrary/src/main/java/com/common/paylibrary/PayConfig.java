package com.common.paylibrary;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import com.common.paylibrary.listener.PayCallback;
import com.common.paylibrary.listener.ThirdLoginListener;
import com.common.paylibrary.receiver.LoginCallbackReceiver;
import com.common.paylibrary.receiver.PayCallbackReceiver;
import com.library.common.utils.ContextManager;

public class PayConfig {
    public static final String ALI_APPID = "2019050664344871";

    public static final String WX_APPID = "wxe342a8e739a2fc93";
    public static final String WX_AAPPSECRET = "90dd12a30ae41678c9810f34c01916eb";


    public static void sendPayReceiver(String ext) {
        Intent intent = new Intent();
        intent.setAction(PayCallbackReceiver.MG_PAY_ACTION);
        intent.putExtra("ext", ext);
        ContextManager.getContext().sendBroadcast(intent);
    }

    public static PayCallbackReceiver registPayReceiver(Activity activity, PayCallback payCallback) {
        PayCallbackReceiver payCallbackReceiver = new PayCallbackReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(PayCallbackReceiver.MG_PAY_ACTION);
        payCallbackReceiver.setWxCallback(payCallback);
        activity.registerReceiver(payCallbackReceiver, intentFilter);
        return payCallbackReceiver;
    }

    public static void sendLoginReceiver(String code) {
        Intent intent = new Intent();
        intent.setAction(LoginCallbackReceiver.MG_LOGIN_ACTION);
        intent.putExtra("code", code);
        ContextManager.getContext().sendBroadcast(intent);
    }

    public static LoginCallbackReceiver registLoginReceiver(Activity activity, ThirdLoginListener payCallback) {
        LoginCallbackReceiver payCallbackReceiver = new LoginCallbackReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(LoginCallbackReceiver.MG_LOGIN_ACTION);
        payCallbackReceiver.setWxCallback(payCallback);
        activity.registerReceiver(payCallbackReceiver, intentFilter);
        return payCallbackReceiver;
    }
}
