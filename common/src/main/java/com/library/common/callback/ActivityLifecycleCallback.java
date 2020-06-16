package com.library.common.callback;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;


/**
 * 生命周期管理类
 */
public class ActivityLifecycleCallback implements Application.ActivityLifecycleCallbacks {

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        Log.d("ActivityLifecycle", "onActivityCreated");
        Log.e("onActivityCreated", activity.getClass().getName());
        CActivityManager.getAppManager().addActivity(activity);
    }

    @Override
    public void onActivityStarted(Activity activity) {
        Log.d("ActivityLifecycle", "onActivityStarted");
    }

    @Override
    public void onActivityResumed(Activity activity) {
        Log.d("ActivityLifecycle", "onActivityResumed");
    }

    @Override
    public void onActivityPaused(Activity activity) {
        Log.d("ActivityLifecycle", "onActivityPaused");
    }

    @Override
    public void onActivityStopped(Activity activity) {
        Log.d("ActivityLifecycle", "onActivityStopped");
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        Log.d("ActivityLifecycle", "onActivitySaveInstanceState");
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        Log.d("ActivityLifecycle", "onActivityDestroyed");
        CActivityManager.getAppManager().finishActivity(activity);
    }
}
