package com.library.repository.models;

public class RechargePhoneDetailModel {


    /**
     * type : 话费
     * price : 400.40
     * phone : 18850528382
     * CNYPrice : 400.40
     * num : 400AR
     * payType : 余额
     */

    private String type;
    private String price;
    private String phone;
    private String CNYPrice;
    private String num;
    private String payType;
    private String datetime;
    /**
     * status : 2
     * statusTip : 充值中
     * processRemark :
     */

    private String status;
    private String statusTip;
    private String processRemark;


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCNYPrice() {
        return CNYPrice;
    }

    public void setCNYPrice(String CNYPrice) {
        this.CNYPrice = CNYPrice;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getPayType() {
        return payType;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusTip() {
        return statusTip;
    }

    public void setStatusTip(String statusTip) {
        this.statusTip = statusTip;
    }

    public String getProcessRemark() {
        return processRemark;
    }

    public void setProcessRemark(String processRemark) {
        this.processRemark = processRemark;
    }
}
