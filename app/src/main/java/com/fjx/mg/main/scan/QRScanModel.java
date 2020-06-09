package com.fjx.mg.main.scan;

import com.library.repository.models.PaymentCodeModel;

/**
 * Author    by hanlz
 * Date      on 2020/1/23.
 * Description：
 */
public class QRScanModel {
    private int code;
    private String msg;
    private PaymentCodeModel data;



    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public PaymentCodeModel getData() {
        return data;
    }

    public void setData(PaymentCodeModel data) {
        this.data = data;
    }


}
