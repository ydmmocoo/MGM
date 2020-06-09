package com.library.repository.db.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.library.repository.models.NearbyCityTypeListModel;

import org.greenrobot.greendao.converter.PropertyConverter;

import java.util.List;

/**
 * Author    by hanlz
 * Date      on 2019/10/23.
 * Description：
 */
public class NearbyCityTypeListModelPropertyConverter implements PropertyConverter<List<NearbyCityTypeListModel>, String> {

    @Override
    public List<NearbyCityTypeListModel> convertToEntityProperty(String databaseValue) {
        if (databaseValue == null) {
            return null;
        }
        // 先得获得这个，然后再typeToken.getType()，否则会异常
        TypeToken<List<NearbyCityTypeListModel>> typeToken = new TypeToken<List<NearbyCityTypeListModel>>() {
        };
        return new Gson().fromJson(databaseValue, typeToken.getType());
    }

    @Override
    public String convertToDatabaseValue(List<NearbyCityTypeListModel> entityProperty) {
        if (entityProperty == null || entityProperty.size() == 0) {
            return null;
        } else {
            String sb = new Gson().toJson(entityProperty);
            return sb;

        }
    }
}
