package com.library.repository.models;

import java.util.List;

public class MomentsInfoModel {

    private MomentsInfoBean momentsInfo;
    private UserInfoBean userInfo;
    private ReplyListBean replyList;
    private List<PraiseListBean> praiseList;
    private List<String> typeList;

    public MomentsInfoBean getMomentsInfo() {
        return momentsInfo;
    }

    public void setMomentsInfo(MomentsInfoBean momentsInfo) {
        this.momentsInfo = momentsInfo;
    }

    public UserInfoBean getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfoBean userInfo) {
        this.userInfo = userInfo;
    }

    public ReplyListBean getReplyList() {
        return replyList;
    }

    public void setReplyList(ReplyListBean replyList) {
        this.replyList = replyList;
    }

    public List<PraiseListBean> getPraiseList() {
        return praiseList;
    }

    public void setPraiseList(List<PraiseListBean> praiseList) {
        this.praiseList = praiseList;
    }

    public List<String> getTypeList() {
        return typeList;
    }

    public void setTypeList(List<String> typeList) {
        this.typeList = typeList;
    }

    public static class MomentsInfoBean {

        private String mId;
        private String content;
        private String createTime;
        private String address;
        private String distance;
        private String distanceUnit;
        private List<String> urls;

        public String getMId() {
            return mId;
        }

        public void setMId(String mId) {
            this.mId = mId;
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

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getDistance() {
            return distance;
        }

        public void setDistance(String distance) {
            this.distance = distance;
        }

        public String getDistanceUnit() {
            return distanceUnit;
        }

        public void setDistanceUnit(String distanceUnit) {
            this.distanceUnit = distanceUnit;
        }

        public List<String> getUrls() {
            return urls;
        }

        public void setUrls(List<String> urls) {
            this.urls = urls;
        }
    }

    public static class UserInfoBean {

        private String userIdfer;
        private String userNickName;
        private String userAvatar;
        private String sex;
        private List<String> tags;

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

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public List<String> getTags() {
            return tags;
        }

        public void setTags(List<String> tags) {
            this.tags = tags;
        }
    }

    public static class ReplyListBean {

        private boolean hasNext;
        private List<ListBean> list;

        public boolean isHasNext() {
            return hasNext;
        }

        public void setHasNext(boolean hasNext) {
            this.hasNext = hasNext;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }
    }

    public static class ListBean {
        private String replyId;
        private String content;
        private String createTime;
        private String LikeNum;
        private String userIdfer;
        private String userNickName;
        private String userAvatar;
        private String toUserNick;
        private String toReplyCont;
        private int flag;

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

        public void setLikeNum(String likeNum) {
            LikeNum = likeNum;
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

        public int getFlag() {
            return flag;
        }

        public void setFlag(int flag) {
            this.flag = flag;
        }
    }

    public static class PraiseListBean {

        private String pId;
        private String userIdfer;
        private String userNickName;
        private String userAvatar;
        private Boolean last = false;

        public Boolean getLast() {
            return last;
        }

        public void setLast(Boolean last) {
            this.last = last;
        }

        public String getpId() {
            return pId;
        }

        public void setpId(String pId) {
            this.pId = pId;
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
    }
}
