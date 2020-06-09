package com.library.repository.repository.translate.model;

public class RateResultModel {


    /**
     * from : USD
     * to : CNY
     * fromname : 美元
     * toname : 人民币
     * updatetime : 2019-06-04 17:39:08
     * rate : 6.9124
     * camount : 6.9124
     */

    private String from;
    private String to;
    private String fromname;
    private String toname;
    private String updatetime;
    private String rate;
    private String camount;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getFromname() {
        return fromname;
    }

    public void setFromname(String fromname) {
        this.fromname = fromname;
    }

    public String getToname() {
        return toname;
    }

    public void setToname(String toname) {
        this.toname = toname;
    }

    public String getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getCamount() {
        return camount;
    }

    public void setCamount(String camount) {
        this.camount = camount;
    }
}
