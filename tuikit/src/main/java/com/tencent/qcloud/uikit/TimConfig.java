package com.tencent.qcloud.uikit;

import android.app.Activity;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.library.common.base.BaseActivity;
import com.library.common.base.BaseApp;
import com.library.common.callback.CActivityManager;
import com.library.common.utils.ContextManager;
import com.library.common.utils.JsonUtil;
import com.library.common.utils.LogTUtil;
import com.library.common.utils.NotifyUtil;
import com.library.common.utils.SharedPreferencesUtil;
import com.library.common.view.shortcutbadger.ShortcutBadger;
import com.tencent.imsdk.TIMCustomElem;
import com.tencent.imsdk.TIMElem;
import com.tencent.imsdk.TIMElemType;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMMessageListener;
import com.tencent.imsdk.TIMMessageStatus;
import com.tencent.imsdk.TIMTextElem;
import com.tencent.imsdk.TIMUserConfig;
import com.tencent.imsdk.TIMUserProfile;
import com.tencent.imsdk.TIMUserStatusListener;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.session.SessionWrapper;
import com.tencent.imsdk.utils.IMFunc;
import com.tencent.qcloud.uikit.business.chat.model.CacheUtil;
import com.tencent.qcloud.uikit.business.chat.model.ElemExtModel;
import com.tencent.qcloud.uikit.business.chat.model.MessageInfoUtil;
import com.tencent.qcloud.uikit.common.utils.SoundPlayUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import static com.tencent.qcloud.uikit.business.chat.model.MessageInfoUtil.TRANSFER_ACCOUNT_UN_RECEIVED;
import static com.tencent.qcloud.uikit.business.chat.model.MessageInfoUtil.TRANSFER_RED_PACKET_UN_RECEIVED;


public class TimConfig {

    public static boolean isRelease =!BuildConfig.DEBUG ;

    private static int TIM_APPID_DEBUG = 1400246080;//测试
    private static int TIM_APPID_REALEASE = 1400208096;//正式


//    private static String XM_PUSH_APPID = "2882303761518035227";
//    private static String XM_PUSH_APPKEY = "qJEBxkAlHe0zsBiVMz2m5g==";

    private static String BUGLY_APPID_REALSE = "7a2584213b";
    private static String BUGLY_APPID_DEBUG = "7a2584213b";

    public static String FACEBOOK_API_KEY = "899172270510395";//facebook APPKEY
    public static String FACEBOOK_APP_SECRET = "b869434ddab6694f3e592a1fe4e1bdd9";

    public static int getTimAPPID = getAppId();

    private static int getAppId() {
        if (isRelease) {
            return TIM_APPID_REALEASE;
        } else {
            return TIM_APPID_DEBUG;
        }
    }

    public static String getBuglyAppId() {
        if (isRelease) {
            return BUGLY_APPID_REALSE;
        } else {
            return BUGLY_APPID_DEBUG;
        }
    }

    public static String getKey() {
        if (isRelease) {
            return "mg!DDQwew@%#";
        } else {
            return "fix!ASDFA%#@";
        }
    }

    //ws = new WebSocket("wss://www.messageglobal-online.com/wss");
    public static String getWebSocket() {
        if (isRelease) {
            return "wss://www.messageglobal-online.com/wss";
        } else {
            return "ws://139.9.38.218:8282";
        }
    }


    public static int count = 0;
    private static MaterialDialog dialog;

    //    private static int TIM_APPID = 1400207495;
    public static void config(Application application) {
        SoundPlayUtils.init();
        LogTUtil.init(TimConfig.isRelease);//初始化log工具类
        Context context = application.getApplicationContext();
        //判断是否是在主线程
        if (SessionWrapper.isMainProcess(context)) {
            /**
             * TUIKit的初始化函数
             *
             * @param context  应用的上下文，一般为对应应用的ApplicationContext
             * @param sdkAppID 您在腾讯云注册应用时分配的sdkAppID
             * @param configs  TUIKit的相关配置项，一般使用默认即可，需特殊配置参考API文档
             */
            long current = System.currentTimeMillis();
            TUIKit.init(application, getTimAPPID, BaseUIKitConfigs.getDefaultConfigs());
            System.out.println(">>>>>>>>>>>>>>>>>>" + (System.currentTimeMillis() - current));
            //添加自定初始化配置
            customConfig();
            System.out.println(">>>>>>>>>>>>>>>>>>" + (System.currentTimeMillis() - current));
        }
    }

    public static void customConfig() {
        TIMUserConfig userConfig = new TIMUserConfig()
                .setUserStatusListener(new TIMUserStatusListener() {
                    @Override
                    public void onForceOffline() {
                        final Activity activity = CActivityManager.getAppManager().currentActivity();
                        if (activity == null) return;
                        SharedPreferencesUtil.name("user_data").put("UserInfoModel", "").apply();
                        dialog = new MaterialDialog.Builder(activity)
                                .title(R.string.attention)
                                .content(R.string.force_logout)
                                .cancelable(false)
                                .positiveText(R.string.relogin)
                                .onPositive(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        Intent intent = new Intent("mg_LoginActivity");
                                        intent.putExtra("newMain", true);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        activity.startActivity(intent);
                                        dialog.dismiss();
                                    }
                                })
                                .onNegative(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        Intent intent = new Intent("mg_LoginActivity");
                                        intent.putExtra("newMain", true);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        activity.startActivity(intent);
                                        dialog.dismiss();
                                    }
                                })
                                .negativeText("").build();

