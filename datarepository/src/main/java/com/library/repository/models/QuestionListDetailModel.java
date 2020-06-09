package com.library.repository.models;

import android.text.TextUtils;

import com.library.repository.db.model.StringConverter;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;

import java.util.List;


@Entity
public class QuestionListDetailModel {
    /**
     * Hid : 9
     * title : 求购房子111111
     * countryName : 中国
     * cityName : 广州
     * price : 1000.00
     * layout : Studio
     * language : 中文
     * contactPhone : 15860750234
     * contactName : 黄
     * contactWeixin : ws2008
     * area : 30
     * sex : 男
     * desc : 单独卫生间、可养宠物
     * status : 2
     * statusTip :
     * createTime : 05-21
     */

    @Id
    Long _id;

    @Unique
    private String qId;
    private String question;
    private String price;
    private String reply_count;
    private String createTime;
    private String img;
    private String status;


    @Generated(hash = 1852580504)
    public QuestionListDetailModel(Long _id, String qId, String question, String price, String reply_count,
            String createTime, String img, String status) {
        this._id = _id;
        this.qId = qId;
        this.question = question;
        this.price = price;
        this.reply_count = reply_count;
        this.createTime = createTime;
        this.img = img;
        this.status = status;
    }

    @Generated(hash = 1538202016)
    public QuestionListDetailModel() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long get_id() {
        return _id;
    }

    public void set_id(Long _id) {
        this._id = _id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getReply_count() {
        return reply_count;
    }

    public void setReply_count(String reply_count) {
        this.reply_count = reply_count;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }


    public String getQId() {
        return this.qId;
    }

    public void setQId(String qId) {
        this.qId = qId;
    }
}
