package com.fjx.mg.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;

import com.google.android.gms.maps.model.LatLng;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Author    by hanlz
 * Date      on 2019/10/22.
 * Description：
 */
public class MapNaviUtils {

    public static final String PN_GAODE_MAP = "com.autonavi.minimap"; // 高德地图包名
    public static final String PN_BAIDU_MAP = "com.baidu.BaiduMap"; // 百度地图包名
    public static final String DOWNLOAD_GAODE_MAP = "http://www.autonavi.com/"; // 高德地图下载地址
    public static final String DOWNLOAD_BAIDU_MAP = "http://map.baidu.com/zt/client/index/"; // 百度地图下载地址
    public static final String DOWNLOAD_GOOGLE_MAP = "https://play.google.com/store/apps/details?id=com.google.android.apps.maps"; // 百度地图下载地址


    /**
     * 检查应用是否安装
     *
     * @return
     */
    public static boolean isGdMapInstalled() {
        return isInstallPackage(PN_GAODE_MAP);
    }

    public static boolean isBaiduMapInstalled() {
        return isInstallPackage(PN_BAIDU_MAP);
    }

    private static boolean isInstallPackage(String packageName) {
        return new File("/data/data/" + packageName).exists();
    }

    /**
     * 百度坐标系 (BD-09) 与 火星坐标系 (GCJ-02)的转换
     * 即 百度 转 谷歌、高德
     *
     * @param latLng
     * @returns
     */
    public static LatLng BD09ToGCJ02(LatLng latLng) {
        double x_pi = 3.14159265358979324 * 3000.0 / 180.0;
        double x = latLng.longitude - 0.0065;
        double y = latLng.latitude - 0.006;
        double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * x_pi);
        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * x_pi);
        double gg_lat = z * Math.sin(theta);
        double gg_lng = z * Math.cos(theta);
        return new LatLng(gg_lat, gg_lng);
    }

    /**
     * 火星坐标系 (GCJ-02) 与百度坐标系 (BD-09) 的转换
     * 即谷歌、高德 转 百度
     *
     * @param latLng
     * @returns
     */
    public static LatLng GCJ02ToBD09(LatLng latLng) {
        double x_pi = 3.14159265358979324 * 3000.0 / 180.0;
        double z = Math.sqrt(latLng.longitude * latLng.longitude + latLng.latitude * latLng.latitude) + 0.00002 * Math.sin(latLng.latitude * x_pi);
        double theta = Math.atan2(latLng.latitude, latLng.longitude) + 0.000003 * Math.cos(latLng.longitude * x_pi);
        double bd_lat = z * Math.sin(theta) + 0.006;
        double bd_lng = z * Math.cos(theta) + 0.0065;
        return new LatLng(bd_lat, bd_lng);
    }

    /**
     * 打开高德地图导航功能
     *
     * @param context
     * @param slat    起点纬度
     * @param slon    起点经度
     * @param sname   起点名称 可不填（0,0，null）
     * @param dlat    终点纬度
     * @param dlon    终点经度
     * @param dname   终点名称 必填
     */
    public static void openGaoDeNavi(Context context, double slat, double slon, String sname, double dlat, double dlon, String dname) {
        String uriString = null;
        StringBuilder builder = new StringBuilder("amapuri://route/plan?sourceApplication=maxuslife");
        if (0 == slat) {
//            //如果不传起点（注释下面这段），默认就是现在我的位置（手机当前定位）
//            AMapLocation location = LocationService.getInstance().getAMapLocation();
//            if (LocationService.isSuccess(location)) {
//                builder.append("&sname=我的位置")
//                        .append("&slat=").append(location.getLatitude())
//                        .append("&slon=").append(location.getLongitude());
//            }
        } else {
            builder.append("&sname=").append(sname)
                    .append("&slat=").append(slat)
                    .append("&slon=").append(slon);
        }
        builder.append("&dlat=").append(dlat)
                .append("&dlon=").append(dlon)
                .append("&dname=").append(dname)
                .append("&dev=0")
                .append("&t=0");
        uriString = builder.toString();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setPackage(PN_GAODE_MAP);
        intent.setData(Uri.parse(uriString));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 打开百度地图导航功能(默认坐标点是高德地图，需要转换)
     *
     * @param context
     * @param slat    起点纬度
     * @param slon    起点经度
     * @param sname   起点名称 可不填（0,0，null）
     * @param dlat    终点纬度
     * @param dlon    终点经度
     * @param dname   终点名称 必填
     */
    public static void openBaiDuNavi(Context context, double slat, double slon, String sname, double dlat, double dlon, String dname) {
        String uriString = null;
        //终点坐标转换
        LatLng destination = new LatLng(dlat, dlon);
        LatLng destinationLatLng = GCJ02ToBD09(destination);
        dlat = destinationLatLng.latitude;
        dlon = destinationLatLng.longitude;

        StringBuilder builder = new StringBuilder("baidumap://map/direction?mode=driving&");
        if (slat != 0) {
            //起点坐标转换
            LatLng origin = new LatLng(slat, slon);
            LatLng originLatLng = GCJ02ToBD09(origin);
            slat = originLatLng.latitude;
            slon = originLatLng.longitude;

            builder.append("origin=latlng:")
                    .append(slat)
                    .append(",")
                    .append(slon)
                    .append("|name:")
                    .append(sname);
        }
        builder.append("&destination=latlng:")
                .append(dlat)
                .append(",")
                .append(dlon)
                .append("|name:")
                .append(dname);
        uriString = builder.toString();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setPackage(PN_BAIDU_MAP);
        intent.setData(Uri.parse(uriString));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void installGaode(Context context) {
        Intent intent = new Intent();
        //意图的行为，隐式意图
        intent.setAction(Intent.ACTION_VIEW);
        //意图的数据
        intent.setData(Uri.parse(DOWNLOAD_GAODE_MAP));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //启动
        context.startActivity(intent);
    }

    public static void installBaidu(Context context) {
        Intent intent = new Intent();
        //意图的行为，隐式意图
        intent.setAction(Intent.ACTION_VIEW);
        //意图的数据
        intent.setData(Uri.parse(DOWNLOAD_BAIDU_MAP));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //启动
        context.startActivity(intent);
    }

    public static void installGoogle(Context context) {
        Intent intent = new Intent();
        //意图的行为，隐式意图
        intent.setAction(Intent.ACTION_VIEW);
        //意图的数据
        intent.setData(Uri.parse(DOWNLOAD_GOOGLE_MAP));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //启动
        context.startActivity(intent);
    }

    //验证各种导航地图是否安装
    public static boolean isAvilible(Context context, String packageName) {
        //获取packagemanager
        final PackageManager packageManager = context.getPackageManager();
        //获取所有已安装程序的包信息
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
        //用于存储所有已安装程序的包名
        List<String> packageNames = new ArrayList<String>();
        //从pinfo中将包名字逐一取出，压入pName list中
        if (packageInfos != null) {
            for (int i = 0; i < packageInfos.size(); i++) {
                String packName = packageInfos.get(i).packageName;
                packageNames.add(packName);
            }
        }
        //判断packageNames中是否有目标程序的包名，有TRUE，没有FALSE
        return packageNames.contains(packageName);
    }


    //谷歌地图,起点就是定位点
    public static void startNaviGoogle(Context context, String latitude, String longitude) {
        Uri gmmIntentUri = Uri.parse("google.navigation:q=" + latitude + "," + longitude + "&mode=w");
        Intent intent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        intent.setPackage("com.google.android.apps.maps");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static boolean isGoogleMapInstalled(Context context) {
        if (isAvilible(context, "com.google.android.apps.maps")) {
            return true;
        }
        return false;
    }

}
