package com.tencent.qcloud.uikit.business.chat.c2c.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.library.common.utils.CommonToast;
import com.library.common.utils.StringUtil;
import com.tencent.imsdk.TIMElem;
import com.tencent.imsdk.TIMTextElem;
import com.tencent.qcloud.uikit.api.chat.IChatPanel;
import com.tencent.qcloud.uikit.business.chat.c2c.TransReceiveListener;
import com.tencent.qcloud.uikit.business.chat.c2c.model.C2CChatInfo;
import com.tencent.qcloud.uikit.business.chat.c2c.model.C2CChatManager;
import com.tencent.qcloud.uikit.business.chat.c2c.presenter.C2CChatPresenter;
import com.tencent.qcloud.uikit.business.chat.model.ElemExtModel;
import com.tencent.qcloud.uikit.business.chat.model.MessageInfo;
import com.tencent.qcloud.uikit.business.chat.view.ChatPanel;
import com.tencent.qcloud.uikit.business.chat.view.widget.ChatAdapter;
import com.tencent.qcloud.uikit.common.component.action.PopActionClickListener;
import com.tencent.qcloud.uikit.common.component.action.PopMenuAction;
import com.tencent.qcloud.uikit.common.component.audio.UIKitAudioArmMachine;
import com.tencent.qcloud.uikit.common.component.titlebar.PageTitleBar;

import java.util.ArrayList;
import java.util.List;

/**
 * 私聊面板view
 */
public class C2CChatPanel extends ChatPanel implements IChatPanel {

    private C2CChatPresenter mPresenter;
    private C2CChatInfo mBaseInfo;
    private OnSendTransferListener onSendTransferListener;
    private ChatAdapter adapter;
    private OnSendPersonalInfoListener OnSendPersonalInfoListener;

    public C2CChatPanel(Context context) {
        super(context);
    }

    public C2CChatPanel(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public C2CChatPanel(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public void setOnSendTransferListener(OnSendTransferListener onSendTransferListener) {
        this.onSendTransferListener = onSendTransferListener;
    }

    public void setOnSendPersonalInfoListener(OnSendPersonalInfoListener OnSendPersonalInfoListener) {
        this.OnSendPersonalInfoListener = OnSendPersonalInfoListener;
    }

    public void setBaseChatId(String chatId) {
        mPresenter = new C2CChatPresenter(this);
        mBaseInfo = mPresenter.getC2CChatInfo(chatId);
        if (mBaseInfo == null)
            return;
        String chatTitle = mBaseInfo.getChatName();
        mTitleBar.setTitle(chatTitle, PageTitleBar.POSITION.CENTER);
        mPresenter.loadChatMessages(null);
    }

    @Override
    public void exitChat() {
        UIKitAudioArmMachine.getInstance().stopRecord();
        C2CChatManager.getInstance().destroyC2CChat();
    }

    @Override
    public void sendMessage(MessageInfo messageInfo) {

        if (messageInfo.getMsgType() == MessageInfo.MSG_TYPE_CUSTOM_TRANSFER) {
            //转账消息
            onSendTransferListener.onSendTransferListener(messageInfo, false);
            return;
        } else if (messageInfo.getMsgType() == MessageInfo.MSG_TYPE_CUSTOM_RED_PACKET) {
            //红包消息
            onSendTransferListener.onSendTransferListener(messageInfo, true);
            return;
        }

        mPresenter.sendC2CMessage(messageInfo, false);
    }

    @Override
    protected void sendPersonalInfo() {
        if (OnSendPersonalInfoListener != null)
            OnSendPersonalInfoListener.onSendPersonalInfo();
    }

    @Override
    public void loadMessages() {
        mPresenter.loadChatMessages(mAdapter.getItemCount() > 0 ? mAdapter.getItem(1) : null);
    }

    @Override
    public void initDefault() {
        super.initDefault();
        mTitleBar.getLeftGroup().setVisibility(View.VISIBLE);
        mTitleBar.getRightGroup().setVisibility(GONE);
//        ChatAdapter adapter = new ChatAdapter();
//        setChatAdapter(adapter);
        initDefaultEvent();
    }

    public void initChatAdapter(TransReceiveListener listener) {
        adapter = new ChatAdapter();
        adapter.setTransReceiveListener(listener);
        setChatAdapter(adapter);
    }

    public void notifyDataSetChanged() {
        if (adapter != null)
            adapter.notifyDataSetChanged();
    }


    @Override
    protected void initPopActions(final MessageInfo msg) {
        if (msg == null) {
            return;
        }
        List<PopMenuAction> actions = new ArrayList<>();

        PopMenuAction action = new PopMenuAction();
        if (msg.getMsgType() == MessageInfo.MSG_TYPE_TEXT || msg.getMsgType() == MessageInfo.MSG_TYPE_TEXT + 1) {
            action.setActionName("复制");
            action.setActionClickListener(new PopActionClickListener() {
                @Override
                public void onActionClick(int position, Object data) {
                    MessageInfo m = (MessageInfo) data;
                    TIMElem elem = m.getTIMMessage().getElement(0);
                    if (elem instanceof TIMTextElem) {
                        TIMTextElem textElem = (TIMTextElem) elem;
                        StringUtil.copyClip(textElem.getText());
                        CommonToast.toast("复制成功");
                    }
                }
            });
            actions.add(action);
        }


        action = new PopMenuAction();
        action.setActionName("删除");
        action.setActionClickListener(new PopActionClickListener() {
            @Override
            public void onActionClick(int position, Object data) {
                mPresenter.deleteC2CMessage(position, (MessageInfo) data);
            }
        });
        actions.add(action);
        if (msg.isSelf()) {
            action = new PopMenuAction();
            action.setActionName("撤销");
            action.setActionClickListener(new PopActionClickListener() {
                @Override
                public void onActionClick(int position, Object data) {
                    mPresenter.revokeC2CMessage(position, (MessageInfo) data);
                }
            });
            actions.add(action);
            if (msg.getStatus() == MessageInfo.MSG_STATUS_SEND_FAIL) {
                action = new PopMenuAction();
                action.setActionName("重发");
                action.setActionClickListener(new PopActionClickListener() {
                    @Override
                    public void onActionClick(int position, Object data) {
                        mPresenter.sendC2CMessage(msg, true);
                    }
                });
                actions.add(action);
            }
        }
        setMessagePopActions(actions, false);
    }



    public C2CChatPresenter getPresenter() {
        return mPresenter;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        exitChat();
    }

    public interface OnSendTransferListener {

        void onSendTransferListener(MessageInfo messageInfo, boolean isRedPacket);
    }


    public interface OnSendPersonalInfoListener {

        void onSendPersonalInfo();
    }
}
