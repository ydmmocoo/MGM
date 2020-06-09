package com.library.repository.core.net;


public class NetCode {
    public static final int SUCCESS = 10000;
    public static final int LOGIN_OVERDUE = 9999;//登录过期
    public static final int PERMISSION_DENIED = 9997;//权限不足
    public static final int PERMISSION_FORBIDDEN = 9998;//账号会员禁用
    public static final int USER_FAILED = 9990;
    public static final int LOGIN_OTHER_DEVICE_TIP = 9992;
    public static final int SENSITIVITY_CONTENT = 8997;//存在敏感内容
    public static boolean isShowGestureLockActivity = false;
}
