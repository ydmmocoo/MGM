package com.library.repository.models;

import java.util.List;

/**
 * Author    by hanlz
 * Date      on 2019/10/18.
 * Description：
 */
public class NearbyCityDetailModel {

    private String cId;//记录id
    private String uId;//用户id
    private String uPhone;//用户电话
    private String uAvatar;//用户头像
    private String uNick;//用户昵称
    private String content;//内容
    private String typeName;//类型
    private String typeI;//	类型id
    private List<String> images;//配图
    private String readNum;//浏览次数
    private String commentNum;//	评论次数
    private String likeNum;//点赞数
    private String createTime;//时间
    private String isTop;//是否置顶
    private boolean isLike;//是否点过赞，true是，false否
    private String exp;//过期时间
    private String topPerPirce;//置顶单价
    private String topDay;//置顶天数
    private String tI;//置顶记录id,0表示无置顶记录
    private String remainTopTime;//置顶剩余的时间
    private String remainDays;//置顶剩余的天数

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

    public String getuPhone() {
        return uPhone;
    }

    public void setuPhone(String uPhone) {
        this.uPhone = uPhone;
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

    public void setTypeNam(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeI() {
        return typeI;
    }

    public void setTypeI(String typeI) {
        this.typeI = typeI;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public String getReadNum() {
        return readNum;
    }

    public void setReadNum(String readNum) {
        this.readNum = readNum;
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

    public String getIsTop() {
        return isTop;
    }

    public void setIsTop(String isTop) {
        this.isTop = isTop;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public boolean isLike() {
        return isLike;
    }

    public void setLike(boolean like) {
        isLike = like;
    }

    public String getExp() {
        return exp;
    }

    public void setExp(String exp) {
        this.exp = exp;
    }

    public String getTopPerPirce() {
        return topPerPirce;
    }

    public void setTopPerPirce(String topPerPirce) {
        this.topPerPirce = topPerPirce;
    }

    public String getTopDay() {
        return topDay;
    }

    public void setTopDay(String topDay) {
        this.topDay = topDay;
    }

    public String gettI() {
        return tI;
    }

    public void settI(String tI) {
        this.tI = tI;
    }

    public String getRemainTopTime() {
        return remainTopTime;
    }

    public void setRemainTopTime(String remainTopTime) {
        this.remainTopTime = remainTopTime;
    }

    public String getRemainDays() {
        return remainDays;
    }

    public void setRemainDays(String remainDays) {
        this.remainDays = remainDays;
    }
}
