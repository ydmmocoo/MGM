package com.library.repository.models;

import java.util.List;

/**
 * @author yedeman
 * @date 2020/5/31.
 * email：ydmmocoo@gmail.com
 * description：
 */
public class OrderDetailBean {

    /**
     * orderInfo : {"oId":"35","expectedDeliveryTime":"18:04","address":{"name":"叶","address":"福建省厦门市","phone":"15606004971","distance":"157.4"},"remark":"","goodsCount":1,"goodsList":[{"gId":"13","gName":"666","seName":"666","aName":"312331","num":"1","price":"666","totalPrice":"666","img":"http://139.9.38.218:8080/Uploads/image/2020-05-24/5eca73e882ada.png"}],"reservedTelephone":"","deliveryPrice":"0","goodsPrice":"666","fullReduction":"0","firstReduction":"0","redRrice":"0","totalPrice":"666","price":"666","servicePrice":"0","orderStatus":"2","orderId":"eb2020053116340284320730","createTime":"2020-05-31 16:34:02","type":"1","refundRemark":"","refundStatus":"0","orderType":"1","payStatus":"1","shopName":"键锋小吃店","payType":"支付宝","tels":null}
     * shareInfo : {"title":"标题","content":"红包分享内容"}
     */

    private OrderInfoBean orderInfo;
    private ShareInfoBean shareInfo;

    public OrderInfoBean getOrderInfo() {
        return orderInfo;
    }

    public void setOrderInfo(OrderInfoBean orderInfo) {
        this.orderInfo = orderInfo;
    }

    public ShareInfoBean getShareInfo() {
        return shareInfo;
    }

    public void setShareInfo(ShareInfoBean shareInfo) {
        this.shareInfo = shareInfo;
    }

    public static class OrderInfoBean {
        /**
         * oId : 35
         * expectedDeliveryTime : 18:04
         * address : {"name":"叶","address":"福建省厦门市","phone":"15606004971","distance":"157.4"}
         * remark :
         * goodsCount : 1
         * goodsList : [{"gId":"13","gName":"666","seName":"666","aName":"312331","num":"1","price":"666","totalPrice":"666","img":"http://139.9.38.218:8080/Uploads/image/2020-05-24/5eca73e882ada.png"}]
         * reservedTelephone :
         * deliveryPrice : 0
         * goodsPrice : 666
         * fullReduction : 0
         * firstReduction : 0
         * redRrice : 0
         * totalPrice : 666
         * price : 666
         * servicePrice : 0
         * orderStatus : 2
         * orderId : eb2020053116340284320730
         * createTime : 2020-05-31 16:34:02
         * type : 1
         * refundRemark :
         * refundStatus : 0
         * orderType : 1
         * payStatus : 1
         * shopName : 键锋小吃店
         * payType : 支付宝
         * tels : null
         */

        private String oId;
        private String expectedDeliveryTime;
        private AddressBean address;
        private String remark;
        private int goodsCount;
        private String reservedTelephone;
        private String deliveryPrice;
        private String goodsPrice;
        private String fullReduction;
        private String firstReduction;
        private String redRrice;
        private String totalPrice;
        private String price;
        private String servicePrice;
        private String orderStatus;
        private String orderId;
        private String createTime;
        private String type;
        private String refundRemark;
        private String refundStatus;
        private String orderType;
        private String payStatus;
        private String shopName;
        private String payType;
        private String evaluateStatus;
        private List<TelsBean> tels;
        private int expireTime;
        private String shopLogo;
        private String shopAddress;
        private String lng;
        private String lat;
        private List<GoodsListBean> goodsList;

        public String getOId() {
            return oId;
        }

        public void setOId(String oId) {
            this.oId = oId;
        }

        public String getExpectedDeliveryTime() {
            return expectedDeliveryTime;
        }

        public void setExpectedDeliveryTime(String expectedDeliveryTime) {
            this.expectedDeliveryTime = expectedDeliveryTime;
        }

        public AddressBean getAddress() {
            return address;
        }

