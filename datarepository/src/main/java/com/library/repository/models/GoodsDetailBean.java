package com.library.repository.models;

import java.util.List;

/**
 * @author yedeman
 * @date 2020/6/1.
 * email：ydmmocoo@gmail.com
 * description：
 */
public class GoodsDetailBean {

    /**
     * goodInfo : {"gId":"15","name":"132","img":"http://139.9.38.218:8080/Uploads/image/2020-05-31/5ed3a6207d9ad.png","cId":"3","desc":"132312\n3\n123\n123\n12\n312312312","minBuyNum":"1","unit":"he ","price":"11","specialCount":1,"saleCount":0,"goodImgList":[{"img":"http://139.9.38.218:8080/Uploads/image/2020-05-31/5ed3a6207dd57.png"}],"specialList":[{"sId":"21","name":"1","price":"11","currentNum":"104","maxNum":"11111111"}],"attrList":[{"gamId":"37","name":"3211","optList":[{"aId":"76","name":"123"},{"aId":"77","name":"12"}]},{"gamId":"38","name":"dead","optList":[{"aId":"78","name":"fsdsd"},{"aId":"79","name":"sdfdsfasfa"}]}]}
     */

    private GoodInfoBean goodInfo;

    public GoodInfoBean getGoodInfo() {
        return goodInfo;
    }

    public void setGoodInfo(GoodInfoBean goodInfo) {
        this.goodInfo = goodInfo;
    }

    public static class GoodInfoBean {
        /**
         * gId : 15
         * name : 132
         * img : http://139.9.38.218:8080/Uploads/image/2020-05-31/5ed3a6207d9ad.png
         * cId : 3
         * desc : 132312
         3
         123
         123
         12
         312312312
         * minBuyNum : 1
         * unit : he
         * price : 11
         * specialCount : 1
         * saleCount : 0
         * goodImgList : [{"img":"http://139.9.38.218:8080/Uploads/image/2020-05-31/5ed3a6207dd57.png"}]
         * specialList : [{"sId":"21","name":"1","price":"11","currentNum":"104","maxNum":"11111111"}]
         * attrList : [{"gamId":"37","name":"3211","optList":[{"aId":"76","name":"123"},{"aId":"77","name":"12"}]},{"gamId":"38","name":"dead","optList":[{"aId":"78","name":"fsdsd"},{"aId":"79","name":"sdfdsfasfa"}]}]
         */

        private String gId;
        private String name;
        private String img;
        private String cId;
        private String desc;
        private String minBuyNum;
        private String unit;
        private String price;
        private int specialCount;
        private int saleCount;
        private long count;
        private List<GoodImgListBean> goodImgList;
        private List<SpecialListBean> specialList;
        private List<AttrListBean> attrList;

        public String getGId() {
            return gId;
        }

        public void setGId(String gId) {
            this.gId = gId;
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

        public String getCId() {
            return cId;
        }

        public void setCId(String cId) {
            this.cId = cId;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getMinBuyNum() {
            return minBuyNum;
        }

        public void setMinBuyNum(String minBuyNum) {
            this.minBuyNum = minBuyNum;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public int getSpecialCount() {
            return specialCount;
        }

        public void setSpecialCount(int specialCount) {
            this.specialCount = specialCount;
        }

        public int getSaleCount() {
            return saleCount;
        }

        public void setSaleCount(int saleCount) {
            this.saleCount = saleCount;
        }

        public long getCount() {
            return count;
        }

        public void setCount(long count) {
            this.count = count;
        }

        public List<GoodImgListBean> getGoodImgList() {
            return goodImgList;
        }

        public void setGoodImgList(List<GoodImgListBean> goodImgList) {
            this.goodImgList = goodImgList;
        }

        public List<SpecialListBean> getSpecialList() {
            return specialList;
        }

        public void setSpecialList(List<SpecialListBean> specialList) {
            this.specialList = specialList;
        }

        public List<AttrListBean> getAttrList() {
            return attrList;
        }

        public void setAttrList(List<AttrListBean> attrList) {
            this.attrList = attrList;
        }

        public static class GoodImgListBean {
            /**
             * img : http://139.9.38.218:8080/Uploads/image/2020-05-31/5ed3a6207dd57.png
             */

            private String img;

            public String getImg() {
                return img;
            }

            public void setImg(String img) {
                this.img = img;
            }
        }

        public static class SpecialListBean {
            /**
             * sId : 21
             * name : 1
             * price : 11
             * currentNum : 104
             * maxNum : 11111111
             */

            private String sId;
            private String name;
            private String price;
            private String currentNum;
            private String maxNum;

            public String getSId() {
                return sId;
            }

            public void setSId(String sId) {
                this.sId = sId;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }

            public String getCurrentNum() {
                return currentNum;
            }

            public void setCurrentNum(String currentNum) {
                this.currentNum = currentNum;
            }

            public String getMaxNum() {
                return maxNum;
            }

            public void setMaxNum(String maxNum) {
                this.maxNum = maxNum;
            }
        }

        public static class AttrListBean {
            /**
             * gamId : 37
             * name : 3211
             * optList : [{"aId":"76","name":"123"},{"aId":"77","name":"12"}]
             */

            private String gamId;
            private String name;
            private List<OptListBean> optList;

            public String getGamId() {
                return gamId;
            }

            public void setGamId(String gamId) {
                this.gamId = gamId;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public List<OptListBean> getOptList() {
                return optList;
            }

            public void setOptList(List<OptListBean> optList) {
                this.optList = optList;
            }

            public static class OptListBean {
                /**
                 * aId : 76
                 * name : 123
                 */

                private String aId;
                private String name;

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
        }
    }
}
