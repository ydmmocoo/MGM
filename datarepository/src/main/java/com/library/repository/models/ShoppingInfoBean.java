package com.library.repository.models;

import java.util.List;

/**
 * @author yedeman
 * @date 2020/5/29.
 * email：ydmmocoo@gmail.com
 * description：
 */
public class ShoppingInfoBean {

    /**
     * totalPrice : 4
     * totalNum : 4
     * goodsList : [{"gId":"7","gName":"1312312312312","seId":"14","seName":"1","aIds":"57,60,63","aNames":"5345,312321,31231312","pirce":"1","num":"3","img":"http://139.9.38.218:8080/Uploads/image/2020-05-22/5ec738972d9db.png"},{"gId":"7","gName":"1312312312312","seId":"14","seName":"1","aIds":"58,59,62,57,60,63","aNames":"111,1231231231,3123123,5345,312321,31231312","pirce":"1","num":"1","img":"http://139.9.38.218:8080/Uploads/image/2020-05-22/5ec738972d9db.png"}]
     * addressInfo : {"addressId":"5","phone":"15606004971","name":"叶","address":"福建省厦门市","roomNo":"金海湾财富大厦4楼408","sex":"1"}
     * shopInfo : {"shopName":"键锋小吃店","distance":"5546.3","address":"Antananarivo","tels":null,"firstReduction":0,"deliveryPrice":"0","distributionFee":"0"}
     * sendTime : ["18:17","18:20","18:25","18:30","18:35","18:40","18:45","18:50","18:55","19:00","19:05","19:10","19:15","19:20","19:25","19:30","19:35","19:40","19:45","19:50","19:55","20:00","20:05","20:10","20:15","20:20","20:25","20:30","20:35","20:40","20:45","20:50","20:55","21:00","21:05","21:10","21:15","21:20","21:25","21:30","21:35","21:40","21:45","21:50","21:55","22:00","22:05","22:10","22:15","22:20","22:25","22:30","22:35","22:40","22:45","22:50","22:55","23:00","23:05","23:10","23:15","23:20","23:25","23:30","23:35","23:40","23:45","23:50","23:55","00:00","00:05"]
     */

    private long totalPrice;
    private int totalNum;
    private AddressInfoBean addressInfo;
    private ShopInfoBean shopInfo;
    private List<GoodsListBean> goodsList;
    private List<String> sendTime;

    public long getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(long totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
    }

    public AddressInfoBean getAddressInfo() {
        return addressInfo;
    }

    public void setAddressInfo(AddressInfoBean addressInfo) {
        this.addressInfo = addressInfo;
    }

    public ShopInfoBean getShopInfo() {
        return shopInfo;
    }

    public void setShopInfo(ShopInfoBean shopInfo) {
        this.shopInfo = shopInfo;
    }

    public List<GoodsListBean> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<GoodsListBean> goodsList) {
        this.goodsList = goodsList;
    }

    public List<String> getSendTime() {
        return sendTime;
    }

    public void setSendTime(List<String> sendTime) {
        this.sendTime = sendTime;
    }

    public static class AddressInfoBean {
        /**
         * addressId : 5
         * phone : 15606004971
         * name : 叶
         * address : 福建省厦门市
         * roomNo : 金海湾财富大厦4楼408
         * sex : 1
         */

        private String addressId;
        private String phone;
        private String name;
        private String address;
        private String roomNo;
        private String sex;

        public String getAddressId() {
            return addressId;
        }

        public void setAddressId(String addressId) {
            this.addressId = addressId;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getRoomNo() {
            return roomNo;
        }

        public void setRoomNo(String roomNo) {
            this.roomNo = roomNo;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }
    }

    public static class ShopInfoBean {
        /**
         * shopName : 键锋小吃店
         * distance : 5546.3
         * address : Antananarivo
         * tels : null
         * firstReduction : 0
         * deliveryPrice : 0
         * distributionFee : 0
         */

        private String shopName;
        private String distance;
        private String address;
        private Object tels;
        private int firstReduction;
        private String deliveryPrice;
        private String distributionFee;
        private String isTakeYouself;
        private String fullReduction;

        public String getShopName() {
            return shopName;
        }

        public void setShopName(String shopName) {
            this.shopName = shopName;
        }

        public String getDistance() {
            return distance;
        }

        public void setDistance(String distance) {
            this.distance = distance;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public Object getTels() {
            return tels;
        }

        public void setTels(Object tels) {
            this.tels = tels;
        }

        public int getFirstReduction() {
            return firstReduction;
        }

        public void setFirstReduction(int firstReduction) {
            this.firstReduction = firstReduction;
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

        public String getFullReduction() {
            return fullReduction;
        }

        public void setFullReduction(String fullReduction) {
            this.fullReduction = fullReduction;
        }
    }

    public static class GoodsListBean {
        /**
         * gId : 7
         * gName : 1312312312312
         * seId : 14
         * seName : 1
         * aIds : 57,60,63
         * aNames : 5345,312321,31231312
         * pirce : 1
         * num : 3
         * img : http://139.9.38.218:8080/Uploads/image/2020-05-22/5ec738972d9db.png
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
