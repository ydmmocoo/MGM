package com.fjx.mg.main.scan.paymentcode.socket;

import okhttp3.WebSocket;
import okio.ByteString;

/**
 * Author    by hanlz
 * Date      on 2020/4/3.
 * Description：
 */
public interface IWsManager {

    WebSocket getWebSocket();

    void startConnect();

    void stopConnect();

    boolean isWsConnected();

    int getCurrentStatus();

    void setCurrentStatus(int currentStatus);

    boolean sendMessage(String msg);

    boolean sendMessage(ByteString byteString);
}
