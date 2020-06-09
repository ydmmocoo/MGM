package com.library.repository.db.model;

import com.library.common.utils.JsonUtil;
import com.library.repository.models.NewsDetailModel;

import org.greenrobot.greendao.converter.PropertyConverter;

public class NewsAuthorConvert implements PropertyConverter<NewsDetailModel.AuthorInfoBean, String> {

    @Override
    public NewsDetailModel.AuthorInfoBean convertToEntityProperty(String databaseValue) {
        NewsDetailModel.AuthorInfoBean strings = JsonUtil.strToModel(databaseValue, NewsDetailModel.AuthorInfoBean.class);
        if (strings == null) strings = new NewsDetailModel.AuthorInfoBean();
        return strings;
    }

    @Override
    public String convertToDatabaseValue(NewsDetailModel.AuthorInfoBean entityProperty) {
        return JsonUtil.moderToString(entityProperty);
    }
}