package com.library.repository.db;

import com.library.repository.db.dao.CompanyListDaos;
import com.library.repository.db.dao.CompanyTypeDao;
import com.library.repository.db.dao.FingerprintDao;
import com.library.repository.db.dao.FriendRequestDao;

import com.library.repository.db.dao.HouseDetailDao;
import com.library.repository.db.dao.JobListModelDao;
import com.library.repository.db.dao.NearbyCityTopDao;
import com.library.repository.db.dao.NewsDetailDao;
import com.library.repository.db.dao.NewsListDao;
import com.library.repository.db.dao.NewsTypeDao;
import com.library.repository.db.dao.QuestionListDao;
import com.library.repository.db.dao.YellowPageDetailDao;

public class DBDaoFactory {


    /**
     * 好友请求数据本地缓存
     *
     * @return
     */
    public static FriendRequestDao getFriendRequestDao() {
        return new FriendRequestDao();
    }

    /**
     * 房屋租售列表数据缓存
     *
     * @return
     */
    public static HouseDetailDao getHouseDetailDao() {
        return new HouseDetailDao();
    }

    /**
     * 求职招聘本地列表
     *
     * @return
     */
    public static JobListModelDao getJobListModelDao() {
        return new JobListModelDao();
    }

    /**
     * 黄页1
     *
     * @return
     */
    public static CompanyListDaos getCompanyListDaos() {
        return new CompanyListDaos();
    }

    /**
     * 有偿问答
     *
     * @return
     */
    public static QuestionListDao getQuestionListDao() {
        return new QuestionListDao();
    }

    /**
     * 黄页1
     *
     * @return
     */
    public static YellowPageDetailDao getYellowPageDetailDao() {
        return new YellowPageDetailDao();
    }

    /**
     * 头条资讯本地表
     *
     * @return
     */
    public static NewsDetailDao getNewsDetailDao() {
        return new NewsDetailDao();
    }


    /**
     * 资讯列表页面缓存
     *
     * @return
     */
    public static NewsListDao getNewsListDao() {
        return new NewsListDao();
    }

    /**
     * 资讯分类
     *
     * @return
     */
    public static NewsTypeDao getNewsTypeDao() {
        return new NewsTypeDao();
    }


    /**
     * 黄页列别
     *
     * @return
     */
    public static CompanyTypeDao getCompanyTypeDao() {
        return new CompanyTypeDao();
    }

    public static FingerprintDao getFingerprintDao() {
        return new FingerprintDao();
    }



    /**
     * 同城
     *
     * @return
     */
    public static NearbyCityTopDao getNearbyCityTopDao() {
        return new NearbyCityTopDao();
    }

}
