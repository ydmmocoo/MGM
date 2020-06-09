package com.library.repository.models;

import java.util.List;

public class QuestionReplyModel {
    /**
     * hasNext : false
     * replyList : [{"rId":"1","content":"123456789","userAvatar":"http://thirdwx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTI4YnuA2IFZZYwuhnCN2NqwRMNQ0ib42tZ2Pf8m7pib7nZj9jFibvQDsR7CNhqYdr86RdMxSiboq0zxZw/132","userNickName":"🙏","replyTime":"2019-09-01","rewardCount":"0","isAccpet":"2"}]
     */

    private boolean hasNext;
    private List<ReplyListBean> replyList;

    public boolean isHasNext() {
        return hasNext;
    }

    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }

    public List<ReplyListBean> getReplyList() {
        return replyList;
    }

    public void setReplyList(List<ReplyListBean> replyList) {
        this.replyList = replyList;
    }

    public static class ReplyListBean {
        /**
         * rId : 1
         * content : 123456789
         * userAvatar : http://thirdwx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTI4YnuA2IFZZYwuhnCN2NqwRMNQ0ib42tZ2Pf8m7pib7nZj9jFibvQDsR7CNhqYdr86RdMxSiboq0zxZw/132
         * userNickName : 🙏
         * replyTime : 2019-09-01
         * rewardCount : 0
         * isAccpet : 2
         */

        private int status = 1;
        private int positions = -1;
        private Boolean mine = false;
        private Boolean open = false;
        private String rId;
        private String uid;
        private String content;
        private String userAvatar;
        private String userNickName;
        private String replyTime;
        private String rewardCount;
        private String isAccpet;
        private List<String> imagesList;//附带图片列表

        public int getPositions() {
            return positions;
        }

        public void setPositions(int positions) {
            this.positions = positions;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public List<String> getImagesList() {
            return imagesList;
        }

        public void setImagesList(List<String> imagesList) {
            this.imagesList = imagesList;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public Boolean getMine() {
            return mine;
        }

        public void setMine(Boolean mine) {
            this.mine = mine;
        }

        public Boolean getOpen() {
            return open;
        }

        public void setOpen(Boolean open) {
            this.open = open;
        }

        public String getRId() {
            return rId;
        }

        public void setRId(String rId) {
            this.rId = rId;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getUserAvatar() {
            return userAvatar;
        }

        public void setUserAvatar(String userAvatar) {
            this.userAvatar = userAvatar;
        }

        public String getUserNickName() {
            return userNickName;
        }

        public void setUserNickName(String userNickName) {
            this.userNickName = userNickName;
        }

        public String getReplyTime() {
            return replyTime;
        }

        public void setReplyTime(String replyTime) {
            this.replyTime = replyTime;
        }

        public String getRewardCount() {
            return rewardCount;
        }

        public void setRewardCount(String rewardCount) {
            this.rewardCount = rewardCount;
        }

        public String getIsAccpet() {
            return isAccpet;
        }

        public void setIsAccpet(String isAccpet) {
            this.isAccpet = isAccpet;
        }
    }
}
