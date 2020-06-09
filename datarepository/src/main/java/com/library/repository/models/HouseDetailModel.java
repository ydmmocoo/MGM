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
public class HouseDetailModel {
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
    private String Hid;
    private String title;
    private String countryName;
    private String cityName;
    private String price;
    private String layout;
    private String language;
    private String contactPhone;
    private String contactName;
    private String contactWeixin;
    private String area;
    private String sex;
    private String desc;
    private String status;//	1:关闭，2：开启,3.过期
    private String statusTip;
    private String createTime;
    private String img;
    private int type;
    private boolean isCollect;
    private String uId;

    @Convert(converter = StringConverter.class, columnType = String.class)
    private List<String> images;

    @Generated(hash = 410638168)
    public HouseDetailModel(Long _id, String Hid, String title, String countryName, String cityName, String price,
            String layout, String language, String contactPhone, String contactName, String contactWeixin, String area,
            String sex, String desc, String status, String statusTip, String createTime, String img, int type,
            boolean isCollect, String uId, List<String> images) {
        this._id = _id;
        this.Hid = Hid;
        this.title = title;
        this.countryName = countryName;
        this.cityName = cityName;
        this.price = price;
        this.layout = layout;
        this.language = language;
        this.contactPhone = contactPhone;
        this.contactName = contactName;
        this.contactWeixin = contactWeixin;
        this.area = area;
        this.sex = sex;
        this.desc = desc;
        this.status = status;
        this.statusTip = statusTip;
        this.createTime = createTime;
        this.img = img;
        this.type = type;
        this.isCollect = isCollect;
        this.uId = uId;
        this.images = images;
    }

    @Generated(hash = 1955833589)
    public HouseDetailModel() {
    }

    public String getHid() {
        return Hid;
    }

    public void setHid(String Hid) {
        this.Hid = Hid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getPrice() {
        return TextUtils.isEmpty(price) ? "" : price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getLayout() {
        return layout;
    }

    public void setLayout(String layout) {
        this.layout = layout;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactWeixin() {
        return contactWeixin;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isCollect() {
        return isCollect;
    }

    public void setCollect(boolean collect) {
        isCollect = collect;
    }

    public void setContactWeixin(String contactWeixin) {
        this.contactWeixin = contactWeixin;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusTip() {
        return statusTip;
    }

    public void setStatusTip(String statusTip) {
        this.statusTip = statusTip;
    }

    public String getCreateTime() {
        return createTime;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
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

    public String getUId() {
        return this.uId;
    }

    public void setUId(String uId) {
        this.uId = uId;
    }
}
