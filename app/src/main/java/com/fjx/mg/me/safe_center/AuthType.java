package com.fjx.mg.me.safe_center;

public interface AuthType {
    int GESTURE = 2;//手势修改或者重置验证
    int SECURITY_ISSUES = 3;//设置安全问题
    int LOGIN_PASSWORD = 4;//扽里密码修改或者重置
    int BIND_MOBILE = 5;//换绑手机
    int PAY_PASSWORD = 6;//设置支付密码
    int FINGERPRINT = 7;//指纹设置
    int BIND_DEVICE = 8;//绑定设备
    int FORGET_PASSWORD = 9;//忘记密码，重置

}
