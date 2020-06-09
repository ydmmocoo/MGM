package com.fjx.mg.friend.chat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;

import com.afollestad.materialdialogs.MaterialDialog;
import com.fjx.mg.R;
import com.fjx.mg.friend.chat.redpacket.GroupRedPacketActivity;
import com.fjx.mg.friend.chat.redpacket.GroupRedPacketDetailActivity;
import com.fjx.mg.utils.NicknameSateSpUtil;
import com.fjx.mg.utils.RankPermissionHelper;
import com.library.common.constant.IntentConstants;
import com.library.common.utils.CommonImageLoader;
import com.library.common.utils.CommonToast;
import com.library.common.utils.DimensionUtil;
import com.library.common.utils.JsonUtil;
import com.library.repository.core.net.CommonObserver;
import com.library.repository.core.net.RxScheduler;
import com.library.repository.models.GroupRedPacketDetailModel;
import com.library.repository.models.GroupRedPacketModel;
import com.library.repository.models.ResponseModel;
import com.library.repository.repository.RepositoryFactory;
import com.tencent.imsdk.TIMCustomElem;
import com.tencent.imsdk.TIMFriendshipManager;
import com.tencent.imsdk.TIMGroupManager;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMUserProfile;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.ext.group.TIMGroupSelfInfo;
import com.tencent.qcloud.uikit.business.chat.c2c.TransReceiveListener;
import com.tencent.qcloud.uikit.business.chat.group.model.GroupChatManager;
import com.tencent.qcloud.uikit.business.chat.group.view.GroupChatPanel;
import com.tencent.qcloud.uikit.business.chat.model.CacheUtil;
import com.tencent.qcloud.uikit.business.chat.model.ElemExtModel;
import com.tencent.qcloud.uikit.business.chat.model.MessageInfo;
import com.tencent.qcloud.uikit.business.chat.model.MessageInfoUtil;
import com.tencent.qcloud.uikit.common.BaseFragment;
import com.tencent.qcloud.uikit.common.component.titlebar.PageTitleBar;
import com.tencent.qcloud.uikit.common.utils.SoundPlayUtils;


/**
 * Created by valxehuang on 2018/7/30.
 */

public class GroupChatFragment extends BaseFragment {

    private final int TRANSFER_REQUESTCODE = 11;
    private final int REDPACKET_REQUESTCODE = 12;

    private View mBaseView;
    private GroupChatPanel chatPanel;
    private PageTitleBar chatTitleBar;
    private String groupChatId;
    private String elemData;

