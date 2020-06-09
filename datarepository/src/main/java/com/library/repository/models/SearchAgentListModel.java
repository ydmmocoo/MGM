package com.library.repository.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Author    by hanlz
 * Date      on 2019/10/22.
 * Description：
 */
public class SearchAgentListModel implements Parcelable {

    private String uid;
    private String nickName;
    private String avatar;
    private String remark;
    private String address;
    private String availableMargin;
    private String lng;
    private String lat;

    protected SearchAgentListModel(Parcel in) {
        uid = in.readString();
        nickName = in.readString();
        avatar = in.readString();
        remark = in.readString();
        address = in.readString();
        availableMargin = in.readString();
        lng = in.readString();
        lat = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(uid);
        dest.writeString(nickName);
        dest.writeString(avatar);
        dest.writeString(remark);
        dest.writeString(address);
        dest.writeString(availableMargin);
        dest.writeString(lng);
        dest.writeString(lat);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SearchAgentListModel> CREATOR = new Creator<SearchAgentListModel>() {
        @Override
        public SearchAgentListModel createFromParcel(Parcel in) {
            return new SearchAgentListModel(in);
        }

        @Override
        public SearchAgentListModel[] newArray(int size) {
            return new SearchAgentListModel[size];
        }
    };

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAvailableMargin() {
        return availableMargin;
    }

    public void setAvailableMargin(String availableMargin) {
        this.availableMargin = availableMargin;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }
}
