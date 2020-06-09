package com.common.paylibrary;

import com.common.paylibrary.model.PayExtModel;
import com.common.paylibrary.model.UsagePayMode;
import com.common.paylibrary.model.WXPayModel;
import com.library.common.utils.CommonToast;
import com.library.common.utils.ContextManager;
import com.tencent.mm.opensdk.modelbiz.OpenWebview;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * Created by 15 on 2017/9/22.
 */

public class WXPayUtil {
    public static IWXAPI msgApi;

    private WXPayUtil() {
        init();
    }

    public static WXPayUtil newInstance() {
        return new WXPayUtil();
    }


    private void init() {
        if (msgApi == null) {
            msgApi = WXAPIFactory.createWXAPI(ContextManager.getContext(), null);
            msgApi.registerApp(PayConfig.WX_APPID);
        }
    }

    public void OpenWebview() {
        if (!msgApi.isWXAppInstalled()) {
            CommonToast.toast("微信未安装");
            return;
        }

        OpenWebview.Req req = new OpenWebview.Req();
        req.url = "https://api.mch.weixin.qq.com/papay/entrustweb";
        msgApi.sendReq(req);
    }

    public void doPay(WXPayModel payBean) {
        if (!msgApi.isWXAppInstalled()) {
            CommonToast.toast("微信未安装");
            return;
        }
        PayReq request = new PayReq();
        request.appId = payBean.getAppid();
        request.partnerId = payBean.getPartnerid();
        request.prepayId = payBean.getPrepayid();
        request.packageValue = payBean.getPackageX();
        request.nonceStr = payBean.getNoncestr();
        request.timeStamp = payBean.getTimestamp();
        request.sign = payBean.getSign();
        PayExtModel model = new PayExtModel(UsagePayMode.food_pay, "");
        request.extData = model.toString();

        msgApi.sendReq(request);
    }

    public void doPay(UsagePayMode type, WXPayModel payBean) {
        if (!msgApi.isWXAppInstalled()) {
            CommonToast.toast("微信未安装");
            return;
        }
        PayReq request = new PayReq();
        request.appId = payBean.getAppid();
        request.partnerId = payBean.getPartnerid();
        request.prepayId = payBean.getPrepayid();
//        request.packageValue = "Sign=WXPay";
        request.packageValue = payBean.getPackageX();
        request.nonceStr = payBean.getNoncestr();
        request.timeStamp = payBean.getTimestamp();
        request.sign = payBean.getSign();
        PayExtModel model = new PayExtModel(type, payBean.getTransferId());
        request.extData = model.toString();

        msgApi.sendReq(request);
    }

    public void doPayGroup(UsagePayMode type, WXPayModel payBean) {
        if (!msgApi.isWXAppInstalled()) {
            CommonToast.toast("微信未安装");
            return;
        }
        PayReq request = new PayReq();
        request.appId = payBean.getAppid();
        request.partnerId = payBean.getPartnerid();
        request.prepayId = payBean.getPrepayid();
//        request.packageValue = "Sign=WXPay";
        request.packageValue = payBean.getPackageX();
        request.nonceStr = payBean.getNoncestr();
        request.timeStamp = payBean.getTimestamp();
        request.sign = payBean.getSign();
        PayExtModel model = new PayExtModel(type, payBean.getrId());
        request.extData = model.toString();
        msgApi.sendReq(request);
    }


    public void doLogin() {
        if (!msgApi.isWXAppInstalled()) {

        } else {
            final SendAuth.Req req = new SendAuth.Req();
            req.scope = "snsapi_userinfo";
            req.state = "wechat_sdk_demo_test";
            msgApi.sendReq(req);
        }
    }

}
