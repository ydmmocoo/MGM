package com.library.repository.db.dao;

import com.library.repository.db.base.BaseDaoImpl;
import com.library.repository.db.base.DBHelper;
import com.library.repository.models.JobModel;
import com.library.repository.models.JobModelDao;
import com.library.repository.models.NewsDetailModel;
import com.library.repository.models.NewsDetailModelDao;

import java.util.List;

public class NewsDetailDao extends BaseDaoImpl<NewsDetailModel> {
    public NewsDetailDao() {
        baseDao = DBHelper.getInstance().getDaoSession().getNewsDetailModelDao();
    }

    @Override
    public NewsDetailModel queryModel(String hid) {
        return baseDao.queryBuilder().where(NewsDetailModelDao.Properties.NewsId.eq(hid)).unique();
    }

    @Override
    public List<NewsDetailModel> queryList() {
        return baseDao.queryBuilder().list();
    }

}

