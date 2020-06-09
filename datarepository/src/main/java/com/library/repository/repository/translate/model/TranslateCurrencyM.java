package com.library.repository.repository.translate.model;

public class TranslateCurrencyM {


    /**
     * currency : USD
     * name : 美元
     */

    private String currency;
    private String name;

    public TranslateCurrencyM(String currency, String name) {
        this.currency = currency;
        this.name = name;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
