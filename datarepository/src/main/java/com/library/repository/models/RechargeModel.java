package com.library.repository.models;

import com.google.gson.annotations.SerializedName;

import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

public class RechargeModel extends BaseM {
    @Id()
    Long _id;
    private boolean isCheck;


    /**
     * package : 50AR
     * price : 50.00AR
     */

    @SerializedName("package")
    private String packageX;
    private String price;
    private String unit;
    private String bId;
    private String cunit;
    private String funit;

    @Generated(hash = 267522247)
    public RechargeModel(Long _id, Boolean isCheck, String funit,
                         String cunit, String bId, String unit, String price,
                         String packageX) {
        this._id = _id;
        this.packageX = packageX;
        this.price = price;
        this.unit = unit;
        this.bId = bId;
        this.cunit = cunit;
        this.funit = funit;
        this.isCheck = isCheck;
        this._id = _id;
    }

    public Long get_id() {
        return this._id;
    }

    public void set_id(Long _id) {
        this._id = _id;
    }

    public String getPackageX() {
        return packageX;
    }

    public String getFunit() {
        return funit;
    }

    public void setFunit(String funit) {
        this.funit = funit;
    }

    public void setPackageX(String packageX) {
        this.packageX = packageX;
    }

    public String getCunit() {
        return cunit;
    }

    public void setCunit(String cunit) {
        this.cunit = cunit;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getbId() {
        return bId;
    }

    public void setbId(String bId) {
        this.bId = bId;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }
}
