package com.library.repository.repository.remote;

import com.library.repository.models.ResponseModel;
import com.library.repository.models.UnReadCountBean;

import io.reactivex.Observable;
import retrofit2.http.POST;

/**
 * create by hanlz
 * 2019/9/26
 * Describe:
 */
public interface RemoteFriendsApi {

    /**
     * 获取一些未读的记录数量
     */
    @POST("stat/unReadCount")
    Observable<ResponseModel<UnReadCountBean>> unReadCount();
}
