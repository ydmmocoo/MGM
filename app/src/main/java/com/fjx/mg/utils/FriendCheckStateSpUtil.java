package com.fjx.mg.utils;

import com.library.common.utils.SharedPreferencesUtil;

/**
 * Author    by hanlz
 * Date      on 2019/12/11.
 * Description：好友验证 key--userId value--是否开启好友验证
 */
public class FriendCheckStateSpUtil {
    private final String NAME = "FriendCheckStateSpUtil";

    public void put(String key, boolean value) {
        SharedPreferencesUtil.name(NAME).put(key, value).apply();
    }

    public boolean get(String key) {
        return SharedPreferencesUtil.name(NAME).getBoolean(key, false);
    }
}
