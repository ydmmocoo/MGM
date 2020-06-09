package com.library.repository.repository.remote;

import com.library.repository.models.CmpanydetaisModel;
import com.library.repository.models.CompanyDetailModel;
import com.library.repository.models.CompanyListModel;
import com.library.repository.models.CompanyTypeListModel;
import com.library.repository.models.CompanyTypeListModelV1;
import com.library.repository.models.QuestionInfo;
import com.library.repository.models.ResponseModel;
import com.library.repository.models.YellowPageDetailModel;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface RemoteCompanyApi {


    /**
     * 企业黄历列表
     *
     * @param page
     * @param title
     * @return
     */
    @POST("company/companyList")
    @FormUrlEncoded
    Observable<ResponseModel<CompanyListModel>> companyList(@Field("page") int page,
                                                            @Field("title") String title,
                                                            @Field("serviceId") String serviceId);

    @POST("company/companyList")
    @FormUrlEncoded
    Observable<ResponseModel<CompanyListModel>> companyListV1(@Field("page") int page,
                                                              @Field("title") String title,
                                                              @Field("serviceId") String serviceId,
                                                              @Field("secondServiceId") String secondServiceId,
                                                              @Field("countryId") String countryId,
                                                              @Field("cityId") String cityId);


    @POST("company/companyList")
    @FormUrlEncoded
    Observable<ResponseModel<CompanyListModel>> companyListV2(@Field("page") String page,
                                                              @Field("title") String title,
                                                              @Field("serviceId") String serviceId,
                                                              @Field("secondServiceId") String secondServiceId,
                                                              @Field("countryId") String countryId,
                                                              @Field("cityId") String cityId,
                                                              @Field("uid") String uid);


    /**
     * 企业黄历详情
     *
     * @param cId
     * @return
     */
    @POST("company/detail")
    @FormUrlEncoded
    Observable<ResponseModel<CmpanydetaisModel>> companyDetail(@Field("cId") String cId);

    /**
     * 企业黄历详情
     *
     * @param cId
     * @return
     */
    @POST("company/detail")
    @FormUrlEncoded
    Observable<ResponseModel<YellowPageDetailModel>> YellowPageDetail(@Field("cId") String cId);

    /**
     * 问题详情
     *
     * @param qId
     * @return
     */
    @POST("question/info")
    @FormUrlEncoded
    Observable<ResponseModel<QuestionInfo>> QuestionInfo(@Field("qId") String qId);

    /**
     * 关闭提问
     *
     * @param qId
     * @return
     */
    @POST("question/closeQuestion")
    @FormUrlEncoded
    Observable<ResponseModel<Object>> closeQuestion(@Field("qId") String qId);


    /**
     * 关闭提问
     *
     * @param rId
     * @return
     */
    @POST("question/acceptReply")
    @FormUrlEncoded
    Observable<ResponseModel<Object>> acceptReply(@Field("rId") String rId);


    /**
     * 我发布的企业黄历
     *
     * @param page
     * @param title
     * @return
     */
    @POST("company/myCompanyList")
    @FormUrlEncoded
    Observable<ResponseModel<CompanyListModel>> myCompanyList(@Field("page") int page, @Field("title") String title);


    /**
     * 发布公司信息
     *
     * @param title
     * @param service
     * @param name
     * @param phone
     * @param address
     * @param desc
     * @param images
     * @return
     */
    @POST("company/add")
    @FormUrlEncoded
    Observable<ResponseModel<CmpanydetaisModel>> companyAdd(@Field("title") String title,
                                                            @Field("service") String service,
                                                            @Field("name") String name,
                                                            @Field("phone") String phone,
                                                            @Field("address") String address,
                                                            @Field("desc") String desc,
                                                            @Field("images") String images,
                                                            @Field("secondService") String secondService,
                                                            @Field("countryId") String countryId,
                                                            @Field("cityId") String cityId);

    @POST("company/edit")
    @FormUrlEncoded
    Observable<ResponseModel<CmpanydetaisModel>> companyEdit(@Field("title") String title,
                                                             @Field("service") String service,
                                                             @Field("name") String name,
                                                             @Field("phone") String phone,
                                                             @Field("address") String address,
                                                             @Field("desc") String desc,
                                                             @Field("cId") String cId,
                                                             @Field("images") String images,
                                                             @Field("secondService") String secondService,
                                                             @Field("countryId") String countryId,
                                                             @Field("cityId") String cityId);


    @POST("company/certification")
    @FormUrlEncoded
    Observable<ResponseModel<Object>> certification(@Field("name") String name,
                                                    @Field("licenseNo") String licenseNo,
                                                    @Field("businessImg") String businessImg,
                                                    @Field("employImg") String employImg);

    @POST("company/del")
    @FormUrlEncoded
    Observable<ResponseModel<Object>> deleteCompany(@Field("cId") String cId);

    @POST("company/updateReadNum")
    @FormUrlEncoded
    Observable<ResponseModel> updateReadNumCompany(@Field("cId") String cId);


    @POST("company/getCompanyType")
    Observable<ResponseModel<CompanyTypeListModel>> getCompanyType();

    @POST("company/getCompanyType")
    Observable<ResponseModel<CompanyTypeListModelV1>> getCompanyTypeV1();


}
