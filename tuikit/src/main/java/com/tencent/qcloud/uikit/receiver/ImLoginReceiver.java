package com.tencent.qcloud.uikit.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

public class ImLoginReceiver extends BroadcastReceiver {

    private IMLogin imLogin;

    public static final String MG_LOGIN_SUCCESS = "mg_im_login_success";
    public static final String MG_LOGIN_FAILED = "mg_im_login_failed";


    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if (imLogin == null) return;
        if (TextUtils.equals(action, MG_LOGIN_SUCCESS)) {
            imLogin.loginSuccess();
        } else {
            imLogin.loginFailed();
        }
    }

    public void setImLogin(IMLogin imLogin) {
        this.imLogin = imLogin;
    }

    public interface IMLogin {
        void loginSuccess();

        void loginFailed();
    }
}
