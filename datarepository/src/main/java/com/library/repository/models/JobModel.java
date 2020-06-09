package com.library.repository.models;


import android.util.Log;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class JobModel {

    /**
     * jobId : 5
     * title : 产品
     * countryName : 中国
     * cityName : 厦门
     * sex : 男
     * pay : 100cny
     * contactPhone : 15860750234
     * contactWeixin : wx20089
     * createTime : 2019-05-17 17:24:33
     * jobType : 行政,销售
     * status : 2
     * statusTip :
     */

    @Id
    Long _id;

    @Unique
    private String jobId;
    private String title;
    private String countryName;
    private String cityName;
    private String sex;
    private String pay;
    private String contactPhone;
    private String contactWeixin;
    private String createTime;
    private String jobType;
    private String status;
    private String statusTip;
    private int type;//1招聘，2求职
    private String desc;
    private String companyDesc;
    private String workYear;
    private String education;
    private boolean isCollect;
    private String jobTypeId;
    private String sexId;
    private String educationId;

    @Generated(hash = 987158527)
    public JobModel(Long _id, String jobId, String title, String countryName,
            String cityName, String sex, String pay, String contactPhone,
            String contactWeixin, String createTime, String jobType, String status,
            String statusTip, int type, String desc, String companyDesc,
            String workYear, String education, boolean isCollect, String jobTypeId,
            String sexId, String educationId) {
        this._id = _id;
        this.jobId = jobId;
        this.title = title;
        this.countryName = countryName;
        this.cityName = cityName;
        this.sex = sex;
        this.pay = pay;
        this.contactPhone = contactPhone;
        this.contactWeixin = contactWeixin;
        this.createTime = createTime;
        this.jobType = jobType;
        this.status = status;
        this.statusTip = statusTip;
        this.type = type;
        this.desc = desc;
        this.companyDesc = companyDesc;
        this.workYear = workYear;
        this.education = education;
        this.isCollect = isCollect;
        this.jobTypeId = jobTypeId;
        this.sexId = sexId;
        this.educationId = educationId;
    }

    @Generated(hash = 1353622141)
    public JobModel() {
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getPay() {
        return pay;
    }

    public void setPay(String pay) {
        this.pay = pay;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getContactWeixin() {
        return contactWeixin;
    }

    public void setContactWeixin(String contactWeixin) {
        this.contactWeixin = contactWeixin;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
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

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getCompanyDesc() {
        return companyDesc;
    }

    public void setCompanyDesc(String companyDesc) {
        this.companyDesc = companyDesc;
    }

    public String getWorkYear() {
        return workYear;
    }

    public void setWorkYear(String workYear) {
        this.workYear = workYear;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public void setStatusTip(String statusTip) {
        this.statusTip = statusTip;
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

    public String getJobTypeId() {
        return this.jobTypeId;
    }

    public void setJobTypeId(String jobTypeId) {
        this.jobTypeId = jobTypeId;
    }

    public String getSexId() {
        return this.sexId;
    }

    public void setSexId(String sexId) {
        this.sexId = sexId;
    }

    public String getEducationId() {
        return this.educationId;
    }

    public void setEducationId(String educationId) {
        this.educationId = educationId;
    }
}
