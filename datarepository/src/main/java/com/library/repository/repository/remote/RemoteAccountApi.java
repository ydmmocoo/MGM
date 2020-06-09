package com.library.repository.repository.remote;

import com.common.paylibrary.model.AliPayModel;
import com.common.paylibrary.model.AliUserModel;
import com.common.paylibrary.model.WXPayModel;
import com.google.gson.JsonObject;
import com.library.repository.models.AddressModel;
import com.library.repository.models.AuthQuestionModel;
import com.library.repository.models.BalanceDetailModel;
import com.library.repository.models.BalanceModel;
import com.library.repository.models.BillRecordModel;
import com.library.repository.models.CashListModel;
import com.library.repository.models.CompanyCerModel;
import com.library.repository.models.InviteListModel;
import com.library.repository.models.InviteModel;
import com.library.repository.models.LevelHomeModel;
import com.library.repository.models.OtherUserModel;
import com.library.repository.models.PayCodeModel;
import com.library.repository.models.PaymentCodeModel;
import com.library.repository.models.PersonCerModel;
import com.library.repository.models.PhoneContactListModel;
import com.library.repository.models.RechargeModel;
import com.library.repository.models.ResponseModel;
import com.library.repository.models.ScoreListModel;
import com.library.repository.models.UserInfoModel;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;

public interface RemoteAccountApi {

    /**
     * 注册
     *
     * @param nickName  昵称
     * @param phone     手机号
     * @param smsCode   验证码
     * @param psw       密码
     * @param loginType 1:微信，2:支付宝
     */
    @POST("user/register")
    @FormUrlEncoded
    Observable<ResponseModel<UserInfoModel>> register(@Field("nickName") String nickName,
                                                      @Field("sn") String sn,
                                                      @Field("phone") String phone,
                                                      @Field("smsCode") String smsCode,
                                                      @Field("psw") String psw,
                                                      @Field("openId") String openId,
                                                      @Field("loginType") String loginType,
                                                      @Field("sex") String sex,
                                                      @Field("avatar") String avatar,
                                                      @Field("androidDeviceNo") String androidDeviceNo,
                                                      @Field("longitude") String longitude,
                                                      @Field("latitude") String latitude);

    /**
     * 密码登陆
     *
     * @param phone 手机号
     * @param psw   密码
     */
    @POST("user/loginPsw")
    @FormUrlEncoded
    Observable<ResponseModel<UserInfoModel>> loginPwd(@Field("phone") String phone,
                                                      @Field("psw") String psw,
                                                      @Field("longitude") String longitude,
                                                      @Field("latitude") String latitude,
                                                      @Field("androidDeviceNo") String androidDeviceNo);


    /**
     * 第三方登录
     *
     * @param openId
     * @return
     */
    @POST("user/loginAuth")
    @FormUrlEncoded
    Observable<ResponseModel<UserInfoModel>> loginAuth(@Field("openId") String openId,
                                                       @Field("type") String type,
                                                       @Field("longitude") String longitude,
                                                       @Field("latitude") String latitude,
                                                       @Field("androidDeviceNo") String androidDeviceNo);


    @POST("user/getAliAuth")
    Observable<ResponseModel<String>> getAliAuth();


    /**
     * 验证码登陆
     *
     * @param phone   手机号
     * @param smsCode 短信验证码
     * @return
     */
    @POST("user/loginCode")
    @FormUrlEncoded
    Observable<ResponseModel<UserInfoModel>> loginCode(@Field("phone") String phone,
                                                       @Field("smsCode") String smsCode,
                                                       @Field("longitude") String longitude,
                                                       @Field("latitude") String latitude,
                                                       @Field("androidDeviceNo") String androidDeviceNo);


    /**
     * 获取短信验证码
     *
     * @param phone 手机号
     * @param cCode 区号
     * @return
     */
    @POST("user/sendSmsCode")
    @FormUrlEncoded
    Observable<ResponseModel<UserInfoModel>> sendSmsCode(@Field("phone") String phone, @Field("cCode") String cCode);


