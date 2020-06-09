package com.library.repository.repository.translate.model;

import com.library.repository.models.BaseM;

public class RateModel<T> extends BaseM {

    private int status;
    private String msg;
    private T result;

    public RateModel() {
    }

    public RateModel(int status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }
}