        public void setAddress(AddressBean address) {
            this.address = address;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public int getGoodsCount() {
            return goodsCount;
        }

        public void setGoodsCount(int goodsCount) {
            this.goodsCount = goodsCount;
        }

        public String getReservedTelephone() {
            return reservedTelephone;
        }

        public void setReservedTelephone(String reservedTelephone) {
            this.reservedTelephone = reservedTelephone;
        }

        public String getDeliveryPrice() {
            return deliveryPrice;
        }

        public void setDeliveryPrice(String deliveryPrice) {
            this.deliveryPrice = deliveryPrice;
        }

        public String getGoodsPrice() {
            return goodsPrice;
        }

        public void setGoodsPrice(String goodsPrice) {
            this.goodsPrice = goodsPrice;
        }

        public String getFullReduction() {
            return fullReduction;
        }

        public void setFullReduction(String fullReduction) {
            this.fullReduction = fullReduction;
        }

        public String getFirstReduction() {
            return firstReduction;
        }

        public void setFirstReduction(String firstReduction) {
            this.firstReduction = firstReduction;
        }

        public String getRedRrice() {
            return redRrice;
        }

        public void setRedRrice(String redRrice) {
            this.redRrice = redRrice;
        }

        public String getTotalPrice() {
            return totalPrice;
        }

        public void setTotalPrice(String totalPrice) {
            this.totalPrice = totalPrice;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getServicePrice() {
            return servicePrice;
        }

        public void setServicePrice(String servicePrice) {
            this.servicePrice = servicePrice;
        }

        public String getOrderStatus() {
            return orderStatus;
        }

        public void setOrderStatus(String orderStatus) {
            this.orderStatus = orderStatus;
        }

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getRefundRemark() {
            return refundRemark;
        }

        public void setRefundRemark(String refundRemark) {
            this.refundRemark = refundRemark;
        }

        public String getRefundStatus() {
            return refundStatus;
        }

        public void setRefundStatus(String refundStatus) {
            this.refundStatus = refundStatus;
        }

        public String getOrderType() {
            return orderType;
        }

        public void setOrderType(String orderType) {
            this.orderType = orderType;
        }

        public String getPayStatus() {
            return payStatus;
        }

        public void setPayStatus(String payStatus) {
            this.payStatus = payStatus;
        }

        public String getShopName() {
            return shopName;
        }

        public void setShopName(String shopName) {
            this.shopName = shopName;
        }

        public String getPayType() {
            return payType;
        }

        public void setPayType(String payType) {
            this.payType = payType;
        }

        public String getEvaluateStatus() {
            return evaluateStatus;
        }

        public void setEvaluateStatus(String evaluateStatus) {
            this.evaluateStatus = evaluateStatus;
        }

        public List<TelsBean> getTels() {
            return tels;
        }

        public void setTels(List<TelsBean> tels) {
            this.tels = tels;
        }

        public int getExpireTime() {
            return expireTime;
        }

        public void setExpireTime(int expireTime) {
            this.expireTime = expireTime;
        }

        public String getShopLogo() {
            return shopLogo;
        }

        public void setShopLogo(String shopLogo) {
            this.shopLogo = shopLogo;
        }

        public String getShopAddress() {
            return shopAddress;
        }

        public void setShopAddress(String shopAddress) {
            this.shopAddress = shopAddress;
        }

        public String getLng() {
            return lng;
        }

        public void setLng(String lng) {
            this.lng = lng;
        }

        public String getLat() {
            return lat;
        }

        public void setLat(String lat) {
            this.lat = lat;
        }

        public List<GoodsListBean> getGoodsList() {
            return goodsList;
        }

        public void setGoodsList(List<GoodsListBean> goodsList) {
            this.goodsList = goodsList;
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

        public static class AddressBean {
            /**
             * name : 叶
             * address : 福建省厦门市
             * phone : 15606004971
             * distance : 157.4
             */

            private String name;
            private String address;
            private String phone;
            private String sex;
            private String roomNo;
            private String distance;

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

            public String getPhone() {
                return phone;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }

            public String getSex() {
                return sex;
            }

            public void setSex(String sex) {
                this.sex = sex;
            }

            public String getRoomNo() {
                return roomNo;
            }

            public void setRoomNo(String roomNo) {
                this.roomNo = roomNo;
            }

            public String getDistance() {
                return distance;
            }

            public void setDistance(String distance) {
                this.distance = distance;
            }
        }

        public static class GoodsListBean {
            /**
             * gId : 13
             * gName : 666
             * seName : 666
             * aName : 312331
             * num : 1
             * price : 666
             * totalPrice : 666
             * img : http://139.9.38.218:8080/Uploads/image/2020-05-24/5eca73e882ada.png
             */

            private String gId;
            private String gName;
            private String seName;
            private String aName;
            private String num;
            private String price;
            private String totalPrice;
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

            public String getSeName() {
                return seName;
            }

            public void setSeName(String seName) {
                this.seName = seName;
            }

            public String getAName() {
                return aName;
            }

            public void setAName(String aName) {
                this.aName = aName;
            }

            public String getNum() {
                return num;
            }

            public void setNum(String num) {
                this.num = num;
            }

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }

            public String getTotalPrice() {
                return totalPrice;
            }

            public void setTotalPrice(String totalPrice) {
                this.totalPrice = totalPrice;
            }

            public String getImg() {
                return img;
            }

            public void setImg(String img) {
                this.img = img;
            }
        }
    }

    public static class ShareInfoBean {
        /**
         * title : 标题
         * content : 红包分享内容
         */

        private String title;
        private String content;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
