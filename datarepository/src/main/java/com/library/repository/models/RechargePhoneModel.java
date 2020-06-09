package com.library.repository.models;

public class RechargePhoneModel {

    private String areaCode;
    private String phone;

    public RechargePhoneModel() {
    }

    public RechargePhoneModel(String areaCode, String phone) {
        this.areaCode = areaCode;
        this.phone = phone;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
