package com.library.repository.models;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;


@Entity
public class MyQuestionListDetailModel {
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
    private int type;


    @Generated(hash = 790362102)
    public MyQuestionListDetailModel(Long _id, String qId, String question, String price, String reply_count,
            String createTime, String img, int type) {
        this._id = _id;
        this.qId = qId;
        this.question = question;
        this.price = price;
        this.reply_count = reply_count;
        this.createTime = createTime;
        this.img = img;
        this.type = type;
    }

    @Generated(hash = 1426596630)
    public MyQuestionListDetailModel() {
    }

    public Long get_id() {
        return _id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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
