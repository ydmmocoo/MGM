package com.library.repository.models;

import java.util.List;

/**
 * 新闻推荐
 */
public class RecommentListModel {

    private String newsId;//新闻id
    private String newsTitle;//标题
    private String newsAuth;//出处
    private String commentNum;//评论数
    private String publishTime;//发布时间
    private List<String> imgs;//封面

    public String getNewsId() {
        return newsId;
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

    public String getNewsAuth() {
        return newsAuth;
    }

    public void setNewsAuth(String newsAuth) {
        this.newsAuth = newsAuth;
    }

    public String getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(String commentNum) {
        this.commentNum = commentNum;
    }

    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }

    public List<String> getImgs() {
        return imgs;
    }

    public void setImgs(List<String> imgs) {
        this.imgs = imgs;
    }
}
