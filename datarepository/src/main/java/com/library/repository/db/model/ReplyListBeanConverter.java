package com.library.repository.db.model;

import com.library.common.utils.JsonUtil;
import com.library.repository.models.MomentsListBean;

import org.greenrobot.greendao.converter.PropertyConverter;

/**
 * create by hanlz
 * 2019/9/26
 * Describe:
 */
public class ReplyListBeanConverter implements PropertyConverter<MomentsListBean.ReplyListBean, String> {
    @Override
    public MomentsListBean.ReplyListBean convertToEntityProperty(String databaseValue) {
        MomentsListBean.ReplyListBean momentsInfoBeans = JsonUtil.strToModel(databaseValue, MomentsListBean.ReplyListBean.class);
        if (momentsInfoBeans == null) {
            momentsInfoBeans = new MomentsListBean.ReplyListBean();
        }
        return momentsInfoBeans;
    }

    @Override
    public String convertToDatabaseValue(MomentsListBean.ReplyListBean entityProperty) {
        return JsonUtil.moderToString(entityProperty);
    }
}
