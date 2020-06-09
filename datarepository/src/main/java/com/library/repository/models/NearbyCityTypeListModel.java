package com.library.repository.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Author    by Administrator
 * Date      on 2019/10/16.
 * Description：
 */

public class NearbyCityTypeListModel implements Parcelable {

    private String cId;//分类id
    private String typeName;//分类名称
    private String desc;//输入提示
    private String img;//图片地址

    public NearbyCityTypeListModel() {
    }

    protected NearbyCityTypeListModel(Parcel in) {
        cId = in.readString();
        typeName = in.readString();
        desc = in.readString();
        img = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(cId);
        dest.writeString(typeName);
        dest.writeString(desc);
        dest.writeString(img);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<NearbyCityTypeListModel> CREATOR = new Creator<NearbyCityTypeListModel>() {
        @Override
        public NearbyCityTypeListModel createFromParcel(Parcel in) {
            return new NearbyCityTypeListModel(in);
        }

        @Override
        public NearbyCityTypeListModel[] newArray(int size) {
            return new NearbyCityTypeListModel[size];
        }
    };

    public String getcId() {
        return cId;
    }

    public void setcId(String cId) {
        this.cId = cId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
