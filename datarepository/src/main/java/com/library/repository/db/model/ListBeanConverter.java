package com.library.repository.db.model;

import com.library.common.utils.JsonUtil;
import com.library.repository.models.MomentsListBean;

import org.greenrobot.greendao.converter.PropertyConverter;

/**
 * create by hanlz
 * 2019/9/26
 * Describe:
 */
public class ListBeanConverter implements PropertyConverter<MomentsListBean.ListBean, String> {
    @Override
    public MomentsListBean.ListBean convertToEntityProperty(String databaseValue) {
        MomentsListBean.ListBean momentsInfoBeans = JsonUtil.strToModel(databaseValue, MomentsListBean.ListBean.class);
        if (momentsInfoBeans == null) {
            momentsInfoBeans = new MomentsListBean.ListBean();
        }
        return momentsInfoBeans;
    }

    @Override
    public String convertToDatabaseValue(MomentsListBean.ListBean entityProperty) {
        return JsonUtil.moderToString(entityProperty);
    }
}
