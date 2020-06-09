package com.library.repository.models;

import java.util.List;

/**
 * @author yedeman
 * @date 2020/5/31.
 * email：ydmmocoo@gmail.com
 * description：
 */
public class OrderBean {
    /**
     * hasNext : false
     * orderList : [{"oId":"35","shopName":"键锋小吃店","shopLogo":"","goodsCount":1,"goodsList":[{"gId":"13","gName":"666","seName":"666","aName":"312331","num":"1","price":"666","totalPrice":"666","img":"http://139.9.38.218:8080/Uploads/image/2020-05-24/5eca73e882ada.png"}],"totalPrice":"666","orderStatus":"2","payStatus":"1","refundRemark":"","refundStatus":"0","type":"1","orderType":"1","tips":"待接单","orderId":"eb2020053116340284320730","createTime":"2020-05-31 16:34:02","sId":"1","evaluateStatus":"0"},{"oId":"34","shopName":"键锋小吃店","shopLogo":"","goodsCount":1,"goodsList":[{"gId":"13","gName":"666","seName":"666","aName":"312331","num":"1","price":"666","totalPrice":"666","img":"http://139.9.38.218:8080/Uploads/image/2020-05-24/5eca73e882ada.png"}],"totalPrice":"666","orderStatus":"2","payStatus":"1","refundRemark":"","refundStatus":"0","type":"1","orderType":"1","tips":"待接单","orderId":"eb2020053116213340996768","createTime":"2020-05-31 16:21:33","sId":"1","evaluateStatus":"0"},{"oId":"33","shopName":"键锋小吃店","shopLogo":"","goodsCount":1,"goodsList":[{"gId":"13","gName":"666","seName":"666","aName":"312331","num":"1","price":"666","totalPrice":"666","img":"http://139.9.38.218:8080/Uploads/image/2020-05-24/5eca73e882ada.png"}],"totalPrice":"666","orderStatus":"2","payStatus":"1","refundRemark":"","refundStatus":"0","type":"1","orderType":"1","tips":"待接单","orderId":"eb2020053116083055652935","createTime":"2020-05-31 16:08:30","sId":"1","evaluateStatus":"0"},{"oId":"32","shopName":"键锋小吃店","shopLogo":"","goodsCount":1,"goodsList":[{"gId":"13","gName":"666","seName":"666","aName":"312331","num":"1","price":"666","totalPrice":"666","img":"http://139.9.38.218:8080/Uploads/image/2020-05-24/5eca73e882ada.png"}],"totalPrice":"666","orderStatus":"2","payStatus":"1","refundRemark":"","refundStatus":"0","type":"1","orderType":"1","tips":"待接单","orderId":"eb2020053111413100478402","createTime":"2020-05-31 11:41:31","sId":"1","evaluateStatus":"0"},{"oId":"31","shopName":"键锋小吃店","shopLogo":"","goodsCount":3,"goodsList":[{"gId":"4","gName":"t","seName":"dafsdfsdfas","aName":"","num":"1","price":"123","totalPrice":"123","img":"http://139.9.38.218:8080http://139.9.38.218:8080/Uploads/image/2020-05-21/5ec63a7801086.png"},{"gId":"7","gName":"1312312312312","seName":"1","aName":"5345,312321","num":"1","price":"1","totalPrice":"1","img":"http://139.9.38.218:8080/Uploads/image/2020-05-22/5ec738972d9db.png"},{"gId":"13","gName":"666","seName":"666","aName":"312331","num":"1","price":"666","totalPrice":"666","img":"http://139.9.38.218:8080/Uploads/image/2020-05-24/5eca73e882ada.png"}],"totalPrice":"790","orderStatus":"0","payStatus":"2","refundRemark":"","refundStatus":"0","type":"1","orderType":"1","tips":"待付款","orderId":"eb2020053111290075238073","createTime":"2020-05-31 11:29:00","sId":"1","evaluateStatus":"0"},{"oId":"30","shopName":"键锋小吃店","shopLogo":"","goodsCount":3,"goodsList":[{"gId":"4","gName":"t","seName":"dafsdfsdfas","aName":"","num":"1","price":"123","totalPrice":"123","img":"http://139.9.38.218:8080http://139.9.38.218:8080/Uploads/image/2020-05-21/5ec63a7801086.png"},{"gId":"13","gName":"666","seName":"666","aName":"312331","num":"1","price":"666","totalPrice":"666","img":"http://139.9.38.218:8080/Uploads/image/2020-05-24/5eca73e882ada.png"},{"gId":"7","gName":"1312312312312","seName":"1","aName":"5345,312321","num":"1","price":"1","totalPrice":"1","img":"http://139.9.38.218:8080/Uploads/image/2020-05-22/5ec738972d9db.png"}],"totalPrice":"790","orderStatus":"0","payStatus":"2","refundRemark":"","refundStatus":"0","type":"1","orderType":"1","tips":"待付款","orderId":"eb2020053111271844644359","createTime":"2020-05-31 11:27:18","sId":"1","evaluateStatus":"0"},{"oId":"29","shopName":"键锋小吃店","shopLogo":"","goodsCount":2,"goodsList":[{"gId":"7","gName":"1312312312312","seName":"1","aName":"5345,312321","num":"1","price":"1","totalPrice":"1","img":"http://139.9.38.218:8080/Uploads/image/2020-05-22/5ec738972d9db.png"},{"gId":"13","gName":"666","seName":"666","aName":"312331","num":"1","price":"666","totalPrice":"666","img":"http://139.9.38.218:8080/Uploads/image/2020-05-24/5eca73e882ada.png"}],"totalPrice":"667","orderStatus":"0","payStatus":"2","refundRemark":"","refundStatus":"0","type":"1","orderType":"1","tips":"待付款","orderId":"eb2020053111175998925849","createTime":"2020-05-31 11:17:59","sId":"1","evaluateStatus":"0"},{"oId":"27","shopName":"键锋小吃店","shopLogo":"","goodsCount":3,"goodsList":[{"gId":"7","gName":"1312312312312","seName":"1","aName":"5345,312321","num":"3","price":"1","totalPrice":"3","img":"http://139.9.38.218:8080/Uploads/image/2020-05-22/5ec738972d9db.png"},{"gId":"7","gName":"1312312312312","seName":"1","aName":"111,1231231","num":"1","price":"1","totalPrice":"1","img":"http://139.9.38.218:8080/Uploads/image/2020-05-22/5ec738972d9db.png"},{"gId":"13","gName":"666","seName":"666","aName":"312331","num":"1","price":"666","totalPrice":"666","img":"http://139.9.38.218:8080/Uploads/image/2020-05-24/5eca73e882ada.png"}],"totalPrice":"670","orderStatus":"0","payStatus":"2","refundRemark":"","refundStatus":"0","type":"1","orderType":"1","tips":"待付款","orderId":"eb2020053000242236004041","createTime":"2020-05-30 00:24:22","sId":"1","evaluateStatus":"0"}]
     */
    private boolean hasNext;
    private List<OrderListBean> orderList;

