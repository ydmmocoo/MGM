package com.library.repository.db.dao;

import com.library.repository.db.base.BaseDaoImpl;
import com.library.repository.db.base.DBHelper;
import com.library.repository.models.HouseDetailModel;
import com.library.repository.models.HouseDetailModelDao;
import com.library.repository.models.JobModel;
import com.library.repository.models.JobModelDao;

import java.util.List;

public class JobListModelDao extends BaseDaoImpl<JobModel> {
    public JobListModelDao() {
        baseDao = DBHelper.getInstance().getDaoSession().getJobModelDao();
    }

    @Override
    public JobModel queryModel(String hid) {
        return baseDao.queryBuilder().where(JobModelDao.Properties.JobId.eq(hid)).unique();
    }

    @Override
    public List<JobModel> queryList() {
        return baseDao.queryBuilder().list();
    }

    public List<JobModel> queryList(int type) {
        return baseDao.queryBuilder().where(JobModelDao.Properties.Type.eq(type)).list();
    }

    public void deleteAll(int type) {
        List<JobModel> list = queryList(type);
        baseDao.deleteInTx(list);
    }
}

