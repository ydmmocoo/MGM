package com.tencent.qcloud.uikit.common.utils;

import com.tencent.imsdk.TIMUserProfile;

public class TIMStringUtil {
    public static String getPhone(TIMUserProfile profile) {
        byte[] bytes = profile.getCustomInfo().get("phone");
        if (bytes != null)
            return new String(bytes);
        return "";
    }
}
