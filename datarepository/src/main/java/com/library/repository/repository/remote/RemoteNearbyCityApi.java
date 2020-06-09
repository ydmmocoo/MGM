package com.library.repository.repository.remote;

import com.common.paylibrary.model.WXPayModel;
import com.library.repository.cache.CacheHeaders;
import com.library.repository.models.AddByBalanceModel;
import com.library.repository.models.CommentReplyModel;
import com.library.repository.models.MyCommentModel;
import com.library.repository.models.NearbyCItyGetListModel;
import com.library.repository.models.NearbyCityCommentListModel;
import com.library.repository.models.NearbyCityCommentModel;
import com.library.repository.models.NearbyCityConfigModel;
import com.library.repository.models.NearbyCityGetMoneyModel;
import com.library.repository.models.NearbyCityHotCompanyModel;
import com.library.repository.models.NearbyCityInfoDetailModel;
import com.library.repository.models.NearbyCityInfoModel;
import com.library.repository.models.NearbyCityPraiseModel;
import com.library.repository.models.NewsCommentModel;
import com.library.repository.models.ResponseModel;
import com.library.repository.models.SetTopDetailInfoModel;
import com.library.repository.models.UnReadCountBean;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Author    by hanlz
 * Date      on 2019/10/16.
 * Description：
 */
public interface RemoteNearbyCityApi {

    /**
     * 获取一些配置
     *
     * @return
     */
    @POST("CityCircle/getConf")
    Observable<ResponseModel<NearbyCityConfigModel>> getConf();


    /**
     * 获取热门商家
     *
     * @return
     */
    @POST("CityCircle/getHotCompany")
    Observable<ResponseModel<NearbyCityHotCompanyModel>> getHotCompany();

    /**
     * 通过余额支付发布同城
     *
     * @return
     */
    @POST("CityCircle/addByBalance")
    @FormUrlEncoded
    Observable<ResponseModel<AddByBalanceModel>> addByBalance(@Field("cId") String cId,
                                                              @Field("content") String content,
                                                              @Field("images") String images,
                                                              @Field("eId") String eId,
                                                              @Field("topDays") String topDays,
                                                              @Field("perPrice") int perPrice);

    /**
     * 通过支付宝支付发布同城
     *
     * @return
     */
    @POST("CityCircle/addByAli")
    @FormUrlEncoded
    Observable<ResponseModel<String>> addByAli(@Field("cId") String cId,
                                               @Field("content") String content,
                                               @Field("images") String images,
                                               @Field("eId") String eId,
                                               @Field("topDays") String topDays,
                                               @Field("perPrice") int perPrice);

    /**
     * 通过微信支付发布同城
     *
     * @return
     */
    @POST("CityCircle/addByWx")
    @FormUrlEncoded
    Observable<ResponseModel<WXPayModel>> addByWx(@Field("cId") String cId,
                                                  @Field("content") String content,
                                                  @Field("images") String images,
                                                  @Field("eId") String eId,
                                                  @Field("topDays") String topDays,
                                                  @Field("perPrice") int perPrice);

    /*--------------------------------同城修改置顶star---------------------------------*/

    /**
     * 通过余额支付置顶
     *
     * @return
     */
    @POST("CityCircle/setTopByBalance")
    @FormUrlEncoded
    Observable<ResponseModel<Object>> setTopByBalance(@Field("cId") String cId,
                                                      @Field("topDays") String topDays,
                                                      @Field("perPrice") int perPrice);

    /**
     * 通过支付宝设置同城置顶
     *
     * @return
     */
    @POST("CityCircle/setTopByAli")
    @FormUrlEncoded
    Observable<ResponseModel<String>> setTopByAli(@Field("cId") String cId,
                                                  @Field("topDays") String topDays,
                                                  @Field("perPrice") int perPrice);

    /**
     * 通过支付宝设置同城置顶
     *
     * @return
     */
    @POST("CityCircle/setTopByWx")
    @FormUrlEncoded
    Observable<ResponseModel<WXPayModel>> setTopByWx(@Field("cId") String cId,
                                                     @Field("topDays") String topDays,
                                                     @Field("perPrice") int perPrice);


    /*--------------------------------同城修改置顶end---------------------------------*/

    /**
     * 获取我发布同城列表
     *
     * @return
     */
    @POST("CityCircle/myCityCircleList")
    @FormUrlEncoded
    Observable<ResponseModel<NearbyCityInfoModel>> myCityCircleList(@Field("page") String page,
                                                                    @Field("k") String k,
                                                                    @Field("typeId") String typeId);

