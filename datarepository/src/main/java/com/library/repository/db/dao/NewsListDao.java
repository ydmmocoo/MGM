package com.library.repository.db.dao;

import com.library.repository.db.base.BaseDaoImpl;
import com.library.repository.db.base.DBHelper;
import com.library.repository.models.NewsListModel;
import com.library.repository.models.NewsListModelDao;

import java.util.List;

public class NewsListDao extends BaseDaoImpl<NewsListModel> {
    public NewsListDao() {
        baseDao = DBHelper.getInstance().getDaoSession().getNewsListModelDao();
    }

    @Override
    public NewsListModel queryModel(String hid) {
        return baseDao.queryBuilder().where(NewsListModelDao.Properties.NewsId.eq(hid)).unique();
    }

    @Override
    public List<NewsListModel> queryList() {
        return baseDao.queryBuilder().list();
    }

    public List<NewsListModel> queryList(int type) {
        return baseDao.queryBuilder().where(NewsListModelDao.Properties.TypeId.eq(type)).list();
    }

    public void deleteAll(int typeId) {
        baseDao.deleteInTx(queryList(typeId));
    }

}