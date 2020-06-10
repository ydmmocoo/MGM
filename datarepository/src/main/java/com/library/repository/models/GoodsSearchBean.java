package com.library.repository.models;

import java.util.List;

/**
 * @author yedeman
 * @date 2020/6/9.
 * email：ydmmocoo@gmail.com
 * description：
 */
public class GoodsSearchBean {

    /**
     * hasNext : false
     * goodsList : [{"gId":"14","gImg":"http://139.9.38.218:8080/Uploads/image/2020-06-09/5edef67c0cc51.png","gName":"巧克力水果蛋糕","saleCount":0,"price":"2000","specialCount":2,"minBuyNum":"1","specialList":[{"sId":"20","name":"12寸","currentNum":"100","price":"2000"},{"sId":"21","name":"9寸","currentNum":"100","price":"2000"}],"attrList":[]},{"attrList":[],"gId":"13","gImg":"http://139.9.38.218:8080/Uploads/image/2020-06-09/5edef64e564a7.png","gName":"方格水果蛋糕","saleCount":0,"price":"2000","specialCount":1,"minBuyNum":"1","specialList":[{"sId":"19","name":"6寸","currentNum":"100","price":"2000"}]},{"gId":"12","gImg":"http://139.9.38.218:8080/Uploads/image/2020-06-09/5edef6283d201.png","gName":"缤纷水果蛋糕","saleCount":0,"price":"2000","specialCount":3,"minBuyNum":"1","specialList":[{"sId":"16","name":"6寸","currentNum":"100","price":"2000"},{"sId":"17","name":"8寸","currentNum":"100","price":"3000"},{"sId":"18","name":"12寸","currentNum":"100","price":"5000"}],"attrList":[]},{"gId":"11","gImg":"http://139.9.38.218:8080/Uploads/image/2020-06-09/5edef5669a4cf.png","gName":"荔枝杨梅慕斯蛋糕","saleCount":0,"price":"10000","specialCount":1,"minBuyNum":"1","specialList":[{"sId":"15","name":"6寸","currentNum":"100","price":"10000"}]},{"gId":"10","gImg":"http://139.9.38.218:8080/Uploads/image/2020-06-09/5edef51aad04a.png","gName":"草莓慕斯蛋糕","saleCount":0,"price":"2000","specialCount":1,"minBuyNum":"1","specialList":[{"sId":"14","name":"小份","currentNum":"100","price":"2000"}]},{"gId":"9","gImg":"http://139.9.38.218:8080/Uploads/image/2020-06-09/5edef4f246f02.png","gName":"芒果慕斯蛋糕","saleCount":0,"price":"1000","specialCount":1,"minBuyNum":"1","specialList":[{"sId":"13","name":"三角块","currentNum":"100","price":"1000"}]}]
     * cateList : [{"cId":"5","cateName":"千层蛋糕"},{"cId":"6","cateName":"慕斯蛋糕"},{"cId":"7","cateName":"水果蛋糕"},{"cId":"8","cateName":"创意蛋糕"}]
     */

    private boolean hasNext;
    private List<GoodsListBean> goodsList;
    private List<CateListBean> cateList;

    public boolean isHasNext() {
        return hasNext;
    }

    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }

    public List<GoodsListBean> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<GoodsListBean> goodsList) {
        this.goodsList = goodsList;
    }

    public List<CateListBean> getCateList() {
        return cateList;
    }

    public void setCateList(List<CateListBean> cateList) {
        this.cateList = cateList;
    }

    public static class GoodsListBean {
        /**
         * gId : 14
         * gImg : http://139.9.38.218:8080/Uploads/image/2020-06-09/5edef67c0cc51.png
         * gName : 巧克力水果蛋糕
         * saleCount : 0
         * price : 2000
         * specialCount : 2
         * minBuyNum : 1
         * specialList : [{"sId":"20","name":"12寸","currentNum":"100","price":"2000"},{"sId":"21","name":"9寸","currentNum":"100","price":"2000"}]
         * attrList : []
         */

        private String gId;
        private String gImg;
        private String gName;
        private int saleCount;
        private String price;
        private int specialCount;
        private String minBuyNum;
        private int count;
        private List<SpecialListBean> specialList;
        private List<AttrListBean> attrList;

        public String getGId() {
            return gId;
        }

        public void setGId(String gId) {
            this.gId = gId;
        }

        public String getGImg() {
            return gImg;
        }

        public void setGImg(String gImg) {
            this.gImg = gImg;
        }

        public String getGName() {
            return gName;
        }

        public void setGName(String gName) {
            this.gName = gName;
        }

        public int getSaleCount() {
            return saleCount;
        }

        public void setSaleCount(int saleCount) {
            this.saleCount = saleCount;
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

        public String getMinBuyNum() {
            return minBuyNum;
        }

        public void setMinBuyNum(String minBuyNum) {
            this.minBuyNum = minBuyNum;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
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

        public static class SpecialListBean {
            /**
             * sId : 20
             * name : 12寸
             * currentNum : 100
             * price : 2000
             */

            private String sId;
            private String name;
            private String currentNum;
            private String price;

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

            public String getCurrentNum() {
                return currentNum;
            }

            public void setCurrentNum(String currentNum) {
                this.currentNum = currentNum;
            }

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
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
            private List<GoodsDetailBean.GoodInfoBean.AttrListBean.OptListBean> optList;

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

            public List<GoodsDetailBean.GoodInfoBean.AttrListBean.OptListBean> getOptList() {
                return optList;
            }

            public void setOptList(List<GoodsDetailBean.GoodInfoBean.AttrListBean.OptListBean> optList) {
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

    public static class CateListBean {
        /**
         * cId : 5
         * cateName : 千层蛋糕
         */

        private String cId;
        private String cateName;

        public String getCId() {
            return cId;
        }

        public void setCId(String cId) {
            this.cId = cId;
        }

        public String getCateName() {
            return cateName;
        }

        public void setCateName(String cateName) {
            this.cateName = cateName;
        }
    }
}
