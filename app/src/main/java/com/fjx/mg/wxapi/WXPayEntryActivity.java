package com.fjx.mg.wxapi;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.common.paylibrary.PayConfig;
import com.common.paylibrary.WXPayUtil;
import com.fjx.mg.R;
import com.library.common.utils.CommonToast;
import com.library.common.utils.JsonUtil;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelpay.PayResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;


public class WXPayEntryActivity extends AppCompatActivity implements IWXAPIEventHandler {

    protected IWXAPI api;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        api = WXPayUtil.msgApi;
        api.handleIntent(getIntent(), this);
        super.onCreate(savedInstanceState);

    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }


    @Override
    public void onResp(BaseResp resp) {
        Log.d("WXPayEntryActivity", JsonUtil.moderToString(resp));
        if (resp instanceof PayResp) {
            //支付成功
            if (resp.errCode == 0) {
                PayResp payResp = (PayResp) resp;
                String payExtData = payResp.extData;//支付附带信息，自定义为支付类型，转账、红包、充值等
                //支付成功，分发本地广播通知到对应界面
                PayConfig.sendPayReceiver(payExtData);
            } else if (resp.errCode == -2) {
                CommonToast.toast(R.string.cancel_pay);
            }
            finish();

        }

//        Intent intent = new Intent();
//        intent.setAction(PayCallbackReceiver.PAY_ACTION);


    }
}