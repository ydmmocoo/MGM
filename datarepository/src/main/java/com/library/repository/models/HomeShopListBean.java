package com.library.repository.models;

import java.util.List;

/**
 * @author yedeman
 * @date 2020/5/20.
 * email：ydmmocoo@gmail.com
 * description：
 */
public class HomeShopListBean {
    /**
     * haxNext : false
     * shopList : [{"sId":"1","name":"键锋小吃店","logo":"http://139.9.38.218:8080/Uploads/user/2020-06-01/5ed4acb54b70b.png","avgScore":"4.2","saleCount":0,"deliveryPrice":"0","distributionFee":"0","distance":9043433.37,"isTakeYouself":"1","reductionList":[],"goodsList":[]}]
     */

    private boolean haxNext;
    private List<ShopListBean> shopList;

    public boolean isHaxNext() {
        return haxNext;
    }

    public void setHaxNext(boolean haxNext) {
        this.haxNext = haxNext;
    }

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
         * logo : http://139.9.38.218:8080/Uploads/user/2020-06-01/5ed4acb54b70b.png
         * avgScore : 4.2
         * saleCount : 0
         * deliveryPrice : 0
         * distributionFee : 0
         * distance : 9043433.37
         * isTakeYouself : 1
         * reductionList : []
         * goodsList : []
         */

        private String sId;
        private String name;
        private String logo;
        private String avgScore;
        private int saleCount;
        private String deliveryPrice;
        private String distributionFee;
        private double distance;
        private String isTakeYouself;
        private List<ReductionListBean> reductionList;
        private List<GoodsListBean> goodsList;

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

        public String getAvgScore() {
            return avgScore;
        }

        public void setAvgScore(String avgScore) {
            this.avgScore = avgScore;
        }

        public int getSaleCount() {
            return saleCount;
        }

        public void setSaleCount(int saleCount) {
            this.saleCount = saleCount;
        }

        public String getDeliveryPrice() {
            return deliveryPrice;
        }

        public void setDeliveryPrice(String deliveryPrice) {
            this.deliveryPrice = deliveryPrice;
        }

        public String getDistributionFee() {
            return distributionFee;
        }

        public void setDistributionFee(String distributionFee) {
            this.distributionFee = distributionFee;
        }

        public double getDistance() {
            return distance;
        }

        public void setDistance(double distance) {
            this.distance = distance;
        }

        public String getIsTakeYouself() {
            return isTakeYouself;
        }

        public void setIsTakeYouself(String isTakeYouself) {
            this.isTakeYouself = isTakeYouself;
        }

        public List<ReductionListBean> getReductionList() {
            return reductionList;
        }

        public void setReductionList(List<ReductionListBean> reductionList) {
            this.reductionList = reductionList;
        }

        public List<GoodsListBean> getGoodsList() {
            return goodsList;
        }

        public void setGoodsList(List<GoodsListBean> goodsList) {
            this.goodsList = goodsList;
        }

        public static class ReductionListBean {

            private String rId;
            private String price;
            private String fullPrice;

            public String getrId() {
                return rId;
            }

            public void setrId(String rId) {
                this.rId = rId;
            }

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }

            public String getFullPrice() {
                return fullPrice;
            }

            public void setFullPrice(String fullPrice) {
                this.fullPrice = fullPrice;
            }
        }

        public static class GoodsListBean {

            private String gId;
            private String price;
            private String specialCount;
            private String gImg;
            private String gName;

            public String getgId() {
                return gId;
            }

            public void setgId(String gId) {
                this.gId = gId;
            }

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }

            public String getSpecialCount() {
                return specialCount;
            }

            public void setSpecialCount(String specialCount) {
                this.specialCount = specialCount;
            }

            public String getgImg() {
                return gImg;
            }

            public void setgImg(String gImg) {
                this.gImg = gImg;
            }

            public String getgName() {
                return gName;
            }

            public void setgName(String gName) {
                this.gName = gName;
            }
        }
    }
}
