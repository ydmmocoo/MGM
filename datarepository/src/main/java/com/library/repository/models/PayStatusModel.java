package com.library.repository.models;

public class PayStatusModel {
    //{"payTip":"支付成功","price":"10AR","status":"1","uId":"88",      "servicePrice":"-1AR","realPrice":"9AR"}
    //{"payTip":"支付中", "price":0,      "status":"2","uId":"10000018"}
    private String avatar;
    private String nickName;
    private String payTip;
    private String price;
    private String status;
    private String uId;
    private String servicePrice;
    private String realPrice;

    public String getServicePrice() {
        return servicePrice;
    }

    public void setServicePrice(String servicePrice) {
        this.servicePrice = servicePrice;
    }

    public String getRealPrice() {
        return realPrice;
    }

    public void setRealPrice(String realPrice) {
        this.realPrice = realPrice;
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

    public String getPayTip() {
        return payTip;
    }

    public void setPayTip(String payTip) {
        this.payTip = payTip;
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

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }
}
