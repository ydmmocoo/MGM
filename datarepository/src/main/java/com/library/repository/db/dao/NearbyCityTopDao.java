package com.library.repository.db.dao;

import com.library.repository.db.base.BaseDaoImpl;
import com.library.repository.db.base.DBHelper;
import com.library.repository.models.NearbyCityConfigModel;
import com.library.repository.models.NearbyCityConfigModelDao;

import java.util.List;

/**
 * Author    by hanlz
 * Date      on 2019/10/23.
 * Description：
 */
public class NearbyCityTopDao extends BaseDaoImpl<NearbyCityConfigModel> {

    public NearbyCityTopDao() {
        baseDao = DBHelper.getInstance().getDaoSession().getNearbyCityConfigModelDao();

    }

    public NearbyCityConfigModelDao dao() {
        NearbyCityConfigModelDao nearbyCityConfigModelDao = DBHelper.getInstance().getDaoSession().getNearbyCityConfigModelDao();
        return nearbyCityConfigModelDao;
    }

    @Override
    public NearbyCityConfigModel queryModel(String key) {
        return baseDao.queryBuilder().where(NearbyCityConfigModelDao.Properties.Unit.eq(key)).unique();
    }

    @Override
    public List<NearbyCityConfigModel> queryList() {
        return baseDao.queryBuilder().list();
    }
}
