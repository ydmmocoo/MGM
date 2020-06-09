package com.library.repository.repository.remote;


import com.library.repository.models.ResponseModel;
import com.library.repository.models.SearchAgentModel;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Author    by hanlz
 * Date      on 2019/10/16.
 * Description：营业网点 Retrofit Api
 */
public interface RemoteSalesNetworkApi {


    /**
     *
     *
     * @return
     */
    @POST("user/agentList")
    @FormUrlEncoded
    Observable<ResponseModel<SearchAgentModel>> agentList(@Field("lng") String lng,
                                                          @Field("lat") String lat,
                                                          @Field("type") String type,
                                                          @Field("remark") String remark,
                                                          @Field("price") String price);


}
