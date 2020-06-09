package com.common.paylibrary.api;

import android.app.Activity;

import com.common.paylibrary.AliPayUtil;
import com.common.paylibrary.WXPayUtil;
import com.common.paylibrary.model.AliPayModel;
import com.common.paylibrary.model.UsagePayMode;
import com.common.paylibrary.model.WXPayModel;

public class PayApiImpl implements PayApi {


    @Override
    public void weChatPay(UsagePayMode type, WXPayModel payModel) {
        WXPayUtil.newInstance().doPay(type, payModel);
    }

    @Override
    public void aLiPay(Activity activity, UsagePayMode type, AliPayModel payModel) {
        AliPayUtil.with(activity).doPay( type, payModel);
    }


}
