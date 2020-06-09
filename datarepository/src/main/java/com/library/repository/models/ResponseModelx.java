package com.library.repository.models;

public class ResponseModelx extends BaseM {

    private int code;
    private String msg;

    public ResponseModelx() {
    }

    public ResponseModelx(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

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

}
