package com.library.repository.models;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.library.repository.db.model.StringConverter;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;

import java.util.List;

import org.greenrobot.greendao.annotation.Generated;


@Entity
public class NewsListModel implements MultiItemEntity {
    //1大图、2三图、3小图
    public static final int BIG_SINGLE_IMAGE = 1;
    public static final int SMALL_SINGLE_IMAGE = 3;
    public static final int MUL_IMAGE = 2;
    /**
     * newsId : 9
     * layoutType : 3
     * newsTitle : test3
     * newsAuth : 人民网3333
     * commentNum : 30
     * publishTime : 3小时前10:36
     * imgs : []
     */

    @Id()
    Long _id;

    @Unique
    private String UniqueId;


    private String newsId;
    private String layoutType;
    private String newsTitle;
    private String newsAuth;
    private String commentNum;
    private String publishTime;
    private String readNum;

    private int typeId;

    @Convert(converter = StringConverter.class, columnType = String.class)
    private List<String> imgs;

    public NewsListModel(int itemType) {
        super();

    }

    public NewsListModel() {
        super();
    }

    @Generated(hash = 267522247)
    public NewsListModel(Long _id, String UniqueId, String newsId,
            String layoutType, String newsTitle, String newsAuth, String commentNum,
            String publishTime, String readNum, int typeId, List<String> imgs) {
        this._id = _id;
        this.UniqueId = UniqueId;
        this.newsId = newsId;
        this.layoutType = layoutType;
        this.newsTitle = newsTitle;
        this.newsAuth = newsAuth;
        this.commentNum = commentNum;
        this.publishTime = publishTime;
        this.readNum = readNum;
        this.typeId = typeId;
        this.imgs = imgs;
    }

    @Override
    public int getItemType() {
        return Integer.parseInt(layoutType);
    }

    public String getNewsId() {
        return newsId;
    }

    public void setNewsId(String newsId) {
        this.newsId = newsId;
    }

    public String getLayoutType() {
        return layoutType;
    }

    public void setLayoutType(String layoutType) {
        this.layoutType = layoutType;
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

    public Long get_id() {
        return this._id;
    }

    public void set_id(Long _id) {
        this._id = _id;
    }

    public int getTypeId() {
        return this.typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public String getUniqueId() {
        return this.UniqueId;
    }

    public void setUniqueId(String UniqueId) {
        this.UniqueId = UniqueId;
    }

    public String getReadNum() {
        return this.readNum;
    }

    public void setReadNum(String readNum) {
        this.readNum = readNum;
    }

}
