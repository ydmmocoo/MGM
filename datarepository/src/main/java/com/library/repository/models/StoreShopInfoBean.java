package com.library.repository.models;

import java.util.List;

/**
 * @author yedeman
 * @date 2020/5/28.
 * email：ydmmocoo@gmail.com
 * description：
 */
public class StoreShopInfoBean {

    /**
     * shopInfo : {"sId":"1","shopName":"键锋小吃店","saleNum":0,"address":"Antananarivo","avgScore":"0.0","distance":0,"typeName":"美食","tel":"17040505320","tels":[{"tId":"","tel":""}],"isAll":"0","isClose":"1","openTimeList":[{"oId":"","openTime":""}],"uid":"192","deliveryPrice":"0","distributionFee":"0","firstReduction":0,"shopImgs":[{"img":""}],"shopCertImg":[{"img":"http://139.9.38.218:8080/Uploads/image/2020-05-20/5ec4ea3d14dd8.png"}],"goodsCateList":[{"cId":"4","name":"31231231231231231231231"},{"cId":"1","name":"31231"},{"cId":"3","name":"312eqweq3123"}],"evaluationCount":0,"reductionList":[{"rId":"","price":"","fullPrice":""}],"decorationInfo":{"bgImg":"","adImg":"","assoGood":[{"gId":"","gName":""}]},"evaluateCount":0,"isCollect":false}
     */

    private ShopInfoBean shopInfo;

    public ShopInfoBean getShopInfo() {
        return shopInfo;
    }

    public void setShopInfo(ShopInfoBean shopInfo) {
        this.shopInfo = shopInfo;
    }

    public static class ShopInfoBean {
        /**
         * sId : 1
         * shopName : 键锋小吃店
         * saleNum : 0
         * address : Antananarivo
         * avgScore : 0.0
         * distance : 0
         * typeName : 美食
         * tel : 17040505320
         * tels : [{"tId":"","tel":""}]
         * isAll : 0
         * isClose : 1
         * openTimeList : [{"oId":"","openTime":""}]
         * uid : 192
         * deliveryPrice : 0
         * distributionFee : 0
         * firstReduction : 0
         * shopImgs : [{"img":""}]
         * shopCertImg : [{"img":"http://139.9.38.218:8080/Uploads/image/2020-05-20/5ec4ea3d14dd8.png"}]
         * goodsCateList : [{"cId":"4","name":"31231231231231231231231"},{"cId":"1","name":"31231"},{"cId":"3","name":"312eqweq3123"}]
         * evaluationCount : 0
         * reductionList : [{"rId":"","price":"","fullPrice":""}]
         * decorationInfo : {"bgImg":"","adImg":"","assoGood":[{"gId":"","gName":""}]}
         * evaluateCount : 0
         * isCollect : false
         */

        private String sId;
        private String shopName;
        private int saleNum;
        private String shopLogo;
        private String address;
        private String avgScore;
        private int distance;
        private String typeName;
        private String tel;
        private String isAll;
        private String isClose;
        private String uid;
        private String deliveryPrice;
        private String distributionFee;
        private String firstReduction;
        private int evaluationCount;
        private DecorationInfoBean decorationInfo;
        private int evaluateCount;
        private boolean isCollect;
        private List<TelsBean> tels;
        private List<OpenTimeListBean> openTimeList;
        private List<String> shopImgs;
        private List<String> shopCertImg;
        private List<GoodsCateListBean> goodsCateList;
        private List<ReductionListBean> reductionList;

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

        public int getSaleNum() {
            return saleNum;
        }

        public void setSaleNum(int saleNum) {
            this.saleNum = saleNum;
        }

        public String getShopLogo() {
            return shopLogo;
        }

