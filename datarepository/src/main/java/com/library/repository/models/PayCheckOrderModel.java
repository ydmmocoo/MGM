package com.library.repository.models;

/**
 * Author    by hanlz
 * Date      on 2020/2/28.
 * Description：
 */
public class PayCheckOrderModel {
    private String appId;//appid
    private String prepayId;//订单号
    private String appKey;//appkey
    private String extension;//订单信息
    private String price;//价格
    private String name;//收款方

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getPrepayId() {
        return prepayId;
    }

    public void setPrepayId(String prepayId) {
        this.prepayId = prepayId;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
