package com.tencent.qcloud.uikit.util;

import android.text.TextUtils;

import com.library.common.utils.JsonUtil;
import com.library.common.utils.SharedPreferencesUtil;
import com.tencent.imsdk.friendship.TIMFriend;

import java.util.ArrayList;
import java.util.List;

/**
 * Author    by hanlz
 * Date      on 2020/1/7.
 * Description：判断是否是好友关系
 */
public class FriendShipUtil {

    public static List<TIMFriend> getAllFriend() {
        String text = SharedPreferencesUtil.name("tim_friend_data").getString("all_friend", "");
        if (TextUtils.isEmpty(text)) return new ArrayList<>();
        return JsonUtil.jsonToList(text, TIMFriend.class);
    }

    public static TIMFriend getFriend(String uid) {
        List<TIMFriend> list = getAllFriend();
        for (TIMFriend friend : list) {
            if (TextUtils.equals(friend.getIdentifier(), uid)) return friend;
        }
        return null;
    }
}
