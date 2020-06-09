package com.fjx.mg.utils;

import com.library.common.utils.SharedPreferencesUtil;

/**
 * Author    by hanlz
 * Date      on 2019/12/11.
 * Description：消息免打扰开关 key--groupId_userId value--是否开启消息面打扰
 */
public class MessageSateSpUtil {
    private final String NAME = "MessageSateSpUtil";

    public void put(String key, boolean value) {
        SharedPreferencesUtil.name(NAME).put(key, value).apply();
    }

    public boolean get(String key) {
        return SharedPreferencesUtil.name(NAME).getBoolean(key, false);
    }
}
