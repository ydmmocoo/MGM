package com.library.repository.db.model;

import com.library.common.utils.JsonUtil;

import org.greenrobot.greendao.converter.PropertyConverter;

import java.util.ArrayList;
import java.util.List;

public class StringConverter implements PropertyConverter<List<String>, String> {

    @Override
    public List<String> convertToEntityProperty(String databaseValue) {
        List<String> strings = JsonUtil.jsonToList(databaseValue, String.class);
        if (strings == null) strings = new ArrayList<>();
        return strings;
    }

    @Override
    public String convertToDatabaseValue(List<String> entityProperty) {
        return JsonUtil.moderToString(entityProperty);
    }
}
