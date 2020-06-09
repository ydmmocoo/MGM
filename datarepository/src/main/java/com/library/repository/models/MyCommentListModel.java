package com.library.repository.models;

/**
 * Author    by hanlz
 * Date      on 2019/10/23.
 * Description：
 */
public class MyCommentListModel {
    private String commentId;//评论记录id
    private String cId;//记录id
    private String title;//新闻标题
    private String likeNum;//	点赞数量
    private String content;//评论内容
    private String authNickName;//作者昵称
    private String authId;//作者id
    private String createTime;//评论时间
    private String replyNum;//回复数
    private String typeName;//	分类名称
    private String typeId;//分类id
    private String typeImg;//分类图片

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getcId() {
        return cId;
    }

    public void setcId(String cId) {
        this.cId = cId;
    }

    public String getNewsTitle() {
        return title;
    }

    public void setNewsTitle(String newsTitle) {
        this.title = newsTitle;
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

    public String getReplyNum() {
        return replyNum;
    }

    public void setReplyNum(String replyNum) {
        this.replyNum = replyNum;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getTypeImg() {
        return typeImg;
    }

    public void setTypeImg(String typeImg) {
        this.typeImg = typeImg;
    }
}
