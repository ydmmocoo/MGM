package com.library.repository.db.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.library.repository.models.TotalCityCircleListModel;

import org.greenrobot.greendao.converter.PropertyConverter;

import java.util.List;

/**
 * Author    by hanlz
 * Date      on 2019/10/24.
 * Description：
 */
public class TotalCityCircleListModelConverter implements PropertyConverter<List<TotalCityCircleListModel>, String> {
    @Override
    public List<TotalCityCircleListModel> convertToEntityProperty(String databaseValue) {
        if (databaseValue == null) {
            return null;
        }
        TypeToken<List<TotalCityCircleListModel>> typeToken = new TypeToken<List<TotalCityCircleListModel>>() {
        };
        return new Gson().fromJson(databaseValue, typeToken.getType());
    }

    @Override
    public String convertToDatabaseValue(List<TotalCityCircleListModel> entityProperty) {
        if (entityProperty == null && entityProperty.size() <= 0) {
            return null;
        }
        return new Gson().toJson(entityProperty);
    }
}
