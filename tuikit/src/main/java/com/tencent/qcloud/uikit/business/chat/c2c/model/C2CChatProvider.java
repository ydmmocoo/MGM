package com.tencent.qcloud.uikit.business.chat.c2c.model;

import android.text.TextUtils;

import com.library.common.utils.JsonUtil;
import com.tencent.imsdk.TIMCustomElem;
import com.tencent.imsdk.TIMElem;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.ext.message.TIMMessageExt;
import com.tencent.imsdk.ext.message.TIMMessageLocator;
import com.tencent.qcloud.uikit.api.chat.IChatAdapter;
import com.tencent.qcloud.uikit.api.chat.IChatProvider;
import com.tencent.qcloud.uikit.business.chat.model.CacheUtil;
import com.tencent.qcloud.uikit.business.chat.model.ElemExtModel;
import com.tencent.qcloud.uikit.business.chat.model.MessageInfo;
import com.tencent.qcloud.uikit.business.chat.model.MessageInfoUtil;
import com.tencent.qcloud.uikit.business.chat.view.ChatListView;

import java.util.ArrayList;
import java.util.List;


public class C2CChatProvider implements IChatProvider {

    private ArrayList<MessageInfo> dataSource = new ArrayList();
    private IChatAdapter adapter;

    @Override
    public List<MessageInfo> getDataSource() {
        return dataSource;
    }

    @Override
    public boolean addMessageInfos(List<MessageInfo> msgs, boolean front) {
        boolean flag;
        if (front) {
            flag = dataSource.addAll(0, msgs);
            updateAdapter(ChatListView.DATA_CHANGE_TYPE_ADD_FRONT, msgs.size());
        } else {
            flag = dataSource.addAll(msgs);
            updateAdapter(ChatListView.DATA_CHANGE_TYPE_ADD_BACK, msgs.size());
        }
        return flag;
    }

    private boolean checkExist(MessageInfo msg) {
        if (msg != null) {
            String msgId = msg.getMsgId();
            for (int i = dataSource.size() - 1; i >= 0; i--) {
                if (dataSource.get(i).getTIMMessage().getMsgId().equals(msgId))
                    return true;
            }
        }
        return false;
    }

    @Override
    public boolean deleteMessageInfos(List<MessageInfo> messages) {
        for (int i = 0; i < dataSource.size(); i++) {
            for (int j = 0; j < messages.size(); j++) {
                if (dataSource.get(i).getMsgId().equals(messages.get(j).getMsgId())) {
                    dataSource.remove(i);
                    updateAdapter(ChatListView.DATA_CHANGE_TYPE_DELETE, i);
                    break;
                }
            }
        }
        return false;
    }

    @Override
    public boolean updateMessageInfos(List<MessageInfo> messages) {
        return false;
    }


    /**
     * 新消息
     *
     * @param msg
     * @return
     */
    public boolean addMessageInfo(MessageInfo msg) {
        if (msg == null) {
            updateAdapter(ChatListView.DATA_CHANGE_TYPE_LOAD, 0);
            return true;
        }
        if (checkExist(msg))
            return true;
        boolean flag = dataSource.add(msg);
//        if (isReceiveTrans(msg)) {
//            updateAdapter();
//        } else {
            updateAdapter(ChatListView.DATA_CHANGE_TYPE_ADD_BACK, 1);
//        }
        return flag;
    }

    private boolean isReceiveTrans(MessageInfo msg) {
        TIMMessage timMessage = msg.getTIMMessage();
        if (timMessage.getElementCount() == 0) return false;
        TIMElem timElem = timMessage.getElement(0);
        if (timElem instanceof TIMCustomElem) {
            final TIMCustomElem elem = (TIMCustomElem) timElem;
            String data = new String(elem.getData());
            final ElemExtModel dataModel = JsonUtil.strToModel(data, ElemExtModel.class);
            if (dataModel == null) return false;
            String type = dataModel.getMessageType();
            if (TextUtils.equals(type, MessageInfoUtil.TRANSFER_ACCOUNT_RECEIVED) ||
                    TextUtils.equals(type, MessageInfoUtil.TRANSFER_RED_PACKET_RECEIVED)) {
                CacheUtil.getInstance().saveTransferMessageId(timMessage.getMsgId());
                return true;
            }
        }

        return false;
    }

    public boolean deleteMessageInfo(MessageInfo msg) {
        for (int i = 0; i < dataSource.size(); i++) {
            if (dataSource.get(i).getMsgId().equals(msg.getMsgId())) {
                dataSource.remove(i);
                updateAdapter(ChatListView.DATA_CHANGE_TYPE_DELETE, -1);
                return true;
            }
        }
        return false;
    }

    public boolean updateMessageInfo(MessageInfo message) {
        for (int i = 0; i < dataSource.size(); i++) {
            if (dataSource.get(i).getMsgId().equals(message.getMsgId())) {
                dataSource.remove(i);
                dataSource.add(i, message);
                updateAdapter(ChatListView.DATA_CHANGE_TYPE_UPDATE, i);
                return true;
            }
        }
        return false;
    }

    public boolean updateMessageRevoked(TIMMessageLocator locator) {
        for (int i = 0; i < dataSource.size(); i++) {
            MessageInfo messageInfo = dataSource.get(i);
            TIMMessage msg = messageInfo.getTIMMessage();
            TIMMessageExt ext = new TIMMessageExt(msg);
            if (ext.checkEquals(locator)) {
                messageInfo.setMsgType(MessageInfo.MSG_STATUS_REVOKE);
                messageInfo.setStatus(MessageInfo.MSG_STATUS_REVOKE);
                updateAdapter(ChatListView.DATA_CHANGE_TYPE_UPDATE, i);
                return true;
            }
        }
        return false;
    }


    public boolean updateMessageRevoked(String msgId) {
        for (int i = 0; i < dataSource.size(); i++) {
            MessageInfo messageInfo = dataSource.get(i);
            if (messageInfo.getMsgId().equals(msgId)) {
                messageInfo.setMsgType(MessageInfo.MSG_STATUS_REVOKE);
                messageInfo.setStatus(MessageInfo.MSG_STATUS_REVOKE);
                updateAdapter(ChatListView.DATA_CHANGE_TYPE_UPDATE, i);
                return true;
            }
        }
        return false;
    }


    public void remove(int index) {
        dataSource.remove(index);
        updateAdapter(ChatListView.DATA_CHANGE_TYPE_DELETE, index);
    }

    public void clear() {
        dataSource.clear();
        updateAdapter(ChatListView.DATA_CHANGE_TYPE_LOAD, 0);
    }


    private void updateAdapter(int type, int data) {
        if (adapter != null) {
            adapter.notifyDataSetChanged(type, data);
        }

    }

    private void updateAdapter() {
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }

    }


    public IChatAdapter getAdapter() {
        return adapter;
    }

    public void attachAdapter(IChatAdapter adapter) {
        this.adapter = adapter;
    }

}
