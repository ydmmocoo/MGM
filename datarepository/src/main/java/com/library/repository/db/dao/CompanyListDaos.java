package com.library.repository.db.dao;

import com.library.repository.db.base.BaseDaoImpl;
import com.library.repository.db.base.DBHelper;
import com.library.repository.models.CmpanydetaisModel;
import com.library.repository.models.CmpanydetaisModelDao;

import java.util.List;

public class CompanyListDaos extends BaseDaoImpl<CmpanydetaisModel> {
    public CompanyListDaos() {
        baseDao = DBHelper.getInstance().getDaoSession().getCmpanydetaisModelDao();
    }

    @Override
    public CmpanydetaisModel queryModel(String cid) {
        return baseDao.queryBuilder().where(CmpanydetaisModelDao.Properties.CId.eq(cid)).unique();
    }

    @Override
    public List<CmpanydetaisModel> queryList() {
        return baseDao.queryBuilder().list();
    }

    public List<CmpanydetaisModel> queryList(String type) {
        return baseDao.queryBuilder().where(CmpanydetaisModelDao.Properties.Type.eq(type)).list();
    }


}

