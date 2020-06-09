package com.tencent.qcloud.uikit.operation.group.mvp;

import android.util.Log;

import androidx.annotation.NonNull;

import com.library.common.utils.CommonToast;
import com.library.common.utils.JsonUtil;
import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMFriendshipManager;
import com.tencent.imsdk.TIMGroupManager;
import com.tencent.imsdk.TIMGroupMemberInfo;
import com.tencent.imsdk.TIMGroupReceiveMessageOpt;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMUserProfile;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.ext.group.TIMGroupSelfInfo;
import com.tencent.imsdk.log.QLog;
import com.tencent.qcloud.uikit.business.chat.group.model.GroupChatInfo;
import com.tencent.qcloud.uikit.business.chat.group.model.GroupChatManager;
import com.tencent.qcloud.uikit.business.chat.group.model.GroupInfoUtils;
import com.tencent.qcloud.uikit.business.chat.model.MessageInfo;
import com.tencent.qcloud.uikit.business.chat.model.MessageInfoUtil;
import com.tencent.qcloud.uikit.common.IUIKitCallBack;
import com.tencent.qcloud.uikit.common.utils.UIUtils;
import com.tencent.qcloud.uikit.operation.group.event.GroupConversionRefreshEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Author    by hanlz
 * Date      on 2019/12/3.
 * Description：
 */
public class GrouChatSettingsPresenter extends GroupChatSettingsContact.Persenter {

    private GroupChatManager mChatManager;

    public GrouChatSettingsPresenter(GroupChatSettingsContact.View view) {
        super(view);
        mChatManager = GroupChatManager.getInstance();
    }


    public void deleteGroup() {
        mChatManager.deleteGroup(null, new IUIKitCallBack() {
            @Override
            public void onSuccess(Object data) {
                mView.getCurActivity().finish();
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
                QLog.e("deleteGroup", errCode + ":" + errMsg);
                CommonToast.toast(errMsg);
            }
        });
    }

    public void quiteGroup(final String groupId) {
        TIMConversation conversation = TIMManager.getInstance().getConversation(TIMConversationType.Group, groupId);
        conversation.deleteLocalMessage(new TIMCallBack() {
            @Override
            public void onError(int i, String s) {

            }

            @Override
            public void onSuccess() {
                TIMManager.getInstance().deleteConversationAndLocalMsgs(TIMConversationType.Group, groupId);
                mChatManager.quiteGroup(new IUIKitCallBack() {
                    @Override
                    public void onSuccess(Object data) {
                        mView.getCurActivity().finish();
                        EventBus.getDefault().post(new GroupConversionRefreshEvent());
                    }

                    @Override
                    public void onError(String module, int errCode, String errMsg) {
                        mView.getCurActivity().finish();
                        QLog.e("quiteGroup", errCode + ":" + errMsg);
                    }
                });
            }
        });

    }

    /**
     * 修改群名
     *
     * @param name
     */
    public void modifyGroupName(final String name) {
        mChatManager.modifyGroupInfo(name, GroupInfoUtils.MODIFY_GROUP_NAME, new IUIKitCallBack() {
            @Override
            public void onSuccess(Object data) {
                if (mView != null) {
                    mView.setGroupName(name);
                }
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
                QLog.e("modifyGroupName", errCode + ":" + errMsg);
                UIUtils.toastLongMessage(errMsg);
            }
        });
    }

    /**
     * 修改本群昵称
     *
     * @param nickname
     */
    public void modifyGroupNickname(final String nickname) {
        mChatManager.modifyGroupNickname(nickname, GroupInfoUtils.MODIFY_MEMBER_NAME, new IUIKitCallBack() {
            @Override
            public void onSuccess(Object data) {
                if (mView != null) {
                    mView.setNickname(nickname);
                }
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
                QLog.e("modifyGroupNickname", errCode + ":" + errMsg);
                UIUtils.toastLongMessage(errMsg);
            }
        });
    }

    /**
     * 获取本人在群里的资料
     */
    public void getSelfInfo(String groupId) {
        TIMGroupManager.getInstance().getSelfInfo(groupId, new TIMValueCallBack<TIMGroupSelfInfo>() {
            @Override
            public void onError(int i, String s) {

            }

            @Override
            public void onSuccess(TIMGroupSelfInfo timGroupSelfInfo) {
                if (mView != null) {
                    mView.setNameCard(timGroupSelfInfo);
                }
            }
        });
    }

    /**
     * 置顶聊天
     *
     * @param flag
     */
    public void setTopSession(boolean flag) {
        mChatManager.setTopSession(flag);
    }

