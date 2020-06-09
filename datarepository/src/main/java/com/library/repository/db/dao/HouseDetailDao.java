package com.library.repository.db.dao;

import com.library.repository.db.base.BaseDaoImpl;
import com.library.repository.db.base.DBHelper;
import com.library.repository.models.HouseDetailModel;
import com.library.repository.models.HouseDetailModelDao;

import java.util.List;

public class HouseDetailDao extends BaseDaoImpl<HouseDetailModel> {
    public HouseDetailDao() {
        baseDao = DBHelper.getInstance().getDaoSession().getHouseDetailModelDao();
    }

    @Override
    public HouseDetailModel queryModel(String hid) {
        return baseDao.queryBuilder().where(HouseDetailModelDao.Properties.Hid.eq(hid)).unique();
    }

    @Override
    public List<HouseDetailModel> queryList() {
        return baseDao.queryBuilder().list();
    }

    public List<HouseDetailModel> queryList(int type) {
        return baseDao.queryBuilder().where(HouseDetailModelDao.Properties.Type.eq(type)).list();
    }

    public void deleteAll(int type) {
        List<HouseDetailModel> list = queryList(type);
        baseDao.deleteInTx(list);


    }
}

