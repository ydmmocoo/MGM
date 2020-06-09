package com.library.repository.models;

import com.tencent.imsdk.friendship.TIMFriend;

/**
 * Author    by hanlz
 * Date      on 2020/2/10.
 * Description：
 */
public class SearchTimFriendModel {
    private String no;
    private String type;//1--搜索结果匹配到mgm号  2---搜索结果没匹配到mgm号且匹配到手机号  3--单纯的输入用户
    private TIMFriend timFriend;

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public TIMFriend getTimFriend() {
        return timFriend;
    }

    public void setTimFriend(TIMFriend timFriend) {
        this.timFriend = timFriend;
    }
}
