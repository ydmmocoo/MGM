package com.library.repository.db.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.library.repository.models.NearbyCityCompanyListModel;

import org.greenrobot.greendao.converter.PropertyConverter;

import java.util.List;

/**
 * Author    by hanlz
 * Date      on 2019/10/24.
 * Description：
 */
public class NearbyCityHotCompanyModelConverter implements PropertyConverter<List<NearbyCityCompanyListModel>, String> {
    @Override
    public List<NearbyCityCompanyListModel> convertToEntityProperty(String databaseValue) {
        if (databaseValue == null) {
            return null;
        }

        TypeToken<List<NearbyCityCompanyListModel>> typeToken = new TypeToken<List<NearbyCityCompanyListModel>>() {
        };
        return new Gson().fromJson(databaseValue, typeToken.getType());
    }

    @Override
    public String convertToDatabaseValue(List<NearbyCityCompanyListModel> entityProperty) {
        if (entityProperty.isEmpty()) {
            return null;
        }
        return new Gson().toJson(entityProperty);
    }
}
