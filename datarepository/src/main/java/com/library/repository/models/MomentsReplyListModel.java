package com.library.repository.models;

import java.util.List;

public class MomentsReplyListModel {

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
        private String replyId;
        private String content;
        private String createTime;
        private String LikeNum;
        private String userIdfer;
        private String userNickName;
        private String userAvatar;
        private String toUserNick;
        private String toReplyCont;
        private String toUserIdfer;
        private String userId;
        private int flag;
        private String url;
        private String type;


        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getReplyId() {
            return replyId;
        }

        public void setReplyId(String replyId) {
            this.replyId = replyId;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getLikeNum() {
            return LikeNum;
        }

        public void setLikeNum(String LikeNum) {
            this.LikeNum = LikeNum;
        }

        public String getUserIdfer() {
            return userIdfer;
        }

        public void setUserIdfer(String userIdfer) {
            this.userIdfer = userIdfer;
        }

        public String getUserNickName() {
            return userNickName;
        }

        public void setUserNickName(String userNickName) {
            this.userNickName = userNickName;
        }

        public String getUserAvatar() {
            return userAvatar;
        }

        public void setUserAvatar(String userAvatar) {
            this.userAvatar = userAvatar;
        }

        public String getToUserNick() {
            return toUserNick;
        }

        public void setToUserNick(String toUserNick) {
            this.toUserNick = toUserNick;
        }

        public String getToReplyCont() {
            return toReplyCont;
        }

        public void setToReplyCont(String toReplyCont) {
            this.toReplyCont = toReplyCont;
        }

        public String getToUserIdfer() {
            return toUserIdfer;
        }

        public void setToUserIdfer(String toUserIdfer) {
            this.toUserIdfer = toUserIdfer;
        }

        public int getFlag() {
            return flag;
        }

        public void setFlag(int flag) {
            this.flag = flag;
        }
    }
}
