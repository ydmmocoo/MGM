package com.library.common.utils;

import android.app.Activity;

import androidx.fragment.app.Fragment;

import com.gyf.immersionbar.ImmersionBar;

public class StatusBarManager {


    /**
     * 黑色字体的状态栏
     *
     * @param activity
     * @param color
     */
    public static void setColor(Activity activity, int color) {
        ImmersionBar.with(activity)
                .fitsSystemWindows(true)
                .statusBarColor(color)
                .keyboardEnable(true)
                .navigationBarEnable(false)
                .statusBarDarkFont(true)
                .init();


    }


    /**
     * 白色字体的状态栏
     *
     * @param activity
     * @param color
     */
    public static void setLightFontColor(Activity activity, int color) {
        ImmersionBar.with(activity).fitsSystemWindows(true)
                .statusBarColor(color)
                .keyboardEnable(true)
                .navigationBarEnable(false)
                .statusBarDarkFont(false)
                .init();
    }

    public static void setLightFontColor(Fragment fragment, int color) {
        ImmersionBar.with(fragment).fitsSystemWindows(true)
                .statusBarColor(color)
                .keyboardEnable(true)
                .statusBarDarkFont(false)
                .navigationBarEnable(false)
                .init();
    }


    /**
     * 全屏沉浸式
     *
     * @param activity
     */
    public static void transparentNavigationBar(Activity activity) {
        ImmersionBar.with(activity).fitsSystemWindows(false)
                .statusBarDarkFont(true)
                .navigationBarEnable(false)
                .keyboardEnable(true)
                .transparentNavigationBar()
                .init();
    }

    public static void transparentNavigationBar(Fragment fragment) {
        ImmersionBar.with(fragment).fitsSystemWindows(false)
                .navigationBarEnable(false)
                .statusBarDarkFont(true)
                .keyboardEnable(true)
                .transparentNavigationBar()
                .init();
    }


}
