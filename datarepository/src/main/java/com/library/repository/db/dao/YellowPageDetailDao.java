package com.library.repository.db.dao;

import com.library.repository.db.base.BaseDaoImpl;
import com.library.repository.db.base.DBHelper;
import com.library.repository.models.CmpanydetaisModel;
import com.library.repository.models.CmpanydetaisModelDao;
import com.library.repository.models.YellowPageDetailModel;
import com.library.repository.models.YellowPageDetailModelDao;

import java.util.List;

public class YellowPageDetailDao extends BaseDaoImpl<YellowPageDetailModel> {
    public YellowPageDetailDao() {
        baseDao = DBHelper.getInstance().getDaoSession().getYellowPageDetailModelDao();
    }

    @Override
    public YellowPageDetailModel queryModel(String cid) {
        return baseDao.queryBuilder().where(YellowPageDetailModelDao.Properties.CId.eq(cid)).unique();
    }

    @Override
    public List<YellowPageDetailModel> queryList() {
        return baseDao.queryBuilder().list();
    }

    public List<YellowPageDetailModel> queryList(String type) {
        return baseDao.queryBuilder().where(YellowPageDetailModelDao.Properties.Type.eq(type)).list();
    }


}

