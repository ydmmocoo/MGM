package com.library.repository.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.library.repository.db.model.NearbyCityExpListModelPropertyConverter;
import com.library.repository.db.model.NearbyCityTypeListModelPropertyConverter;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;
import java.util.List;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Author    by Administrator
 * Date      on 2019/10/16.
 * Description：
 */
@Entity
public class NearbyCityConfigModel implements Parcelable {

    @Id(autoincrement = true)//设置自增长
    private Long id;
    //用到了这个Convert注解，表明它们的转换类，这样就可以转换成String保存到数据库中了
    @Convert(columnType = String.class, converter = NearbyCityTypeListModelPropertyConverter.class)
    private List<NearbyCityTypeListModel> typeList;//分类列表
    @Convert(columnType = String.class, converter = NearbyCityExpListModelPropertyConverter.class)
    private List<NearbyCityExpListModel> expList;//时效列表
    private String topPerPrice;//置顶每天价格
    private String unit;//	单位


    @Generated(hash = 1614722689)
    public NearbyCityConfigModel(Long id, List<NearbyCityTypeListModel> typeList,
            List<NearbyCityExpListModel> expList, String topPerPrice, String unit) {
        this.id = id;
        this.typeList = typeList;
        this.expList = expList;
        this.topPerPrice = topPerPrice;
        this.unit = unit;
    }


    protected NearbyCityConfigModel(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        typeList = in.createTypedArrayList(NearbyCityTypeListModel.CREATOR);
        expList = in.createTypedArrayList(NearbyCityExpListModel.CREATOR);
        topPerPrice = in.readString();
        unit = in.readString();
    }


    @Generated(hash = 898646613)
    public NearbyCityConfigModel() {
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(id);
        }
        dest.writeTypedList(typeList);
        dest.writeTypedList(expList);
        dest.writeString(topPerPrice);
        dest.writeString(unit);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<NearbyCityConfigModel> CREATOR = new Creator<NearbyCityConfigModel>() {
        @Override
        public NearbyCityConfigModel createFromParcel(Parcel in) {
            return new NearbyCityConfigModel(in);
        }

        @Override
        public NearbyCityConfigModel[] newArray(int size) {
            return new NearbyCityConfigModel[size];
        }
    };

    public List<NearbyCityTypeListModel> getTypeList() {
        return typeList;
    }

    public void setTypeList(List<NearbyCityTypeListModel> typeList) {
        this.typeList = typeList;
    }

    public List<NearbyCityExpListModel> getExpList() {
        return expList;
    }

    public void setExpList(List<NearbyCityExpListModel> expList) {
        this.expList = expList;
    }

    public String getTopPerPrice() {
        return topPerPrice;
    }

    public void setTopPerPrice(String topPerPrice) {
        this.topPerPrice = topPerPrice;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