    /**
     * 获取用户信息
     *
     * @param phone 手机号
     * @return
     */
    @POST("user/getUserInfo")
    @FormUrlEncoded
    Observable<ResponseModel<OtherUserModel>> getUserInfo(@Field("phone") String phone, @Field("payCode") String PayCode);

    /**
     * 获取用户信息
     *
     * @param phone 手机号
     * @return
     */
    @POST("user/getUserInfo")
    @FormUrlEncoded
    Observable<ResponseModel<OtherUserModel>> getUserInfo(@Field("phone") String phone, @Field("identifier") String identifier, @Field("payCode") String PayCode);

    /**
     * 获取用户信息
     *
     * @param PayCode 手机号
     * @return
     */
    @POST("user/getUserInfo")
    @FormUrlEncoded
    Observable<ResponseModel<OtherUserModel>> getUserInfos(@Field("payCode") String PayCode);

    /**
     * 获取用户信息
     *
     * @param PayCode 手机号
     * @return
     */
    @POST("user/getUserInfo")
    @FormUrlEncoded
    Observable<ResponseModel<OtherUserModel>> getUserIdentifier(@Field("identifier") String PayCode);

    @POST("user/refreshToken")
    @FormUrlEncoded
    Observable<ResponseModel<UserInfoModel>> refreshToken(@Field("gCode") String gCode, @Field("uid") String uid);

    /**
     * 获取用户信息
     *
     * @return
     */
    @POST("user/profile")
    Observable<ResponseModel<UserInfoModel>> getUserProfile();


    /**
     * 设置隐私
     *
     * @return
     */
    @POST("moments/setSecret")
    @FormUrlEncoded
    Observable<ResponseModel<Object>> setSecret(@Field("frinendAccess") String frinendAccess, @Field("strangeAccess") String strangeAccess);

    /**
     * 获取用户信息
     *
     * @return
     */
    @Multipart
    @POST("user/updateProfile")
    Observable<ResponseModel<UserInfoModel>> updateProfile(@PartMap Map<String, Object> map);

    /**
     * 获取用户信息
     *
     * @return
     */

    @POST("user/updateProfile")
    @FormUrlEncoded
    Observable<ResponseModel<UserInfoModel>> updateProfile(@Field("nickName") String nickName,
                                                           @Field("sex") String sex,
                                                           @Field("longitude") String longitude,
                                                           @Field("latitude") String latitude,
                                                           @Field("avatar") String avatar,
                                                           @Field("address") String address,
                                                           @Field("inviteCode") String inviteCode
    );


    /**
     * 获取余额明细列表
     *
     * @param page
     * @return
     */
    @POST("user/balanceRecod")
    @FormUrlEncoded
    Observable<ResponseModel<BalanceDetailModel>> balanceRecod(@Field("page") int page);


    /**
     * 提交实名认证消息
     *
     * @param page   真名
     * @param idCard 身份证id
     * @param phone  手机号
     * @param sn     区号
     * @param front  正面
     * @param back   背面
     * @return
     */
    @POST("user/certification")
    @FormUrlEncoded
    Observable<ResponseModel<Object>> certification(@Field("name") String page,
                                                    @Field("idCard") String idCard,
                                                    @Field("phone") String phone,
                                                    @Field("sn") String sn,
                                                    @Field("front") String front,
                                                    @Field("back") String back);

    @POST("user/addressList")
    @FormUrlEncoded
    Observable<ResponseModel<AddressModel>> addressList(@Field("page") int page);

    /**
     * 添加收货地址
     *
     * @param name      姓名
     * @param sex       性别
     * @param phone     手机号码
     * @param address   市区定位
     * @param longitude 经度
     * @param latitude  纬度
     * @param roomNo    详细地址门牌号
     * @return
     */
    @POST("user/addAddress")
    @FormUrlEncoded
    Observable<ResponseModel<Object>> addAddress(@Field("name") String name,
                                                 @Field("sex") String sex,
                                                 @Field("phone") String phone,
                                                 @Field("address") String address,
                                                 @Field("longitude") String longitude,
                                                 @Field("latitude") String latitude,
                                                 @Field("roomNo") String roomNo);

