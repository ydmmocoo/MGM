package com.library.repository.db.base;

import org.greenrobot.greendao.AbstractDao;

import java.util.List;

/**
 * Created by WYiang on 2017/11/9.
 */

public abstract class BaseDaoImpl<T> implements BaseDAO<T> {
    protected AbstractDao<T, Long> baseDao;


    @Override
    public void insertModel(T t) {
        baseDao.insertOrReplace(t);
    }

    @Override
    public void insertList(List<T> list) {
        baseDao.insertOrReplaceInTx(list);
    }

    @Override
    public void deleteModel(T t) {
        if (t == null) return;
        baseDao.delete(t);
    }


    @Override
    public void deleteList(List<T> t) {
        baseDao.deleteInTx(t);
    }

    @Override
    public void deleteAll() {
        baseDao.deleteAll();
    }

    @Override
    public void updateModel(T t) {
        baseDao.update(t);
    }

    @Override
    public void updateList(List<T> t) {
        baseDao.updateInTx(t);
    }
}
