package com.library.repository.repository.remote;

import com.library.repository.models.CommentReplyModel;
import com.library.repository.models.CompanyListModel;
import com.library.repository.models.HouseConfigModel;
import com.library.repository.models.HouseDetailModel;
import com.library.repository.models.HouseListModel;
import com.library.repository.models.JobConfigModel;
import com.library.repository.models.JobListModel;
import com.library.repository.models.JobModel;
import com.library.repository.models.MyCompanyCommentModel;
import com.library.repository.models.MyHouseCommentModel;
import com.library.repository.models.MyNewsCommentModel;
import com.library.repository.models.MyQuestionListModel;
import com.library.repository.models.MyReplyListCommentModel;
import com.library.repository.models.NewsCommentModel;
import com.library.repository.models.QuestionListModel;
import com.library.repository.models.ResponseModel;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface RemoteJobApi {


    /**
     * 我发布的招聘求职
     *
     * @param page
     * @return
     */
    @POST("job/MyJobsList")
    @FormUrlEncoded
    Observable<ResponseModel<JobListModel>> MyJobsList(@Field("page") int page);


    @POST("job/del")
    @FormUrlEncoded
    Observable<ResponseModel<Object>> deleteJob(@Field("jobId") String jobId);


    @POST("job/getAllCity")
    Observable<ResponseModel<Object>> getAllCity();


    /**
     * z招聘求职列表
     *
     * @param page
     * @param type 1： 招聘2：求职
     * @return
     */
    @POST("job/jobsList")
    @FormUrlEncoded
    Observable<ResponseModel<JobListModel>> jobsList(@Field("page") int page,
                                                     @Field("type") int type,
                                                     @Field("jobTypeId") String jobTypeId,
                                                     @Field("payId") String payId,
                                                     @Field("workId") String workId,
                                                     @Field("educationId") String educationId
    );


    @POST("job/jobsList")
    @FormUrlEncoded
    Observable<ResponseModel<JobListModel>> jobsList(@Field("page") int page, @Field("title") String title);

    @POST("job/getConf")
    Observable<ResponseModel<JobConfigModel>> getConf();

    /**
     * @param title       标题
     * @param countryName 国家
     * @param cityName    城市
     * @param typeIds     只为类型
     * @param sex         性别
     * @param pay         薪资
     * @param phone       手机
     * @param weixin      微信
     * @param desc        描述
     * @param type        类型 	1：招聘，2：求职
     * @return
     */
    @POST("job/add")
    @FormUrlEncoded
    Observable<ResponseModel<Object>> addJob(@Field("title") String title,
                                             @Field("countryName") String countryName,
                                             @Field("cityName") String cityName,
                                             @Field("jobTypeId") String typeIds,
                                             @Field("sex") String sex,
                                             @Field("pay") String pay,
                                             @Field("phone") String phone,
                                             @Field("weixin") String weixin,
                                             @Field("desc") String desc,
                                             @Field("companyDesc") String companyDesc,
                                             @Field("workYear") String workyear,
                                             @Field("education") String education,
                                             @Field("expireType") String expireId,
                                             @Field("type") int type);

    @POST("job/add")
    @FormUrlEncoded
    Observable<ResponseModel<Object>> addJob(@FieldMap Map<String, Object> map);

    @POST("job/edit")
    @FormUrlEncoded
    Observable<ResponseModel<Object>> editJob(@Field("title") String title,
                                              @Field("countryName") String countryName,
                                              @Field("cityName") String cityName,
                                              @Field("jobTypeId") String typeIds,
                                              @Field("sex") String sex,
                                              @Field("pay") String pay,
                                              @Field("phone") String phone,
                                              @Field("weixin") String weixin,
                                              @Field("desc") String desc,
                                              @Field("companyDesc") String companyDesc,
                                              @Field("workYear") String workyear,
                                              @Field("education") String education,
                                              @Field("jobId") String jobId,
                                              @Field("expireType") String expireId,
                                              @Field("type") int type);

    @POST("job/edit")
    @FormUrlEncoded
    Observable<ResponseModel<Object>> editJob(@FieldMap Map<String, Object> map);

    @POST("job/jobDetail")
    @FormUrlEncoded
    Observable<ResponseModel<JobModel>> jobDetail(@Field("jobId") String jobId, @Field("uid") String uid);

    @POST("job/myCollect")
    @FormUrlEncoded
    Observable<ResponseModel<JobListModel>> myCollect(@Field("page") int page);


    /**
     * 收藏职位
     *
     * @param jobId
     * @return
     */
    @POST("job/collectJob")
    @FormUrlEncoded
    Observable<ResponseModel<Object>> collectJob(@Field("jobId") String jobId);


    /**
     * 取消收藏
     *
     * @param jobId
     * @return
     */
    @POST("job/cancelCollectJob")
    @FormUrlEncoded
    Observable<ResponseModel<Object>> cancelCollectJob(@Field("jobId") String jobId);


    /**
     * 获取房源的配置信息
     *
     * @return
     */
    @POST("house/getFabuConf")
    Observable<ResponseModel<HouseConfigModel>> getFabuConf();


    /**
     * @param type     类型：1:求租，2：求购，3：出租，4：出售
     * @param htype    1:租房，2：售房
     * @param cityId   区域城市id
     * @param layoutId 户型id
     * @param order    排序1:价格降序,2:价格升序
     * @param page     页码
     * @return
     */
    @POST("house/houseList")
    @FormUrlEncoded
    Observable<ResponseModel<HouseListModel>> houseList(@Field("type") String type,
                                                        @Field("htype") int htype,
                                                        @Field("cityId") String cityId,
                                                        @Field("layoutId") String layoutId,
                                                        @Field("order") String order,
                                                        @Field("page") int page);

    /**
     * @param status 状态1:进行中,2:已经结束   必传
     * @param p      赏金,1:降序，2：升序
     * @param t      时间排序,1:降序，2:升序
     * @param title  问题搜索
     * @param page   页码
     * @return
     */
    @POST("question/questionList")
    @FormUrlEncoded
    Observable<ResponseModel<QuestionListModel>> questionList(@Field("status") int status,
                                                              @Field("p") String p,
                                                              @Field("t") String t,
                                                              @Field("title") String title,
                                                              @Field("page") String page);

    /**
     * @param p      赏金,1:降序，2：升序
     * @param t      时间排序,1:降序，2:升序
     * @param title  问题搜索
     * @param page   页码
     * @return
     */
    @POST("question/questionList")
    @FormUrlEncoded
    Observable<ResponseModel<QuestionListModel>> questionList(@Field("p") String p,
                                                              @Field("t") String t,
                                                              @Field("title") String title,
                                                              @Field("page") String page);

    /**
     * @param status 状态1:进行中,2:已经结束   必传
     * @param page   页码
     * @return
     */
    @POST("question/myQuestionList")
    @FormUrlEncoded
    Observable<ResponseModel<MyQuestionListModel>> myquestionList(@Field("status") int status,
                                                                  @Field("page") String page);


    @POST("house/houseList")
    @FormUrlEncoded
    Observable<ResponseModel<HouseListModel>> houseList(@Field("page") int page, @Field("title") String title);


    @POST("house/add")
    @FormUrlEncoded
    Observable<ResponseModel<Object>> houseAdd(@Field("type") String type,
                                               @Field("title") int title,
                                               @Field("countryId") String countryId,
                                               @Field("cityId") String cityId,
                                               @Field("price") String price,
                                               @Field("layout") String layout,
                                               @Field("language") String language,
                                               @Field("phone") String phone,
                                               @Field("weixin") String weixin,
                                               @Field("name") String name,
                                               @Field("sex") String sex,
                                               @Field("desc") String desc,
                                               @Field("area") String area);

    @POST("house/add")
    @FormUrlEncoded
    Observable<ResponseModel<Object>> houseAdd(@FieldMap Map<String, Object> map);

    @POST("house/edit")
    @FormUrlEncoded
    Observable<ResponseModel<Object>> houseEdit(@FieldMap Map<String, Object> map);

    @POST("house/detail")
    @FormUrlEncoded
    Observable<ResponseModel<HouseDetailModel>> houseDetail(@Field("hId") String hId, @Field("uid") String uid);


    @POST("house/myCollect")
    @FormUrlEncoded
    Observable<ResponseModel<HouseListModel>> myCollectHouse(@Field("page") int page);


    /**
     * 我发布的房屋租售
     *
     * @param page
     * @return
     */
    @POST("house/myHouseList")
    @FormUrlEncoded
    Observable<ResponseModel<HouseListModel>> myHouseList(@Field("page") int page);

    @POST("house/collect")
    @FormUrlEncoded
    Observable<ResponseModel<Object>> collectHouse(@Field("hId") String hId);

    @POST("house/cancelCollect")
    @FormUrlEncoded
    Observable<ResponseModel<Object>> cancelCollectHouse(@Field("hId") String hId);

    @POST("house/commentList")
    @FormUrlEncoded
    Observable<ResponseModel<NewsCommentModel>> houseCommentList(@Field("page") int page, @Field("hId") String hId);

    @POST("house/addComment")
    @FormUrlEncoded
    Observable<ResponseModel<Object>> addComment(@Field("content") String content, @Field("hId") String hId);

    @POST("house/addReply")
    @FormUrlEncoded
    Observable<ResponseModel<Object>> addReply(@Field("commentId") String newsId,
                                               @Field("content") String content,
                                               @Field("toUid") String toUid,
                                               @Field("replyId") String replyId);

    /**
     * 获取某条评论的回复列表
     *
     * @param commentId
     * @param page
     * @return
     */
    @POST("house/replyList")
    @FormUrlEncoded
    Observable<ResponseModel<CommentReplyModel>> replyList(@Field("commentId") String commentId, @Field("page") int page);


    /**
     * 删除回复
     *
     * @param replyId
     * @return
     */
    @POST("house/delReply")
    @FormUrlEncoded
    Observable<ResponseModel<Object>> delReply(@Field("replyId") String replyId);

    /**
     * 删除评论
     *
     * @param commentId
     * @return
     */
    @POST("house/delComment")
    @FormUrlEncoded
    Observable<ResponseModel<Object>> delComment(@Field("commentId") String commentId);


    @POST("house/myComment")
    @FormUrlEncoded
    Observable<ResponseModel<MyHouseCommentModel>> myHouseComment(@Field("page") int page);

    @POST("company/myComment")
    @FormUrlEncoded
    Observable<ResponseModel<MyCompanyCommentModel>> myConmpanyComment(@Field("page") int page);


    @POST("question/myReplyList")
    @FormUrlEncoded
    Observable<ResponseModel<MyReplyListCommentModel>> myReplyList(@Field("page") int page);

    @POST("house/del")
    @FormUrlEncoded
    Observable<ResponseModel<Object>> deleteHouse(@Field("hId") String hId);


    @POST("house/topicDetail")
    @FormUrlEncoded
    Observable<ResponseModel<MyHouseCommentModel.CommentListBean>> topicDetail(@Field("commentId") String commentId);


    /**
     * 设置房屋状态
     *
     * @param hId
     * @param status 1:关闭，2：重新开启
     * @return
     */
    @POST("house/setStatus")
    @FormUrlEncoded
    Observable<ResponseModel<Object>> setStatus(@Field("hId") String hId, @Field("status") String status);

    /**
     * 设置求职招聘状态
     *
     * @param jobId
     * @param status status 1:关闭，2：开启
     * @return
     */
    @POST("job/setJob")
    @FormUrlEncoded
    Observable<ResponseModel<Object>> setJobStatus(@Field("jobId") String jobId, @Field("status") String status);

}
