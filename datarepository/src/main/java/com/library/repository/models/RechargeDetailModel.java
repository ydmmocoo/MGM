package com.library.repository.models;

public class RechargeDetailModel {


    /**
     * type : 电费
     * price : 401.00
     * phone : null
     * account : null
     * servicePrice : 1.00
     * service : marker
     * payType : 余额
     */

    private String type;
    private String price;
    private String phone;
    private String account;
    private String servicePrice;
    private String service;
    private String payType;
    private String CNYPrice;
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

    public String getCNYPrice() {
        return CNYPrice;
    }

    public void setCNYPrice(String CNYPrice) {
        this.CNYPrice = CNYPrice;
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

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getServicePrice() {
        return servicePrice;
    }

    public void setServicePrice(String servicePrice) {
        this.servicePrice = servicePrice;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getPayType() {
        return payType;
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