    /**
     * 修改地址
     *
     * @param name
     * @param sex
     * @param phone
     * @param address
     * @param longitude
     * @param latitude
     * @param roomNo
     * @return
     */
    @POST("user/modifyAddress")
    @FormUrlEncoded
    Observable<ResponseModel<Object>> modifyAddress(@Field("name") String name,
                                                    @Field("sex") String sex,
                                                    @Field("phone") String phone,
                                                    @Field("address") String address,
                                                    @Field("longitude") String longitude,
                                                    @Field("latitude") String latitude,
                                                    @Field("roomNo") String roomNo,
                                                    @Field("addressId") String addressId);

    /**
     * 删除地址
     *
     * @param addressId
     * @return
     */
    @POST("user/delAddress")
    @FormUrlEncoded
    Observable<ResponseModel<Object>> delAddress(@Field("addressId") String addressId);


    @POST("user/modifyPassword")
    @FormUrlEncoded
    Observable<ResponseModel<Object>> modifyPassword(@Field("smsCode") String smsCode,
                                                     @Field("oldPsw") String oldPsw,
                                                     @Field("psw") String psw,
                                                     @Field("confirmPsw") String confirmPsw);


    @POST("user/setPayPassword")
    @FormUrlEncoded
    Observable<ResponseModel<Object>> setPayPassword(@Field("smsCode") String smsCode,
                                                     @Field("psw") String psw,
                                                     @Field("confirmPsw") String confirmPsw);


    @POST("security/setPayPassword")
    @FormUrlEncoded
    Observable<ResponseModel<Object>> setPayPassword(@Field("psw") String psw);

    @POST("user/modifyPayPassword")
    @FormUrlEncoded
    Observable<ResponseModel<Object>> modifyPayPassword(@Field("smsCode") String smsCode,
                                                        @Field("psw") String psw,
                                                        @Field("confirmPsw") String confirmPsw,
                                                        @Field("oldPsw") String oldPsw);


    /**
     * @param page
     * @param billType    1:收入,2:支出
     * @param accountType 类型,1:充值,2:话费,3:流量费，4：电费，5：水费，6：网费，7：转账，8：红包，9：收款，
     * @return
     */
    @POST("user/getBillList")
    @FormUrlEncoded
    Observable<ResponseModel<BillRecordModel>> getBillList(@Field("page") int page,
                                                           @Field("billType") String billType,
                                                           @Field("accountType") String accountType);


    /**
     * 充值包
     *
     * @return
     */
    @POST("user/balancePackage")
    Observable<ResponseModel<List<RechargeModel>>> balancePackage();

    @POST("user/getAliUserInfo")
    @FormUrlEncoded
    Observable<ResponseModel<AliUserModel>> getAliUserInfo(@Field("code") String code);

    @POST("user/getWeixinUserInfo")
    @FormUrlEncoded
    Observable<ResponseModel<Object>> getWeixinUserInfo(@Field("code") String code);


    @POST("user/batchCheckUser")
    @FormUrlEncoded
    Observable<ResponseModel<PhoneContactListModel>> batchCheckUser(@Field("phones") String phones);

    @POST("user/getInviteCode")
    Observable<ResponseModel<InviteModel>> getInviteCode();

    @POST("user/billDetail")
    @FormUrlEncoded
    Observable<ResponseModel<BillRecordModel.RecordListBean>> billDetail(@Field("billId") String billId);


    @POST("user/getUserBalance")
    Observable<ResponseModel<BalanceModel>> getUserBalance();


    /**
     * 获取用户等级以及相关的等级配置
     *
     * @return
     */
    @POST("user/getUserRank")
    Observable<ResponseModel<LevelHomeModel>> getUserRank();


