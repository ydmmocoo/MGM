package com.library.repository.db.dao;

import com.library.repository.db.base.BaseDaoImpl;
import com.library.repository.db.base.DBHelper;
import com.library.repository.db.model.CompanyTypeModel;
import com.library.repository.db.model.CompanyTypeModelDao;

import java.util.List;

public class CompanyTypeDao extends BaseDaoImpl<CompanyTypeModel> {

    public CompanyTypeDao() {
        baseDao = DBHelper.getInstance().getDaoSession().getCompanyTypeModelDao();
    }

    @Override
    public CompanyTypeModel queryModel(String cid) {
        return baseDao.queryBuilder().where(CompanyTypeModelDao.Properties.CId.eq(cid)).unique();
    }

    @Override
    public List<CompanyTypeModel> queryList() {
        return baseDao.queryBuilder().list();
    }
}
