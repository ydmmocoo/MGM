package com.library.repository.db.model;

import com.library.common.utils.JsonUtil;
import com.library.repository.models.MomentsListBean;

import org.greenrobot.greendao.converter.PropertyConverter;

/**
 * create by hanlz
 * 2019/9/26
 * Describe:
 */
public class UserInfoBeanConverter implements PropertyConverter<MomentsListBean.UserInfoBean, String> {
    @Override
    public MomentsListBean.UserInfoBean convertToEntityProperty(String databaseValue) {
        MomentsListBean.UserInfoBean momentsInfoBeans = JsonUtil.strToModel(databaseValue, MomentsListBean.UserInfoBean.class);
        if (momentsInfoBeans == null) {
            momentsInfoBeans = new MomentsListBean.UserInfoBean();
        }
        return momentsInfoBeans;
    }

    @Override
    public String convertToDatabaseValue(MomentsListBean.UserInfoBean entityProperty) {
        return JsonUtil.moderToString(entityProperty);
    }
}
