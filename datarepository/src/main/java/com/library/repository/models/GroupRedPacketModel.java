package com.library.repository.models;

/**
 * Author    by hanlz
 * Date      on 2019/12/6.
 * Description：
 */
public class GroupRedPacketModel {

    private String rId;//红包记录id
    private String url;//支付地址
    private String price;

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getrId() {
        return rId;
    }

    public void setrId(String rId) {
        this.rId = rId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
