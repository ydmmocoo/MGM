package com.library.repository.models;

import java.util.List;

/**
 * @author yedeman
 * @date 2020/5/26.
 * email：ydmmocoo@gmail.com
 * description：
 */
public class StoreGoodsBean {

    private List<CateListBean> cateList;

    public List<CateListBean> getCateList() {
        return cateList;
    }

    public void setCateList(List<CateListBean> cateList) {
        this.cateList = cateList;
    }

    public static class CateListBean {
        /**
         * cateName : 热卖
         * goodsList : [{"gId":"13","gImg":"http://139.9.38.218:8080/Uploads/image/2020-05-24/5eca73e882ada.png","gName":"666","saleCount":0,"price":"666","specialCount":1,"minBuyNum":"1","specialList":[{"sId":"18","name":"666","currentNum":"666","price":"666"}],"attrList":[{"gamId":"32","name":"312312","optList":[{"aId":"64","name":"312331"},{"aId":"65","name":"312321"},{"aId":"66","name":"31231"},{"aId":"67","name":"31231"}]}]},{"gId":"7","gImg":"http://139.9.38.218:8080/Uploads/image/2020-05-22/5ec738972d9db.png","gName":"1312312312312","saleCount":0,"price":"1","specialCount":1,"minBuyNum":"1","specialList":[{"sId":"14","name":"1","currentNum":"1","price":"1"}],"attrList":[{"gamId":"29","name":"1111111111111","optList":[{"aId":"57","name":"5345"},{"aId":"58","name":"111"}]},{"gamId":"30","name":"1323123131231231231231231231231","optList":[{"aId":"60","name":"312321"},{"aId":"59","name":"1231231231"}]},{"gamId":"31","name":"12fff222","optList":[{"aId":"63","name":"31231312"},{"aId":"62","name":"3123123"},{"aId":"61","name":"13123"}]}]},{"gId":"4","gImg":"http://139.9.38.218:8080http://139.9.38.218:8080/Uploads/image/2020-05-21/5ec63a7801086.png","gName":"t","saleCount":0,"price":"123","specialCount":1,"minBuyNum":"113","specialList":[{"sId":"11","name":"dafsdfsdfas","currentNum":"2147483647","price":"123"}],"attrList":[]},{"gId":"3","gImg":"http://139.9.38.218:8080/Uploads/image/2020-05-21/5ec6399136f2a.png","gName":"q","saleCount":0,"price":"1","specialCount":1,"minBuyNum":"2","specialList":[{"sId":"10","name":"tt","currentNum":"11","price":"1"}],"attrList":[]},{"gId":"1","gImg":"http://139.9.38.218:8080http://139.9.38.218:8080http://139.9.38.218:8080http://n.sinaimg.cn/photo/transform/700/w1000h500/20200511/fe11-itmiwry9590007.jpg","gName":"3232","saleCount":0,"price":"2","specialCount":4,"minBuyNum":"232312","specialList":[{"sId":"4","name":"11","currentNum":"3","price":"2"},{"sId":"3","name":"3121","currentNum":"31","price":"31"},{"sId":"1","name":"312","currentNum":"312","price":"312"},{"sId":"5","name":"3123123","currentNum":"312","price":"312312"}],"attrList":[]}]
         */

        private String cateName;
        private List<GoodsListBean> goodsList;

        public String getCateName() {
            return cateName;
        }

        public void setCateName(String cateName) {
            this.cateName = cateName;
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
             * gImg : http://139.9.38.218:8080/Uploads/image/2020-05-24/5eca73e882ada.png
             * gName : 666
             * saleCount : 0
             * price : 666
             * specialCount : 1
             * minBuyNum : 1
             * specialList : [{"sId":"18","name":"666","currentNum":"666","price":"666"}]
             * attrList : [{"gamId":"32","name":"312312","optList":[{"aId":"64","name":"312331"},{"aId":"65","name":"312321"},{"aId":"66","name":"31231"},{"aId":"67","name":"31231"}]}]
             */

            private long groupId;
            private String groupName;
            private long count;
            private String gId;
            private String gImg;
            private String gName;
            private long saleCount;
            private String price;
            private int specialCount;
            private String minBuyNum;
            private List<SpecialListBean> specialList;
            private List<AttrListBean> attrList;

            public long getGroupId() {
                return groupId;
            }

            public void setGroupId(long groupId) {
                this.groupId = groupId;
            }

            public String getGroupName() {
                return groupName;
            }

            public void setGroupName(String groupName) {
                this.groupName = groupName;
            }

            public long getCount() {
                return count;
            }

            public void setCount(long count) {
                this.count = count;
            }

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

            public long getSaleCount() {
                return saleCount;
            }

            public void setSaleCount(long saleCount) {
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
                 * sId : 18
                 * name : 666
                 * currentNum : 666
                 * price : 666
                 */

                private String sId;
                private String name;
                private String currentNum;
                private String price;
                private boolean isSelected;

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

                public boolean isSelected() {
                    return isSelected;
                }

                public void setSelected(boolean selected) {
                    isSelected = selected;
                }
            }

            public static class AttrListBean {
                /**
                 * gamId : 32
                 * name : 312312
                 * optList : [{"aId":"64","name":"312331"},{"aId":"65","name":"312321"},{"aId":"66","name":"31231"},{"aId":"67","name":"31231"}]
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
                     * aId : 64
                     * name : 312331
                     */

                    private String aId;
                    private String name;
                    private boolean isSelected;

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

                    public boolean isSelected() {
                        return isSelected;
                    }

                    public void setSelected(boolean selected) {
                        isSelected = selected;
                    }
                }
            }
        }
    }
}
