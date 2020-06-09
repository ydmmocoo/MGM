package com.library.repository.models;

/**
 * Author    by hanlz
 * Date      on 2020/2/10.
 * Description：
 */
public class CheckOrderIdModel {
    //    {
//        "msg": "成功",
//            "code": 10000,
//            "data": [
//        {
//            "orderId": "12345",
//                "msg": "成功",
//                "status": "2"
//        },
//        {
//            "orderId": "456",
//                "msg": "订单不存在"
//        }
//    ]
//    }
    private String orderId;
    private String msg;
    private String status;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
