package com.tencent.qcloud.uikit.business.chat.group.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.tencent.qcloud.uikit.R;
import com.tencent.qcloud.uikit.api.chat.IChatPanel;
import com.tencent.qcloud.uikit.business.chat.c2c.TransReceiveListener;
import com.tencent.qcloud.uikit.business.chat.group.model.GroupChatInfo;
import com.tencent.qcloud.uikit.business.chat.group.model.GroupChatManager;
import com.tencent.qcloud.uikit.business.chat.group.presenter.GroupChatPresenter;
import com.tencent.qcloud.uikit.business.chat.model.ElemExtModel;
import com.tencent.qcloud.uikit.business.chat.model.MessageInfo;
import com.tencent.qcloud.uikit.business.chat.view.ChatPanel;
import com.tencent.qcloud.uikit.business.chat.view.widget.ChatAdapter;
import com.tencent.qcloud.uikit.common.UIKitConstants;
import com.tencent.qcloud.uikit.common.component.action.PopActionClickListener;
import com.tencent.qcloud.uikit.common.component.action.PopMenuAction;
import com.tencent.qcloud.uikit.common.component.audio.UIKitAudioArmMachine;
import com.tencent.qcloud.uikit.common.component.titlebar.PageTitleBar;
import com.tencent.qcloud.uikit.common.utils.UIUtils;
import com.tencent.qcloud.uikit.operation.group.GroupApplyManagerActivity;
import com.tencent.qcloud.uikit.operation.group.GroupManagerActivity;
import com.tencent.qcloud.uikit.operation.group.GroupSettingsActivity;

import java.util.ArrayList;
import java.util.List;


public class GroupChatPanel extends ChatPanel implements IChatPanel {

    private static final int DELAY_GONE_TIME = 500;
    private GroupChatPresenter mPresenter;
    private GroupChatInfo mBaseInfo;
    private OnSendTransferListener onSendTransferListener;
    private ChatAdapter mAdapter;

    public GroupChatPanel(Context context) {
        super(context);
        init();
    }

    public GroupChatPanel(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GroupChatPanel(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mTipsGroup.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                getContext().startActivity(new Intent(getContext(), GroupApplyManagerActivity.class));
                postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mTipsGroup.setVisibility(View.GONE);
                    }
                }, DELAY_GONE_TIME);
            }
        });
    }


    public void setBaseChatId(String peer) {
        mPresenter = new GroupChatPresenter(this);
        mPresenter.getGroupChatInfo(peer);
        mPresenter.loadChatMessages(null);
    }

    public void onGroupInfoLoaded(GroupChatInfo groupInfo) {
        if (groupInfo == null)
            return;
        mBaseInfo = groupInfo;
        mPresenter.loadChatMessages(null);
        mPresenter.loadApplyInfos();
        String chatTitle = mBaseInfo.getChatName();
        int size = mBaseInfo.getMemberDetails().size();
//        if (mBaseInfo.getMemberCount() > 0)
//            chatTitle = chatTitle + "(" + mBaseInfo.getMemberCount() + "人)";
        mTitleBar.setTitle(chatTitle + "(" + size + ")", PageTitleBar.POSITION.LEFT);
        mTitleBar.getLeftGroup().setVisibility(View.VISIBLE);
    }

    public void notifyDataSetChanged() {
        if (mAdapter != null)
            mAdapter.notifyDataSetChanged();
    }

    public void isShowNickname(boolean state) {
        if (mAdapter != null) {
            mAdapter.isShowNickname(state);
        }
    }

    public void onChatActive(Object object) {
        mTipsContent.setText(object + getResources().getString(R.string.group_apply_tips));
        mTipsGroup.setVisibility(View.VISIBLE);
    }

    public void onGroupNameChanged(String newName) {
        int size = mBaseInfo.getMemberDetails().size();
        getTitleBar().setTitle(newName + "(" + size + ")", PageTitleBar.POSITION.LEFT);
    }

    @Override
    public void exitChat() {
        mPresenter.exitChat();
        UIKitAudioArmMachine.getInstance().stopRecord();
        GroupChatManager.getInstance().destroyGroupChat();
    }


    @Override
    public void sendMessage(MessageInfo messageInfo) {
        if (messageInfo.getMsgType() == MessageInfo.MSG_TYPE_CUSTOM_RED_PACKET) {
            //转账消息
            if (onSendTransferListener != null)
                onSendTransferListener.onSendTransferListener(messageInfo, false);
            return;
        }
        mPresenter.sendGroupMessage(messageInfo);
    }

    @Override
    public void loadMessages() {
        mPresenter.loadChatMessages(mAdapter.getItemCount() > 0 ? mAdapter.getItem(1) : null);
    }

    @Override
    public void initDefault() {
        super.initDefault();
        mAdapter = new ChatAdapter();
        setChatAdapter(mAdapter);
        initDefaultEvent();
        mTitleBar.getRightIcon().setImageResource(R.drawable.group_chat_settings);
        mTitleBar.setRightClick(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mBaseInfo != null) {
//                    Intent intent = new Intent(getContext(), GroupManagerActivity.class);
//                    Intent intent = new Intent(getContext(), GroupSettingsActivity.class);
                    Intent intent = new Intent("com.fjx_GroupSettingsActivity");
                    intent.putExtra(UIKitConstants.GROUP_ID, mBaseInfo.getPeer());
                    getContext().startActivity(intent);
                } else {
                    UIUtils.toastLongMessage("请稍后再试试~");
                }
            }
        });
    }

    public void setTransReceiveListener(TransReceiveListener listener) {
        mAdapter.setTransReceiveListener(listener);
    }

    @Override
    protected void initPopActions(final MessageInfo msg) {
        List<PopMenuAction> actions = new ArrayList<>();
        PopMenuAction action = new PopMenuAction();
        action.setActionName("删除");
        action.setActionClickListener(new PopActionClickListener() {
            @Override
            public void onActionClick(int position, Object data) {
                mPresenter.deleteMessage(position, (MessageInfo) data);
            }
        });
        actions.add(action);
        if (msg.isSelf()) {
            action = new PopMenuAction();
            action.setActionName("撤销");
            action.setActionClickListener(new PopActionClickListener() {
                @Override
                public void onActionClick(int position, Object data) {
                    mPresenter.revokeMessage(position, (MessageInfo) data);
                }
            });
            actions.add(action);
            if (msg.getStatus() == MessageInfo.MSG_STATUS_SEND_FAIL) {
                action = new PopMenuAction();
                action.setActionName("重发");
                action.setActionClickListener(new PopActionClickListener() {
                    @Override
                    public void onActionClick(int position, Object data) {
                        sendMessage(msg);
                    }
                });
                actions.add(action);
            }
        }
        setMessagePopActions(actions, false);
    }

    @Override
    protected void sendPersonalInfo() {

    }

    public void forceStopChat() {
        if (getContext() instanceof Activity) {
            ((Activity) getContext()).finish();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        exitChat();
    }

    public GroupChatPresenter getPresenter() {
        return mPresenter;
    }

    public void setOnSendTransferListener(OnSendTransferListener onSendTransferListener) {
        this.onSendTransferListener = onSendTransferListener;
    }

    public interface OnSendTransferListener {

        void onSendTransferListener(MessageInfo messageInfo, boolean isRedPacket);
    }
}
