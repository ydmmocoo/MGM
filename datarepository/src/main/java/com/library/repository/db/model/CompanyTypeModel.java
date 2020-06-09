package com.library.repository.db.model;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class CompanyTypeModel {

    @Id
    Long _id;

    @Unique
    private String cId;
    private String name;

    @Generated(hash = 1583020726)
    public CompanyTypeModel(Long _id, String cId, String name) {
        this._id = _id;
        this.cId = cId;
        this.name = name;
    }

    @Generated(hash = 436601993)
    public CompanyTypeModel() {
    }

    public String getcId() {
        return cId;
    }

    public void setcId(String cId) {
        this.cId = cId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long get_id() {
        return this._id;
    }

    public void set_id(Long _id) {
        this._id = _id;
    }

    public String getCId() {
        return this.cId;
    }

    public void setCId(String cId) {
        this.cId = cId;
    }
}
