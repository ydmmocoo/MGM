package com.library.common.base;

import android.app.Application;

/**
 * @author yedeman
 * @date 2020/5/11.
 * email：ydmmocoo@gmail.com
 * description：
 */
public class BaseApp extends Application {

    private static BaseApp instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance=this;
    }

    public static synchronized BaseApp getInstance() {
        return instance;
    }
}