        public void setShopLogo(String shopLogo) {
            this.shopLogo = shopLogo;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getAvgScore() {
            return avgScore;
        }

        public void setAvgScore(String avgScore) {
            this.avgScore = avgScore;
        }

        public int getDistance() {
            return distance;
        }

        public void setDistance(int distance) {
            this.distance = distance;
        }

        public String getTypeName() {
            return typeName;
        }

        public void setTypeName(String typeName) {
            this.typeName = typeName;
        }

        public String getTel() {
            return tel;
        }

        public void setTel(String tel) {
            this.tel = tel;
        }

        public String getIsAll() {
            return isAll;
        }

        public void setIsAll(String isAll) {
            this.isAll = isAll;
        }

        public String getIsClose() {
            return isClose;
        }

        public void setIsClose(String isClose) {
            this.isClose = isClose;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
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

        public String getFirstReduction() {
            return firstReduction;
        }

        public void setFirstReduction(String firstReduction) {
            this.firstReduction = firstReduction;
        }

        public int getEvaluationCount() {
            return evaluationCount;
        }

        public void setEvaluationCount(int evaluationCount) {
            this.evaluationCount = evaluationCount;
        }

        public DecorationInfoBean getDecorationInfo() {
            return decorationInfo;
        }

        public void setDecorationInfo(DecorationInfoBean decorationInfo) {
            this.decorationInfo = decorationInfo;
        }

        public int getEvaluateCount() {
            return evaluateCount;
        }

        public void setEvaluateCount(int evaluateCount) {
            this.evaluateCount = evaluateCount;
        }

        public boolean isCollect() {
            return isCollect;
        }

        public void setCollect(boolean collect) {
            isCollect = collect;
        }

        public List<TelsBean> getTels() {
            return tels;
        }

        public void setTels(List<TelsBean> tels) {
            this.tels = tels;
        }

        public List<OpenTimeListBean> getOpenTimeList() {
            return openTimeList;
        }

        public void setOpenTimeList(List<OpenTimeListBean> openTimeList) {
            this.openTimeList = openTimeList;
        }

        public List<String> getShopImgs() {
            return shopImgs;
        }

        public void setShopImgs(List<String> shopImgs) {
            this.shopImgs = shopImgs;
        }

        public List<String> getShopCertImg() {
            return shopCertImg;
        }

        public void setShopCertImg(List<String> shopCertImg) {
            this.shopCertImg = shopCertImg;
        }

        public List<GoodsCateListBean> getGoodsCateList() {
            return goodsCateList;
        }

        public void setGoodsCateList(List<GoodsCateListBean> goodsCateList) {
            this.goodsCateList = goodsCateList;
        }

        public List<ReductionListBean> getReductionList() {
            return reductionList;
        }

        public void setReductionList(List<ReductionListBean> reductionList) {
            this.reductionList = reductionList;
        }

        public static class DecorationInfoBean {
            /**
             * bgImg :
             * adImg :
             * assoGood : [{"gId":"","gName":""}]
             */

            private String bgImg;
            private String adImg;
            private List<AssoGoodBean> assoGood;

            public String getBgImg() {
                return bgImg;
            }

            public void setBgImg(String bgImg) {
                this.bgImg = bgImg;
            }

            public String getAdImg() {
                return adImg;
            }

            public void setAdImg(String adImg) {
                this.adImg = adImg;
            }

            public List<AssoGoodBean> getAssoGood() {
                return assoGood;
            }

            public void setAssoGood(List<AssoGoodBean> assoGood) {
                this.assoGood = assoGood;
            }

            public static class AssoGoodBean {
                /**
                 * gId :
                 * gName :
                 */

                private String gId;
                private String gName;

                public String getGId() {
                    return gId;
                }

                public void setGId(String gId) {
                    this.gId = gId;
                }

                public String getGName() {
                    return gName;
                }

                public void setGName(String gName) {
                    this.gName = gName;
                }
            }
        }

        public static class TelsBean {
            /**
             * tId :
             * tel :
             */

            private String tId;
            private String tel;

            public String getTId() {
                return tId;
            }

            public void setTId(String tId) {
                this.tId = tId;
            }

            public String getTel() {
                return tel;
            }

            public void setTel(String tel) {
                this.tel = tel;
            }
        }

        public static class OpenTimeListBean {
            /**
             * oId :
             * openTime :
             */

            private String oId;
            private String openTime;

            public String getOId() {
                return oId;
            }

            public void setOId(String oId) {
                this.oId = oId;
            }

            public String getOpenTime() {
                return openTime;
            }

            public void setOpenTime(String openTime) {
                this.openTime = openTime;
            }
        }

        public static class GoodsCateListBean {
            /**
             * cId : 4
             * name : 31231231231231231231231
             */

            private String cId;
            private String name;

            public String getCId() {
                return cId;
            }

            public void setCId(String cId) {
                this.cId = cId;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }

        public static class ReductionListBean {
            /**
             * rId :
             * price :
             * fullPrice :
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
