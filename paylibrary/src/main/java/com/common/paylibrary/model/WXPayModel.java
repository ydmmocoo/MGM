package com.common.paylibrary.model;

import com.google.gson.annotations.SerializedName;

public class WXPayModel {

    /**
     * appid : wxe342a8e739a2fc93
     * partnerid : 1534867221
     * prepayid : wx1614235270231813b8ea17d90376681574
     * package : Sign=WXPay
     * noncestr : ToqYaUgLvrn47VwFXwAd5QQii7R5nTHG
     * timestamp : 1557987832
     * sign : 5EEFF964EF32AB1FA054D3842AEE4112
     * transferId : 42
     */

    private String appid;
    private String partnerid;
    private String prepayid;
    @SerializedName("package")
    private String packageX;
    private String noncestr;
    private String timestamp;
    private String sign;
    private String transferId;
    private String orderId;
    //TODO 2019-12-8 21:46:48 新增红包记录id
    private String rId;

    public String getrId() {
        return rId;
    }

    public void setrId(String rId) {
        this.rId = rId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getPartnerid() {
        return partnerid;
    }

    public void setPartnerid(String partnerid) {
        this.partnerid = partnerid;
    }

    public String getPrepayid() {
        return prepayid;
    }

    public void setPrepayid(String prepayid) {
        this.prepayid = prepayid;
    }

    public String getPackageX() {
        return packageX;
    }

    public void setPackageX(String packageX) {
        this.packageX = packageX;
    }

    public String getNoncestr() {
        return noncestr;
    }

    public void setNoncestr(String noncestr) {
        this.noncestr = noncestr;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getTransferId() {
        return transferId;
    }

    public void setTransferId(String transferId) {
        this.transferId = transferId;
    }

}
