package com.library.repository.models;

/**
 * Author    by hanlz
 * Date      on 2019/12/2.
 * Description：
 */
public class TimGroupInfoModel {

    private String userName;//群成员名称
    private String faceUrl;//用户头像
    private boolean isSelected;//是否选中
    private String nickName;//用户昵称
    private String remark;//备注
    private String identifier;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUrl() {
        return faceUrl;
    }

    public void setUrl(String url) {
        this.faceUrl = url;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getFaceUrl() {
        return faceUrl;
    }

    public void setFaceUrl(String faceUrl) {
        this.faceUrl = faceUrl;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
}
