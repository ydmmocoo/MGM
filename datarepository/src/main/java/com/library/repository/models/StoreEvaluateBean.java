package com.library.repository.models;

import java.util.List;

/**
 * @author yedeman
 * @date 2020/6/1.
 * email：ydmmocoo@gmail.com
 * description：
 */
public class StoreEvaluateBean {

    /**
     * shopInfo : {"avgGlobalScore":"4.2","avgTasteScore":"4.4","avgPackageScore":"4.6","avgDeliveryScore":"4.8","evaluateCount":5,"slideShowCount":3,"goodsEvaluateCount":4,"badEvaluateCount":1,"goodTasteCount":0,"goodPackageCount":0,"deliveryFastCount":0}
     * evaluateList : [{"eId":"5","fromUid":"10000002","fromNickName":"张剑锋","globalScore":"1","content":"垃圾，好难吃，吃了一次不想再吃第二次\n比爷拉出来的翔还难吃","createTime":"2020-05-29","imgs":["http://139.9.38.218/Uploads/image/2020-05-29/5ed0f55922d7c.png"],"goodsList":[{"gId":"3","gName":"q#tt"}],"userImg":"http://139.9.38.218/Uploads/user/2020-05-15/5ebe0a6e8689a_60_60.png"},{"eId":"4","fromUid":"10000002","fromNickName":"张剑锋","globalScore":"5","content":"Faddish;adds;Ladakh\nFeast;Dakar;larks\nfdsflsdajfldsjalfdsajfldsajflkasdjlkfadsjlkfsdaklfjasd\nFdsafdsakf;Assad;Asia;","createTime":"2020-05-29","imgs":["http://139.9.38.218/Uploads/image/2020-05-29/5ed0d7b3cea10.png"],"goodsList":[{"gId":"7","gName":"1312312312312#1#5345,312321"}],"userImg":"http://139.9.38.218/Uploads/user/2020-05-15/5ebe0a6e8689a_60_60.png"},{"eId":"3","fromUid":"10000002","fromNickName":"张剑锋","globalScore":"5","content":"4234324234234","createTime":"2020-05-29","imgs":["http://139.9.38.218/Uploads/image/2020-05-29/5ed0d56d1da7d.png","http://139.9.38.218/Uploads/image/2020-05-29/5ed0d56d1de9a.png","http://139.9.38.218/Uploads/image/2020-05-29/5ed0d56d1e19a.png"],"goodsList":[{"gId":"13","gName":"666#312312"}],"userImg":"http://139.9.38.218/Uploads/user/2020-05-15/5ebe0a6e8689a_60_60.png"},{"eId":"2","fromUid":"10000002","fromNickName":"张剑锋","globalScore":"5","content":"","createTime":"2020-05-29","imgs":[],"goodsList":[{"gId":"13","gName":"666#312312"}],"userImg":"http://139.9.38.218/Uploads/user/2020-05-15/5ebe0a6e8689a_60_60.png"},{"eId":"1","fromUid":"10000002","fromNickName":"张剑锋","globalScore":"5","content":"3123123123","createTime":"2020-05-29","imgs":[],"goodsList":[{"gId":"13","gName":"666#312312"},{"gId":"7","gName":"1312312312312#1#5345,312321"}],"userImg":"http://139.9.38.218/Uploads/user/2020-05-15/5ebe0a6e8689a_60_60.png"}]
     * hasNext : false
     */

    private ShopInfoBean shopInfo;
    private boolean hasNext;
    private List<EvaluateListBean> evaluateList;

    public ShopInfoBean getShopInfo() {
        return shopInfo;
    }

    public void setShopInfo(ShopInfoBean shopInfo) {
        this.shopInfo = shopInfo;
    }

