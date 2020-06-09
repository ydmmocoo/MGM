package com.library.repository.db.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.library.repository.models.MomentsListBean;

import org.greenrobot.greendao.converter.PropertyConverter;

import java.util.List;

/**
 * Author    by hanlz
 * Date      on 2019/10/25.
 * Description：
 */
public class MomentsListBeanConverter implements PropertyConverter<List<MomentsListBean>, String> {
    @Override
    public List<MomentsListBean> convertToEntityProperty(String databaseValue) {
        if (databaseValue == null) {
            return null;
        }
        TypeToken<List<MomentsListBean>> typetoken = new TypeToken<List<MomentsListBean>>() {
        };

        return new Gson().fromJson(databaseValue, typetoken.getType());
    }

    @Override
    public String convertToDatabaseValue(List<MomentsListBean> entityProperty) {
        if (entityProperty == null && entityProperty.size() <= 0) {
            return null;
        }
        return new Gson().toJson(entityProperty);
    }
}
