package com.library.repository.models;

/**
 * Author    by hanlz
 * Date      on 2019/10/23.
 * Description：
 */
public class NearbyCityUserInfoModel {
    private String uImg;//	头像
    private String nickName;//昵称

    public String getuImg() {
        return uImg;
    }

    public void setuImg(String uImg) {
        this.uImg = uImg;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}
