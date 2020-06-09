package com.tencent.qcloud.uikit.business.chat.c2c;

import com.tencent.qcloud.uikit.business.chat.model.MessageInfo;

public interface TransReceiveListener {
    void onClickTransfer(MessageInfo receiveMessafe, boolean isRedpacket);

    void onClickTransferReceiver(MessageInfo receiveMessafe, boolean isRedpacket);
}