    public NicknameStateReceiver mReceiver;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mBaseView = inflater.inflate(R.layout.fragment_chat_group, container, false);
        //由会话列表传入的会话ID（群组ID）
        if (getArguments() != null) {
            groupChatId = getArguments().getString(IntentConstants.CHAT_INFO);
        }
        initView();
        return mBaseView;
    }

    private void initView() {
        //从布局文件中获取聊天面板组件
        chatPanel = mBaseView.findViewById(R.id.chat_panel);
        //单聊组件的默认UI和交互初始化
        chatPanel.initDefault();
        chatPanel.group();
        chatPanel.setTransReceiveListener(new TransReceiveListener() {
            @Override
            public void onClickTransfer(final MessageInfo receiveMessafe, boolean isRedpacket) {
                groupRedPacketInfo(receiveMessafe);
            }

            @Override
            public void onClickTransferReceiver(MessageInfo receiveMessafe, boolean isRedpacket) {
                groupRedPacketInfo(receiveMessafe);
            }
        });
        /*
         * GroupChatPanel在初始化完成后需要入会话ID（即群组ID，具体可参考IMSDK接入文档）来加载聊天消息。在上一章节SessionClickListener中回调函数的参数SessionInfo对象中持有每一会话的会话ID，如果是群会话则为群组ID，所以在会话列表点击时都可传入会话ID。

         * 特殊的如果用户应用不具备类似会话列表相关的组件，则在使用群聊面板时需自行实现逻辑获取群组ID传入。
         */
        chatPanel.setBaseChatId(groupChatId);
        /*
         * 发送红包逻辑处理
         */
        chatPanel.setOnSendTransferListener(new GroupChatPanel.OnSendTransferListener() {
            @Override
            public void onSendTransferListener(MessageInfo messageInfo, boolean isRedPacket) {
                if (RankPermissionHelper.NoPrivileges(2)) return;
                GroupChatFragment.this.startActivityForResult(GroupRedPacketActivity.newIntent(getContext(),
                        JsonUtil.moderToString(messageInfo), groupChatId), REDPACKET_REQUESTCODE);
            }
        });


        //获取标题栏
        chatTitleBar = chatPanel.getTitleBar();
        //设置标题栏的返回按钮点击事件,需开发者自行控制
        chatTitleBar.setLeftClick(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });

        chatTitleBar = chatPanel.getTitleBar();
        chatTitleBar.mLeftIcon.setImageResource(R.drawable.iv_back);
        chatTitleBar.mCenterTitle.setTextColor(Color.WHITE);

        mReceiver = new NicknameStateReceiver();
        getActivity().registerReceiver(mReceiver, new IntentFilter("com.group.state"));
        NicknameSateSpUtil util = new NicknameSateSpUtil();
        chatPanel.isShowNickname(util.get(groupChatId));
    }


    private void groupRedPacketInfo(final MessageInfo receiveMessafe) {
        TIMCustomElem element = (TIMCustomElem) receiveMessafe.getTIMMessage().getElement(0);
        byte[] data = element.getData();
        final String message = new String(data);
        final ElemExtModel model = JsonUtil.strToModel(message, ElemExtModel.class);
        RepositoryFactory.getRemotePayRepository().redEnvelopeInfo(model.getrId())
                .compose(RxScheduler.<ResponseModel<GroupRedPacketDetailModel>>toMain())
                .subscribe(new CommonObserver<GroupRedPacketDetailModel>() {
                    @Override
                    public void onSuccess(GroupRedPacketDetailModel data) {
                        if ("1".equals(data.getIsRecive())) {//已经领取过
                            switch (data.getStatus()) {
                                case "1"://全部被领取
                                    CommonToast.toast(R.string.red_packet_have_been_collected);
                                    break;
                                case "2"://部分被领取
                                    startActivity(GroupRedPacketDetailActivity.newIntent(getActivity(), groupChatId, model.getrId()));
                                    break;
                                case "3"://表示红包退还,
                                    CommonToast.toast(R.string.red_packet_has_been_returned);
                                    break;
                                default:
                                    break;
                            }

                        } else {
                            if (data.getReciveNum().equals(data.getTotalNum())) {
                                CommonToast.toast(R.string.red_packet_have_been_collected);
                            } else {
                                showRedPacketDialog(receiveMessafe, data);
                            }
                        }
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        CommonToast.toast(data.getMsg());
                    }

                    @Override
                    public void onNetError(ResponseModel data) {

                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) return;
        if ((requestCode == REDPACKET_REQUESTCODE || requestCode == TRANSFER_REQUESTCODE) && resultCode == 1) {
            elemData = data.getStringExtra("message");
            if (TextUtils.isEmpty(elemData)) return;
            sendCustomerTransferMessage(requestCode == REDPACKET_REQUESTCODE);
        }
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
        chatPanel.getPresenter().sendGroupMessage(messageInfo);
    }

    private void showRedPacketDialog(final MessageInfo receiveMessage, final GroupRedPacketDetailModel redPacketDetailModel) {
        TIMCustomElem element = (TIMCustomElem) receiveMessage.getTIMMessage().getElement(0);
        final byte[] data = element.getData();
        final String message = new String(data);
        final ElemExtModel model = JsonUtil.strToModel(message, ElemExtModel.class);
        Context context = getActivity();
        if (context == null) return;
        final MaterialDialog dialog = new MaterialDialog.Builder(context)
                .backgroundColor(ContextCompat.getColor(context, R.color.trans))
                .customView(R.layout.dialog_redpacket, false)
                .build();
        Window window = dialog.getWindow();
        if (window == null) return;
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TIMGroupManager.getInstance().getSelfInfo(groupChatId, new TIMValueCallBack<TIMGroupSelfInfo>() {
            @Override
            public void onError(int i, String s) {

            }

            @Override
            public void onSuccess(TIMGroupSelfInfo timGroupSelfInfo) {
                TIMUserProfile timUserProfile = TIMFriendshipManager.getInstance().queryUserProfile(timGroupSelfInfo.getUser());
                if (timUserProfile == null) return;
                View customView = dialog.getCustomView();
                //初始化
                ImageView ivAvatar = customView.findViewById(R.id.ivAvatar);
                TextView tvNickName = customView.findViewById(R.id.tvNickName);

                CommonImageLoader.load(timUserProfile.getFaceUrl())
                        .circle()
                        .placeholder(R.drawable.user_default)
                        .error(R.drawable.user_default)
                        .into(ivAvatar);

                if (!TextUtils.isEmpty(model.getRemark())) {
                    tvNickName.setText(model.getRemark());
                } else if (!TextUtils.isEmpty(timUserProfile.getNickName())) {
                    tvNickName.setText(timUserProfile.getNickName());
                } else {
                    tvNickName.setText(timUserProfile.getIdentifier());
                }


                final ImageView ivGetRedPacket = customView.findViewById(R.id.ivGetRedPacket);
                ivGetRedPacket.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openRedPacket(model, receiveMessage, redPacketDetailModel);
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }

        });
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = DimensionUtil.dip2px(266);
        dialog.getWindow().setAttributes(lp);
    }

    private void openRedPacket(final ElemExtModel model, final MessageInfo receiveMessage, final GroupRedPacketDetailModel redPacketDetailModel) {
        showLoading();
        RepositoryFactory.getRemotePayRepository().reciveRedEnvelope(model.getrId(), groupChatId)
                .compose(RxScheduler.<ResponseModel<GroupRedPacketModel>>toMain())
                .subscribe(new CommonObserver<GroupRedPacketModel>() {
                    @Override
                    public void onSuccess(GroupRedPacketModel data) {
                        SoundPlayUtils.play(3);
                        hideLoading();
                        sendRedpacketReceiveSuccess(receiveMessage, redPacketDetailModel);
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        hideLoading();
                    }

                    @Override
                    public void onNetError(ResponseModel data) {

                    }
                });
    }

    private void sendRedpacketReceiveSuccess(MessageInfo receivedMsg, GroupRedPacketDetailModel model) {
        final TIMCustomElem elem = (TIMCustomElem) receivedMsg.getTIMMessage().getElement(0);
        String data = new String(elem.getData());
        final ElemExtModel dataModel = JsonUtil.strToModel(data, ElemExtModel.class);

        MessageInfo messageInfo = MessageInfoUtil.buildTransferCustomMessage(MessageInfoUtil.TRANSFER_RED_PACKET_RECEIVED, "");
        TIMMessage timMessage = new TIMMessage();
        dataModel.setMessageType(MessageInfoUtil.TRANSFER_RED_PACKET_RECEIVED);
        dataModel.setBeReceivedMessageId(receivedMsg.getMsgId());
        dataModel.setSendUserName(model.getNickName());
        dataModel.setSendUserId(model.getuId());
        dataModel.setReceiveUserName(TIMFriendshipManager.getInstance().queryUserProfile(TIMManager.getInstance().getLoginUser()).getNickName());
        dataModel.setReceiveUserId(TIMManager.getInstance().getLoginUser());
        elem.setData(JsonUtil.moderToString(dataModel).getBytes());
        CacheUtil.getInstance().saveTransferMessageId(receivedMsg.getMsgId());
        timMessage.addElement(elem);
        messageInfo.setTIMMessage(timMessage);

        startActivity(GroupRedPacketDetailActivity.newIntent(getActivity(), groupChatId, dataModel.getrId()),
                ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity()).toBundle());

        GroupChatManager.getInstance().sendGroupMessage(messageInfo, false, null);
        chatPanel.notifyDataSetChanged();
    }

    public class NicknameStateReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                boolean state = intent.getBooleanExtra("state", false);
                chatPanel.isShowNickname(state);
            }
        }
    }

    @Override
    public void onDestroy() {
        getActivity().unregisterReceiver(mReceiver);
        super.onDestroy();
    }
}
