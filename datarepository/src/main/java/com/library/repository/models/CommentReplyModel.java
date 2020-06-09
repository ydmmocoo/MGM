package com.library.repository.models;

import java.util.List;

public class CommentReplyModel {


    /**
     * hasNext : false
     * replyList : [{"replyId":"4","content":"dog","createTime":"2019-05-08 20:37:29","LikeNum":"0","userId":"10","userNickName":"飞雨天","userAvatar":"","isLike":false,"toUserNick":"","toReplyCont":""}]
     */

    private boolean hasNext;
    private String replyNum;
    private List<ReplyListBean> replyList;

    public boolean isHasNext() {
        return hasNext;
    }

    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }

    public String getReplyNum() {
        return replyNum;
    }

    public void setReplyNum(String replyNum) {
        this.replyNum = replyNum;
    }

    public List<ReplyListBean> getReplyList() {
        return replyList;
    }

    public void setReplyList(List<ReplyListBean> replyList) {
        this.replyList = replyList;
    }

    public static class ReplyListBean {
        /**
         * replyId : 4
         * content : dog
         * createTime : 2019-05-08 20:37:29
         * LikeNum : 0
         * userId : 10
         * userNickName : 飞雨天
         * userAvatar :
         * isLike : false
         * toUserNick :
         * toReplyCont :
         */

        private String replyId;
        private String createTime;
        private String userId;
        private String content;
        private String userNickName;
        private String userAvatar;
        private boolean isLike;
        private String LikeNum;
        private String toUserNick;
        private String toReplyCont;

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

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
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

        public boolean isLike() {
            return isLike;
        }

        public void setLike(boolean like) {
            isLike = like;
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
    }
}
