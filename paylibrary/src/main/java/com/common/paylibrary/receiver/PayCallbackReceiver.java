package com.common.paylibrary.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.common.paylibrary.listener.PayCallback;
import com.common.paylibrary.model.PayExtModel;
import com.library.common.utils.JsonUtil;

/**
 * Created by yiang on 2018/4/23.
 */

public class PayCallbackReceiver extends BroadcastReceiver {

    public static final String MG_PAY_ACTION = "mg_pay_action";
    private PayCallback payCallback;

    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();
        if (TextUtils.equals(action, MG_PAY_ACTION)) {
            String ext = intent.getStringExtra("ext");
            PayExtModel extModel = JsonUtil.strToModel(TextUtils.isEmpty(ext)?"":ext, PayExtModel.class);
            payCallback.payResult(extModel);
        }

    }

    public void setWxCallback(PayCallback wxCallback) {
        if (wxCallback == null) return;
        this.payCallback = wxCallback;
    }
}
