package com.fjx.mg.video;

import android.net.TrafficStats;

import com.fjx.mg.R;

/**
 * Author    by hanlz
 * Date      on 2019/11/14.
 * Description：
 */
public class NetSpeed {
    private static final String TAG = NetSpeed.class.getSimpleName();
    private long lastTotalRxBytes = 0;
    private long lastTimeStamp = 0;

    public String getNetSpeed(int uid) {
        long nowTotalRxBytes = getTotalRxBytes(uid);
        long nowTimeStamp = System.currentTimeMillis();
        long speed = ((nowTotalRxBytes - lastTotalRxBytes) * 1000 / (nowTimeStamp - lastTimeStamp));//毫秒转换
        lastTimeStamp = nowTimeStamp;
        lastTotalRxBytes = nowTotalRxBytes;
        StringBuilder sb = new StringBuilder();
        sb.append(R.string.current_net_speed);
        sb.append("：");
        sb.append(speed);
        sb.append("kb/s");
        return sb.toString();
    }


    //getApplicationInfo().uid
    public long getTotalRxBytes(int uid) {
        return TrafficStats.getUidRxBytes(uid) == TrafficStats.UNSUPPORTED ? 0 : (TrafficStats.getTotalRxBytes() / 1024);//转为KB
    }


}
