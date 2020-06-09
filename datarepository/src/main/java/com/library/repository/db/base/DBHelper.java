package com.library.repository.db.base;

import android.content.Context;

import com.library.common.utils.ContextManager;
import com.library.repository.db.model.DaoMaster;
import com.library.repository.db.model.DaoSession;


/**
 * Created by WYiang on 2017/11/2.
 */

public class DBHelper {
    /**
     * A flag to show how easily you can switch from standard SQLite to the encrypted SQLCipher.
     */
    public static final boolean ENCRYPTED = false;
    private static DaoSession daoSession;

    private DBHelper() {
    }

    private static class DBHolder {
        private final static DBHelper INSTANCE = new DBHelper();
    }

    public static DBHelper getInstance() {
        return DBHolder.INSTANCE;
    }


    /**
     * 初始化
     */
    public void initMBADb() {
        Context context = ContextManager.getContext();

        MySQLiteOpenHelper helper = new MySQLiteOpenHelper(context, "mg-db", null);
        DaoMaster daoMaster = new DaoMaster(helper.getWritableDatabase());
        daoSession = daoMaster.newSession();

//        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, ENCRYPTED ? "mba-db-encrypted" : "mba-db");
//        Database db = ENCRYPTED ? helper.getEncryptedWritableDb("super-secret") : helper.getWritableDb();
    }

    /**
     * 获取数据库会议，通过这个可以拿数据表
     *
     * @return
     */
    public DaoSession getDaoSession() {
        return daoSession;
    }


}
