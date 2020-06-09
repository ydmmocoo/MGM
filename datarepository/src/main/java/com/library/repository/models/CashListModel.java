package com.library.repository.models;

import java.util.List;

public class CashListModel {


    /**
     * cashList : [{"cId":"1","uid":"10000002","name":"","nickName":"17359276732","toUid":"10000003","toName":"","toNickname":"🤔","balance":"29","statusTip":"待领取","status":"2","createTime":"2019-07-27 02:07:48","reciveTime":""}]
     * hasNext : false
     */

    private boolean hasNext;
    private List<CashModel> cashList;

    public boolean isHasNext() {
        return hasNext;
    }

    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }

    public List<CashModel> getCashList() {
        return cashList;
    }

    public void setCashList(List<CashModel> cashList) {
        this.cashList = cashList;
    }

    public static class CashModel {
        /**
         * cId : 1
         * uid : 10000002
         * name :
         * nickName : 17359276732
         * toUid : 10000003
         * toName :
         * toNickname : 🤔
         * balance : 29
         * statusTip : 待领取
         * status : 2
         * createTime : 2019-07-27 02:07:48
         * reciveTime :
         */

        private String cId;
        private String uid;
        private String name;
        private String nickName;
        private String toUid;
        private String toName;
        private String toNickname;
        private String balance;
        private String statusTip;
        private String status;
        private String createTime;
        private String reciveTime;

        public String getCId() {
            return cId;
        }

        public void setCId(String cId) {
            this.cId = cId;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getToUid() {
            return toUid;
        }

        public void setToUid(String toUid) {
            this.toUid = toUid;
        }

        public String getToName() {
            return toName;
        }

        public void setToName(String toName) {
            this.toName = toName;
        }

        public String getToNickname() {
            return toNickname;
        }

        public void setToNickname(String toNickname) {
            this.toNickname = toNickname;
        }

        public String getBalance() {
            return balance;
        }

        public void setBalance(String balance) {
            this.balance = balance;
        }

        public String getStatusTip() {
            return statusTip;
        }

        public void setStatusTip(String statusTip) {
            this.statusTip = statusTip;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getReciveTime() {
            return reciveTime;
        }

        public void setReciveTime(String reciveTime) {
            this.reciveTime = reciveTime;
        }
    }
}
