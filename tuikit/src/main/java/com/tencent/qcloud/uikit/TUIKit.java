package com.tencent.qcloud.uikit;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.library.common.callback.CActivityManager;
import com.library.common.utils.ContextManager;
import com.library.common.utils.JsonUtil;
import com.library.common.utils.NotifyUtil;
import com.library.common.utils.SharedPreferencesUtil;
import com.library.common.view.shortcutbadger.ShortcutBadger;
import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMConnListener;
import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMFriendshipManager;
import com.tencent.imsdk.TIMGroupEventListener;
import com.tencent.imsdk.TIMGroupTipsElem;
import com.tencent.imsdk.TIMLogLevel;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMMessageListener;
import com.tencent.imsdk.TIMOfflinePushListener;
import com.tencent.imsdk.TIMOfflinePushNotification;
import com.tencent.imsdk.TIMRefreshListener;
import com.tencent.imsdk.TIMSNSChangeInfo;
import com.tencent.imsdk.TIMSdkConfig;
import com.tencent.imsdk.TIMUserConfig;
import com.tencent.imsdk.TIMUserStatusListener;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.ext.message.TIMManagerExt;
import com.tencent.imsdk.ext.message.TIMMessageReceipt;
import com.tencent.imsdk.ext.message.TIMMessageReceiptListener;
import com.tencent.imsdk.ext.message.TIMUserConfigMsgExt;
import com.tencent.imsdk.friendship.TIMFriend;
import com.tencent.imsdk.friendship.TIMFriendPendencyInfo;
import com.tencent.imsdk.friendship.TIMFriendPendencyItem;
import com.tencent.imsdk.friendship.TIMFriendPendencyRequest;
import com.tencent.imsdk.friendship.TIMFriendPendencyResponse;
import com.tencent.imsdk.friendship.TIMFriendshipListener;
import com.tencent.imsdk.friendship.TIMPendencyType;
import com.tencent.qcloud.uikit.business.chat.c2c.model.C2CChatManager;
import com.tencent.qcloud.uikit.business.chat.group.model.GroupChatManager;
import com.tencent.qcloud.uikit.business.session.model.SessionManager;
import com.tencent.qcloud.uikit.common.BackgroundTasks;
import com.tencent.qcloud.uikit.common.IUIKitCallBack;
import com.tencent.qcloud.uikit.common.component.face.FaceManager;
import com.tencent.qcloud.uikit.common.utils.FileUtil;
import com.tencent.qcloud.uikit.operation.UIKitMessageRevokedManager;

import java.util.ArrayList;
import java.util.List;


public class TUIKit {

    private static Context appContext;
    private static BaseUIKitConfigs baseConfigs;

    /**
     * TUIKit的初始化函数
     *
     * @param context  应用的上下文，一般为对应应用的ApplicationContext
     * @param sdkAppID 您在腾讯云注册应用时分配的sdkAppID
     * @param configs  TUIKit的相关配置项，一般使用默认即可，需特殊配置参考API文档
     */
    public static void init(Context context, int sdkAppID, BaseUIKitConfigs configs) {
        appContext = context;
        baseConfigs = configs;
        baseConfigs.setAppCacheDir(context.getCacheDir().getAbsolutePath());
        long current = System.currentTimeMillis();

        initIM(context, sdkAppID);
        System.out.println("TUIKIT>>>>>>>>>>>>>>>>>>" + (System.currentTimeMillis() - current));
        current = System.currentTimeMillis();

        BackgroundTasks.initInstance();
        FileUtil.initPath(); // 取决于app什么时候获取到权限，即使在application中初始化，首次安装时，存在获取不到权限，建议app端在activity中再初始化一次，确保文件目录完整创建
        System.out.println("TUIKIT>>>>>>>>>>>>>>>>>>" + (System.currentTimeMillis() - current));
        current = System.currentTimeMillis();
        FaceManager.loadFaceFiles();
        System.out.println("TUIKIT>>>>>>>>>>>>>>>>>>" + (System.currentTimeMillis() - current));

        SessionManager.getInstance().init();
        C2CChatManager.getInstance().init();
        GroupChatManager.getInstance().init();
    }

