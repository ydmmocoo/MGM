package com.fjx.mg.utils;

import android.net.TrafficStats;

/**
 * Author    by hanlz
 * Date      on 2020/1/20.
 * Description：网络下载速度工具类
 */
public class DownLoadSpeedUtil {

    public static long lastTotalRxBytes;
    public static long lastTimeStamp;

    /**
     * 获取网络下载速度
     *
     * @param uid
     * @return
     */
    public static int getNetSpeed(int uid) {

        long nowTotalRxBytes = getTotalRxBytes(uid);
        long nowTimeStamp = System.currentTimeMillis();
        long speed = ((nowTotalRxBytes - lastTotalRxBytes) * 1000 / (nowTimeStamp - lastTimeStamp));//毫秒转换

        lastTimeStamp = nowTimeStamp;
        lastTotalRxBytes = nowTotalRxBytes;

        int result = 0;
        try {
            result = (int) speed;
        } catch (Exception e) {
        }
        return result;
    }

    public static long getTotalRxBytes(int uid) {
        //转为KB
        return TrafficStats.getUidRxBytes(uid) == TrafficStats.UNSUPPORTED ? 0 : (TrafficStats.getTotalRxBytes() / 1024);
    }

    /**
     * 下载速度字符串格式化
     *
     * @param speed
     * @return
     */
    public static String speedFormat(int speed) {
        String result;
        if (speed > 1024) {
            int partA = speed / 1024;
            int partB = (speed - partA * 1024) / 100;
            result = partA + "." + partB + "m/s";
        } else {
            result = speed + "kb/s";
        }
        return result;
    }

}
