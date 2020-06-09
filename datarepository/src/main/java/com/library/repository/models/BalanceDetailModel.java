package com.library.repository.models;

import java.util.List;

public class BalanceDetailModel {

    /**
     * hasNext : false
     * balanceList : [{"balanceId":"63","type":"发红包","price":"-0.01","cnyPrice":"-0.01","createTime":"2019-05-20 17:54:46"},{"balanceId":"62","type":"接受转账","price":"0.01","cnyPrice":"0.01","createTime":"2019-05-20 10:06:37"},{"balanceId":"61","type":"接受转账","price":"0.01","cnyPrice":"0.01","createTime":"2019-05-20 10:04:40"}]
     */

    private boolean hasNext;
    private List<BalanceListBean> balanceList;

    public boolean isHasNext() {
        return hasNext;
    }

    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }

    public List<BalanceListBean> getBalanceList() {
        return balanceList;
    }

    public void setBalanceList(List<BalanceListBean> balanceList) {
        this.balanceList = balanceList;
    }

    public static class BalanceListBean {
        /**
         * balanceId : 63
         * type : 发红包
         * price : -0.01
         * cnyPrice : -0.01
         * createTime : 2019-05-20 17:54:46
         */

        private String balanceId;
        private String type;
        private String price;
        private String cnyPrice;
        private String createTime;
        private String remainBalance;
        private String sendTime;
        private String userRemark;
        private String cate;
        /**
         * from :
         * to :
         * reciveTime :
         * orderId : tp2019071910445382323952
         */

        private String from;
        private String to;
        private String reciveTime;
        private String orderId;

        public String getBalanceId() {
            return balanceId;
        }

        public String getRemainBalance() {
            return remainBalance;
        }

        public void setRemainBalance(String remainBalance) {
            this.remainBalance = remainBalance;
        }

        public void setBalanceId(String balanceId) {
            this.balanceId = balanceId;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getCnyPrice() {
            return cnyPrice;
        }

        public void setCnyPrice(String cnyPrice) {
            this.cnyPrice = cnyPrice;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getFrom() {
            return from;
        }

        public void setFrom(String from) {
            this.from = from;
        }

        public String getTo() {
            return to;
        }

        public void setTo(String to) {
            this.to = to;
        }

        public String getReciveTime() {
            return reciveTime;
        }

        public void setReciveTime(String reciveTime) {
            this.reciveTime = reciveTime;
        }

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public String getSendTime() {return sendTime;}

        public void setSendTime(String sendTime) {this.sendTime = sendTime;}

        public String getUserRemark() {return userRemark;}

        public void setUserRemark(String  userRemark) {this.userRemark = userRemark;}

        public String getCate() { return cate; }

        public void setCate(String cate) { this.cate = cate; }
    }
}
