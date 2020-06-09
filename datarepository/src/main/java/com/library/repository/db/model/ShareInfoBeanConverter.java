package com.library.repository.db.model;

import com.library.common.utils.JsonUtil;
import com.library.repository.models.MomentsListBean;

import org.greenrobot.greendao.converter.PropertyConverter;

/**
 * Author    by hanlz
 * Date      on 2019/11/7.
 * Description：
 */
public class ShareInfoBeanConverter implements PropertyConverter<MomentsListBean.MomentsInfoBean.ShareInfoBean, String> {


    @Override
    public MomentsListBean.MomentsInfoBean.ShareInfoBean convertToEntityProperty(String databaseValue) {
        MomentsListBean.MomentsInfoBean.ShareInfoBean shareInfoBean = JsonUtil.strToModel(databaseValue,MomentsListBean.MomentsInfoBean.ShareInfoBean.class);
        if (shareInfoBean == null) {
            shareInfoBean = new MomentsListBean.MomentsInfoBean.ShareInfoBean();
        }
        return shareInfoBean;
    }

    @Override
    public String convertToDatabaseValue(MomentsListBean.MomentsInfoBean.ShareInfoBean entityProperty) {
        return JsonUtil.moderToString(entityProperty);
    }
}
