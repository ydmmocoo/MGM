package com.fjx.mg;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.common.sharesdk.repostory.ShareApi;
import com.common.sharesdk.repostory.ShareApiImpl;
import com.fjx.mg.main.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

/**
 * @author yedeman
 * @date 2020/5/21.
 * email：ydmmocoo@gmail.com
 * description：
 */
public class GoogleFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    //获取到谷歌到token
    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        Log.e("Google token", "Refreshed token: $token");
        sendRegistrationToServer(token);
    }

    //  回传给服务器操作
    private void sendRegistrationToServer(String token) {
        //这里我做了本地保存，你可以在你需要到地方获取
        ShareApi shareApi = new ShareApiImpl();
        shareApi.saveRegistrationId(token);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.e("55555", "getInstanceId failed", task.getException());
                            return;
                        }
                        String token = task.getResult().getToken();
                        //谷歌返回的token
                        Log.e("Google_token", token);
                    }
                });

        //这个应该可以看懂
        if (remoteMessage.getNotification() != null && remoteMessage.getNotification().getBody() != null) {
            sendNotification(getApplicationContext(), remoteMessage.getNotification().getTitle(),
                    remoteMessage.getNotification().getBody(),remoteMessage.getData());
        } else {
            sendNotification(getApplicationContext(), remoteMessage.getData().get("title"),
                    remoteMessage.getData().get("body"),remoteMessage.getData());
        }
    }

    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();
    }

    @Override
    public void onMessageSent(String s) {
        super.onMessageSent(s);
    }

    @Override
    public void onSendError(String s, Exception e) {
        super.onSendError(s, e);
    }

    private void sendNotification(Context iContext,
                                  String messageTitle,
                                  String messageBody,
                                  Map<String, String> map) {

        //跳转到你想要跳转到页面
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Bundle bundle=new Bundle();
        bundle.putString("type",map.get("type"));
        if (("1").equals(map.get("type"))) {
            bundle.putString("id", map.get("id"));
        }
        intent.putExtras(bundle);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        String channelId = getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setTicker(messageTitle)//标题
                        .setSmallIcon(R.mipmap.app_logo)//你的推送栏图标
                        .setContentTitle(messageTitle)
                        .setContentText(messageBody)//内容
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        //判断版本
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "notification",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }
        //这里如果需要的话填写你自己项目的，可以在控制台找到，强转成int类型
        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}
