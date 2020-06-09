package com.library.repository.models;

public class ConvertModel {
    /**
     * CNYPrice : 1930000000
     * servicePrice : 0
     * totalPrice : 1000000000000
     * realPrice : 1000000000000
     */

    private String CNYPrice;
    private String servicePrice;
    private String totalPrice;
    private String realPrice;

    public String getCNYPrice() {
        return CNYPrice;
    }

    public void setCNYPrice(String CNYPrice) {
        this.CNYPrice = CNYPrice;
    }

    public String getServicePrice() {
        return servicePrice;
    }

    public void setServicePrice(String servicePrice) {
        this.servicePrice = servicePrice;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getRealPrice() {
        return realPrice;
    }

    public void setRealPrice(String realPrice) {
        this.realPrice = realPrice;
    }
}
