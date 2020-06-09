package com.library.repository.repository.remote;


import com.library.repository.models.PayByBalanceModel;
import com.library.repository.models.PayCheckOrderModel;
import com.library.repository.models.ResponseModel;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Author    by hanlz
 * Date      on 2020/2/28.
 * Description：
 */
public interface RemoteMGMPayApi {

    /**
     * 校验订单数据
     *
     * @param orderString
     * @return
     */
    @POST("open/checkOrder")
    @FormUrlEncoded
    Observable<ResponseModel<PayCheckOrderModel>> checkOrder(@Field("orderString") String orderString);

    /**
     * mg app 用户端支付（余额支付）
     *
     * @param appId     appId
     * @param prepayId  订单号
     * @param appKey    appKey
     * @return
     */
    @POST("open/payByBalance")
    @FormUrlEncoded
    Observable<ResponseModel<PayByBalanceModel>> payByBalance(@Field("appId") String appId,
                                                              @Field("prepayId") String prepayId,
                                                              @Field("appKey") String appKey);

}
