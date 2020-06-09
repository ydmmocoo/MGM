package com.fjx.mg.utils;

import com.library.common.utils.SharedPreferencesUtil;

/**
 * Author    by hanlz
 * Date      on 2019/12/11.
 * Description：保存群昵称状态 key--groupId value--昵称是否开启状态
 */
public class NicknameSateSpUtil {
    private final String NAME = "NicknameSateSpUtil";

    public void put(String key, boolean value) {
        SharedPreferencesUtil.name(NAME).put(key, value).apply();
    }

    public boolean get(String key) {
        return SharedPreferencesUtil.name(NAME).getBoolean(key, false);
    }
}
