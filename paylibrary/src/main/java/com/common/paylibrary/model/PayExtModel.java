package com.common.paylibrary.model;

import com.library.common.utils.JsonUtil;

public class PayExtModel {

    private UsagePayMode type;
    private Object message;
    private Object obj;//预留存放参数

    public PayExtModel(UsagePayMode type, Object message) {
        this.type = type;
        this.message = message;
    }

    public PayExtModel(Object message) {
        this.message = message;
    }

    public UsagePayMode getType() {
        return type;
    }

    public void setType(UsagePayMode type) {
        this.type = type;
    }

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return JsonUtil.moderToString(this);
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }
}
