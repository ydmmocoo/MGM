package com.library.repository.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Author    by hanlz
 * Date      on 2019/10/17.
 * Description：
 */
public class NearbyCityInfoModel implements Parcelable {

    private boolean hasNext;//是否有下一页
    private List<MyCityCircleListModel> cityCircleList;//列表

    protected NearbyCityInfoModel(Parcel in) {
        hasNext = in.readByte() != 0;
        cityCircleList = in.createTypedArrayList(MyCityCircleListModel.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (hasNext ? 1 : 0));
        dest.writeTypedList(cityCircleList);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<NearbyCityInfoModel> CREATOR = new Creator<NearbyCityInfoModel>() {
        @Override
        public NearbyCityInfoModel createFromParcel(Parcel in) {
            return new NearbyCityInfoModel(in);
        }

        @Override
        public NearbyCityInfoModel[] newArray(int size) {
            return new NearbyCityInfoModel[size];
        }
    };

    public boolean getHasNext() {
        return hasNext;
    }

    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }

    public List<MyCityCircleListModel> getCityCircleList() {
        return cityCircleList;
    }

    public void setCityCircleList(List<MyCityCircleListModel> cityCircleList) {
        this.cityCircleList = cityCircleList;
    }
}
