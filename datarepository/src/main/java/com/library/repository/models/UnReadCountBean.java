package com.library.repository.models;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

import java.util.List;

/**
 * create by hanlz
 * 2019/9/26
 * Describe:朋友圈和推荐是否有更新
 */
public class UnReadCountBean implements Parcelable {

    private String momentsFriendCount;//朋友圈未读数量
    private String momentsRecCount;//朋友推荐未读数量
    private JSONObject jsonObject;


    protected UnReadCountBean(Parcel in) {
        momentsFriendCount = in.readString();
        momentsRecCount = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(momentsFriendCount);
        dest.writeString(momentsRecCount);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<UnReadCountBean> CREATOR = new Creator<UnReadCountBean>() {
        @Override
        public UnReadCountBean createFromParcel(Parcel in) {
            return new UnReadCountBean(in);
        }

        @Override
        public UnReadCountBean[] newArray(int size) {
            return new UnReadCountBean[size];
        }
    };

    public String getMomentsFriendCount() {
        return momentsFriendCount;
    }

    public void setMomentsFriendCount(String momentsFriendCount) {
        this.momentsFriendCount = momentsFriendCount;
    }

    public String getMomentsRecCount() {
        return momentsRecCount;
    }

    public void setMomentsRecCount(String momentsRecCount) {
        this.momentsRecCount = momentsRecCount;
    }


    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public void setJsonObject(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }
}
