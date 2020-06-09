package com.library.repository.models;

import com.library.repository.db.model.MomentsListBeanConverter;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.util.List;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class CityCircleListModel {

    @Id(autoincrement = true)
    private Long _id;
    private boolean hasNext;
    //    private TypeInfoBean typeInfo;

    @Convert(converter = MomentsListBeanConverter.class, columnType = String.class)
    private List<MomentsListBean> momentsList;


    @Generated(hash = 63144210)
    public CityCircleListModel(Long _id, boolean hasNext,
            List<MomentsListBean> momentsList) {
        this._id = _id;
        this.hasNext = hasNext;
        this.momentsList = momentsList;
    }

    @Generated(hash = 1795311515)
    public CityCircleListModel() {
    }

    public boolean isHasNext() {
        return hasNext;
    }

    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }

//    public TypeInfoBean getTypeInfo() {
//        return typeInfo;
//    }
//
//    public void setTypeInfo(TypeInfoBean typeInfo) {
//        this.typeInfo = typeInfo;
//    }

    public List<MomentsListBean> getMomentsList() {
        return momentsList;
    }

    public void setMomentsList(List<MomentsListBean> momentsList) {
        this.momentsList = momentsList;
    }

    public boolean getHasNext() {
        return this.hasNext;
    }

    public Long get_id() {
        return this._id;
    }

    public void set_id(Long _id) {
        this._id = _id;
    }


    public static class TypeInfoBean {

        private String typeName;
        private String img;

        public String getTypeName() {
            return typeName;
        }

        public void setTypeName(String typeName) {
            this.typeName = typeName;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }
    }

}
