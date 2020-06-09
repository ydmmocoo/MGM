package com.library.repository.models;

import com.library.repository.db.model.StringConverter;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;

import java.util.List;

@Entity
public class CmpanydetaisModel {
    @Id
    Long _id;
    @Unique
    private String cId;
    private String title;
    private String service;
    private String contactName;
    private String contactPhone;
    private String desc;
    private String address;
    private String type;
    private String serviceName;
    private String secondServiceName;
    private String countryName;
    private String countryId;
    private String secondService;
    private String cityName;
    private String cityId;
    private String identifier;
    private String readCount;
    private String uId;
    @Convert(converter = StringConverter.class, columnType = String.class)
    private List<String> imgs;
    @Generated(hash = 1279515719)
    public CmpanydetaisModel(Long _id, String cId, String title, String service, String contactName, String contactPhone, String desc, String address, String type, String serviceName,
            String secondServiceName, String countryName, String countryId, String secondService, String cityName, String cityId, String identifier, String readCount, String uId,
            List<String> imgs) {
        this._id = _id;
        this.cId = cId;
        this.title = title;
        this.service = service;
        this.contactName = contactName;
        this.contactPhone = contactPhone;
        this.desc = desc;
        this.address = address;
        this.type = type;
        this.serviceName = serviceName;
        this.secondServiceName = secondServiceName;
        this.countryName = countryName;
        this.countryId = countryId;
        this.secondService = secondService;
        this.cityName = cityName;
        this.cityId = cityId;
        this.identifier = identifier;
        this.readCount = readCount;
        this.uId = uId;
        this.imgs = imgs;
    }
    @Generated(hash = 1881093118)
    public CmpanydetaisModel() {
    }
    public String getCId() {
        return cId;
    }

    public void setCId(String cId) {
        this.cId = cId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getServiceName() {
        return this.serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getSecondServiceName() {
        return this.secondServiceName;
    }

    public void setSecondServiceName(String secondServiceName) {
        this.secondServiceName = secondServiceName;
    }

    public String getCountryName() {
        return this.countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getCountryId() {
        return this.countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    public String getCityName() {
        return this.cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCityId() {
        return this.cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getSecondService() {
        return this.secondService;
    }

    public void setSecondService(String secondService) {
        this.secondService = secondService;
    }

    public String getIdentifier() {
        return this.identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
    public String getReadCount() {
        return this.readCount;
    }
    public void setReadCount(String readCount) {
        this.readCount = readCount;
    }
    public String getUId() {
        return this.uId;
    }
    public void setUId(String uId) {
        this.uId = uId;
    }
}
