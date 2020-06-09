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
public class PraiseListConverter implements PropertyConverter<List<MomentsListBean.PraiseListBean>,String> {

    @Override
    public List<MomentsListBean.PraiseListBean> convertToEntityProperty(String databaseValue) {
        List<MomentsListBean.PraiseListBean> beans = JsonUtil.jsonToList(databaseValue, MomentsListBean.PraiseListBean.class);
        if (beans == null) beans = new ArrayList<>();
        return beans;
    }

    @Override
    public String convertToDatabaseValue(List<MomentsListBean.PraiseListBean> entityProperty) {
        return JsonUtil.moderToString(entityProperty);
    }
}
