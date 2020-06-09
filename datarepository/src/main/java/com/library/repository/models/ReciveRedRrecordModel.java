package com.library.repository.models;

import java.util.List;

/**
 * Author    by hanlz
 * Date      on 2019/12/8.
 * Description：
 */
public class ReciveRedRrecordModel {

    private List<ReciveListBean> reciveList;
    private boolean hasNext;

    public List<ReciveListBean> getReciveList() {
        return reciveList;
    }

    public void setReciveList(List<ReciveListBean> reciveList) {
        this.reciveList = reciveList;
    }

    public boolean isHasNext() {
        return hasNext;
    }

    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }

    public class ReciveListBean {
        private String rrId;//记录id
        private String price;//领取金额
        private String createTime;//时间
        private String avatar;//头像
        private String nickName;//昵称
        private String isTop;//	1：最佳

        public String getRrId() {
            return rrId;
        }

        public void setRrId(String rrId) {
            this.rrId = rrId;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getIsTop() {
            return isTop;
        }

        public void setIsTop(String isTop) {
            this.isTop = isTop;
        }
    }
}
