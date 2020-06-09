package com.library.repository.models;

public class IMNoticeModel {


    /**
     * billId : 165
     * typeName : 转账
     * price : 0.01
     * cnyPrice : 0.00
     * remark : 转账
     * createTime : 2019-06-05 17:03:31
     * type : 7
     * title : 转账支付提醒
     * from :
     * to : 🤔
     * payType : 余额
     */

    private String billId;
    private String typeName;
    private String price;
    private String cnyPrice;
    private String remark;
    private String createTime;
    private String type;
    private String title;
    private String from;
    private String to;
    private String payType;
    private String reciveTime;
    private String secondType;
    private String orderId;
    private String remainBalance;
    private String sendTime;
    private String userRemark;
    private String toUid;


    public String getRemainBalance() {
        return remainBalance;
    }

    public void setRemainBalance(String remainBalance) {
        this.remainBalance = remainBalance;
    }

    public String getSecondType() {
        return secondType;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public void setSecondType(String secondType) {
        this.secondType = secondType;
    }

    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCnyPrice() {
        return cnyPrice;
    }

    public void setCnyPrice(String cnyPrice) {
        this.cnyPrice = cnyPrice;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getType() {
        return type;
    }

    public String getReciveTime() {
        return reciveTime;
    }

    public void setReciveTime(String reciveTime) {
        this.reciveTime = reciveTime;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public String getUserRemark() {
        return userRemark;
    }

    public void setUserRemark(String userRemark) {
        this.userRemark = userRemark;
    }

    public String getToUid() {
        return toUid;
    }

    public void setToUid(String toUid) {
        this.toUid = toUid;
    }


}
