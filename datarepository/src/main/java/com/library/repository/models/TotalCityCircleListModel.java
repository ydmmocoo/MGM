package com.library.repository.models;

import java.util.List;

/**
 * Author    by hanlz
 * Date      on 2019/10/17.
 * Description：获取同城列表
 */
public class TotalCityCircleListModel {
    private String cId;//同城记录id
    private String uId;//发布用户id
    private String uAvatar;//发布用户头像
    private String uNick;//发布用户昵称
    private String content;//内容
    private String typeName;//分类名称
    private String typeId;//分类id
    private List<String> images;//同城配图
    private int readNum;//浏览次数
    private String commentNum;//评论次数
    private String likeNum;//点赞次数
    private String createTime;//	时间
    private boolean isTop;//是否置顶，true：是，false：否
    private boolean isLike;//是否点过赞
    private String uPhone;//用户手机号码
    private List<String> thumImages;//同城配图的缩略图
    private String typeImg;//分类背景图

    public String getcId() {
        return cId;
    }

    public void setcId(String cId) {
        this.cId = cId;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getuAvatar() {
        return uAvatar;
    }

    public void setuAvatar(String uAvatar) {
        this.uAvatar = uAvatar;
    }

    public String getuNick() {
        return uNick;
    }

    public void setuNick(String uNick) {
        this.uNick = uNick;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public int getReadNum() {
        return readNum;
    }

    public void setReadNum(int readNum) {
        this.readNum = readNum;
    }

    public boolean isTop() {
        return isTop;
    }

    public void setTop(boolean top) {
        isTop = top;
    }

    public String getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(String commentNum) {
        this.commentNum = commentNum;
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


    public boolean isLike() {
        return isLike;
    }

    public void setLike(boolean like) {
        isLike = like;
    }

    public String getuPhone() {
        return uPhone;
    }

    public void setuPhone(String uPhone) {
        this.uPhone = uPhone;
    }

    public List<String> getThumImages() {
        return thumImages;
    }

    public void setThumImages(List<String> thumImages) {
        this.thumImages = thumImages;
    }

    public String getTypeImg() {
        return typeImg;
    }

    public void setTypeImg(String typeImg) {
        this.typeImg = typeImg;
    }
}