    public static void login(String userid, String usersig, final IUIKitCallBack callback) {
        TIMManager.getInstance().login(userid, usersig, new TIMCallBack() {
            @Override
            public void onError(int code, String desc) {
                callback.onError("TUIKit login", code, desc);

            }

            @Override
            public void onSuccess() {
                callback.onSuccess(null);
            }
        });
    }

    private static void initIM(Context context, int sdkAppID) {
        TIMSdkConfig config = getBaseConfigs().getTIMSdkConfig();
        if (config == null) {
            config = new TIMSdkConfig(sdkAppID)
                    .setLogLevel(TIMLogLevel.DEBUG);
//            config.setLogCallbackLevel(TIMLogLevel.DEBUG);
//            config.setLogListener(new TIMLogListener() {
//                @Override
//                public void log(int level, String tag, String msg) {
//                    Log.e("TUIKit", "test session wrapper jni, msg = " + msg);
//                }
//            });
            config.enableLogPrint(true);
        }
        TIMManager.getInstance().init(context, config);
        // 设置离线消息通知
        TIMManager.getInstance().setOfflinePushListener(new TIMOfflinePushListener() {

            @Override
            public void handleNotification(TIMOfflinePushNotification var1) {

            }
        });

        TIMUserConfig userConfig = new TIMUserConfig();
        userConfig.setUserStatusListener(new TIMUserStatusListener() {
            @Override
            public void onForceOffline() {
                if (baseConfigs.getIMEventListener() != null) {
                    baseConfigs.getIMEventListener().onForceOffline();
                }
//                TUIKit.unInit();
            }

            @Override
            public void onUserSigExpired() {
                if (baseConfigs.getIMEventListener() != null) {
                    baseConfigs.getIMEventListener().onUserSigExpired();
                }
//                TUIKit.unInit();
            }
        });

        userConfig.setConnectionListener(new TIMConnListener() {
            @Override
            public void onConnected() {
                if (getBaseConfigs().getIMEventListener() != null)
                    getBaseConfigs().getIMEventListener().onConnected();
            }

            @Override
            public void onDisconnected(int code, String desc) {
                if (getBaseConfigs().getIMEventListener() != null)
                    getBaseConfigs().getIMEventListener().onDisconnected(code, desc);
            }

            @Override
            public void onWifiNeedAuth(String name) {
                if (getBaseConfigs().getIMEventListener() != null)
                    getBaseConfigs().getIMEventListener().onWifiNeedAuth(name);
            }
        });

        userConfig.setRefreshListener(new TIMRefreshListener() {
            @Override
            public void onRefresh() {

            }

            @Override
            public void onRefreshConversation(List<TIMConversation> conversations) {
                SessionManager.getInstance().onRefreshConversation(conversations);
                if (TUIKit.getBaseConfigs().getIMEventListener() != null) {
                    TUIKit.getBaseConfigs().getIMEventListener().onRefreshConversation(conversations);
                }
            }
        });

        userConfig.setGroupEventListener(new TIMGroupEventListener() {
            @Override
            public void onGroupTipsEvent(TIMGroupTipsElem elem) {
                if (TUIKit.getBaseConfigs().getIMEventListener() != null) {
                    TUIKit.getBaseConfigs().getIMEventListener().onGroupTipsEvent(elem);
                }
            }
        });

        userConfig.setFriendshipListener(new TIMFriendshipListener() {

            @Override
            public void onAddFriends(List<String> list) {
                Log.d("setFriendshipListener", JsonUtil.moderToString(list));
            }

            @Override
            public void onDelFriends(List<String> list) {
                Log.d("setFriendshipListener", JsonUtil.moderToString(list));
                for (String imUserId : list) {
                    TIMManagerExt.getInstance().deleteConversation(TIMConversationType.C2C, imUserId);
                }
            }

            @Override
            public void onFriendProfileUpdate(List<TIMSNSChangeInfo> list) {
                Log.d("setFriendshipListener", "onFriendProfileUpdate:" + JsonUtil.moderToString(list));

            }

            @Override
            public void onAddFriendReqs(List<TIMFriendPendencyInfo> list) {
                Log.d("setFriendshipListener", JsonUtil.moderToString(list));
                if (!list.isEmpty()) {
//                    showNotifaction(list.get(0));
                    getPendencyList(list.get(0));
                }

            }
        });

        TIMManager.getInstance().addMessageListener(new TIMMessageListener() {
            @Override
            public boolean onNewMessages(List<TIMMessage> msgs) {
                if (TUIKit.getBaseConfigs().getIMEventListener() != null) {
                    TUIKit.getBaseConfigs().getIMEventListener().onNewMessages(msgs);
                }
                return false;
            }
        });
        //TIMUserConfig 增加 TIMUserConfigMsgExt 中的所有接口
//        TIMUserConfigMsgExt ext = new TIMUserConfigMsgExt(userConfig);
//        ext.setMessageRevokedListener(UIKitMessageRevokedManager.getInstance());
//        // 禁用自动上报，通过调用已读上报接口来实现已读功能
//        ext.setAutoReportEnabled(false);
//        TIMManager.getInstance().setUserConfig(ext);

    }

