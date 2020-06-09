package com.common.sharesdk;

import android.app.Application;
import com.mob.MobSDK;

public class ShareSdkConfig {

    public static void init(final Application application) {
        MobSDK.init(application.getApplicationContext());
        /*try {
            MobPush.getRegistrationId(new MobPushCallback<String>() {
                @Override
                public void onCallback(String s) {
                    try {
                        if (!TextUtils.isEmpty(s)) {
                            ShareApi shareApi = new ShareApiImpl();
                            shareApi.saveRegistrationId(s);
                            Log.d("MobPushLog", s + "\n");
                        }
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        MobPush.setClickNotificationToLaunchMainActivity(false);//TODO  根据实际情况选择跳转
        MobPush.addPushReceiver(new MobPushReceiver() {

            @Override
            public void onCustomMessageReceive(Context context, MobPushCustomMessage message) {
                //接收到自定义消息（透传消息）
                String extrasMap = message.getExtrasMap().toString();
                Log.e("getExtrasMap", "onCustomMessageReceive\n" + extrasMap);
            }

            @Override
            public void onNotifyMessageReceive(Context context, MobPushNotifyMessage message) {
                //接收到通知消息
            }

            @Override
            public void onNotifyMessageOpenedReceive(Context context, MobPushNotifyMessage message) {
                String extrasMap = message.getExtrasMap().toString();
                Log.e("getExtrasMap", extrasMap);
                SharedPreferencesUtil.name("openActName").put("extrasMap", extrasMap).apply();
                EventBus.getDefault().post(new ShareOpenActEvent(extrasMap));
            }

            @Override
            public void onTagsCallback(Context context, String[] tags, int operation, int errorCode) {
                //标签操作回调
            }

            @Override
            public void onAliasCallback(Context context, String alias, int operation, int errorCode) {
                //别名操作回调
            }
        });*/
        MobSDK.submitPolicyGrantResult(true, null);
    }
}
