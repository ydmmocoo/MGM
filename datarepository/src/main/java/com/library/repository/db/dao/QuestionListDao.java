package com.library.repository.db.dao;

import com.library.repository.db.base.BaseDaoImpl;
import com.library.repository.db.base.DBHelper;
import com.library.repository.models.CompanyDetailModelDao;
import com.library.repository.models.QuestionListDetailModel;
import com.library.repository.models.QuestionListDetailModelDao;

import java.util.List;

public class QuestionListDao extends BaseDaoImpl<QuestionListDetailModel> {
    public QuestionListDao() {
        baseDao = DBHelper.getInstance().getDaoSession().getQuestionListDetailModelDao();
    }

    @Override
    public QuestionListDetailModel queryModel(String cid) {
        return baseDao.queryBuilder().where(QuestionListDetailModelDao.Properties.QId.eq(cid)).unique();
    }

    @Override
    public List<QuestionListDetailModel> queryList() {
        return baseDao.queryBuilder().list();
    }

    public List<QuestionListDetailModel> queryList(String type) {
        return baseDao.queryBuilder().where(CompanyDetailModelDao.Properties.Type.eq(type)).list();
    }


}

