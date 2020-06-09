package com.library.repository.models;

import java.util.List;

/**
 * @author yedeman
 * @date 2020/5/20.
 * email：ydmmocoo@gmail.com
 * description：
 */
public class HotShopBean {

    private List<ShopListBean> shopList;

    public List<ShopListBean> getShopList() {
        return shopList;
    }

    public void setShopList(List<ShopListBean> shopList) {
        this.shopList = shopList;
    }

    public static class ShopListBean {
        /**
         * sId : 1
         * name : 键锋小吃店
         * logo :
         * goodImg : http://139.9.38.218:8080http://139.9.38.218:8080http://139.9.38.218:8080http://n.sinaimg.cn/photo/transform/700/w1000h500/20200511/fe11-itmiwry9590007.jpg
         */

        private String sId;
        private String name;
        private String logo;
        private String goodImg;

        public String getSId() {
            return sId;
        }

        public void setSId(String sId) {
            this.sId = sId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getLogo() {
            return logo;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }

        public String getGoodImg() {
            return goodImg;
        }

        public void setGoodImg(String goodImg) {
            this.goodImg = goodImg;
        }
    }
}
