package com.library.repository.models;

import java.util.List;

public class MyReplyListCommentModel {

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
        private int positions = -1;
        private String rId;
        private String content;
        private String userAvatar;
        private String userNickName;
        private String replyTime;
        private String rewardCount;
        private String rewardPrice;
        private String qId;
        private String status;
        private String question;
        private Boolean open = false;
        private List<String> imagesList;//附带图片列表

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public int getPositions() {
            return positions;
        }

        public void setPositions(int positions) {
            this.positions = positions;
        }

        public List<String> getImagesList() {
            return imagesList;
        }

        public void setImagesList(List<String> imagesList) {
            this.imagesList = imagesList;
        }

        public Boolean getOpen() {
            return open;
        }

        public void setOpen(Boolean open) {
            this.open = open;
        }

        public String getrId() {
            return rId;
        }

        public void setrId(String rId) {
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

        public String getRewardPrice() {
            return rewardPrice;
        }

        public void setRewardPrice(String rewardPrice) {
            this.rewardPrice = rewardPrice;
        }

        public String getqId() {
            return qId;
        }

        public void setqId(String qId) {
            this.qId = qId;
        }

        public String getQuestion() {
            return question;
        }

        public void setQuestion(String question) {
            this.question = question;
        }
    }
}
