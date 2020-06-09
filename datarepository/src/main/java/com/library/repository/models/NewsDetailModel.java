package com.library.repository.models;


import com.library.repository.db.model.NewsAuthorConvert;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;

@Entity
public class NewsDetailModel {

    /**
     * newsId : 9
     * newsContent : ssasad
     * readNum : 0
     * newsTitle : test3
     * authorInfo : {"id":"2","nickname":"头条","avatar":""}
     */

    @Id
    Long _id;

    @Unique
    private String newsId;
    private String newsContent;
    private String readNum;
    private String newsTitle;
    private String newsAuth;
    private String createTime;
    private boolean isCollect;
    private String img;
    @Convert(converter = NewsAuthorConvert.class, columnType = String.class)
    private AuthorInfoBean authorInfo;

    @Generated(hash = 264173641)
    public NewsDetailModel(Long _id, String newsId, String newsContent,
            String readNum, String newsTitle, String newsAuth, String createTime,
            boolean isCollect, String img, AuthorInfoBean authorInfo) {
        this._id = _id;
        this.newsId = newsId;
        this.newsContent = newsContent;
        this.readNum = readNum;
        this.newsTitle = newsTitle;
        this.newsAuth = newsAuth;
        this.createTime = createTime;
        this.isCollect = isCollect;
        this.img = img;
        this.authorInfo = authorInfo;
    }

    @Generated(hash = 1700453335)
    public NewsDetailModel() {
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getNewsAuth() {
        return newsAuth;
    }

    public void setNewsAuth(String newsAuth) {
        this.newsAuth = newsAuth;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getNewsId() {
        return newsId;
    }

    public void setNewsId(String newsId) {
        this.newsId = newsId;
    }

    public String getNewsContent() {
        return newsContent;
    }

    public void setNewsContent(String newsContent) {
        this.newsContent = newsContent;
    }

    public String getReadNum() {
        return readNum;
    }

    public void setReadNum(String readNum) {
        this.readNum = readNum;
    }

    public String getNewsTitle() {
        return newsTitle;
    }

    public void setNewsTitle(String newsTitle) {
        this.newsTitle = newsTitle;
    }

    public AuthorInfoBean getAuthorInfo() {
        return authorInfo;
    }

    public void setAuthorInfo(AuthorInfoBean authorInfo) {
        this.authorInfo = authorInfo;
    }

    public static class AuthorInfoBean {
        /**
         * id : 2
         * nickname : 头条
         * avatar :
         */

        private String id;
        private String nickname;
        private String avatar;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }
    }

    public boolean isCollect() {
        return isCollect;
    }

    public void setCollect(boolean collect) {
        isCollect = collect;
    }

    public Long get_id() {
        return this._id;
    }

    public void set_id(Long _id) {
        this._id = _id;
    }

    public boolean getIsCollect() {
        return this.isCollect;
    }

    public void setIsCollect(boolean isCollect) {
        this.isCollect = isCollect;
    }
}
