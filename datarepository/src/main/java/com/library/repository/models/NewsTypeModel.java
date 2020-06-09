package com.library.repository.models;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

import java.util.Objects;

@Entity
public class NewsTypeModel {


    /**
     * typeId : 1
     * typeName : 热点
     */

    @Id
    Long _id;
    @Unique
    private int typeId;
    private String typeName;
    private String jumpType;
    private String url;


    @Generated(hash = 1559676484)
    public NewsTypeModel(Long _id, int typeId, String typeName, String jumpType,
                         String url) {
        this._id = _id;
        this.typeId = typeId;
        this.typeName = typeName;
        this.jumpType = jumpType;
        this.url = url;
    }

    @Generated(hash = 631218298)
    public NewsTypeModel() {
    }


    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public Long get_id() {
        return this._id;
    }

    public void set_id(Long _id) {
        this._id = _id;
    }

    public String getJumpType() {
        return jumpType;
    }

    public void setJumpType(String jumpType) {
        this.jumpType = jumpType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NewsTypeModel model = (NewsTypeModel) o;
        return typeId == model.typeId &&
                Objects.equals(_id, model._id) &&
                Objects.equals(typeName, model.typeName) &&
                Objects.equals(jumpType, model.jumpType) &&
                Objects.equals(url, model.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_id, typeId, typeName, jumpType, url);
    }
}


