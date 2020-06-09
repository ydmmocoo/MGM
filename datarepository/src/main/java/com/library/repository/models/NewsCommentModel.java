package com.library.repository.models;

import android.text.TextUtils;

import java.util.List;

public class NewsCommentModel {

    /**
     * commentList : [{"comentId":"10","content":"dog","likeNum":"0","createTime":"05-08 16:43:34","replyNum":"0","userId":"10","userNickName":"飞雨天","userAvatar":""},{"comentId":"9","content":"dog","likeNum":"0","createTime":"05-08 16:43:30","replyNum":"0","userId":"10","userNickName":"飞雨天","userAvatar":""}]
     * hasNext : true
     */

    private boolean hasNext;
    private String commentNum;
    private List<CommentListBean> commentList;

    public boolean isHasNext() {
        return hasNext;
    }

    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }

    public List<CommentListBean> getCommentList() {
        return commentList;
    }

    public String getCommentNum() {
        return TextUtils.isEmpty(commentNum) ? "0" : commentNum;
    }

    public void setCommentNum(String commentNum) {
        this.commentNum = commentNum;
    }

    public void setCommentList(List<CommentListBean> commentList) {
        this.commentList = commentList;
    }

    public static class CommentListBean {
        /**
         * comentId : 10
         * content : dog
         * likeNum : 0
         * createTime : 05-08 16:43:34
         * replyNum : 0
         * userId : 10
         * userNickName : 飞雨天
         * userAvatar :
         */

        private String commentId;
        private String content;
        private String likeNum;
        private String createTime;
        private String replyNum;
        private String userId;
        private String userNickName;
        private String userAvatar;
        private boolean isLike;

        public boolean isLike() {
            return isLike;
        }

        public void setLike(boolean like) {
            isLike = like;
        }

        public String getComentId() {
            return commentId;
        }

        public void setComentId(String comentId) {
            this.commentId = comentId;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getLikeNum() {
            return likeNum;
        }

        public void setLikeNum(String likeNum) {
            this.likeNum = likeNum;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getReplyNum() {
            return replyNum;
        }

        public void setReplyNum(String replyNum) {
            this.replyNum = replyNum;
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
    }
}
