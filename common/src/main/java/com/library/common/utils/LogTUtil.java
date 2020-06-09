package com.library.common.utils;

import android.util.Log;

/**
 * Author    by hanlz
 * Date      on 2020/3/26.
 * Description：只用于测试环境打印一些开发中需要查看的数据 减少线上版本打印不必要数据
 * color：Warning--黄色 Info---青色  Debug----白色  Error---红色 Verbose---绿色
 */
public class LogTUtil {

    private static boolean isDebug = true;
    private static String TAG = "LogTUtil";

    public static void init(boolean isDebug) {
        LogTUtil.isDebug = isDebug;
    }

    public static void d(String tag, String msg) {
        if (!isDebug) {
            if (StringUtil.isNotEmpty(tag)) {
                Log.d(tag, msg);
            } else {
                Log.d(TAG, msg);
            }
        }
    }

    public static void d(String tag, int strId) {
        if (!isDebug) {
            if (StringUtil.isNotEmpty(tag)) {
                Log.d(tag, ContextManager.getContext().getString(strId));
            } else {
                Log.d(TAG, ContextManager.getContext().getString(strId));
            }
        }
    }

    public static void w(String tag, String msg) {
        if (!isDebug) {
            if (StringUtil.isNotEmpty(tag)) {
                Log.w(tag, msg);
            } else {
                Log.w(TAG, msg);
            }
        }
    }

    public static void w(String tag, int strId) {
        if (!isDebug) {
            if (StringUtil.isNotEmpty(tag)) {
                Log.w(tag, ContextManager.getContext().getString(strId));
            } else {
                Log.w(TAG, ContextManager.getContext().getString(strId));
            }
        }
    }


    public static void e(String tag, String msg) {
        if (!isDebug) {
            if (StringUtil.isNotEmpty(tag)) {
                Log.e(tag, msg);
            } else {
                Log.e(TAG, msg);
            }
        }
    }

    public static void e(String tag, int strId) {
        if (!isDebug) {
            if (StringUtil.isNotEmpty(tag)) {
                Log.e(tag, ContextManager.getContext().getString(strId));
            } else {
                Log.e(TAG, ContextManager.getContext().getString(strId));
            }
        }
    }


    public static void i(String tag, String msg) {
        if (!isDebug) {
            if (StringUtil.isNotEmpty(tag)) {
                Log.i(tag, msg);
            } else {
                Log.i(TAG, msg);
            }
        }
    }

    public static void i(String tag, int strId) {
        if (!isDebug) {
            if (StringUtil.isNotEmpty(tag)) {
                Log.i(tag, ContextManager.getContext().getString(strId));
            } else {
                Log.i(TAG, ContextManager.getContext().getString(strId));
            }
        }
    }


    public static void v(String tag, String msg) {
        if (!isDebug) {
            if (StringUtil.isNotEmpty(tag)) {
                Log.v(tag, msg);
            } else {
                Log.v(TAG, msg);
            }
        }
    }

    public static void v(String tag, int strId) {
        if (!isDebug) {
            if (StringUtil.isNotEmpty(tag)) {
                Log.v(tag, ContextManager.getContext().getString(strId));
            } else {
                Log.v(TAG, ContextManager.getContext().getString(strId));
            }
        }
    }
}
