package com.library.repository.models;

import java.util.List;

public class PriceListModel {

    private List<PriceListBean> priceList;

    public List<PriceListBean> getPriceList() {
        return priceList;
    }

    public void setPriceList(List<PriceListBean> priceList) {
        this.priceList = priceList;
    }

    public static class PriceListBean {
        private Boolean Click = false;
        private String price;
        private String unit;

        public Boolean getClick() {
            return Click;
        }

        public void setClick(Boolean click) {
            Click = click;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }
    }
}
