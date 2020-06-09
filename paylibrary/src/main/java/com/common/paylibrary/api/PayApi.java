package com.common.paylibrary.api;

import android.app.Activity;

import com.common.paylibrary.model.AliPayModel;
import com.common.paylibrary.model.UsagePayMode;
import com.common.paylibrary.model.WXPayModel;

public interface PayApi {

    /**
     * 发起微信支付
     *
     * @param type     支付类型
     * @param payModel 支付账单等其他信息
     */
    void weChatPay(UsagePayMode type, WXPayModel payModel);


    /**
     * 发起支付宝支付
     *
     * @param type     支付类型
     * @param payModel 支付账单等其他类型
     */
    void aLiPay(Activity activity, UsagePayMode type, AliPayModel payModel);
}
