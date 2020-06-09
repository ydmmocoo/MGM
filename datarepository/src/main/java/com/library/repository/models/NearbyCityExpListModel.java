package com.library.repository.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Author    by Administrator
 * Date      on 2019/10/16.
 * Description：
 */
public class NearbyCityExpListModel implements Parcelable {

    private String eId;//id
    private String time;//时长
    private String price;//价格
    private String unit;//单位

    public NearbyCityExpListModel() {
    }

    protected NearbyCityExpListModel(Parcel in) {
        eId = in.readString();
        time = in.readString();
        price = in.readString();
        unit = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(eId);
        dest.writeString(time);
        dest.writeString(price);
        dest.writeString(unit);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<NearbyCityExpListModel> CREATOR = new Creator<NearbyCityExpListModel>() {
        @Override
        public NearbyCityExpListModel createFromParcel(Parcel in) {
            return new NearbyCityExpListModel(in);
        }

        @Override
        public NearbyCityExpListModel[] newArray(int size) {
            return new NearbyCityExpListModel[size];
        }
    };

    public String geteId() {
        return eId;
    }

    public void seteId(String eId) {
        this.eId = eId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
