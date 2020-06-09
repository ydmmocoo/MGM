package com.library.repository.db.dao;

import com.library.repository.data.UserCenter;
import com.library.repository.db.base.BaseDaoImpl;
import com.library.repository.db.base.DBHelper;
import com.library.repository.db.model.DBFriendRequestModel;
import com.library.repository.db.model.DBFriendRequestModelDao;
import com.library.repository.models.UserInfoModel;

import org.greenrobot.greendao.Property;

import java.util.List;

public class FriendRequestDao extends BaseDaoImpl<DBFriendRequestModel> {
    public FriendRequestDao() {
        baseDao = DBHelper.getInstance().getDaoSession().getDBFriendRequestModelDao();
    }

    @Override
    public DBFriendRequestModel queryModel(String key) {
        UserInfoModel infoModel = UserCenter.getUserInfo();
        String uid = infoModel.getUId();
        return baseDao.queryBuilder()
                .where(DBFriendRequestModelDao.Properties.IdentityId.eq(key))
                .where(DBFriendRequestModelDao.Properties.Uid.eq(uid))
                .unique();
    }

    @Override
    public List<DBFriendRequestModel> queryList() {
        UserInfoModel infoModel = UserCenter.getUserInfo();
        String uid = infoModel.getUId();
        //TODO 2019-9-20 15:18:26 此处修改了数据库数据以id倒叙获取 作用是为了后添加的在前面显示
        return baseDao.queryBuilder()
                .where(DBFriendRequestModelDao.Properties.Uid.eq(uid)).orderDesc(DBFriendRequestModelDao.Properties._id).list();
    }
}