                        if (!activity.isFinishing()) {
                            dialog.show();
                        }
                    }

                    @Override
                    public void onUserSigExpired() {
                    }
                });
        //将用户配置与通讯管理器进行绑定
        TIMManager.getInstance().setUserConfig(userConfig);
        TIMManager.getInstance().addMessageListener(new TIMMessageListener() {
            @Override
            public boolean onNewMessages(List<TIMMessage> msgs) {
                if (msgs.size() > 0) {
                    showNotifaction(msgs.get(0));
                    saveReceiveState(msgs);
                }
                return true;
            }
        });

        //添加自定义表情
        //TUIKit.getBaseConfigs().setFaceConfigs(initCustomConfig());
//
//        }
    }

    private static void showNotifaction(final TIMMessage message) {

        final String msg = getMessage(message);
        if (msg == null) return;

        message.getSenderProfile(new TIMValueCallBack<TIMUserProfile>() {

            @Override
            public void onError(int i, String s) {
            }

            @Override
            public void onSuccess(TIMUserProfile profile) {
                if (profile == null) return;
                String nickName = profile.getNickName();
                //String faceUrl = message.getSenderProfile().getFaceUrl();
                Intent intent = new Intent("mg_ChatActivity");
                intent.putExtra("IS_GROUP", false);
                intent.putExtra("INTENT_DATA", message.getSender());
                intent.putExtra("nickName", nickName);

                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                PendingIntent pIntent = PendingIntent.getActivity(CActivityManager.getAppManager().currentActivity(),
                        1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                int smallIcon = R.drawable.app_logo;
                String ticker = ContextManager.getContext().getString(R.string.receive_a_message);
                String title = nickName;
                String content = msg;

                final NotifyUtil notify1 = new NotifyUtil(CActivityManager.getAppManager().currentActivity(), 1);
                notify1.notify_normal_singline(pIntent, smallIcon, ticker, title, content, true, true, false);

                if (Build.MANUFACTURER.equalsIgnoreCase("Xiaomi")) {
                    ShortcutBadger.applyCount(ContextManager.getContext(), count);
                    ShortcutBadger.applyNotification(ContextManager.getContext(), notify1.getNotification(), count);
                }
            }
        });
    }

    public static String getMessage(TIMMessage timMessage) {
        if (timMessage == null || timMessage.status() == TIMMessageStatus.HasDeleted)
            return null;
        if (timMessage.getElementCount() > 0) {
            TIMElem ele = timMessage.getElement(0);
            if (ele == null) {
                return null;
            }
            TIMElemType type = ele.getType();
            if (type == TIMElemType.Invalid) {
                return null;
            }
            if (type == TIMElemType.Custom) {
                TIMCustomElem customElem = (TIMCustomElem) ele;
                String data = new String(customElem.getData());
                if ("group_create".equals(data)) {
                    //创建群组
                    return ContextManager.getContext().getString(R.string.create_group);
                } else {
                    try {
                        String mestype = (String) JsonUtil.jsonToMap(data).get("messageType");
                        if (TextUtils.equals(mestype, MessageInfoUtil.FRIEND_ADD)) {
                            return ContextManager.getContext().getString(R.string.add_friend_success);
                        }
                    } catch (Exception e) {
                        Log.d("timConfig", "mesType is error  " + e.toString());
                    }
                    ElemExtModel model = JsonUtil.strToModel(data, ElemExtModel.class);
                    if (model != null) {
                        String messageType = model.getMessageType();
                        if (TextUtils.equals(TRANSFER_ACCOUNT_UN_RECEIVED, messageType)) {
                            return "[".concat(ContextManager.getContext().getString(R.string.receive_a_transfer_message)).concat("]");
                        } else if (TextUtils.equals(TRANSFER_RED_PACKET_UN_RECEIVED, messageType)) {
                            return "[".concat(ContextManager.getContext().getString(R.string.receive_a_red_package)).concat("]");
                        }
                    }
                }
            } else {
                if (type == TIMElemType.Text) {
                    TIMTextElem txtEle = (TIMTextElem) ele;
                    return txtEle.getText();
                } else if (type == TIMElemType.Face) {
                    return "[".concat(ContextManager.getContext().getString(R.string.action_custom_emoji)).concat("]");
                } else if (type == TIMElemType.Sound) {
                    return "[".concat(ContextManager.getContext().getString(R.string.action_voice)).concat("]");
                } else if (type == TIMElemType.Image) {
                    return "[".concat(ContextManager.getContext().getString(R.string.action_photo)).concat("]");
                } else if (type == TIMElemType.Video) {
                    return "[".concat(ContextManager.getContext().getString(R.string.action_video)).concat("]");
                } else if (type == TIMElemType.File) {
                    return "[".concat(ContextManager.getContext().getString(R.string.action_file)).concat("]");
                }
            }
        }
        return null;
    }

    private static void saveReceiveState(List<TIMMessage> timMessages) {
        for (TIMMessage message : timMessages) {
            if (message.getElementCount() == 0) continue;
            TIMElem elem = message.getElement(0);
            if (elem instanceof TIMCustomElem) {
                final TIMCustomElem customElem = (TIMCustomElem) elem;
                String data = new String(customElem.getData());
                final ElemExtModel dataModel = JsonUtil.strToModel(data, ElemExtModel.class);
                if (dataModel == null) continue;
                String type = dataModel.getMessageType();
                if (TextUtils.equals(type, MessageInfoUtil.TRANSFER_ACCOUNT_RECEIVED)
                        || TextUtils.equals(type, MessageInfoUtil.TRANSFER_RED_PACKET_RECEIVED)) {
                    if (TextUtils.isEmpty(dataModel.getBeReceivedMessageId())) continue;
                    CacheUtil.getInstance().saveTransferMessageId(dataModel.getBeReceivedMessageId());
                }
            }
        }
    }
}
