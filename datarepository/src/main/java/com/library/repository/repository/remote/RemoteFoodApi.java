package com.library.repository.repository.remote;

import com.common.paylibrary.model.WXPayModel;
import com.library.repository.models.CheckGoodsBean;
import com.library.repository.models.CollectShopsBean;
import com.library.repository.models.CouponBean;
import com.library.repository.models.CreateOrderBean;
import com.library.repository.models.GoodsDetailBean;
import com.library.repository.models.GoodsSearchBean;
import com.library.repository.models.HotShopBean;
import com.library.repository.models.OrderBean;
import com.library.repository.models.OrderDetailBean;
import com.library.repository.models.ResponseModel;
import com.library.repository.models.ShoppingInfoBean;
import com.library.repository.models.StoreEvaluateBean;
import com.library.repository.models.StoreShopInfoBean;
import com.library.repository.models.HomeShopListBean;
import com.library.repository.models.ShopTypeBean;
import com.library.repository.models.ShopingCartBean;
import com.library.repository.models.StoreGoodsBean;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * @author yedeman
 * @date 2020/5/20.
 * email：ydmmocoo@gmail.com
 * description：
 */
public interface RemoteFoodApi {

    @POST("shops/getShopTypeList")
    @FormUrlEncoded
    Observable<ResponseModel<ShopTypeBean>> getShopTypeList(@Field("pId") String pId);

    @POST("shops/getHotShops")
    Observable<ResponseModel<HotShopBean>> getHotShops();

    @POST("shops/getShopsList")
    @FormUrlEncoded
    Observable<ResponseModel<HomeShopListBean>> getShopList(@Field("lng") String lng, @Field("lat") String lat,@Field("isRecommend")String isRecommend, @Field("serviceId") String serviceId, @Field("secondServiceId") String secondServiceId,
                                                            @Field("order") String order,@Field("title") String title, @Field("page") int page);

    @POST("goods/getAllGoodsList")
    @FormUrlEncoded
    Observable<ResponseModel<StoreGoodsBean>> getAllGoodsList(@Field("sId") String sId);

    @POST("user/addShopCart")
    @FormUrlEncoded
    Observable<ResponseModel<Object>> addShopCart(@Field("sId") String sId, @Field("gId") String gId, @Field("gName") String gName,
                                                  @Field("seId") String seId, @Field("seName") String seName, @Field("aIds") String aIds,
                                                  @Field("aNames") String aNames, @Field("price") String price, @Field("num") String num,
                                                  @Field("img") String img);

    @POST("user/getShopingCart")
    @FormUrlEncoded
    Observable<ResponseModel<ShopingCartBean>> getShopingCartData(@Field("sId") String sId);

    @POST("shops/getShopInfo")
    @FormUrlEncoded
    Observable<ResponseModel<StoreShopInfoBean>> getShopInfo(@Field("sId") String sId);

    @POST("user/delShopingCart")
    @FormUrlEncoded
    Observable<ResponseModel<Object>> clearShopCart(@Field("sId") String sId);

    @POST("shops/collect")
    @FormUrlEncoded
    Observable<ResponseModel<Object>> collect(@Field("sId") String sId);

    @POST("shops/cancelCollect")
    @FormUrlEncoded
    Observable<ResponseModel<Object>> cancelCollect(@Field("sId") String sId);

    @POST("user/myCollectShops")
    @FormUrlEncoded
    Observable<ResponseModel<CollectShopsBean>> getCollectShops(@Field("lng") String lng, @Field("lat")String lat, @Field("page")int page);

    @POST("user/getShopingInfo")
    @FormUrlEncoded
    Observable<ResponseModel<ShoppingInfoBean>> getShoppingInfo(@Field("sId") String sId);

    @POST("goods/checkGoods")
    @FormUrlEncoded
    Observable<ResponseModel<CheckGoodsBean>> checkGoods(@Field("sId") String sId);

    @POST("user/getCouponList")
    @FormUrlEncoded
    Observable<ResponseModel<CouponBean>> getCouponList(@Field("price") String price,
                                                        @Field("page") String page, @Field("phone") String phone);

    @POST("goods/addOrder")
    @FormUrlEncoded
    Observable<ResponseModel<CreateOrderBean>> createOrder(@Field("sId") String sId, @Field("type") String type, @Field("addressId") String addressId,
                                                           @Field("expectedDeliveryTime") String expectedDeliveryTime, @Field("cId") String cId, @Field("remark") String remark,
                                                           @Field("scId") String scId, @Field("reservedTelephone") String reservedTelephone);

    @POST("goods/buyByBalance")
    @FormUrlEncoded
    Observable<ResponseModel<Object>> payByBalance(@Field("orderId") String orderId);

    @POST("goods/buyByAli")
    @FormUrlEncoded
    Observable<ResponseModel<String>> payByAlipay(@Field("orderId") String orderId);

    @POST("goods/buyByWx")
    @FormUrlEncoded
    Observable<ResponseModel<WXPayModel>> payByWechat(@Field("orderId") String orderId);

    @POST("goods/orderList")
    @FormUrlEncoded
    Observable<ResponseModel<OrderBean>> getOrderList(@Field("payStatus") String payStatus, @Field("orderStatus") String orderStatus,
                                                      @Field("isRefuse") String isRefuse, @Field("page") int page);

    @POST("goods/confirmOrder")
    @FormUrlEncoded
    Observable<ResponseModel<Object>> confirmOrder(@Field("orderId") String orderId);

    @POST("goods/cancelOrder")
    @FormUrlEncoded
    Observable<ResponseModel<Object>> cancelOrder(@Field("orderId") String orderId);

    @POST("goods/cancelRefuse")
    @FormUrlEncoded
    Observable<ResponseModel<Object>> cancelRefuse(@Field("orderId") String orderId);

    @POST("user/rebuy")
    @FormUrlEncoded
    Observable<ResponseModel<Object>> reBuy(@Field("oId") String oId);

    @POST("goods/orderInfo")
    @FormUrlEncoded
    Observable<ResponseModel<OrderDetailBean>> getOrderDetail(@Field("oId") String oId);

    @POST("shops/evaluateList")
    @FormUrlEncoded
    Observable<ResponseModel<StoreEvaluateBean>> getStoreEvaluateList(@Field("sId")String sId, @Field("searchType")String searchType, @Field("page")int page);

    @POST("goods/getGoodInfo")
    @FormUrlEncoded
    Observable<ResponseModel<GoodsDetailBean>> getGoodsDetail(@Field("gId")String gId);

    @POST("shops/addEvaluate")
    @FormUrlEncoded
    Observable<ResponseModel<Object>> addEvaluate(@Field("oId")String oId, @Field("globalScore")String globalScore, @Field("tasteScore")String tasteScore, @Field("packageScore")String packageScore, @Field("deliveryScore")String deliveryScore, @Field("content")String content, @Field("img")String img);

    @POST("goods/refuseOrder")
    @FormUrlEncoded
    Observable<ResponseModel<Object>> refuseOrder(@Field("orderId")String orderId, @Field("remark")String remark);

    @POST("goods/getGoodsList")
    @FormUrlEncoded
    Observable<ResponseModel<GoodsSearchBean>> getGoodsList(@Field("sId")String sId, @Field("cId")String cId,@Field("isHot")String isHot,
                                                            @Field("name")String name,@Field("page")int page);
}
