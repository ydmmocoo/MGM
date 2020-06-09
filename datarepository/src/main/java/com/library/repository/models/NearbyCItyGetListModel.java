package com.library.repository.models;

import com.library.repository.db.model.TotalCityCircleListModelConverter;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.util.List;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Author    by hanlz
 * Date      on 2019/10/17.
 * Description：获取同城列表
 */
@Entity
public class NearbyCItyGetListModel {

    @Id
    private Long _id;
    private boolean hasNext;
    private String count;//数量
    @Convert(columnType = String.class, converter = TotalCityCircleListModelConverter.class)
    private List<TotalCityCircleListModel> cityCircleList;


    @Generated(hash = 976455272)
    public NearbyCItyGetListModel(Long _id, boolean hasNext, String count,
            List<TotalCityCircleListModel> cityCircleList) {
        this._id = _id;
        this.hasNext = hasNext;
        this.count = count;
        this.cityCircleList = cityCircleList;
    }

    @Generated(hash = 1443292017)
    public NearbyCItyGetListModel() {
    }

    public boolean isHasNext() {
        return hasNext;
    }

    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public List<TotalCityCircleListModel> getCityCircleList() {
        return cityCircleList;
    }

    public void setCityCircleList(List<TotalCityCircleListModel> cityCircleList) {
        this.cityCircleList = cityCircleList;
    }

    public Long get_id() {
        return this._id;
    }

    public void set_id(Long _id) {
        this._id = _id;
    }

    public boolean getHasNext() {
        return this.hasNext;
    }
}
