package com.library.repository.models;

import java.util.List;

/**
 * @author yedeman
 * @date 2020/6/18.
 * email：ydmmocoo@gmail.com
 * description：
 */
public class CheckGoodsBean {

    private List<ErrorsBean> errors;

    public List<ErrorsBean> getErrors() {
        return errors;
    }

    public void setErrors(List<ErrorsBean> errors) {
        this.errors = errors;
    }

    public static class ErrorsBean {
        /**
         * gId : 29
         * tip : 该规格库存不足,剩余-1件
         * gName : 树莓慕斯蛋糕
         */

        private String gId;
        private String tip;
        private String gName;

        public String getGId() {
            return gId;
        }

        public void setGId(String gId) {
            this.gId = gId;
        }

        public String getTip() {
            return tip;
        }

        public void setTip(String tip) {
            this.tip = tip;
        }

        public String getGName() {
            return gName;
        }

        public void setGName(String gName) {
            this.gName = gName;
        }
    }
}
