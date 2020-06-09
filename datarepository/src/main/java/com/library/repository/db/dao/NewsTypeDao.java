package com.library.repository.db.dao;

import com.library.repository.db.base.BaseDaoImpl;
import com.library.repository.db.base.DBHelper;
import com.library.repository.models.NewsListModelDao;
import com.library.repository.models.NewsTypeModel;

import java.util.List;

public class NewsTypeDao extends BaseDaoImpl<NewsTypeModel> {
    public NewsTypeDao() {
        baseDao = DBHelper.getInstance().getDaoSession().getNewsTypeModelDao();
    }

    @Override
    public NewsTypeModel queryModel(String hid) {
        return baseDao.queryBuilder().where(NewsListModelDao.Properties.NewsId.eq(hid)).unique();
    }

    @Override
    public List<NewsTypeModel> queryList() {
        return baseDao.queryBuilder().list();
    }


}