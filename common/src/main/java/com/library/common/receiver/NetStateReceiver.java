package com.library.common.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by 15 on 2017/10/12.
 */

public class NetStateReceiver extends BroadcastReceiver {
    public final static int STATE_DATA = 1;
    public final static int STATE_WIFI = 2;
    public final static int STATE_NO_CONNECT = 3;

    private OnNetworkStateChangeListener mOnNetworkStateChangeListener;

    @Override
    public void onReceive(Context context, Intent intent) {
        // 监听网络连接的设置，包括wifi和移动数据的打开和关闭。.
        // 最好用的还是这个监听。wifi如果打开，关闭，以及连接上可用的连接都会接到监听。见log
        String action = intent.getAction();
        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(action)) {
            ConnectivityManager manager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo activeNetwork = manager.getActiveNetworkInfo();
            if (activeNetwork != null) { // connected to the internet
                //网络连接，wifi连接不一定可以联网
                if (activeNetwork.isConnected()) {
                    //wifi可用
                    if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                        mOnNetworkStateChangeListener.onStateChange(STATE_WIFI);
                    } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                        //数据流量可用
                        mOnNetworkStateChangeListener.onStateChange(STATE_DATA);
                    }
                } else {
                    mOnNetworkStateChangeListener.onStateChange(STATE_NO_CONNECT);      //无法联网
                }
            } else {
                mOnNetworkStateChangeListener.onStateChange(STATE_NO_CONNECT); //无网络
            }


        }
    }

    public void setOnNetworkStateChangeListener(OnNetworkStateChangeListener mOnNetworkStateChangeListener) {
        this.mOnNetworkStateChangeListener = mOnNetworkStateChangeListener;
    }

    public interface OnNetworkStateChangeListener {
        void onStateChange(int curState);
    }
}
