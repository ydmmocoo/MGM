package com.library.repository.models;

import java.util.List;

public class PhoneRechargeModel {

    /**
     * hasNext : true
     * recordList : [{"id":"10","type":"","price":"200.00","CNYPrice":"200.00","datetime":"1970-01-01 08:00:00"},{"id":"9","type":"","price":"100.00","CNYPrice":"100.00","datetime":"1970-01-01 08:00:00"}]
     */

    private boolean hasNext;
    private List<RecordListBean> recordList;

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

    public static class RecordListBean {
        /**
         * id : 10
         * type :
         * price : 200.00
         * CNYPrice : 200.00
         * datetime : 1970-01-01 08:00:00
         */

        private String id;
        private String type;
        private String price;
        private String CNYPrice;
        private String datetime;
        private String service;

        /**
         * remark : 充话费
         * status : 2
         * statusTip : 充值中
         * processRemark :
         */

        private String remark;
        private String status;//1:处理成功,2:等待处理,3:取消'
        private String statusTip;
        private String processRemark;
        private String phone;//呵呵
        private String account;//呵呵

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public String getService() {
            return service;
        }

        public void setService(String service) {
            this.service = service;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
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

        public String getCNYPrice() {
            return CNYPrice;
        }

        public void setCNYPrice(String CNYPrice) {
            this.CNYPrice = CNYPrice;
        }

        public String getDatetime() {
            return datetime;
        }

        public void setDatetime(String datetime) {
            this.datetime = datetime;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getStatusTip() {
            return statusTip;
        }

        public void setStatusTip(String statusTip) {
            this.statusTip = statusTip;
        }

        public String getProcessRemark() {
            return processRemark;
        }

        public void setProcessRemark(String processRemark) {
            this.processRemark = processRemark;
        }
    }
}
