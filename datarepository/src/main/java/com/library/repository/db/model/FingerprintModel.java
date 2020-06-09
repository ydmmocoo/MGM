package com.library.repository.db.model;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class FingerprintModel {

    @Id
    Long _id;

    @Unique
    private String userId;
    private boolean fingerEnable;

    @Generated(hash = 935763063)
    public FingerprintModel(Long _id, String userId, boolean fingerEnable) {
        this._id = _id;
        this.userId = userId;
        this.fingerEnable = fingerEnable;
    }

    @Generated(hash = 1534393615)
    public FingerprintModel() {
    }

    public Long get_id() {
        return this._id;
    }

    public void set_id(Long _id) {
        this._id = _id;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean getFingerEnable() {
        return this.fingerEnable;
    }

    public void setFingerEnable(boolean fingerEnable) {
        this.fingerEnable = fingerEnable;
    }


}
