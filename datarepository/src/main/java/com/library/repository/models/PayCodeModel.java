package com.library.repository.models;

/**
 * Author    by hanlz
 * Date      on 2020/4/3.
 * Description：获取付款码
 */
public class PayCodeModel {
    private String qrCode;//二维码字符串
    private String barCode;//条形码字符串
    private String expTime;//二维码字符串

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public String getExpTime() {
        return expTime;
    }

    public void setExpTime(String expTime) {
        this.expTime = expTime;
    }
}
