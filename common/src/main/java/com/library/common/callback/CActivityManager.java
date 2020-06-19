package com.library.common.callback;

import android.app.Activity;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Stack;

public class CActivityManager {

    private static Stack<Activity> activityStack;
    private static CActivityManager instance;

    private CActivityManager() {
    }

    /**
     * 单实例 , UI无需考虑多线程同步问题
     */
    public static CActivityManager getAppManager() {
        if (instance == null) {
            instance = new CActivityManager();
        }
        return instance;
    }

    public Stack<Activity> getActivityStack(){
        return activityStack;
    }

    /**
     * 添加Activity到栈
     */
    public void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<Activity>();
        }
        activityStack.add(activity);
    }

    /**
     * 获取当前Activity（栈顶Activity）
     */
    public Activity currentActivity() {
        if (activityStack == null || activityStack.isEmpty()) {
            return null;
        }
        Activity activity = activityStack.lastElement();
        return activity;
    }

    /**
     * 获取当前Activity（栈顶Activity） 没有找到则返回null
     */
    public Activity findActivity(Class<?> cls) {
        Activity activity = null;
        for (Activity aty : activityStack) {
            if (aty.getClass().equals(cls)) {
                activity = aty;
                break;
            }
        }
        return activity;
    }

    /**
     * 结束当前Activity（栈顶Activity）
     */
    public void finishActivity() {
        Activity activity = activityStack.lastElement();
        finishActivity(activity);
    }

    /**
     * 结束指定的Activity(重载)
     */
    public void finishActivity(Activity activity) {

        if (activity != null) {
            // 为与系统Activity栈保持一致，且考虑到手机设置项里的"不保留活动"选项引起的Activity生命周期调用onDestroy()方法所带来的问题,此处需要作出如下修正
            if (activity.isFinishing()) {
                activityStack.remove(activity);
                activity = null;
            } else {
                activity.finish();
            }
        }
    }

    /**
     * 结束指定的Activity(重载)
     */
    public void finishActivity(Class<?> cls) {
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
            }
        }
    }

    /**
     * 关闭除了指定activity以外的全部activity 如果cls不存在于栈中，则栈全部清空
     *
     * @param cls
     */
    public synchronized void finishOthersActivity(Class<?> cls) {
        try {
            if (activityStack.empty()) {
                return;
            }
            Iterator<Activity> it = activityStack.iterator();
            while (it.hasNext()) {
                Activity activity = it.next();
                if (!(activity.getClass().equals(cls))) {
                    finishActivity(activity);
                }
            }
        } catch (ConcurrentModificationException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 结束所有Activity
     */
    public static void finishAllActivity() {
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (null != activityStack.get(i)) {
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();
    }


    public boolean hasCreateActivity(Class activity) {
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (activity == activityStack.get(i).getClass()) {
                return true;
            }
        }
        return false;
    }
}
