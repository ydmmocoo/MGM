package com.library.repository.models;

/**
 * Author    by hanlz
 * Date      on 2019/12/8.
 * Description：
 */
public class GroupRedPacketDetailModel {
    private String uId;//红包发人id
    private String avatar;//红包发起人头像
    private String nickName;//红包发起人昵称
    private String rId;//红包记录id
    private String remark;//红包备注
    private String isRecive;//当前用户是否领取过这个红包
    private String price;//当前用户领取的金额
    private String status;//红包记录状态,3：表示红包退还,1:全部被领取,2:部分被领取
    private String totalNum;//总的份数
    private String reciveNum;//已经领取分数
    private String totalPrice;//总的金额
    private String recivePrice;//已经领取的金额

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getrId() {
        return rId;
    }

    public void setrId(String rId) {
        this.rId = rId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getIsRecive() {
        return isRecive;
    }

    public void setIsRecive(String isRecive) {
        this.isRecive = isRecive;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(String totalNum) {
        this.totalNum = totalNum;
    }

    public String getReciveNum() {
        return reciveNum;
    }

    public void setReciveNum(String reciveNum) {
        this.reciveNum = reciveNum;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getRecivePrice() {
        return recivePrice;
    }

    public void setRecivePrice(String recivePrice) {
        this.recivePrice = recivePrice;
    }
}
