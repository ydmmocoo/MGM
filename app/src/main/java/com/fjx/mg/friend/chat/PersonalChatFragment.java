package com.fjx.mg.friend.chat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.fjx.mg.R;
import com.fjx.mg.ToolBarManager;
import com.fjx.mg.friend.redpacker.PacketResultActivity;
import com.fjx.mg.friend.transfer.TransferMoneyActivity;
import com.fjx.mg.me.transfer.MeTransferActivityx;
import com.fjx.mg.sharetoapp.AllFriendsSelectActivity;
import com.fjx.mg.utils.RankPermissionHelper;
import com.library.common.utils.CommonImageLoader;
import com.library.common.utils.CommonToast;
import com.library.common.utils.DimensionUtil;
import com.library.common.utils.JsonUtil;
import com.library.common.utils.StringUtil;
import com.library.repository.core.net.CommonObserver;
import com.library.repository.core.net.RxScheduler;
import com.library.repository.data.UserCenter;
import com.library.repository.db.base.DBHelper;
import com.library.repository.db.model.DaoSession;
import com.library.repository.models.OtherUserModel;
import com.library.repository.models.PhoneHistoryModel;
import com.library.repository.models.ResponseModel;
import com.library.repository.repository.RepositoryFactory;
import com.tencent.imsdk.TIMCustomElem;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.friendship.TIMFriend;
import com.tencent.qcloud.uikit.business.chat.c2c.TransReceiveListener;
import com.tencent.qcloud.uikit.business.chat.c2c.model.C2CChatManager;
import com.tencent.qcloud.uikit.business.chat.c2c.view.C2CChatPanel;
import com.tencent.qcloud.uikit.business.chat.model.CacheUtil;
import com.tencent.qcloud.uikit.business.chat.model.ElemExtModel;
import com.tencent.qcloud.uikit.business.chat.model.MessageInfo;
import com.tencent.qcloud.uikit.business.chat.model.MessageInfoUtil;
import com.tencent.qcloud.uikit.common.BaseFragment;
import com.tencent.qcloud.uikit.common.component.titlebar.PageTitleBar;
import com.tencent.qcloud.uikit.common.utils.SoundPlayUtils;
import com.tencent.qcloud.uikit.event.ModifyEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import static com.fjx.mg.friend.chat.ChatActivity.INTENT_DATA;

/**
 * Created by valxehuang on 2018/7/30.
 */

public class PersonalChatFragment extends BaseFragment {

    private final String TAG = "PersonalChatFragment";

    private View mBaseView;
    private C2CChatPanel chatPanel;
    private PageTitleBar chatTitleBar;
    private String chatId, nickName;

