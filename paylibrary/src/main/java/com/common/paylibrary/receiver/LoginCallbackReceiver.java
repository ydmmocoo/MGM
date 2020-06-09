package com.common.paylibrary.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.common.paylibrary.listener.PayCallback;
import com.common.paylibrary.listener.ThirdLoginListener;
import com.common.paylibrary.model.PayExtModel;
import com.library.common.utils.JsonUtil;

/**
 * Created by yiang on 2018/4/23.
 */

public class LoginCallbackReceiver extends BroadcastReceiver {

    public static final String MG_LOGIN_ACTION = "mg_login_action";
    private ThirdLoginListener payCallback;

    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();
        if (TextUtils.equals(action, MG_LOGIN_ACTION)) {
            String code = intent.getStringExtra("code");
            payCallback.onSuccess("", code);
        }

    }

    public void setWxCallback(ThirdLoginListener wxCallback) {
        if (wxCallback == null) return;
        this.payCallback = wxCallback;
    }


}