    public boolean isHasNext() {
        return hasNext;
    }

    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }

    public List<EvaluateListBean> getEvaluateList() {
        return evaluateList;
    }

    public void setEvaluateList(List<EvaluateListBean> evaluateList) {
        this.evaluateList = evaluateList;
    }

    public static class ShopInfoBean {
        /**
         * avgGlobalScore : 4.2
         * avgTasteScore : 4.4
         * avgPackageScore : 4.6
         * avgDeliveryScore : 4.8
         * evaluateCount : 5
         * slideShowCount : 3
         * goodsEvaluateCount : 4
         * badEvaluateCount : 1
         * goodTasteCount : 0
         * goodPackageCount : 0
         * deliveryFastCount : 0
         */

        private String avgGlobalScore;
        private String avgTasteScore;
        private String avgPackageScore;
        private String avgDeliveryScore;
        private int evaluateCount;
        private int slideShowCount;
        private int goodsEvaluateCount;
        private int badEvaluateCount;
        private int goodTasteCount;
        private int goodPackageCount;
        private int deliveryFastCount;

        public String getAvgGlobalScore() {
            return avgGlobalScore;
        }

        public void setAvgGlobalScore(String avgGlobalScore) {
            this.avgGlobalScore = avgGlobalScore;
        }

        public String getAvgTasteScore() {
            return avgTasteScore;
        }

        public void setAvgTasteScore(String avgTasteScore) {
            this.avgTasteScore = avgTasteScore;
        }

        public String getAvgPackageScore() {
            return avgPackageScore;
        }

        public void setAvgPackageScore(String avgPackageScore) {
            this.avgPackageScore = avgPackageScore;
        }

        public String getAvgDeliveryScore() {
            return avgDeliveryScore;
        }

        public void setAvgDeliveryScore(String avgDeliveryScore) {
            this.avgDeliveryScore = avgDeliveryScore;
        }

        public int getEvaluateCount() {
            return evaluateCount;
        }

        public void setEvaluateCount(int evaluateCount) {
            this.evaluateCount = evaluateCount;
        }

        public int getSlideShowCount() {
            return slideShowCount;
        }

        public void setSlideShowCount(int slideShowCount) {
            this.slideShowCount = slideShowCount;
        }

        public int getGoodsEvaluateCount() {
            return goodsEvaluateCount;
        }

        public void setGoodsEvaluateCount(int goodsEvaluateCount) {
            this.goodsEvaluateCount = goodsEvaluateCount;
        }

        public int getBadEvaluateCount() {
            return badEvaluateCount;
        }

        public void setBadEvaluateCount(int badEvaluateCount) {
            this.badEvaluateCount = badEvaluateCount;
        }

        public int getGoodTasteCount() {
            return goodTasteCount;
        }

        public void setGoodTasteCount(int goodTasteCount) {
            this.goodTasteCount = goodTasteCount;
        }

        public int getGoodPackageCount() {
            return goodPackageCount;
        }

        public void setGoodPackageCount(int goodPackageCount) {
            this.goodPackageCount = goodPackageCount;
        }

        public int getDeliveryFastCount() {
            return deliveryFastCount;
        }

        public void setDeliveryFastCount(int deliveryFastCount) {
            this.deliveryFastCount = deliveryFastCount;
        }
    }

    public static class EvaluateListBean {
        /**
         * eId : 5
         * fromUid : 10000002
         * fromNickName : 张剑锋
         * globalScore : 1
         * content : 垃圾，好难吃，吃了一次不想再吃第二次
         比爷拉出来的翔还难吃
         * createTime : 2020-05-29
         * imgs : ["http://139.9.38.218/Uploads/image/2020-05-29/5ed0f55922d7c.png"]
         * goodsList : [{"gId":"3","gName":"q#tt"}]
         * userImg : http://139.9.38.218/Uploads/user/2020-05-15/5ebe0a6e8689a_60_60.png
         */

        private String eId;
        private String fromUid;
        private String fromNickName;
        private String globalScore;
        private String content;
        private String createTime;
        private String userImg;
        private List<String> imgs;
        private List<GoodsListBean> goodsList;

        public String getEId() {
            return eId;
        }

        public void setEId(String eId) {
            this.eId = eId;
        }

        public String getFromUid() {
            return fromUid;
        }

        public void setFromUid(String fromUid) {
            this.fromUid = fromUid;
        }

        public String getFromNickName() {
            return fromNickName;
        }

        public void setFromNickName(String fromNickName) {
            this.fromNickName = fromNickName;
        }

        public String getGlobalScore() {
            return globalScore;
        }

        public void setGlobalScore(String globalScore) {
            this.globalScore = globalScore;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getUserImg() {
            return userImg;
        }

        public void setUserImg(String userImg) {
            this.userImg = userImg;
        }

        public List<String> getImgs() {
            return imgs;
        }

        public void setImgs(List<String> imgs) {
            this.imgs = imgs;
        }

        public List<GoodsListBean> getGoodsList() {
            return goodsList;
        }

        public void setGoodsList(List<GoodsListBean> goodsList) {
            this.goodsList = goodsList;
        }

        public static class GoodsListBean {
            /**
             * gId : 3
             * gName : q#tt
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
}
