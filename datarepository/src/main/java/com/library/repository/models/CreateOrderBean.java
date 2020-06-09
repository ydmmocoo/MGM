package com.library.repository.models;

/**
 * @author yedeman
 * @date 2020/5/30.
 * email：ydmmocoo@gmail.com
 * description：
 */
public class CreateOrderBean {

    /**
     * orderId : eb2020053000242236004041
     * price : 670
     */

    private String orderId;
    private int price;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