    private final int TRANSFER_REQUESTCODE = 11;
    private final int REDPACKET_REQUESTCODE = 12;
    private String elemData;
    private boolean addNewFriend;
    private boolean isShare;
    private String say;
    private boolean isClick=false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mBaseView = inflater.inflate(R.layout.fragment_chat_personal, container, false);
        Bundle datas = getArguments();
        //由会话列表传入的会话ID
        chatId = datas.getString(INTENT_DATA);
        nickName = datas.getString("nickName");
        elemData = datas.getString("message");
        isShare = datas.getBoolean("isShare");
        addNewFriend = datas.getBoolean("addNewFriend");
        say = datas.getString("say");
//        messageInfo = JsonUtil.strToModel(message, MessageInfo.class);
        return mBaseView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    @Override
    public void onResume() {
        super.onResume();
        isClick=false;
        ToolBarManager.with(getActivity(), mBaseView).setTitle(nickName, R.color.white)
                .setBackgroundColor(R.color.colorAccent).setNavigationIcon(R.drawable.iv_back)
                .setRightImage(com.tencent.qcloud.uikit.R.drawable.group_chat_settings, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivityForResult(PersonalSettingsActivity.newIntent(getActivity(), chatId, nickName), 1212);
                    }
                });
    }

    private void initView() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        //从布局文件中获取聊天面板组件
        chatPanel = mBaseView.findViewById(R.id.chat_panel);
        //单聊组件的默认UI和交互初始化
        chatPanel.initDefault();
        chatPanel.initChatAdapter(new TransReceiveListener() {
            @Override
            public void onClickTransfer(MessageInfo receiveMessage, boolean isRedpacket) {
                if (isRedpacket) {
                    showRedPacketDialog(receiveMessage);
                } else {
                    receiveTransfer(receiveMessage, false);
                }
            }

            @Override
            public void onClickTransferReceiver(MessageInfo receivedMsg, boolean isRedpacket) {
                final TIMCustomElem elem = (TIMCustomElem) receivedMsg.getTIMMessage().getElement(0);
                String data = new String(elem.getData());
                final ElemExtModel dataModel = JsonUtil.strToModel(data, ElemExtModel.class);

                MessageInfo messageInfo = MessageInfoUtil.buildTransferCustomMessage(MessageInfoUtil.TRANSFER_RED_PACKET_RECEIVED, "");
                TIMMessage timMessage = new TIMMessage();
                dataModel.setMessageType(MessageInfoUtil.TRANSFER_RED_PACKET_RECEIVED);
                dataModel.setBeReceivedMessageId(receivedMsg.getMsgId());
                elem.setData(JsonUtil.moderToString(dataModel).getBytes());
                CacheUtil.getInstance().saveTransferMessageId(receivedMsg.getMsgId());
                timMessage.addElement(elem);
                messageInfo.setTIMMessage(timMessage);
            }
        });

        /*chatPanel
         * 需要指定会话ID（即聊天对象的identify，具体可参考IMSDK接入文档）来加载聊天消息。在上一章节SessionClickListener中回调函数的参数SessionInfo对象中持有每一会话的会话ID，所以在会话列表点击时都可传入会话ID。
         * 特殊的如果用户应用不具备类似会话列表相关的组件，则需自行实现逻辑获取会话ID传入。
         */
        chatPanel.setBaseChatId(chatId);
        //获取单聊面板的标题栏
        chatTitleBar = chatPanel.getTitleBar();
        chatTitleBar.setVisibility(View.GONE);
        chatTitleBar.mLeftIcon.setImageResource(R.drawable.iv_back);
        chatTitleBar.mCenterTitle.setTextColor(Color.WHITE);
        //单聊面板标记栏返回按钮点击事件，这里需要开发者自行控制
        chatTitleBar.setLeftClick(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
        chatPanel.setOnSendTransferListener(new C2CChatPanel.OnSendTransferListener() {
            @Override
            public void onSendTransferListener(MessageInfo messageInfo, boolean isRedPacket) {
//                if (RankPermissionHelper.showSafeCenterDialog()) return;
                if (isClick){
                    return;
                }
                isClick=true;
                if (RankPermissionHelper.NoPrivileges(2)) return;
                //TODO 此版本只有转账功能且转账使用我们后台接口转和外部钱包转账逻辑页面一样 2020年2月25日10:16:43
                getUserInfo();
//                if (isRedPacket) {
//                    PersonalChatFragment.this.startActivityForResult(RedPacketActivity.newInstance(getContext(),
//                            JsonUtil.moderToString(messageInfo), chatId), REDPACKET_REQUESTCODE);
//                } else {
//                    PersonalChatFragment.this.startActivityForResult(TransferMoneyActivity.newInstance(getContext(),
//                            JsonUtil.moderToString(messageInfo), chatId), TRANSFER_REQUESTCODE);
//                }
            }
        });
        chatPanel.setOnSendPersonalInfoListener(new C2CChatPanel.OnSendPersonalInfoListener() {
            @Override
            public void onSendPersonalInfo() {
                if (isClick){
                    return;
                }
                isClick=true;
                startActivityForResult(AllFriendsSelectActivity.newIntent(getActivity(), ElemExtModel.SHARE_PERSONAL_CARD, "2"), 666);
            }
        });

        if (addNewFriend) {
            addNewFriend = false;
            MessageInfo messageInfo = MessageInfoUtil.buildAddFriendCustomMessage(chatId);
//            chatPanel.getPresenter().sendC2CMessage(messageInfo, false);
            C2CChatManager.getInstance().sendC2CMessage(messageInfo, false, null);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    initView();
                }
            }, 300);
            return;
        }

        if (TextUtils.isEmpty(elemData)) return;
        if (isShare) {
            sendShareToChatListMessage();
            if (StringUtil.isNotEmpty(say)) {
                sendC2CTextMessage();
            }
        } else {
            //sendCustomerTransferMessage(false);
            sendNewTransferMsg();
        }
    }

    private void sendNewTransferMsg() {
        final TIMCustomElem elem = new TIMCustomElem();
        final ElemExtModel dataModel = new ElemExtModel();

        MessageInfo messageInfo = MessageInfoUtil.timTransfer2MesssageInfo();
        TIMMessage timMessage = new TIMMessage();
        dataModel.setMessageType(MessageInfoUtil.TRANSFER_ACCOUNT_NEW_RECEIVED);
        elem.setData(JsonUtil.moderToString(dataModel).getBytes());
        timMessage.addElement(elem);
        messageInfo.setTIMMessage(timMessage);
        chatPanel.sendMessage(messageInfo);
    }

    /**
     * 普通文本信息(分享时候的留言)
     */
    private void sendC2CTextMessage() {
        MessageInfo messageInfo = MessageInfoUtil.buildTextMessage(say);
        chatPanel.getPresenter().sendC2CMessage(messageInfo, false);
    }

    /**
     * 自定义消息:分享app内部消息到好友聊天列表
     */
    private void sendShareToChatListMessage() {
        if (TextUtils.isEmpty(elemData)) return;
        MessageInfo messageInfo = new MessageInfo();
        TIMMessage timMessage = new TIMMessage();
        TIMCustomElem elem = new TIMCustomElem();
        elem.setData(elemData.getBytes());
        timMessage.addElement(elem);
        messageInfo.setSelf(true);
        messageInfo.setMsgType(MessageInfo.MSG_TYPE_NEARBY_CITY);
        messageInfo.setTIMMessage(timMessage);
        messageInfo.setExtra(elemData);
        chatPanel.getPresenter().sendC2CMessage(messageInfo, false);
    }

    /**
     * 自定义消息：发送红包或者转账
     *
     * @return
     */
    private void sendCustomerTransferMessage(boolean isRedPacket) {
        ElemExtModel elemExtModel = JsonUtil.strToModel(elemData, ElemExtModel.class);
        MessageInfo messageInfo = MessageInfoUtil.buildTransferCustomMessage(isRedPacket
                ? MessageInfoUtil.TRANSFER_RED_PACKET_UN_RECEIVED : MessageInfoUtil.TRANSFER_ACCOUNT_UN_RECEIVED, "");
//        String json = MessageInfoUtil.createJson(mMoney, mRemark, transferId, MessageInfoUtil.TRANSFER_ACCOUNT_UN_RECEIVED);
        TIMMessage timMessage = new TIMMessage();
        TIMCustomElem elem = new TIMCustomElem();
        elem.setData(elemData.getBytes());
        elem.setDesc(elemExtModel.getRemark());
        timMessage.addElement(elem);
        messageInfo.setTIMMessage(timMessage);
        messageInfo.setExtra(elemData);
        chatPanel.getPresenter().sendC2CMessage(messageInfo, false);
    }


    /**
     * 接收转账信息
     *
     * @param receivedMsg
     */
    private void receiveTransfer(final MessageInfo receivedMsg, final boolean isRedpacket) {
        final TIMCustomElem elem = (TIMCustomElem) receivedMsg.getTIMMessage().getElement(0);
        String data = new String(elem.getData());
        final ElemExtModel dataModel = JsonUtil.strToModel(data, ElemExtModel.class);
        if (dataModel == null || TextUtils.isEmpty(dataModel.getTransferId())) return;

        showLoading();
        RepositoryFactory.getRemotePayRepository().transferReceive(dataModel.getTransferId())
                .compose(RxScheduler.<ResponseModel<Object>>toMain())
                .subscribe(new CommonObserver<Object>() {
                    @Override
                    public void onSuccess(Object data) {
                        SoundPlayUtils.play(3);
                        hideLoading();
                        if (isRedpacket) {
                            sendRedpacketReceiveSuccess(receivedMsg);
                        } else {
                            sendTransferReceiveSuccess(receivedMsg);

                        }
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        hideLoading();
                        CommonToast.toast(data.getMsg());
                        //此处操作是防止部分红包或者转账已经领取但是因为网络原因没有收到im消息保存到本地
//                        if (data.getMsg().contains("已领取")) {
//                            List<String> transferMessageId = CacheUtil.getInstance().getTransferMessageId();
//                            for (String id : transferMessageId) {
//                                if (TextUtils.equals(receivedMsg.getMsgId(), id)) {
//                                    Log.d(TAG, "转账id不存在");
//                                } else {
//                                    CacheUtil.getInstance().saveTransferMessageId(receivedMsg.getMsgId());
//                                }
//                            }
//                        }

                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                        hideLoading();
                        CommonToast.toast(data.getMsg());
                    }
                });
    }

    private void sendTransferReceiveSuccess(MessageInfo receivedMsg) {
        final TIMCustomElem elem = (TIMCustomElem) receivedMsg.getTIMMessage().getElement(0);
        String data = new String(elem.getData());
        final ElemExtModel dataModel = JsonUtil.strToModel(data, ElemExtModel.class);

        MessageInfo messageInfo = MessageInfoUtil.buildTransferCustomMessage(MessageInfoUtil.TRANSFER_ACCOUNT_RECEIVED, "");
        TIMMessage timMessage = new TIMMessage();
        dataModel.setMessageType(MessageInfoUtil.TRANSFER_ACCOUNT_RECEIVED);
        dataModel.setBeReceivedMessageId(receivedMsg.getMsgId());
//        dataModel.setReceiveUserId(TIMManager.getInstance().getLoginUser());
        elem.setData(JsonUtil.moderToString(dataModel).getBytes());
        CacheUtil.getInstance().saveTransferMessageId(receivedMsg.getMsgId());
        timMessage.addElement(elem);
        messageInfo.setTIMMessage(timMessage);
        C2CChatManager.getInstance().sendC2CMessage(messageInfo, false, null);
        chatPanel.notifyDataSetChanged();
    }


    private void sendRedpacketReceiveSuccess(MessageInfo receivedMsg) {
        final TIMCustomElem elem = (TIMCustomElem) receivedMsg.getTIMMessage().getElement(0);
        String data = new String(elem.getData());
        final ElemExtModel dataModel = JsonUtil.strToModel(data, ElemExtModel.class);

        MessageInfo messageInfo = MessageInfoUtil.buildTransferCustomMessage(MessageInfoUtil.TRANSFER_RED_PACKET_RECEIVED, "");
        TIMMessage timMessage = new TIMMessage();
        dataModel.setMessageType(MessageInfoUtil.TRANSFER_RED_PACKET_RECEIVED);
//        dataModel.setReceiveUserId(TIMManager.getInstance().getLoginUser());
        dataModel.setBeReceivedMessageId(receivedMsg.getMsgId());
        elem.setData(JsonUtil.moderToString(dataModel).getBytes());
        CacheUtil.getInstance().saveTransferMessageId(receivedMsg.getMsgId());
        timMessage.addElement(elem);
        messageInfo.setTIMMessage(timMessage);


        startActivity(PacketResultActivity.newInstance(getActivity(), chatId, JsonUtil.moderToString(dataModel)),
                ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity()).toBundle());


        C2CChatManager.getInstance().sendC2CMessage(messageInfo, false, null);
        chatPanel.notifyDataSetChanged();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 111) {
            sendNewTransferMsg();
        }
        if (data == null) return;
        if ((requestCode == REDPACKET_REQUESTCODE || requestCode == TRANSFER_REQUESTCODE) && resultCode == 1) {
            elemData = data.getStringExtra("message");
            if (TextUtils.isEmpty(elemData)) return;
            sendCustomerTransferMessage(requestCode == REDPACKET_REQUESTCODE);
        }
        if (requestCode == 666) {
            if (resultCode == -1) {
                elemData = data.getStringExtra("message");
                if (TextUtils.isEmpty(elemData)) return;
                sendShareToChatListMessage();
            }
        }

    }

    private void showRedPacketDialog(final MessageInfo receiveMessage) {

        Context context = getActivity();
        if (context == null) return;
        final MaterialDialog dialog = new MaterialDialog.Builder(context)
                .backgroundColor(ContextCompat.getColor(context, R.color.trans))
                .customView(R.layout.dialog_redpacket, false)
                .build();
        Window window = dialog.getWindow();
        if (window == null) return;
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TIMFriend friend = UserCenter.getFriend(chatId);
        if (friend == null) return;
        View customView = dialog.getCustomView();
        //初始化
        ImageView ivAvatar = customView.findViewById(R.id.ivAvatar);
        TextView tvNickName = customView.findViewById(R.id.tvNickName);

        CommonImageLoader.load(friend.getTimUserProfile().getFaceUrl())
                .circle()
                .placeholder(R.drawable.user_default)
                .into(ivAvatar);

        if (!TextUtils.isEmpty(friend.getRemark())) {
            tvNickName.setText(friend.getRemark());
        } else if (!TextUtils.isEmpty(friend.getTimUserProfile().getNickName())) {
            tvNickName.setText(friend.getTimUserProfile().getNickName());
        } else {
            tvNickName.setText(friend.getIdentifier());
        }


        final ImageView ivGetRedPacket = customView.findViewById(R.id.ivGetRedPacket);
        ivGetRedPacket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                receiveTransfer(receiveMessage, true);
                dialog.dismiss();
            }
        });


        dialog.show();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = DimensionUtil.dip2px(266);
        dialog.getWindow().setAttributes(lp);
    }

    private void getUserInfo() {
        //showLoading();
        RepositoryFactory.getRemoteAccountRepository().getUserInfo("", chatId, "")
                .compose(RxScheduler.<ResponseModel<OtherUserModel>>toMain())
                .subscribe(new CommonObserver<OtherUserModel>() {
                    @Override
                    public void onSuccess(OtherUserModel data) {
                        //hideLoading();
                        //存储数据到数据库 便于后面模糊搜索和其他历史记录展示
                        final DaoSession daoSession = DBHelper.getInstance().getDaoSession();
                        PhoneHistoryModel phoneHistoryModel = new PhoneHistoryModel();
                        phoneHistoryModel.setType("1");
                        phoneHistoryModel.setPhone(data.getPhone());
                        phoneHistoryModel.setFaceIcon(data.getAvatar());
                        phoneHistoryModel.setImUserId(data.getIdentifier());
                        phoneHistoryModel.setNickname(TextUtils.isEmpty(data.getUserNick()) ? data.getUserName() : data.getUserNick());
                        daoSession.insertOrReplace(phoneHistoryModel);
                        startActivityForResult(MeTransferActivityx.newInstance(getContext(), JsonUtil.moderToString(data)), 1);
                    }


                    @Override
                    public void onError(ResponseModel data) {
                        //hideLoading();
                        CommonToast.toast(data.getMsg());
                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                        //hideLoading();
                        CommonToast.toast(data.getMsg());
                    }
                });
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void nicknameEvent(ModifyEvent event) {
        nickName = event.getNicename();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(SettingsEvent event) {
        if (TextUtils.equals("2", event.getType())) {
            ChatActivity activity = (ChatActivity) getActivity();
            activity.finish();
        }
    }
}
