package com.library.repository.repository.remote;

import com.google.gson.JsonObject;
import com.library.repository.models.AdListModel;
import com.library.repository.models.AddFriendModel;
import com.library.repository.models.AgentInfoModel;
import com.library.repository.models.CheckOrderIdModel;
import com.library.repository.models.CityCircleListModel;
import com.library.repository.models.ConvertModel;
import com.library.repository.models.MomentsInfoModel;
import com.library.repository.models.MomentsReplyListModel;
import com.library.repository.models.MomentsUserInfoModel;
import com.library.repository.models.NearbyUserModel;
import com.library.repository.models.NewsRecommendReadModel;
import com.library.repository.models.PersonalMomentListModel;
import com.library.repository.models.PhoneRechargeModel;
import com.library.repository.models.PriceListModel;
import com.library.repository.models.RecAppListModel;
import com.library.repository.models.RechargeDetailModel;
import com.library.repository.models.RechargeModel;
import com.library.repository.models.RechargePhoneDetailModel;
import com.library.repository.models.RemoteAreaCode;
import com.library.repository.models.RemoteRateModel;
import com.library.repository.models.ResponseModel;
import com.library.repository.models.ServiceModel;
import com.library.repository.models.ServicePriceModel;
import com.library.repository.models.TagModel;
import com.library.repository.models.TypeListModel;
import com.library.repository.models.UserInfoModel;
import com.library.repository.models.VersionModel;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface RemoteApi {

    /**
     * 客户余额充值
     */
    @POST("user/netWithdrawByBlance")
    @FormUrlEncoded
    Observable<ResponseModel<Object>> chargeUserBalance(@Field("payCode") String payCode, @Field("amount") String amount);


    /**
     * 获取代理商信息
     */
    @POST("user/getAgentInfo")
    @FormUrlEncoded
    Observable<ResponseModel<AgentInfoModel>> getAgentInfo(@Field("payCode") String payCode);

    /**
     * 通知代理网点提现成功
     */
    @POST("workerman/sendAgent")
    @FormUrlEncoded
    Observable<ResponseModel<Object>> sendAgent(@Field("price") String price,
                                                @Field("servicePrice") String servicePrice,
                                                @Field("payCode") String payCode,
                                                @Field("status") String status);

    /**
     * 获取话费充值套餐
     */
    @POST("recharge/phonePackage")
    Observable<ResponseModel<List<RechargeModel>>> phonePackage();

    /**
     * 获取打赏金额列表
     */
    @POST("question/getPriceList")
    Observable<ResponseModel<PriceListModel>> getPriceList();

    /**
     * 获取分类
     */
    @POST("moments/getType")
    Observable<ResponseModel<TypeListModel>> getType();

    /**
     * 同城
     */
    @POST("moments/cityCircle")
    @FormUrlEncoded
    Observable<ResponseModel<CityCircleListModel>> getCityCircle(@Field("page") String page, @Field("type") int type);


    /**
     * 同城
     */
    @POST("moments/cityCircle")
    @FormUrlEncoded
    Observable<ResponseModel<CityCircleListModel>> getCityCircle(@Field("page") String page);

    /**
     * 朋友圈
     */
    @POST("moments/friend")
    @FormUrlEncoded
    Observable<ResponseModel<CityCircleListModel>> MomentsFriend(@Field("page") String page);

    /**
     * 评论朋友圈
     */
    @POST("moments/addReply")
    @FormUrlEncoded
    Observable<ResponseModel<Object>> addReplyMid(@Field("mId") String mId, @Field("content") String content, @Field("toUid") String toUid, @Field("replyId") String replyId);


    /**
     * 自己或者别人朋友圈列表
     */
    @POST("moments/personalMomentList")
    @FormUrlEncoded
    Observable<ResponseModel<PersonalMomentListModel>> personalMomentList(@Field("identifier") String identifier, @Field("page") int page);

    /**
     * 朋友圈详情
     */
    @POST("moments/info")
    @FormUrlEncoded
    Observable<ResponseModel<MomentsInfoModel>> MomentsInfo(@Field("mId") String mId, @Field("lat") String lat, @Field("lng") String lng);


    /**
     * 点击头像或者昵称获取用户信息
     */
    @POST("moments/getUserInfo")
    @FormUrlEncoded
    Observable<ResponseModel<MomentsUserInfoModel>> getUserInfo(@Field("identifier") String identifier);


    /**
     * 设置用户标签
     *
     * @param tags   标签名称，多个用逗号隔开
     * @param tagIds 自定义标签为0,多个用逗号隔开
     * @return
     */
    @POST("moments/addTag")
    @FormUrlEncoded
    Observable<ResponseModel<Object>> addTag(@Field("tags") String tags, @Field("tagIds") String tagIds);


    /**
     * 设置用户标签
     *
     * @param type 类型，1：男性，2：女性
     * @return
     */
    @POST("moments/getTags")
    @FormUrlEncoded
    Observable<ResponseModel<TagModel>> getTags(@Field("type") String type);

    /**
     * 回复评论列表
     */
    @POST("moments/replyList")
    @FormUrlEncoded
    Observable<ResponseModel<MomentsReplyListModel>> MomentsReplyList(@Field("page") String page, @Field("mId") String commentId, @Field("isRead") String isRead);

    /**
     * 读取评论和点赞
     */
    @POST("moments/read")
    Observable<ResponseModel<Object>> momentsRead();

    /**
     * 获取最近浏览的应用列表
     */
    @POST("stat/recAppList")
    Observable<ResponseModel<RecAppListModel>> recAppList();

    /**
     * 获取服务金额
     */
    @POST("question/getServicePrice")
    Observable<ResponseModel<ServicePriceModel>> getServicePrice();


    /**
     * 获取话费充值套餐
     */
    @POST("recharge/dataPackage")
    Observable<ResponseModel<List<RechargeModel>>> dataPackage();


    /**
     * 获取手机充值账单
     *
     * @param page
     * @return
     */
    @POST("recharge/phoneChargeRecord")
    @FormUrlEncoded
    Observable<ResponseModel<PhoneRechargeModel>> phoneChargeRecord(@Field("page") int page, @Field("type") int type);


    /**
     * @param id   缴费id
     * @param type 类型
     * @return
     */
    @POST("recharge/phoneChargeDetail")
    @FormUrlEncoded
    Observable<ResponseModel<RechargePhoneDetailModel>> phoneChargeDetail(@Field("id") String id, @Field("type") String type);


    /**
     * 充值记录 电费、水费、网费
     *
     * @param page
     * @param
     */
    @POST("recharge/electricityChargeRecord")
    @FormUrlEncoded
    Observable<ResponseModel<PhoneRechargeModel>> chargeRecord(@Field("page") int page, @Field("type") int type);


    /**
     * 充值记录 电费、水费、网费账单详情
     *
     * @param id type 类型：1为电费，2为水费，3为网费
     */
    @POST("recharge/electricityChargeDetail")
    @FormUrlEncoded
    Observable<ResponseModel<RechargeDetailModel>> chargeRecordDetail(@Field("id") String id, @Field("type") String type);


    /**
     * 换算接口（支付前进行人民币和服务费的计算）
     *
     * @param type  为人民币转换，2为服务费及人民币换算
     * @param price 换算金额   5.用户网点冲值计算
     * @return
     */
    @POST("config/convert")
    @FormUrlEncoded
    Observable<ResponseModel<ConvertModel>> convert(@Field("type") String type, @Field("price") String price);


    /**
     * 获取用户信息
     *
     * @return
     */
    @Multipart
    @POST("base/uploadFile")
    Observable<ResponseModel<JsonObject>> uploadFile(@Part List<MultipartBody.Part> requestBodyMap);

    /**
     * 获取用户信息
     *
     * @return
     */
    @Multipart
    @POST("base/uploadFile")
    Observable<ResponseModel<String>> uploadFile2(@Part List<MultipartBody.Part> requestBodyMap);

    /**
     * @param type 1:ios,2:安卓
     * @return
     */
    @POST("version/check")
    @FormUrlEncoded
    Observable<ResponseModel<VersionModel>> checkVersion(@Field("type") String type);

    /**
     * 注册--用户协议
     *
     * @return
     */
    @POST("base/registerPro")
    Observable<ResponseModel<String>> registerPro();

    /**
     * 意见反馈
     *
     * @param content
     * @param feedback
     * @return
     */
    @POST("feedback/add")
    @FormUrlEncoded
    Observable<ResponseModel<Object>> feedback(@Field("content") String content, @Field("feedback") String feedback, @Field("type") int type, @Field("identifier") String identifier);

    /**
     * 意见反馈
     *
     * @param content
     * @param feedback
     * @return
     */
    @POST("feedback/add")
    @FormUrlEncoded
    Observable<ResponseModel<Object>> feedbacks(@Field("content") String content, @Field("feedback") String feedback, @Field("identifier") String identifier);


    /**
     * 回复提问
     *
     * @param qId
     * @param content
     * @param images
     * @return
     */
    @POST("question/reply")
    @FormUrlEncoded
    Observable<ResponseModel<Object>> QuestionReply(@Field("qId") String qId, @Field("content") String content, @Field("images") String images);

    /**
     * 获取水电网服务窗商
     *
     * @param type 类型,1:电费,2:水费，3:网费
     * @return
     */
    @POST("recharge/getServiceByType")
    @FormUrlEncoded
    Observable<ResponseModel<List<ServiceModel>>> getServiceByType(@Field("type") int type);


    /**
     * 添加好友
     *
     * @param phone
     * @param content
     * @return
     */
    @POST("chat/addFriend")
    @FormUrlEncoded
    Observable<ResponseModel<AddFriendModel>> addFriend(@Field("identifier") String phone, @Field("content") String content, @Field("reamrk") String reamrk);


    /**
     * 删除好友
     *
     * @param phone
     * @return
     */
    @POST("chat/delFriend")
    @FormUrlEncoded
    Observable<ResponseModel<Object>> delFriend(@Field("identifier") String phone);

    @POST("chat/delFriend")
    @FormUrlEncoded
    Observable<ResponseModel> delFriends(@Field("identifier") String phone);

    /**
     * 附近的人
     *
     * @param lng
     * @param lat
     * @return
     */
    @POST("chat/findAround")
    @FormUrlEncoded
    Observable<ResponseModel<List<NearbyUserModel>>> findAround(@Field("lng") String lng, @Field("lat") String lat);


    /**
     * 添加好友对方验证通过接口
     *
     * @param phone
     * @return
     */
    @POST("chat/confirmFriend")
    @FormUrlEncoded
    Observable<ResponseModel<Object>> confirmFriend(@Field("identifier") String phone);


    @POST("exchange/batchConvert")
    @FormUrlEncoded
    Observable<ResponseModel<List<RemoteRateModel>>> batchConvert(@Field("amount") String phone,
                                                                  @Field("from") String from,
                                                                  @Field("to") String to);


    @POST("moments/add")
    @FormUrlEncoded
    Observable<ResponseModel<Object>> addMoments(@Field("content") String content,
                                                 @Field("type") String type/*1:图文,2:视频*/,
                                                 @Field("tIds") String tIds,
                                                 @Field("showType") String showType,
                                                 @Field("address") String address,
                                                 @Field("lng") String lng,
                                                 @Field("lat") String lat,
                                                 @Field("urls") String urls);

    @POST("moments/add")
    @FormUrlEncoded
    Observable<ResponseModel<Object>> shareMoments(@Field("content") String content,
                                                   @Field("type") String type/*1:图文,2:视频*/,
                                                   @Field("tIds") String tIds,
                                                   @Field("showType") String showType,
                                                   @Field("address") String address,
                                                   @Field("lng") String lng,
                                                   @Field("lat") String lat,
                                                   @Field("urls") String urls,
                                                   @Field("shareType") String shareType,/*1:黄页，2：新闻，3：同城*/
                                                   @Field("shareId") String shareId);

    /**
     * @param type 1:首页，2：新闻详情页,3:求职招聘列表，4：房屋租售列表
     * @return
     */
    @POST("ad/getad")
    @FormUrlEncoded
    Observable<ResponseModel<AdListModel>> getAd(@Field("type") String type);

    /**
     * 新闻推荐
     *
     * @param id 当前浏览的新闻记录id
     * @return
     */
    @POST("invite/getNewsRec")
    @FormUrlEncoded
    Observable<ResponseModel<NewsRecommendReadModel>> requestRecommendNews(@Field("id") String id);

    /**
     * @param type 轮播
     * @return
     */
    @POST("admin/getRechargeListad/getad")
    @FormUrlEncoded
    Observable<ResponseModel<AdListModel>> getAds(@Field("type") String type);

    /**
     * 热门区号
     *
     * @return
     */
    @POST("config/getNationList")
    @FormUrlEncoded
    Observable<ResponseModel<RemoteAreaCode>> getNationList(@Field("type") int type);


    /**
     * 提交问题
     *
     * @return
     */
    @POST("question/setQuestion")
    @FormUrlEncoded
    Observable<ResponseModel<Object>> setQuestion(@Field("orderId") String orderId, @Field("question") String question, @Field("desc") String desc, @Field("images") String images);

    /**
     * 编辑问题
     *
     * @return
     */
    @POST("question/edit")
    @FormUrlEncoded
    Observable<ResponseModel<Object>> setQuestionEdit(@Field("qId") String qId, @Field("question") String question, @Field("desc") String desc, @Field("images") String images);

    /**
     * 获取用户信息
     *
     * @return
     */
    @POST("user/profile")
    Observable<ResponseModel<UserInfoModel>> getUserProfile();


    /**
     * 获取用户信息
     *
     * @return
     */
    @POST("chat/checkOrder")
    @FormUrlEncoded
    Observable<ResponseModel<CheckOrderIdModel>> checkOrder(@Field("outOrderId") String outOrderId);

    @POST("user/RefreshUserSig")
    @FormUrlEncoded
    Observable<ResponseModel<Object>> refreshUserSig(@Field("phone") String phone);//phone传identity
}