    public static List<TIMFriend> getAllFriend() {
        String text = SharedPreferencesUtil.name("tim_friend_data").getString("all_friend", "");
        if (TextUtils.isEmpty(text)) return new ArrayList<>();
        return JsonUtil.jsonToList(text, TIMFriend.class);
    }

    public static TIMFriend getFriend(String uid) {
        List<TIMFriend> list = getAllFriend();
        for (TIMFriend friend : list) {
            if (TextUtils.equals(friend.getIdentifier(), uid)) return friend;
        }
        return null;
    }


    public static void unInit() {
        SessionManager.getInstance().destroySession();
        C2CChatManager.getInstance().destroyC2CChat();
        GroupChatManager.getInstance().destroyGroupChat();
    }


    public static Context getAppContext() {
        return appContext;
    }

    public static BaseUIKitConfigs getBaseConfigs() {
        return baseConfigs;
    }


    private static void showNotifaction(TIMFriendPendencyInfo msg) {
        if (msg == null) return;

//        String faceUrl = message.getSenderProfile().getFaceUrl();
        Intent intent = new Intent("mg_NewFriendRequestActivity");
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pIntent = PendingIntent.getActivity(CActivityManager.getAppManager().currentActivity(),
                1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        int smallIcon = R.drawable.app_logo;
        String ticker = "收到一条好友请求";
        String title = "好友请求";
        String content = msg.getAddWording();

        final NotifyUtil notify1 = new NotifyUtil(CActivityManager.getAppManager().currentActivity(), 1);
        notify1.notify_normal_singline(pIntent, smallIcon, ticker, title, content, true, true, false);

        if (Build.MANUFACTURER.equalsIgnoreCase("Xiaomi")) {
            ShortcutBadger.applyCount(ContextManager.getContext(), 1);
            ShortcutBadger.applyNotification(ContextManager.getContext(), notify1.getNotification(), 1);
        }
    }

    /**
     * im发送好友请求时，在对方和自己都会收到一条通知，且无法判断发起请求方…………%……&*（（（
     *
     * @param pendencyInfo
     */
    private static void getPendencyList(final TIMFriendPendencyInfo pendencyInfo) {
        TIMFriendPendencyRequest request = new TIMFriendPendencyRequest();
        request.setNumPerPage(100);
        request.setTimPendencyGetType(TIMPendencyType.TIM_PENDENCY_COME_IN);
        TIMFriendshipManager.getInstance().getPendencyList(request, new TIMValueCallBack<TIMFriendPendencyResponse>() {

            @Override
            public void onError(int i, String s) {

            }

            @Override
            public void onSuccess(TIMFriendPendencyResponse response) {
                if (response == null || response.getItems().isEmpty()) return;
                for (TIMFriendPendencyItem item : response.getItems()) {
                    if (TextUtils.equals(item.getIdentifier(), pendencyInfo.getFromUser())) {
                        showNotifaction(pendencyInfo);
                        return;
                    }
                }
            }
        });
    }
}
