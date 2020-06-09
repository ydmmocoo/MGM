package com.fjx.mg.utils;

import android.content.Context;
import android.view.WindowManager;

import com.library.common.utils.ContextManager;

/**
 * Author    by hanlz
 * Date      on 2019/10/25.
 * Description：
 */
public class WindowManagerUtils {

    /**
     * 获取屏幕的宽度
     * @return
     */
    public static int getScreenWidth(){
        WindowManager wm = (WindowManager) ContextManager.getContext()
                .getSystemService(Context.WINDOW_SERVICE);

        int width = wm.getDefaultDisplay().getWidth();
        return width;
    }

    /**
     * 获取屏幕的高度
     * @return
     */
    public static int getScreenHight(){
        WindowManager wm = (WindowManager) ContextManager.getContext()
                .getSystemService(Context.WINDOW_SERVICE);

        int height = wm.getDefaultDisplay().getHeight();
        return height;
    }

    /**
     * 获取屏幕的高度的一半
     * @return
     */
    public static int getScreenPartHight(){
        WindowManager wm = (WindowManager) ContextManager.getContext()
                .getSystemService(Context.WINDOW_SERVICE);

        int height = wm.getDefaultDisplay().getHeight();
        return height/2;
    }
}
