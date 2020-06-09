package com.library.repository.db.dao;

import com.library.repository.db.base.BaseDaoImpl;
import com.library.repository.db.base.DBHelper;
import com.library.repository.db.model.FingerprintModel;
import com.library.repository.db.model.FingerprintModelDao;

import java.util.List;

public class FingerprintDao extends BaseDaoImpl<FingerprintModel> {


    public FingerprintDao() {
        baseDao = DBHelper.getInstance().getDaoSession().getFingerprintModelDao();
    }

    @Override
    public FingerprintModel queryModel(String uid) {
        return baseDao.queryBuilder().where(FingerprintModelDao.Properties.UserId.eq(uid)).unique();
    }

    @Override
    public List<FingerprintModel> queryList() {
        return baseDao.queryBuilder().list();
    }


}