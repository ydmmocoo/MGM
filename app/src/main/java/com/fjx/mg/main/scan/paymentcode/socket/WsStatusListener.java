package com.fjx.mg.main.scan.paymentcode.socket;

import okhttp3.Response;
import okio.ByteString;

/**
 * Author    by hanlz
 * Date      on 2020/4/3.
 * Description：
 */
public abstract class WsStatusListener {
    public void onOpen(Response response) {
    }

    public void onMessage(String text) {
    }

    public void onMessage(ByteString bytes) {
    }

    public void onReconnect() {

    }

    public void onClosing(int code, String reason) {
    }


    public void onClosed(int code, String reason) {
    }

    public void onFailure(Throwable t, Response response) {
    }
}
