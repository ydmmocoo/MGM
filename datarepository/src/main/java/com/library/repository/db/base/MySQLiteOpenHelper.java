package com.library.repository.db.base;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;


import com.library.repository.db.model.DBFriendRequestModelDao;
import com.library.repository.db.model.DaoMaster;
import com.library.repository.models.CompanyDetailModelDao;
import com.library.repository.models.HouseDetailModelDao;
import com.library.repository.models.JobModelDao;
import com.library.repository.models.NewsDetailModelDao;
import com.library.repository.models.NewsListModelDao;
import com.library.repository.models.NewsTypeModelDao;

import org.greenrobot.greendao.database.Database;

/**
 * Created by WYiang on 2017/12/7.
 */

public class MySQLiteOpenHelper extends DaoMaster.OpenHelper {
    public MySQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        MigrationHelper.migrate(db, new MigrationHelper.ReCreateAllTableListener() {

                    @Override
                    public void onCreateAllTables(Database db, boolean ifNotExists) {
                        DaoMaster.createAllTables(db, ifNotExists);
                    }

                    @Override
                    public void onDropAllTables(Database db, boolean ifExists) {
                        DaoMaster.dropAllTables(db, ifExists);
                    }
                },
                DBFriendRequestModelDao.class,
                HouseDetailModelDao.class,
                JobModelDao.class,
                CompanyDetailModelDao.class,
                NewsDetailModelDao.class,
                NewsListModelDao.class,
                NewsTypeModelDao.class);
    }
}
