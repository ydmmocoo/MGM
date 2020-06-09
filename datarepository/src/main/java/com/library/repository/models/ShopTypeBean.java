package com.library.repository.models;

import java.util.List;

/**
 * @author yedeman
 * @date 2020/5/20.
 * email：ydmmocoo@gmail.com
 * description：
 */
public class ShopTypeBean {

    private List<ShopTypeListBean> shopTypeList;

    public List<ShopTypeListBean> getShopTypeList() {
        return shopTypeList;
    }

    public void setShopTypeList(List<ShopTypeListBean> shopTypeList) {
        this.shopTypeList = shopTypeList;
    }

    public static class ShopTypeListBean {
        /**
         * cId : 1
         * name : 美食
         * img :
         * secondList : [{"secondId":"8","secondName":"火锅","img":""},{"secondId":"9","secondName":"川湘菜","img":""}]
         */

        private String cId;
        private String name;
        private String img;
        private String secondId;
        private List<SecondListBean> secondList;

        public String getCId() {
            return cId;
        }

        public void setCId(String cId) {
            this.cId = cId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getSecondId() {
            return secondId;
        }

        public void setSecondId(String secondId) {
            this.secondId = secondId;
        }

        public List<SecondListBean> getSecondList() {
            return secondList;
        }

        public void setSecondList(List<SecondListBean> secondList) {
            this.secondList = secondList;
        }

        public static class SecondListBean {
            /**
             * secondId : 8
             * secondName : 火锅
             * img :
             */

            private String secondId;
            private String secondName;
            private String img;

            public String getSecondId() {
                return secondId;
            }

            public void setSecondId(String secondId) {
                this.secondId = secondId;
            }

            public String getSecondName() {
                return secondName;
            }

            public void setSecondName(String secondName) {
                this.secondName = secondName;
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
