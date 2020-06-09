package com.library.repository.db.model;

import com.library.common.utils.JsonUtil;
import com.library.repository.models.CityCircleListModel;
import com.library.repository.models.MomentsListBean;

import org.greenrobot.greendao.converter.PropertyConverter;

import java.util.ArrayList;
import java.util.List;


/**
 * create by hanlz
 * 2019/9/26
 * Describe:
 */
public class CityCircleListConverter implements PropertyConverter<List<MomentsListBean>, String> {

    @Override
    public List<MomentsListBean> convertToEntityProperty(String databaseValue) {
        List<MomentsListBean> beans = JsonUtil.jsonToList(databaseValue, MomentsListBean.class);
        if (beans == null) beans = new ArrayList<>();
        return beans;
    }

    @Override
    public String convertToDatabaseValue(List<MomentsListBean> entityProperty) {
        return JsonUtil.moderToString(entityProperty);
    }
}