    /**
     * 我的邀请人
     *
     * @return
     */
    @POST("user/myInvite")
    @FormUrlEncoded
    Observable<ResponseModel<InviteListModel>> myInvite(@Field("page") int page);

    /**
     * 积分明细
     *
     * @param page
     * @return
     */
    @POST("user/scoreRecord")
    @FormUrlEncoded
    Observable<ResponseModel<ScoreListModel>> scoreRecord(@Field("page") int page);


    /**
     * 通过余额方式购买会员等级
     *
     * @param rank
     * @return
     */
    @POST("user/upgradeByBalance")
    @FormUrlEncoded
    Observable<ResponseModel<Object>> upgradeByBalance(@FieldMap Map<String, Object> rank);


    /**
     * 通过支付宝方式购买会员等级
     *
     * @param rank
     * @return
     */
    @POST("user/upgradeByAli")
    @FormUrlEncoded
    Observable<ResponseModel<String>> upgradeByAli(@FieldMap Map<String, Object> rank);

    /**
     * 通过微信方式购买会员等级
     *
     * @param rank
     * @return
     */
    @POST("user/upgradeByWx")
    @FormUrlEncoded
    Observable<ResponseModel<WXPayModel>> upgradeByWx(@FieldMap Map<String, Object> rank);


    /**
     * 获取用户认证信息
     *
     * @return
     */
    @POST("user/userAuditInfo")
    Observable<ResponseModel<PersonCerModel>> userAuditInfo();

    /**
     * 获取公司认证信息
     *
     * @return
     */
    @POST("user/companyAuditInfo")
    Observable<ResponseModel<CompanyCerModel>> companyAuditInfo();


    /**
     * 设置或者变更手势密码
     *
     * @return
     */
    @POST("security/setGestureCode")
    @FormUrlEncoded
    Observable<ResponseModel<Object>> setGestureCode(@Field("gCode") String gCode, @Field("uid") String uid);


    /**
     * 设置安全问题
     *
     * @param question
     * @param answer
     * @return
     */
    @POST("security/setIssue")
    @FormUrlEncoded
    Observable<ResponseModel<Object>> setIssue(@Field("question") String question, @Field("answer") String answer);


    /**
     * 获取安全认证问题
     *
     * @return
     */
    @POST("security/getSecurityIssue")
    Observable<ResponseModel<AuthQuestionModel>> getSecurityIssue();

    /**
     * 验证,1:身份证验证+手机验证码,2:身份证+登陆密码,3:身份证+安全问题,4:手机验证码+安全问题,5:手机验证码+登陆密码
     * 6:登陆密码验证，7：手机验证码登陆,8密码+密保
     *
     * @return
     */
    @POST("security/check")
    @FormUrlEncoded
    Observable<ResponseModel<Object>> check(@FieldMap Map<String, Object> map);


    /**
     * 重置登录密码
     *
     * @param psw
     * @return
     */
    @POST("security/fogotPassword")
    @FormUrlEncoded
    Observable<ResponseModel<Object>> fogotPassword(@Field("psw") String psw);


    /**
     * 换绑手机
     *
     * @param phone
     * @param areaCode
     * @param smsCode
     * @return
     */
    @POST("security/modifyPhone")
    @FormUrlEncoded
    Observable<ResponseModel<Object>> modifyPhone(@Field("phone") String phone,
                                                  @Field("sn") String areaCode,
                                                  @Field("smsCode") String smsCode);

    @POST("security/bindDevice")
    @FormUrlEncoded
    Observable<ResponseModel<Object>> bindDevice(@Field("phone") String phone);


    /**
     * @param phone
     * @param type  1,拿token
     * @return
     */
    @POST("user/getUser")
    @FormUrlEncoded
    Observable<ResponseModel<JsonObject>> getUser(@Field("phone") String phone, @Field("type") String type);


