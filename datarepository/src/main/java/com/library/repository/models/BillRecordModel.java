package com.library.repository.models;

import java.util.List;

public class BillRecordModel {


    /**
     * hasNext : false
     * recordList : [{"billId":"50","typeName":"转账","price":"0.01","cnyPrice":"0.00","remark":"","createTime":"2019-05-18 14:38:35","type":"7"},{"billId":"49","typeName":"发红包","price":"0.01","cnyPrice":"0.00","remark":"","createTime":"2019-05-18 13:55:23","type":"8"},{"billId":"48","typeName":"转账","price":"0.01","cnyPrice":"0.00","remark":"","createTime":"2019-05-18 13:55:07","type":"7"},{"billId":"47","typeName":"转账","price":"0.01","cnyPrice":"0.00","remark":"","createTime":"2019-05-18 11:28:54","type":"7"},{"billId":"46","typeName":"发红包","price":"0.01","cnyPrice":"0.00","remark":"","createTime":"2019-05-18 11:26:27","type":"8"},{"billId":"33","typeName":"收款","price":"0.01","cnyPrice":"0.00","remark":"","createTime":"2019-05-17 17:40:14","type":"9"},{"billId":"30","typeName":"发红包","price":"0.01","cnyPrice":"0.00","remark":"","createTime":"2019-05-17 16:50:28","type":"8"},{"billId":"29","typeName":"收款","price":"0.01","cnyPrice":"0.00","remark":"","createTime":"2019-05-17 16:49:56","type":"9"},{"billId":"27","typeName":"转账","price":"0.01","cnyPrice":"0.00","remark":"","createTime":"2019-05-17 16:43:23","type":"7"},{"billId":"26","typeName":"转账","price":"0.01","cnyPrice":"0.00","remark":"","createTime":"2019-05-17 16:39:49","type":"7"},{"billId":"20","typeName":"转账","price":"0.01","cnyPrice":"0.00","remark":"","createTime":"2019-05-17 16:23:25","type":"7"},{"billId":"19","typeName":"转账","price":"0.01","cnyPrice":"0.00","remark":"","createTime":"2019-05-17 16:22:21","type":"7"},{"billId":"15","typeName":"发红包","price":"-0.01","cnyPrice":"0.00","remark":"","createTime":"2019-05-17 14:04:16","type":"8"},{"billId":"14","typeName":"收红包","price":"0.01","cnyPrice":"0.00","remark":"","createTime":"2019-05-17 14:02:18","type":"10"},{"billId":"11","typeName":"发红包","price":"-0.01","cnyPrice":"0.00","remark":"","createTime":"2019-05-17 11:38:50","type":"8"},{"billId":"7","typeName":"转账","price":"-0.01","cnyPrice":"0.00","remark":"","createTime":"2019-05-16 20:41:21","type":"7"},{"billId":"4","typeName":"转账","price":"-0.01","cnyPrice":"0.00","remark":"","createTime":"2019-05-16 20:27:35","type":"7"},{"billId":"3","typeName":"转账","price":"-0.01","cnyPrice":"0.00","remark":"","createTime":"2019-05-16 20:26:28","type":"7"},{"billId":"2","typeName":"转账","price":"-0.01","cnyPrice":"0.00","remark":"","createTime":"2019-05-16 17:17:12","type":"7"},{"billId":"1","typeName":"收款","price":"0.01","cnyPrice":"0.00","remark":"","createTime":"2019-05-16 16:25:28","type":"9"}]
     * accountType : [{"aId":1,"name":"充值"},{"aId":2,"name":"交话费"},{"aId":3,"name":"交流量费"},{"aId":4,"name":"交电费"},{"aId":5,"name":"交水费"},{"aId":6,"name":"交网费"},{"aId":7,"name":"转账"},{"aId":8,"name":"发红包"},{"aId":9,"name":"收款"},{"aId":10,"name":"收红包"},{"aId":11,"name":"外卖"},{"aId":12,"name":"机票"},{"aId":13,"name":"酒店住宿"},{"aId":14,"name":"退款"}]
     * billType : [{"bId":1,"name":"收入"},{"bId":2,"name":"支出"}]
     */

    private boolean hasNext;
    private List<RecordListBean> recordList;
    private List<AccountTypeBean> accountType;
    private List<BillTypeBean> billType;

    public boolean isHasNext() {
        return hasNext;
    }

    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }

    public List<RecordListBean> getRecordList() {
        return recordList;
    }

    public void setRecordList(List<RecordListBean> recordList) {
        this.recordList = recordList;
    }

    public List<AccountTypeBean> getAccountType() {
        return accountType;
    }

    public void setAccountType(List<AccountTypeBean> accountType) {
        this.accountType = accountType;
    }

    public List<BillTypeBean> getBillType() {
        return billType;
    }

    public void setBillType(List<BillTypeBean> billType) {
        this.billType = billType;
    }

    public static class RecordListBean {
        /**
         * billId : 50
         * typeName : 转账
         * price : 0.01
         * cnyPrice : 0.00
         * remark :
         * createTime : 2019-05-18 14:38:35
         * type : 7
         */

        private String billId;
        private String typeName;
        private String price;
        private String cnyPrice;
        private String remark;
        private String createTime;
        private String type;
        private String sendTime;
        private String userRemark;
        /**
         * title : 缴费提醒
         * from :
         * to :
         * payType : 余额
         * reciveTime :
         */

        private String title;
        private String from;
        private String to;
        private String payType;
        private String reciveTime;
        private String secondType;//1：转账退还,2:红包退还
        private String orderId;
        private String toUid;

        public String getToUid() {
            return toUid;
        }

        public void setToUid(String toUid) {
            this.toUid = toUid;
        }

        public String getSecondType() {
            return secondType;
        }

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public void setSecondType(String secondType) {
            this.secondType = secondType;
        }


        public String getBillId() {
            return billId;
        }

        public void setBillId(String billId) {
            this.billId = billId;
        }

        public String getTypeName() {
            return typeName;
        }

        public void setTypeName(String typeName) {
            this.typeName = typeName;
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

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
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

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
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

        public String getPayType() {
            return payType;
        }

        public void setPayType(String payType) {
            this.payType = payType;
        }

        public String getReciveTime() {
            return reciveTime;
        }

        public void setReciveTime(String reciveTime) {
            this.reciveTime = reciveTime;
        }

        public String getSendTime() {return sendTime;}

        public void setSendTime(String sendTime) {this.sendTime = sendTime;}

        public String getUserRemark() {return userRemark;}

        public void setUserRemark(String  userRemark) {this.userRemark = userRemark;}
    }

    public static class AccountTypeBean {
        /**
         * aId : 1
         * name : 充值
         */

        private String aId;
        private String name;
        boolean isSelect;

        public boolean isSelect() {
            return isSelect;
        }

        public void setSelect(boolean select) {
            isSelect = select;
        }

        public String getAId() {
            return aId;
        }

        public void setAId(String aId) {
            this.aId = aId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class BillTypeBean {
        /**
         * bId : 1
         * name : 收入
         */

        private String bId;
        private String name;
        boolean isSelect;

        public String getBId() {
            return bId;
        }

        public void setBId(String bId) {
            this.bId = bId;
        }

        public boolean isSelect() {
            return isSelect;
        }

        public void setSelect(boolean select) {
            isSelect = select;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