    /**
     * 获取同城列表
     *
     * @return
     */
    @POST("CityCircle/getList")
    @FormUrlEncoded
    Observable<ResponseModel<NearbyCItyGetListModel>> getList(@Field("page") String page,
                                                              @Field("k") String k,
                                                              @Field("typeId") String typeId);
    /**
     * 获取同城个人发布列表
     *
     * @return
     */
    @POST("CityCircle/getList")
    @FormUrlEncoded
    Observable<ResponseModel<NearbyCItyGetListModel>> getListV1(@Field("page") String page,
                                                              @Field("k") String k,
                                                              @Field("typeId") String typeId,
                                                              @Field("uid") String uid);
    /**
     * 获取同城详情
     *
     * @return
     */
    @POST("CityCircle/info")
    @FormUrlEncoded
    Observable<ResponseModel<NearbyCityInfoDetailModel>> getInfo(@Field("cId") String cId);

    /**
     * 更新阅读数
     *
     * @return
     */
    @POST("CityCircle/updateReadNum")
    @FormUrlEncoded
    Observable<ResponseModel<Object>> updateReadNum(@Field("cId") String cId);

    /**
     * 评论列表
     *
     * @return
     */
    @POST("CityCircle/commentList")
    @FormUrlEncoded
    Observable<ResponseModel<NearbyCityCommentModel>> getCommentList(@Field("page") String page, @Field("cId") String cId);


    /**
     * 评论列表
     *
     * @return
     */
    @POST("CityCircle/myComment")
    @FormUrlEncoded
    Observable<ResponseModel<MyCommentModel>> myComment(@Field("page") String page);


    /**
     * 评论点赞或者话题点赞
     *
     * @return
     */
    @POST("CityCircle/praise")
    @FormUrlEncoded
    Observable<ResponseModel<Object>> praise(@Field("commentId") String commentId, @Field("replyId") String replyId, @Field("cId") String cId);

    /**
     * 评论点赞或者话题点赞
     *
     * @return
     */
    @POST("CityCircle/cancelPraise")
    @FormUrlEncoded
    Observable<ResponseModel<Object>> cancelPraise(@Field("commentId") String commentId,
                                                   @Field("replyId") String replyId,
                                                   @Field("cId") String cId);


    /**
     * 增加评论
     *
     * @return
     */
    @POST("CityCircle/addComment")
    @FormUrlEncoded
    Observable<ResponseModel<Object>> addComment(@Field("cId") String cId, @Field("content") String content);

    /**
     * 删除评论
     *
     * @return
     */
    @POST("CityCircle/delComment")
    @FormUrlEncoded
    Observable<ResponseModel<Object>> delComment(@Field("commentId") String commentId);

    /**
     * 开启和关闭同城帖子
     *
     * @return
     */
    @POST("CityCircle/setStatus")
    @FormUrlEncoded
    Observable<ResponseModel<Object>> setStatus(@Field("cId") String cId, @Field("status") String status);


    /**
     * 详情/修改置顶发布页信息
     *
     * @return
     */
    @POST("CityCircle/info")
    @FormUrlEncoded
    Observable<ResponseModel<SetTopDetailInfoModel>> requestInfo(@Field("cId") String cId);

    /**
     * 同城点赞列表
     */
    @POST("CityCircle/praiseList")
    @FormUrlEncoded
    Observable<ResponseModel<NearbyCityPraiseModel>> praiseList(@Field("cId") String cId, @Field("page") String page);

    /**
     * 回复评论
     */
    @POST("CityCircle/addReply")
    @FormUrlEncoded
    Observable<ResponseModel<Object>> addReply(@Field("commentId") String commentId,
                                               @Field("content") String content,
                                               @Field("toUid") String toUid,
                                               @Field("replyId") String replyId);

    /**
     * 获取层主评论详情
     *
     * @param commentId
     * @return
     */
    @Headers(CacheHeaders.NORMAL)
    @POST("CityCircle/topicDetail")
    @FormUrlEncoded
    Observable<ResponseModel<NearbyCityCommentListModel>> topicDetail(@Field("commentId") String commentId);

    /**
     * 获取某条评论的回复列表
     *
     * @param commentId
     * @param page
     * @return
     */
    @Headers(CacheHeaders.NORMAL)
    @POST("CityCircle/replyList")
    @FormUrlEncoded
    Observable<ResponseModel<CommentReplyModel>> replyList(@Field("commentId") String commentId, @Field("page") int page);

    /**
     * 奖励同城金额
     *
     * @return
     */
    @Headers(CacheHeaders.NORMAL)
    @POST("CityCircle/getMoney")
    @FormUrlEncoded
    Observable<ResponseModel<NearbyCityGetMoneyModel>> getMoney(@Field("sId") String sId);
}
