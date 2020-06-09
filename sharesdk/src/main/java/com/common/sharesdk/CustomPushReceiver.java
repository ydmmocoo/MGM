package com.common.sharesdk;

import android.content.Context;

import com.mob.wrappers.MobPushWrapper;

public class CustomPushReceiver implements MobPushWrapper.MobPushReceiverWrapper {
    @Override
    public void onCustomMessageReceive(Context context, MobPushWrapper.MobPushCustomMessageWrapper messageWrapper) {
    //接收自定义消息
    }

    @Override
    public void onNotifyMessageReceive(Context context, MobPushWrapper.MobPushNotifyMessageWrapper messageWrapper) {
    //接收通知消息
    }

    @Override
    public void onNotifyMessageOpenedReceive(Context context, MobPushWrapper.MobPushNotifyMessageWrapper messageWrapper) {
    //接收通知消息被点击事件
    }

    @Override
    public void onTagsCallback(Context context, String[] strings, int i, int i1) {
    //接收tags的增改删查操作
    }

    @Override
    public void onAliasCallback(Context context, String s, int i, int i1) {
    //接收alias的增改删查操作
    }
}