    public void refreshMemberList(String groupId) {
        mChatManager.getGroupChatInfo(groupId, new IUIKitCallBack() {
            @Override
            public void onSuccess(Object data) {
                mView.getGroupChatInfo((GroupChatInfo) data);
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
                UIUtils.toastLongMessage(errMsg);
            }
        });
    }

    /**
     * 获取群组成员列表
     *
     * @param groupId 群组 ID
     */
    public void getGroupMembers(@NonNull String groupId) {
        TIMGroupManager.getInstance().getGroupMembers(groupId, new TIMValueCallBack<List<TIMGroupMemberInfo>>() {
            @Override
            public void onError(int i, String s) {

            }

            @Override
            public void onSuccess(List<TIMGroupMemberInfo> timGroupMemberInfos) {
                List<TIMUserProfile> datas = new ArrayList<>();
                for (int i = 0; i < timGroupMemberInfos.size(); i++) {
                    TIMUserProfile timUserProfile = TIMFriendshipManager.getInstance().queryUserProfile(timGroupMemberInfos.get(i).getUser());
                    datas.add(timUserProfile);
                }
                if (mView != null) {
                    mView.getTimUserProfile(datas);
                }
            }
        });
    }

    /**
     * 消息免打扰
     * //            TIMGroupReceiveMessageOpt.NotReceive---不接受群消息，服务器不会进行转发
     * //            TIMGroupReceiveMessageOpt.ReceiveNotNotify---接受群消息，不提醒
     * //            TIMGroupReceiveMessageOpt.ReceiveAndNotify----接受群消息并提醒
     */
    public void setReceiverMessagePpt(final String groupId, final String userId, final boolean open) {
        TIMGroupReceiveMessageOpt opt = TIMGroupReceiveMessageOpt.ReceiveAndNotify;
        if (open) {
            //消息免打扰
            opt = TIMGroupReceiveMessageOpt.ReceiveNotNotify;
        } else {
            opt = TIMGroupReceiveMessageOpt.ReceiveAndNotify;
        }

        TIMGroupManager.ModifyMemberInfoParam param = new TIMGroupManager.ModifyMemberInfoParam(groupId, userId);
        param.setReceiveMessageOpt(opt);
        TIMGroupManager.getInstance().modifyMemberInfo(param, new TIMCallBack() {
            @Override
            public void onError(int i, String s) {

            }

            @Override
            public void onSuccess() {
                if (mView != null) {
                    mView.messageSate(groupId, userId, open);
                }
            }
        });
    }

    public void modifyGroupFaceIcon(final String groupId, String faceUrl) {
//        String groupUrl = String.format("https://picsum.photos/id/%d/200/200", new Random().nextInt(1000));
        TIMGroupManager.ModifyGroupInfoParam param = new TIMGroupManager.ModifyGroupInfoParam(groupId);
        param.setFaceUrl(faceUrl);
        TIMGroupManager.getInstance().modifyGroupInfo(param, new TIMCallBack() {
            @Override
            public void onError(int code, String desc) {
                CommonToast.toast("修改群头像失败, code = " + code + ", info = " + desc);
            }

            @Override
            public void onSuccess() {
                CommonToast.toast("修改群头像成功");
            }
        });
    }


    public void sendMessage(final String msg, String groupId) {
        final StringBuilder sb = new StringBuilder();
        //获取群聊会话
        TIMFriendshipManager instance = TIMFriendshipManager.getInstance();
        TIMUserProfile profile = instance.queryUserProfile(TIMManager.getInstance().getLoginUser());
        if (profile == null) {
            List<String> ids = new ArrayList<>();
            ids.add(TIMManager.getInstance().getLoginUser());
            instance.getUsersProfile(ids, false, new TIMValueCallBack<List<TIMUserProfile>>() {
                @Override
                public void onError(int i, String s) {

                }

                @Override
                public void onSuccess(List<TIMUserProfile> timUserProfiles) {
                    sb.append(timUserProfiles.get(0).getNickName());
                }
            });
        } else {
            sb.append(profile.getNickName());
        }
        sb.append(msg);
        final MessageInfo createTips = MessageInfoUtil.buildGroupCustomMessage(MessageInfoUtil.GROUP_CREATE, sb.toString());
        createTips.setPeer(groupId);
        TIMConversation conversation = TIMManager.getInstance().getConversation(TIMConversationType.Group, groupId);
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        createTips.setSelf(true);
        createTips.setRead(true);
        conversation.sendMessage(createTips.getTIMMessage(), new TIMValueCallBack<TIMMessage>() {
            @Override
            public void onError(int i, String s) {

            }

            @Override
            public void onSuccess(TIMMessage timMessage) {
                Log.d("timMessage", JsonUtil.moderToString(timMessage));
            }
        });
    }
}
