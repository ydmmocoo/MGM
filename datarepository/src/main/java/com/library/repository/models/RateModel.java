package com.library.repository.models;

public class RateModel {
    private int drawableId;
    private String amountName;
    private String amountKey;
    private String toAmount;

    public RateModel(int drawableId, String amountName, String amountKey) {
        this.drawableId = drawableId;
        this.amountName = amountName;
        this.amountKey = amountKey;
    }

    public int getDrawableId() {
        return drawableId;
    }

    public void setDrawableId(int drawableId) {
        this.drawableId = drawableId;
    }

    public String getAmountName() {
        return amountName;
    }

    public void setAmountName(String amountName) {
        this.amountName = amountName;
    }

    public String getAmountKey() {
        return amountKey;
    }

    public void setAmountKey(String amountKey) {
        this.amountKey = amountKey;
    }

    public String getToAmount() {
        return toAmount;
    }

    public void setToAmount(String toAmount) {
        this.toAmount = toAmount;
    }
}
