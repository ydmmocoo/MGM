package com.tencent.qcloud.uikit.business.chat.model;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.library.common.utils.JsonUtil;
import com.library.common.utils.SharedPreferencesUtil;
import com.tencent.imsdk.TIMUserProfile;

import java.util.ArrayList;
import java.util.List;

public class CacheUtil {

    private SharedPreferencesUtil sharedPreferencesUtil;


    private CacheUtil() {
        sharedPreferencesUtil = SharedPreferencesUtil.name("tim_cache");
    }

    public static CacheUtil getInstance() {
        return CacheUtilHolder.INSTANCE;
    }

    private static class CacheUtilHolder {
        private static final CacheUtil INSTANCE = new CacheUtil();
    }

    /**
     * 红包或者转账领取之后，保存messageid
     */
    public void saveTransferMessageId(String id) {
        String ids = sharedPreferencesUtil.getString("receive_ids", "");
        List<String> idList = null;
        if (TextUtils.isEmpty(ids)) {
            idList = new ArrayList<>();
        } else {
            idList = JsonUtil.jsonToList(ids, String.class);
        }
        if (idList.contains(id)) return;
        idList.add(id);
        sharedPreferencesUtil.put("receive_ids", JsonUtil.moderToString(idList)).apply();
    }


    /**
     * 获取本地缓存的已收转账或者红包id
     *
     * @return
     */
    public List<String> getTransferMessageId() {
        String ids = sharedPreferencesUtil.getString("receive_ids", "");
        List<String> idList = JsonUtil.jsonToList(ids, String.class);
        if (idList == null) return new ArrayList<>();
        return idList;
    }


    public  TIMUserProfile getTIMUser() {
        String str = SharedPreferencesUtil.name("user_data").getString("TIMUserProfile", "");
        return new Gson().fromJson(str, TIMUserProfile.class);
    }

    public  String userAvatar(String uid) {
        return SharedPreferencesUtil.name("user_avatar_data").getString(uid, "");
    }

}
