package com.library.repository.db.base;

import java.util.List;

/**
 * Created by WYiang on 2017/11/2.
 */

public interface BaseDAO<T> {

    /**
     * 插入单个对象
     *
     * @param t
     */
    void insertModel(T t);

    /**
     * 擦汗如一个集合
     *
     * @param list
     */
    void insertList(List<T> list);

    /**
     * 删除一个对象
     *
     * @param t
     */
    void deleteModel(T t);


    /**
     * 使用删除的方法，表中必须有主键primary，主键必须是Long类型，不要写成long
     *
     * @param t
     */
    void deleteList(List<T> t);

    /**
     * 删除所有数据
     */
    void deleteAll();

    /**
     * 请求一条数据
     *
     * @param key
     * @return
     */
    T queryModel(String key);

    /**
     * 请求一个集合
     *
     * @return
     */
    List<T> queryList();

    /**
     * 更新一个数据
     *
     * @param t
     */
    void updateModel(T t);


    /**
     * 更新多条数据
     *
     * @param t
     */
    void updateList(List<T> t);
}
