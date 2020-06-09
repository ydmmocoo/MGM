package com.library.repository.repository.remote;

import com.common.paylibrary.model.AliPayModel;
import com.common.paylibrary.model.TransBalanceResultM;
import com.common.paylibrary.model.WXPayModel;
import com.library.repository.cache.CacheHeaders;
import com.library.repository.models.GroupRedPacketDetailModel;
import com.library.repository.models.GroupRedPacketModel;
import com.library.repository.models.IMNoticeListModel;
import com.library.repository.models.IMNoticeModel;
import com.library.repository.models.ReciveRedRrecordModel;
import com.library.repository.models.ResponseModel;
import com.library.repository.models.ThreeScanPayModel;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface RemotePayApi {


    /**
     * 户通过微信支付转账、发红包
     *
     * @param receiverId  接受人id
     * @param amount      金额
     * @param instruction 描述
     * @param type        1:转账，2：红包
     * @return
     */
    @POST("chat/transferByWx")
    @FormUrlEncoded
    Observable<ResponseModel<WXPayModel>> transferByWx(@Field("receiverId") String receiverId,
                                                       @Field("amount") String amount,
                                                       @Field("instruction") String instruction,
                                                       @Field("type") String type);

    @POST("chat/transferByWx")
    @FormUrlEncoded
    Observable<ResponseModel<WXPayModel>> transferByWx(@FieldMap Map<String, Object> map);

    /**
     * 用户通过支付宝支付转账、发红包
     *
     * @param receiverId  接收人
     * @param amount      资金
     * @param instruction 描述
     * @param type        1:转账，2：红包
     * @return
     */
    @POST("chat/transferByAli")
    @FormUrlEncoded
    Observable<ResponseModel<AliPayModel>> transferByAli(@Field("receiverId") String receiverId,
                                                         @Field("amount") String amount,
                                                         @Field("instruction") String instruction,
                                                         @Field("type") String type);

    /**
     * 用户通过支付宝支付转账、发红包
     *
     * @return
     */
    @POST("chat/transferByAli")
    @FormUrlEncoded
    Observable<ResponseModel<AliPayModel>> transferByAli(@FieldMap Map<String, Object> map);


    /**
     * 通过余额转账或者发红包
     *
     * @param receiverId
     * @param amount
     * @param instruction
     * @param type
     * @return
     */
    @POST("chat/transferByBalance")
    @FormUrlEncoded
    Observable<ResponseModel<TransBalanceResultM>> transferByBalance(@Field("receiverId") String receiverId,
                                                                     @Field("amount") String amount,
                                                                     @Field("instruction") String instruction,
                                                                     @Field("type") String type);

    /**
     * 通过余额转账或者发红包
     *
     * @return
     */
    /*@POST("chat/transferByBalance")
    @FormUrlEncoded
    Observable<ResponseModel<TransBalanceResultM>> transferByBalance(@FieldMap Map<String, Object> map);*/

    @POST("chat/transferReceive")
    @FormUrlEncoded
    Observable<ResponseModel<Object>> transferReceive(@Field("transferId") String transferId);


    /**
     * 手机流量充值--微信
     *
     * @param map
     * @return
     */
    @POST("recharge/phoneChargeByWx")
    @FormUrlEncoded
    Observable<ResponseModel<WXPayModel>> phoneChargeByWx(@FieldMap Map<String, Object> map);

    /**
     * 用微信支付赏金
     *
     * @param price
     * @return
     */
    @POST("question/setQuestionByWx")
    @FormUrlEncoded
    Observable<ResponseModel<WXPayModel>> setQuestionPriceByWx(@Field("price") String price,
                                                               @Field("question") String question,
                                                               @Field("desc") String desc,
                                                               @Field("images") String images);

    /**
     * 用支付宝支付赏金
     *
     * @param price
     * @return
     */
    @POST("question/setQuestionByAli")
    @FormUrlEncoded
    Observable<ResponseModel<AliPayModel>> setQuestionPriceByAli(@Field("price") String price,
                                                                 @Field("question") String question,
                                                                 @Field("desc") String desc,
                                                                 @Field("images") String images);


    /**
     * 用余额支付赏金
     *
     * @param price
     * @return
     */
    @POST("question/setQuestionByBalance")
    @FormUrlEncoded
    Observable<ResponseModel<Object>> setQuestionPriceByBalance(@Field("price") String price,
                                                                @Field("question") String question,
                                                                @Field("desc") String desc,
                                                                @Field("images") String images);

    /**
     * 通过支付宝打赏作者
     *
     * @param price
     * @return
     */
    @POST("question/rewardByAli")
    @FormUrlEncoded
    Observable<ResponseModel<AliPayModel>> rewardByAli(@Field("price") String price,
                                                       @Field("rId") String rId);


    /**
     * 通过微信打赏作者
     *
     * @param price
     * @return
     */
    @POST("question/rewardByWx")
    @FormUrlEncoded
    Observable<ResponseModel<WXPayModel>> rewardByWx(@Field("price") String price,
                                                     @Field("rId") String rId);


    /**
     * 通过余额打赏作者
     *
     * @param price
     * @param rId
     * @return
     */
    @POST("question/rewardByBalance")
    @FormUrlEncoded
    Observable<ResponseModel<Object>> rewardByBalance(@Field("price") String price,
                                                      @Field("rId") String rId);


    /**
     * 手机流量充值--支付宝
     *
     * @param map
     * @return
     */
    @POST("recharge/phoneChargeByAli")
    @FormUrlEncoded
    Observable<ResponseModel<String>> phoneChargeByAli(@FieldMap Map<String, Object> map);

    /**
     * 手机流量充值--余额
     *
     * @param map
     * @return
     */
    @POST("recharge/phoneCharge")
    @FormUrlEncoded
    Observable<ResponseModel<Object>> phoneCharge(@FieldMap Map<String, Object> map);


    /**
     * 微信转账，不需要对方点击确认，直接到对方账户
     *
     * @param map
     * @return
     */
    @POST("chat/transferDirecByWx")
    @FormUrlEncoded
    Observable<ResponseModel<WXPayModel>> transferDirecByWx(@FieldMap Map<String, Object> map);


    /**
     * 支付宝转账，不需要对方点击确认，直接到对方账户
     *
     * @param map
     * @return
     */
    @POST("chat/transferDirecByAli")
    @FormUrlEncoded
    Observable<ResponseModel<AliPayModel>> transferDirecByAli(@FieldMap Map<String, Object> map);

    /**
     * 余额直接转账给相关用户
     *
     * @param map
     * @return
     */
    @POST("chat/transferDirecByBalance")
    @FormUrlEncoded
    Observable<ResponseModel<Object>> transferDirecByBalance(@FieldMap Map<String, Object> map);


    /**
     * 电水网支付宝充值
     *
     * @param map
     * @return
     */
    @POST("recharge/electricityChargeByAli")
    @FormUrlEncoded
    Observable<ResponseModel<String>> EWNChargeByAli(@FieldMap Map<String, Object> map);


    /**
     * 电水网微信充值
     *
     * @param map
     * @return
     */
    @POST("recharge/electricityChargeByWx")
    @FormUrlEncoded
    Observable<ResponseModel<WXPayModel>> EWNCChargeByWx(@FieldMap Map<String, Object> map);

    /**
     * 电水网余额充值
     *
     * @param map
     * @return
     */
    @POST("recharge/electricityCharge")
    @FormUrlEncoded
    Observable<ResponseModel<Object>> EWNCChargeBalance(@FieldMap Map<String, Object> map);


    /**
     * 余额充值：微信
     *
     * @param map
     * @return
     */
    @POST("user/chargeBalanceByWx")
    @FormUrlEncoded
    Observable<ResponseModel<WXPayModel>> chargeBalanceByWx(@FieldMap Map<String, Object> map);

    /**
     * 余额充值：支付宝
     *
     * @param map
     * @return
     */
    @POST("user/chargeBalanceByAli")
    @FormUrlEncoded
    Observable<ResponseModel<String>> chargeBalanceByAli(@FieldMap Map<String, Object> map);


    /**
     * 验证支付密码
     *
     * @param psw
     * @return
     */
    @POST("user/checkPassword")
    @FormUrlEncoded
    Observable<ResponseModel<String>> checkPayPassword(@Field("psw") String psw);


    /**
     * @param page
     * @param type 1:系统消息,2:支付通知。默认是支付通知
     * @return
     */
    @Headers(CacheHeaders.NORMAL)
    @POST("chat/payNoticeList")
    @FormUrlEncoded
    Observable<ResponseModel<IMNoticeListModel>> payNoticeList(@Field("page") int page, @Field("type") int type);

    @POST("chat/noticeDetail")
    @FormUrlEncoded
    Observable<ResponseModel<IMNoticeModel>> noticeDetail(@Field("nId") String nId);


    /**
     * 检查转账和红包金额
     *
     * @param type  1:转账，2:红包
     * @param price
     * @return
     */
    @POST("chat/checkMoneyLimit")
    @FormUrlEncoded
    Observable<ResponseModel<Object>> checkMoneyLimit(@Field("type") String type, @Field("price") String price);

    /**
     * 检查当天同个人是否转了相同的一笔金额
     *
     * @param toUid 对方id
     * @param money 金额
     * @return
     */
    @POST("chat/checkSameMoney")
    @FormUrlEncoded
    Observable<ResponseModel<Object>> checkSameMoney(@Field("toUid") String toUid, @Field("money") String money);

    /**
     * 检查转账和红包金额
     *
     * @param type  1:转账，2:红包
     * @param price
     * @return
     */
    @POST("chat/checkMoneyLimit")
    @FormUrlEncoded
    Observable<ResponseModel<Object>> checkMoneyLimit2(@Field("type") String type, @Field("price") String price
            , @Field("sType") String sType, @Field("num") String num);

    /**
     * 通过支付宝群聊红包
     *
     * @param price   总金额/单个红包金额
     * @param num     数量
     * @param type    1:拼手气红包，2：普通红包
     * @param remark  红包备注
     * @param groupId 群id
     */
    @POST("chat/sendRedEnvelopeByAli")
    @FormUrlEncoded
    Observable<ResponseModel<GroupRedPacketModel>> sendRedEnvelopeByAli(@Field("type") String type,
                                                                        @Field("price") String price,
                                                                        @Field("num") String num,
                                                                        @Field("remark") String remark,
                                                                        @Field("groupId") String groupId);

    /**
     * 通过微信群聊红包
     *
     * @param price   总金额/单个红包金额
     * @param num     数量
     * @param type    1:拼手气红包，2：普通红包
     * @param remark  红包备注
     * @param groupId 群id
     */
    @POST("chat/sendRedEnvelopeByWx")
    @FormUrlEncoded
    Observable<ResponseModel<WXPayModel>> sendRedEnvelopeByWx(@Field("type") String type,
                                                              @Field("price") String price,
                                                              @Field("num") String num,
                                                              @Field("remark") String remark,
                                                              @Field("groupId") String groupId);

    /**
     * 通过余额群聊红包
     *
     * @param price   总金额/单个红包金额
     * @param num     数量
     * @param type    1:拼手气红包，2：普通红包
     * @param remark  红包备注
     * @param groupId 群id
     */
    @POST("chat/sendRedEnvelopeByBalance")
    @FormUrlEncoded
    Observable<ResponseModel<GroupRedPacketModel>> sendRedEnvelopeByBalance(@Field("type") String type,
                                                                            @Field("price") String price,
                                                                            @Field("num") String num,
                                                                            @Field("remark") String remark,
                                                                            @Field("groupId") String groupId);

    /**
     * 收取群聊红包
     *
     * @param rId     红包记录id
     * @param groupId 群id
     */
    @POST("chat/reciveRedEnvelope")
    @FormUrlEncoded
    Observable<ResponseModel<GroupRedPacketModel>> reciveRedEnvelope(@Field("rId") String rId,
                                                                     @Field("groupId") String groupId);

    /**
     * 红包详情
     *
     * @param rId 红包记录id
     */
    @POST("chat/redEnvelopeInfo")
    @FormUrlEncoded
    Observable<ResponseModel<GroupRedPacketDetailModel>> redEnvelopeInfo(@Field("rId") String rId);

    /**
     * 红包领取记录列表
     *
     * @param rId  红包记录id
     * @param page
     */
    @POST("chat/reciveRedRrecord")
    @FormUrlEncoded
    Observable<ResponseModel<ReciveRedRrecordModel>> reciveRedRrecord(@Field("rId") String rId, @Field("page") String page);

    /**
     * 扫描开放平台的收款码
     *
     * @return
     */
    @POST("user/scanByBalance")
    @FormUrlEncoded
    Observable<ResponseModel<ThreeScanPayModel>> scanByBalance(@Field("amount") String amount, @Field("outOrderId") String outOrderId,
                                                               @Field("payCode") String payCode, @Field("type") String type);

    /**
     * 扫描开放平台的收款码
     *
     * @return
     */
    @POST("user/scanByAli")
    @FormUrlEncoded
    Observable<ResponseModel<String>> scanByAli(@Field("amount") String amount, @Field("outOrderId") String outOrderId,
                                                @Field("payCode") String payCode, @Field("type") String type);


    /**
     * 扫描开放平台的收款码
     *
     * @return
     */
    @POST("user/scanByWx")
    @FormUrlEncoded
    Observable<ResponseModel<WXPayModel>> scanByWx(@Field("amount") String amount, @Field("outOrderId") String outOrderId,
                                                   @Field("payCode") String payCode, @Field("type") String type);
}
