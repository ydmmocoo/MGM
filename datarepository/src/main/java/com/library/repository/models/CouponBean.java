package com.library.repository.models;

import java.util.List;

/**
 * @author yedeman
 * @date 2020/5/29.
 * email：ydmmocoo@gmail.com
 * description：
 */
public class CouponBean {

    /**
     * hasNext : false
     * couponList : [{"cId":"4","typeName":"外卖通用","price":"20","fullPrice":"100","validDate":"2020-05-29","status":3,"isCanUse":false,"remark":"商品现价需满100AR"},{"cId":"5","typeName":"外卖通用","price":"40","fullPrice":"260","validDate":"2020-05-29","status":3,"isCanUse":false,"remark":"商品现价需满260AR"},{"cId":"6","typeName":"外卖通用","price":"50","fullPrice":"300","validDate":"2020-05-29","status":3,"isCanUse":false,"remark":"商品现价需满300AR"}]
     */

    private boolean hasNext;
    private List<CouponListBean> couponList;

    public boolean isHasNext() {
        return hasNext;
    }

    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }

    public List<CouponListBean> getCouponList() {
        return couponList;
    }

    public void setCouponList(List<CouponListBean> couponList) {
        this.couponList = couponList;
    }

    public static class CouponListBean {
        /**
         * cId : 4
         * typeName : 外卖通用
         * price : 20
         * fullPrice : 100
         * validDate : 2020-05-29
         * status : 3
         * isCanUse : false
         * remark : 商品现价需满100AR
         */

        private String cId;
        private String typeName;
        private String price;
        private String fullPrice;
        private String validDate;
        private int status;
        private boolean isCanUse;
        private String remark;

        public String getCId() {
            return cId;
        }

        public void setCId(String cId) {
            this.cId = cId;
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

        public String getFullPrice() {
            return fullPrice;
        }

        public void setFullPrice(String fullPrice) {
            this.fullPrice = fullPrice;
        }

        public String getValidDate() {
            return validDate;
        }

        public void setValidDate(String validDate) {
            this.validDate = validDate;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public boolean isIsCanUse() {
            return isCanUse;
        }

        public void setIsCanUse(boolean isCanUse) {
            this.isCanUse = isCanUse;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }
    }
}
