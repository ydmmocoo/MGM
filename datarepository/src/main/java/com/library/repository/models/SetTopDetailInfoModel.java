package com.library.repository.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Author    by hanlz
 * Date      on 2019/10/21.
 * Description：
 */
public class SetTopDetailInfoModel implements Parcelable {

    private SetTopInfoModel info;

    protected SetTopDetailInfoModel(Parcel in) {
        info = in.readParcelable(SetTopInfoModel.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(info, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SetTopDetailInfoModel> CREATOR = new Creator<SetTopDetailInfoModel>() {
        @Override
        public SetTopDetailInfoModel createFromParcel(Parcel in) {
            return new SetTopDetailInfoModel(in);
        }

        @Override
        public SetTopDetailInfoModel[] newArray(int size) {
            return new SetTopDetailInfoModel[size];
        }
    };

    public SetTopInfoModel getInfo() {
        return info;
    }

    public void setInfo(SetTopInfoModel info) {
        this.info = info;
    }
}
