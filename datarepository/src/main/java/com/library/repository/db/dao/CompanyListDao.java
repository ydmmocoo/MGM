package com.library.repository.db.dao;

import com.library.repository.db.base.BaseDaoImpl;
import com.library.repository.db.base.DBHelper;
import com.library.repository.models.CompanyDetailModel;
import com.library.repository.models.CompanyDetailModelDao;

import java.util.List;

public class CompanyListDao extends BaseDaoImpl<CompanyDetailModel> {
    public CompanyListDao() {
        baseDao = DBHelper.getInstance().getDaoSession().getCompanyDetailModelDao();
    }

    @Override
    public CompanyDetailModel queryModel(String cid) {
        return baseDao.queryBuilder().where(CompanyDetailModelDao.Properties.CId.eq(cid)).unique();
    }

    @Override
    public List<CompanyDetailModel> queryList() {
        return baseDao.queryBuilder().list();
    }

    public List<CompanyDetailModel> queryList(String type) {
        return baseDao.queryBuilder().where(CompanyDetailModelDao.Properties.Type.eq(type)).list();
    }


}

