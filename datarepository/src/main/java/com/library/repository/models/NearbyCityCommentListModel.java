package com.library.repository.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Author    by hanlz
 * Date      on 2019/10/19.
 * Description：
 */
public class NearbyCityCommentListModel implements Parcelable {

    private String commentId;//评论记录id
    private String content;//内容
    private String likeNum;//点赞数量
    private String createTime;//	创建时间
    private String replyNum;//	回复数
    private String userNickName;//	评论人昵称
    private String userAvatar;//	评论人头像
    private String userId;//	评论人id
    private boolean isLike;//是否点过赞

    public NearbyCityCommentListModel() {
    }

    public NearbyCityCommentListModel(String commentId, String content, String likeNum, String createTime, String replyNum, String userNickName, String userAvatar, String userId, boolean isLike) {
        this.commentId = commentId;
        this.content = content;
        this.likeNum = likeNum;
        this.createTime = createTime;
        this.replyNum = replyNum;
        this.userNickName = userNickName;
        this.userAvatar = userAvatar;
        this.userId = userId;
        this.isLike = isLike;
    }

    protected NearbyCityCommentListModel(Parcel in) {
        commentId = in.readString();
        content = in.readString();
        likeNum = in.readString();
        createTime = in.readString();
        replyNum = in.readString();
        userNickName = in.readString();
        userAvatar = in.readString();
        userId = in.readString();
        isLike = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(commentId);
        dest.writeString(content);
        dest.writeString(likeNum);
        dest.writeString(createTime);
        dest.writeString(replyNum);
        dest.writeString(userNickName);
        dest.writeString(userAvatar);
        dest.writeString(userId);
        dest.writeByte((byte) (isLike ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<NearbyCityCommentListModel> CREATOR = new Creator<NearbyCityCommentListModel>() {
        @Override
        public NearbyCityCommentListModel createFromParcel(Parcel in) {
            return new NearbyCityCommentListModel(in);
        }

        @Override
        public NearbyCityCommentListModel[] newArray(int size) {
            return new NearbyCityCommentListModel[size];
        }
    };

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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isLike() {
        return isLike;
    }

    public void setLike(boolean like) {
        isLike = like;
    }
}
