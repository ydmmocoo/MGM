package com.library.repository.models;

import java.util.List;

/**
 * @author yedeman
 * @date 2020/6/18.
 * email：ydmmocoo@gmail.com
 * description：
 */
public class CollectShopsBean {

    /**
     * hasNext : false
     * shopList : [{"sId":"2","shopName":"郁涛汉堡王","shopLogo":"http://139.9.38.218:8080/Uploads/user/2020-06-10/5ee04ae735022.png","avgScore":"5.0","saleNum":5,"deliveryPrice":"20","distributionFee":"4","isTakeYouself":"0","reductionList":[{"rId":"3","price":"5","fullPrice":"30"}]},{"sId":"1","shopName":"漳州卤肉饭","shopLogo":"http://139.9.38.218:8080/Uploads/user/2020-06-12/5ee322e2a7eac.png","avgScore":"0.0","saleNum":0,"deliveryPrice":"500","distributionFee":"30","isTakeYouself":"0","reductionList":[{"rId":"16","price":"10","fullPrice":"500"}]}]
     */

    private boolean hasNext;
    private List<ShopListBean> shopList;

    public boolean isHasNext() {
        return hasNext;
    }

    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }

    public List<ShopListBean> getShopList() {
        return shopList;
    }

    public void setShopList(List<ShopListBean> shopList) {
        this.shopList = shopList;
    }

    public static class ShopListBean {
        /**
         * sId : 2
         * shopName : 郁涛汉堡王
         * shopLogo : http://139.9.38.218:8080/Uploads/user/2020-06-10/5ee04ae735022.png
         * avgScore : 5.0
         * saleNum : 5
         * deliveryPrice : 20
         * distributionFee : 4
         * isTakeYouself : 0
         * reductionList : [{"rId":"3","price":"5","fullPrice":"30"}]
         */

        private String sId;
        private String shopName;
        private String shopLogo;
        private String avgScore;
        private int saleNum;
        private String deliveryPrice;
        private String distributionFee;
        private String isTakeYouself;
        private List<ReductionListBean> reductionList;
        private String distance;

        public String getSId() {
            return sId;
        }

        public void setSId(String sId) {
            this.sId = sId;
        }

        public String getShopName() {
            return shopName;
        }

        public void setShopName(String shopName) {
            this.shopName = shopName;
        }

        public String getShopLogo() {
            return shopLogo;
        }

        public void setShopLogo(String shopLogo) {
            this.shopLogo = shopLogo;
        }

        public String getAvgScore() {
            return avgScore;
        }

        public void setAvgScore(String avgScore) {
            this.avgScore = avgScore;
        }

        public int getSaleNum() {
            return saleNum;
        }

        public void setSaleNum(int saleNum) {
            this.saleNum = saleNum;
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

        public String getDistance() {
            return distance;
        }

        public void setDistance(String distance) {
            this.distance = distance;
        }

        public static class ReductionListBean {
            /**
             * rId : 3
             * price : 5
             * fullPrice : 30
             */

            private String rId;
            private String price;
            private String fullPrice;

            public String getRId() {
                return rId;
            }

            public void setRId(String rId) {
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
    }
}