    /**
     * 获取订单详情
     *
     * @param balanceId
     * @return
     */
    @POST("user/balanceDetail")
    @FormUrlEncoded
    Observable<ResponseModel<BalanceDetailModel.BalanceListBean>> balanceDetail(@Field("balanceId") String balanceId);

    /**
     * 获取奖励的现金红包记录
     *
     * @param status 1:已经领取，2：待领取
     * @return
     */
    @POST("user/getCashList")
    @FormUrlEncoded
    Observable<ResponseModel<CashListModel>> getCashList(@Field("page") int page, @Field("status") int status);

    @POST("user/getCashList")
    Observable<ResponseModel<CashListModel>> getCashList();

    /**
     * 领取红包
     *
     * @param cIds
     * @return
     */
    @POST("user/reciveCash")
    @FormUrlEncoded
    Observable<ResponseModel<JsonObject>> reciveCash(@Field("cId") String cIds);

    /**
     * 批量领取红包
     *
     * @return
     */
    @POST("user/batchReciveCash")
    Observable<ResponseModel<JsonObject>> batchReciveCash();

    /**
     * 生成收款码
     *
     * @param price
     * @return
     */
    @POST("user/createPaymentCode")
    @FormUrlEncoded
    Observable<ResponseModel<PaymentCodeModel>> getQRCollectionCode(@Field("price") String price);

    /**
     * 生成收款码
     *
     * @param price
     * @return
     */
    @POST("user/createPaymentCode")
    @FormUrlEncoded
    Observable<ResponseModel<PaymentCodeModel>> getQRCollectionCode(@Field("price") String price, @Field("type") String type);

    /**
     * 获取默认头像列表
     *
     * @return
     */
    @POST("user/getDefaultAvatar")
    Observable<ResponseModel<List<String>>> getDefaultAvatar();

    /**
     * 2.根据返回的clientId，绑定用户
     * workerman/bind
     */
    @POST("workerman/bind")
    @FormUrlEncoded
    Observable<ResponseModel> workBind(@Field("clientId") String clientid);

    /**
     * 2.发送付款信息
     * workerman/bind
     */
    @POST("workerman/send")
    @FormUrlEncoded
    Observable<ResponseModel> workSend(@Field("price") String price, @Field("payCode") String paycode, @Field("status") String status);

    @POST("workerman/send")
    @FormUrlEncoded
    Observable<ResponseModel> workSends(@Field("payCode") String paycode, @Field("status") String status);


    /**
     * 通知用户充值成功
     */
    @POST("workerman/sendAgent")
    @FormUrlEncoded
    Observable<ResponseModel> sendUser(@Field("price") String price, @Field("servicePrice") String servicePrice, @Field("payCode") String payCode, @Field("status") String status);

    /**
     * 用户扫店铺收款码
     */
    @POST("user/shopScanByBlance")
    @FormUrlEncoded
    Observable<ResponseModel<Object>> shopScanByBlance(@Field("payCode") String payCode, @Field("amount") String amount);

    /**
     * 用户扫店铺收款码(微信支付)
     *
     * @param payCode 店铺展示的收款码
     * @param amount  金额
     * @return
     */
    @POST("user/shopScanByWx")
    @FormUrlEncoded
    Observable<ResponseModel<WXPayModel>> shopScanByWx(@Field("payCode") String payCode, @Field("amount") String amount);

    /**
     * 用户扫店铺收款码(支付宝支付)
     *
     * @param payCode 店铺展示的收款码
     * @param amount  金额
     * @return
     */
    @POST("user/shopScanByAli")
    @FormUrlEncoded
    Observable<ResponseModel<String>> shopScanByAli(@Field("payCode") String payCode, @Field("amount") String amount);

    /**
     * @param type 1:开启，2:关闭
     * @return
     */
    @POST("qrcode/setPayCode")
    @FormUrlEncoded
    Observable<ResponseModel<Object>> setPayCode(@Field("type") String type);

    /**
     * 获取付款码
     */
    @POST("qrcode/getPayCode")
    Observable<ResponseModel<PayCodeModel>> getPayCode();
}
