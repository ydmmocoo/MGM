package com.library.repository.models;

import com.library.repository.db.model.NearbyCityHotCompanyModelConverter;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.util.List;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Author    by Administrator
 * Date      on 2019/10/17.
 * Description：
 */
@Entity
public class NearbyCityHotCompanyModel {

    @Id(autoincrement = true)
    private Long _id;
    @Convert(columnType = String.class, converter = NearbyCityHotCompanyModelConverter.class)
    private List<NearbyCityCompanyListModel> companyList;


    @Generated(hash = 1075120902)
    public NearbyCityHotCompanyModel(Long _id, List<NearbyCityCompanyListModel> companyList) {
        this._id = _id;
        this.companyList = companyList;
    }

    @Generated(hash = 222178287)
    public NearbyCityHotCompanyModel() {
    }


    public List<NearbyCityCompanyListModel> getCompanyList() {
        return companyList;
    }

    public void setCompanyList(List<NearbyCityCompanyListModel> companyList) {
        this.companyList = companyList;
    }

    public Long get_id() {
        return this._id;
    }

    public void set_id(Long _id) {
        this._id = _id;
    }
}
