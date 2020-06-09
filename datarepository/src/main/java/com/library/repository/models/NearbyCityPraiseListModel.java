package com.library.repository.models;

/**
 * Author    by hanlz
 * Date      on 2019/10/21.
 * Description：同城点赞列表
 */
public class NearbyCityPraiseListModel {

    private String createTime;//点赞时间
    private String userId;//用户id
    private String pId;//记录id
    private String userNickName;//用户昵称
    private String userPhone;//用户电话
    private String userAvatar;//用户头像

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }

    public String getUserNickName() {
        return userNickName;
    }

    public void setUserNickName(String userNickName) {
        this.userNickName = userNickName;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }


    public String getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }
}
