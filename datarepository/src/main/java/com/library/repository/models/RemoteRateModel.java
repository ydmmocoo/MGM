package com.library.repository.models;

public class RemoteRateModel {


    /**
     * from : MGA
     * to : HKD
     * fromName : 马尔加什阿里亚
     * toName : 港币
     * rate : 0.0021
     * cAmount : 0.21
     */

    private String from;
    private String to;
    private String fromName;
    private String toName;
    private String rate;
    private String cAmount;

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

    public String getFromName() {
        return fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    public String getToName() {
        return toName;
    }

    public void setToName(String toName) {
        this.toName = toName;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getCAmount() {
        return cAmount;
    }

    public void setCAmount(String cAmount) {
        this.cAmount = cAmount;
    }
}
