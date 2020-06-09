package com.library.repository.models;

import android.util.Log;

import com.library.repository.db.model.StringConverter;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;

import java.util.List;

import org.greenrobot.greendao.annotation.Generated;


@Entity
public class CompanyDetailModel {


    /**
     * cId : 3
     * title : 蜗壳科技
     * service : 软件开发1
     * contactName : 黄1
     * contactPhone : 15860750233
     * desc : 厦门富捷迅软件科技公司主要在全国范围呢从事银行卡收单银联预付卡、网络支付等第三方支付业务。 公司秉着“诚信、专注、创新、共赢”的经营理念与国内各大支付公司及各大商业银行建立了密切的合作关系。 目前公司整合了全新的业务资源全力开展POS T+0业务，真正的秒到机，支持传统POS，手机刷卡器。 合作支付公司：
     * address : 湖里高薪技术园109
     * imgs : ["http://192.168.3.12http://pic.58pic.com/58pic/15/68/59/71X58PICNjx_1024.jpg","http://192.168.3.12http://pic.58pic.com/58pic/15/68/59/71X58PICNjx_1024.jpg"]
     */

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

    public String getReadCount() {
        return readCount;
    }

    public void setReadCount(String readCount) {
        this.readCount = readCount;
    }

    @Convert(converter = StringConverter.class, columnType = String.class)
    private List<String> imgs;

    @Generated(hash = 1343255418)
    public CompanyDetailModel(Long _id, String cId, String title, String service, String contactName, String contactPhone, String desc, String address, String type,
            String serviceName, String secondServiceName, String countryName, String countryId, String secondService, String cityName, String cityId, String identifier,
            String readCount, List<String> imgs) {
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
        this.imgs = imgs;
    }

    @Generated(hash = 1434889416)
    public CompanyDetailModel() {
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
}