    public boolean isHasNext() {
        return hasNext;
    }

    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }

    public List<OrderListBean> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<OrderListBean> orderList) {
        this.orderList = orderList;
    }

    public static class OrderListBean {
        /**
         * oId : 35
         * shopName : 键锋小吃店
         * shopLogo :
         * goodsCount : 1
         * goodsList : [{"gId":"13","gName":"666","seName":"666","aName":"312331","num":"1","price":"666","totalPrice":"666","img":"http://139.9.38.218:8080/Uploads/image/2020-05-24/5eca73e882ada.png"}]
         * totalPrice : 666
         * orderStatus : 2
         * payStatus : 1
         * refundRemark :
         * refundStatus : 0
         * type : 1
         * orderType : 1
         * tips : 待接单
         * orderId : eb2020053116340284320730
         * createTime : 2020-05-31 16:34:02
         * sId : 1
         * evaluateStatus : 0
         */

        private String oId;
        private String shopName;
        private String shopLogo;
        private int goodsCount;
        private String totalPrice;
        private String orderStatus;
        private String payStatus;
        private String refundRemark;
        private String refundStatus;
        private String type;
        private String orderType;
        private String tips;
        private String orderId;
        private String createTime;
        private String sId;
        private String evaluateStatus;
        private List<GoodsListBean> goodsList;

        public String getOId() {
            return oId;
        }

        public void setOId(String oId) {
            this.oId = oId;
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

        public int getGoodsCount() {
            return goodsCount;
        }

        public void setGoodsCount(int goodsCount) {
            this.goodsCount = goodsCount;
        }

        public String getTotalPrice() {
            return totalPrice;
        }

        public void setTotalPrice(String totalPrice) {
            this.totalPrice = totalPrice;
        }

        public String getOrderStatus() {
            return orderStatus;
        }

        public void setOrderStatus(String orderStatus) {
            this.orderStatus = orderStatus;
        }

        public String getPayStatus() {
            return payStatus;
        }

        public void setPayStatus(String payStatus) {
            this.payStatus = payStatus;
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

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getOrderType() {
            return orderType;
        }

        public void setOrderType(String orderType) {
            this.orderType = orderType;
        }

        public String getTips() {
            return tips;
        }

        public void setTips(String tips) {
            this.tips = tips;
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

        public String getSId() {
            return sId;
        }

        public void setSId(String sId) {
            this.sId = sId;
        }

        public String getEvaluateStatus() {
            return evaluateStatus;
        }

        public void setEvaluateStatus(String evaluateStatus) {
            this.evaluateStatus = evaluateStatus;
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
}
