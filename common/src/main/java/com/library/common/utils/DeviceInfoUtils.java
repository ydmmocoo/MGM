package com.library.common.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.TimeZone;

import retrofit2.http.PUT;

/**
 * Author    by hanlz
 * Date      on 2019/11/6.
 * Description：Android设备基本信息获取
 * author path:https://www.jianshu.com/p/6a0ef26f783a
 */
public class DeviceInfoUtils {

    /**
     * @return 设备SDK版本
     */
    public static String getApiVersion() {
        return Build.VERSION.SDK;
    }

    /**
     * @return 设备的系统版本
     */
    public static String getReleaseVersion() {
        return Build.VERSION.RELEASE;
    }

    /**
     * @return 设备型号
     */
    public static String getDeviceModel() {
        return Build.MODEL;
    }

    /**
     * @return 手机品牌
     */
    public static String getBrand() {
        return Build.BRAND;
    }

    /*public static String getAndroidId() {
        String androidId = null;
        try {
            TelephonyManager tm = (TelephonyManager) ContextManager.getContext().getSystemService(Context.TELEPHONY_SERVICE);
            androidId = tm.getDeviceId();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return androidId;
    }*/

    /**
     * 获取ANDROID_ID
     * @param context
     * @return
     */
    public static String getAndroidId(Context context) {
        return Settings.System.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    /**
     * @return 厂商分配的序列号
     */
    public static String getSerial() {
        try {
            return Build.class.getField("SERIAL").get(null).toString(); //厂商分配的序列号
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * @return 生产该产品的内部代码
     */
    public static String getProduct() {
        return Build.PRODUCT;
    }

    /**
     * @return 手机固件编号
     */
    public static String getDisplay() {
        return Build.DISPLAY;
    }

    /**
     * @return 出厂时设备名称
     */
    public static String getDeviceName() {
        return Build.DEVICE; //出厂时设备名称
    }

    /**
     * @return 硬件平台名称
     */
    public static String getHardware() {
        return Build.HARDWARE; //硬件平台名称
    }

    /**
     * @return ROM的标签
     */
    public static String getTags() {
        return Build.TAGS; //ROM的标签
    }

    /**
     * @return 当前时区
     * 当前时区(dstDiff=0,gmfOffset=480)
     */
    public static String getTimeZone() {
        TimeZone tz = TimeZone.getDefault();
        return tz.getDisplayName(false, TimeZone.SHORT) + " Timezon id :: " + tz.getID();
    }

    /**
     * @return 获取系统语言
     */
    public static String getSystemLanguage() {
        String language = null;
        try {
            language = ContextManager.getContext().getResources().getConfiguration().locale.getLanguage();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return language;
    }

    /**
     * @return 获得系统亮度
     */
    public static int getSystemBrightness() {
        int systemBrightness = 0;
        try {
            systemBrightness = Settings.System.getInt(ContextManager.getContext().getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        return systemBrightness;
    }

    /**
     * 获取屏幕分辨率
     *
     * @return
     */
    public static String getScreenRes() {
        try {
            // 获取屏幕分辨率
            WindowManager wm = (WindowManager) (ContextManager.getContext().getSystemService(Context.WINDOW_SERVICE));
            DisplayMetrics dm = new DisplayMetrics();
            wm.getDefaultDisplay().getMetrics(dm);
            int screenWidth = dm.widthPixels;
            int screenHeight = dm.heightPixels;
            int density = dm.densityDpi;
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("screenWidth", screenWidth);
            jsonObject.put("screenHeight", screenHeight);
            jsonObject.put("density", density);
            return jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取androidId
     *
     * @return 系统初始化时生成的随机id
     */
    public static String getAndroidID() {
        try {
            return Settings.System.getString(ContextManager.getContext().getContentResolver(), Settings.System.ANDROID_ID);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @return 获取应用包名
     */
    public static String getPackageName() {
        return ContextManager.getContext().getPackageName();
    }

    /**
     * 应用版本号
     *
     * @return
     */
    public static String getApkVersionName() {
        PackageManager manager = ContextManager.getContext().getPackageManager();
        String name = null;
        try {
            PackageInfo info = manager.getPackageInfo(getPackageName(), 0);
            name = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return name;
    }

    /**
     * 基带版本
     *
     * @return
     */
    public static String getBasebandVer() {
        String Version = "";
        try {
            Class cl = Class.forName("android.os.SystemProperties");
            Object invoker = cl.newInstance();
            Method m = cl.getMethod("get", new Class[]{String.class, String.class});
            Object result = m.invoke(invoker, new Object[]{"gsm.version.baseband", "no message"});
            Version = (String) result;
        } catch (Exception e) {
        }
        return Version;
    }

    /**
     * cpu最大频率
     *
     * @return
     */
    public static String getCPUFreQuency() {
        // 获取CPU最大频率（单位KHZ）
        // "/system/bin/cat" 命令行
        // "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq" 存储最大频率的文件的路径
        String result = "";
        ProcessBuilder cmd;
        try {
            String[] args = {"/system/bin/cat",
                    "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq"};
            cmd = new ProcessBuilder(args);
            Process process = cmd.start();
            InputStream in = process.getInputStream();
            byte[] re = new byte[24];
            while (in.read(re) != -1) {
                result = result + new String(re);
            }
            in.close();
        } catch (IOException ex) {
            ex.printStackTrace();
            result = "N/A";
        }
        return result.trim();
    }


    /**
     * 获取当前内存信息对象
     *
     * @return 当前内存信息对象。
     */
    public static ActivityManager.MemoryInfo getMemoryInfo() {
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        ActivityManager activityManager = (ActivityManager) ContextManager.getContext().getSystemService(Context.ACTIVITY_SERVICE);
        activityManager.getMemoryInfo(mi);
        return mi;
    }

    /**
     * 是否允许位置模拟
     *
     * @return
     */
    public static boolean isAllowMockLocation() {
        boolean isAllowMock = false;
        try {
            isAllowMock = Settings.Secure.getInt(ContextManager.getContext().getContentResolver(), Settings.Secure.ALLOW_MOCK_LOCATION, 0) != 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isAllowMock;
    }


}
