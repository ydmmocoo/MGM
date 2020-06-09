package com.library.repository.db.model;

import com.library.common.utils.JsonUtil;
import com.library.repository.models.MomentsListBean;

import org.greenrobot.greendao.converter.PropertyConverter;

import java.util.ArrayList;
import java.util.List;

/**
 * create by hanlz
 * 2019/9/26
 * Describe:
 */
public class MomentsInfoBeanConverter implements PropertyConverter<MomentsListBean.MomentsInfoBean, String> {
    @Override
    public MomentsListBean.MomentsInfoBean convertToEntityProperty(String databaseValue) {
        MomentsListBean.MomentsInfoBean momentsInfoBeans = JsonUtil.strToModel(databaseValue, MomentsListBean.MomentsInfoBean.class);
        if (momentsInfoBeans == null) {
            momentsInfoBeans = new MomentsListBean.MomentsInfoBean();
        }
        return momentsInfoBeans;
    }

    @Override
    public String convertToDatabaseValue(MomentsListBean.MomentsInfoBean entityProperty) {
        return JsonUtil.moderToString(entityProperty);
    }
}
