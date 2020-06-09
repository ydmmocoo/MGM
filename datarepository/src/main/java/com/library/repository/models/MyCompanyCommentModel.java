package com.library.repository.models;

import java.util.List;

public class MyCompanyCommentModel {


    /**
     * hasNext : false
     * userInfo : {"uImg":"http://192.168.3.12http://thirdwx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTJ7M2qYDvDVTYNIqkgN15J2NqicwKc5GcND1VxiboiayBDfpqGJKDMdq97K1ZsJjM68nZW5oN7u51CVg/132","nickName":"KOKXj+KAlOKXjyk="}
     * commentList : [{"commentId":"70","houseId":"11","houseTitle":"我要出租","content":"好好好","authNickName":"KOKXj+KAlOKXjyk=","authId":"19","createTime":"05月28日 14:15:35"},{"commentId":"69","houseId":"11","houseTitle":"我要出租","content":"中","authNickName":"KOKXj+KAlOKXjyk=","authId":"19","createTime":"05月28日 13:49:45"}]
     */

    private boolean hasNext;
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

    public void setCommentList(List<CommentListBean> commentList) {
        this.commentList = commentList;
    }


    public static class CommentListBean {
        /**
         * commentId : 64
         * newsId : 9
         * newsTitle : test3
         * likeNum : 0
         * content : m
         * authNickName : 头条
         * authId : 2
         * createTime : 05月24日 10:31:00
         */

        private String commentId;
        private String newsId;
        private String cId;
        private String newsTitle;
        private String likeNum;
        private String content;
        private String authNickName;
        private String authId;
        private String createTime;
        private String replyNum;

        public String getcId() {
            return cId;
        }

        public void setcId(String cId) {
            this.cId = cId;
        }

        public String getCommentId() {
            return commentId;
        }

        public void setCommentId(String commentId) {
            this.commentId = commentId;
        }

        public String getNewsId() {
            return newsId;
        }

        public String getReplyNum() {
            return replyNum;
        }

        public void setReplyNum(String replyNum) {
            this.replyNum = replyNum;
        }

        public void setNewsId(String newsId) {
            this.newsId = newsId;
        }

        public String getNewsTitle() {
            return newsTitle;
        }

        public void setNewsTitle(String newsTitle) {
            this.newsTitle = newsTitle;
        }

        public String getLikeNum() {
            return likeNum;
        }

        public void setLikeNum(String likeNum) {
            this.likeNum = likeNum;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getAuthNickName() {
            return authNickName;
        }

        public void setAuthNickName(String authNickName) {
            this.authNickName = authNickName;
        }

        public String getAuthId() {
            return authId;
        }

        public void setAuthId(String authId) {
            this.authId = authId;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }
    }
}
