package com.library.repository.db.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.library.repository.models.NearbyCityExpListModel;

import org.greenrobot.greendao.converter.PropertyConverter;

import java.util.List;

/**
 * Author    by hanlz
 * Date      on 2019/10/23.
 * Description：
 */
public class NearbyCityExpListModelPropertyConverter implements PropertyConverter<List<NearbyCityExpListModel>, String> {

    @Override
    public List<NearbyCityExpListModel> convertToEntityProperty(String databaseValue) {
        if (databaseValue != null) {
            return null;
        }
        TypeToken<List<NearbyCityExpListModel>> typeToken =
                new TypeToken<List<NearbyCityExpListModel>>() {
                };
        return new Gson().fromJson(databaseValue, typeToken.getType());
    }

    @Override
    public String convertToDatabaseValue(List<NearbyCityExpListModel> entityProperty) {
        if (entityProperty == null && entityProperty.size() == 0) {
            return null;
        } else {
            String sb = new Gson().toJson(entityProperty);
            return sb;
        }
    }
}
