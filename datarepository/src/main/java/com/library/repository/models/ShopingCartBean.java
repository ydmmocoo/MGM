package com.library.repository.models;

import java.util.List;

/**
 * @author yedeman
 * @date 2020/5/26.
 * email：ydmmocoo@gmail.com
 * description：
 */
public class ShopingCartBean {

    /**
     * totalPrice : 1332
     * totalNum : 2
     * goodsList : [{"gId":"13","gName":"666","seId":"18","seName":"666","aIds":"64","aNames":"312331","pirce":"666","num":"1","img":"http://139.9.38.218:8080/Uploads/image/2020-05-24/5eca73e882ada.png"},{"gId":"13","gName":"666","seId":"18","seName":"666","aIds":"3123216564","aNames":"312331","pirce":"666","num":"1","img":"http://139.9.38.218:8080/Uploads/image/2020-05-24/5eca73e882ada.png"}]
     */

    private double totalPrice;
    private int totalNum;
    private List<GoodsListBean> goodsList;

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
    }

    public List<GoodsListBean> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<GoodsListBean> goodsList) {
        this.goodsList = goodsList;
    }

    public static class GoodsListBean {
        /**
         * gId : 13
         * gName : 666
         * seId : 18
         * seName : 666
         * aIds : 64
         * aNames : 312331
         * pirce : 666
         * num : 1
         * img : http://139.9.38.218:8080/Uploads/image/2020-05-24/5eca73e882ada.png
         */

        private String gId;
        private String gName;
        private String seId;
        private String seName;
        private String aIds;
        private String aNames;
        private String pirce;
        private String num;
        private String img;

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

        public String getSeId() {
            return seId;
        }

        public void setSeId(String seId) {
            this.seId = seId;
        }

        public String getSeName() {
            return seName;
        }

        public void setSeName(String seName) {
            this.seName = seName;
        }

        public String getAIds() {
            return aIds;
        }

        public void setAIds(String aIds) {
            this.aIds = aIds;
        }

        public String getANames() {
            return aNames;
        }

        public void setANames(String aNames) {
            this.aNames = aNames;
        }

        public String getPirce() {
            return pirce;
        }

        public void setPirce(String pirce) {
            this.pirce = pirce;
        }

        public String getNum() {
            return num;
        }

        public void setNum(String num) {
            this.num = num;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }
    }
}
