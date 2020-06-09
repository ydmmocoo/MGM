package com.fjx.mg;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.library.repository.data.UserCenter;

/**
 * @author yedeman
 * @date 2020/5/21.
 * email：ydmmocoo@gmail.com
 * description：
 */
public class NotifyClickReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        String type=intent.getStringExtra("type");
        if (type.equals("1")){//新闻推送
            intent = new Intent("mg_NewsDetailActivity");
            intent.putExtra("cid", intent.getStringExtra("id"));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }else if (type.equals("2")){//普通推送
            if (!UserCenter.hasLogin()) {
                return;
            }
            intent = new Intent("com.fjx.IMNoticeActivity");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }else if (type.equals("3")){//支付推送
            if (!UserCenter.hasLogin()) {
                return;
            }
            intent = new Intent("com.fjx.IMNoticeActivity");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }
}
