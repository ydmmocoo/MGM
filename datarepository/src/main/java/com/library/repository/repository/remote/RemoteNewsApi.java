package com.library.repository.repository.remote;

import com.library.repository.cache.CacheHeaders;
import com.library.repository.models.CommentReplyModel;
import com.library.repository.models.MyNewsCommentModel;
import com.library.repository.models.NewsCommentModel;
import com.library.repository.models.NewsDetailModel;
import com.library.repository.models.NewsItemModel;
import com.library.repository.models.NewsTypeModel;
import com.library.repository.models.QuestionReplyModel;
import com.library.repository.models.ResponseModel;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface RemoteNewsApi {

    /**
     * 获取新闻资讯的列别
     *
     * @return
     */
    @Headers(CacheHeaders.NORMAL)
    @POST("news/newsTypeList")
    Observable<ResponseModel<List<NewsTypeModel>>> newsTypeList();


    /**
     * 获取头条文章列表
     *
     * @param typeId 类型id
     * @param page   页数
     * @param title  搜索标题
     * @return
     */
//    @Headers(CacheHeaders.NORMAL)
    @POST("news/newsList")
    @FormUrlEncoded
    Observable<ResponseModel<NewsItemModel>> newsList(@Field("typeId") int typeId, @Field("page") int page, @Field("title") String title);

    @POST("news/newsList")
    @FormUrlEncoded
    Observable<ResponseModel<NewsItemModel>> newsList(@Field("page") int page, @Field("title") String title);


    /**
     * 获取新闻资讯详情
     *
     * @param newsId
     * @return
     */
    @Headers(CacheHeaders.NORMAL)
    @POST("news/newsDetail")
    @FormUrlEncoded
    Observable<ResponseModel<NewsDetailModel>> newsDetail(@Field("newsId") String newsId, @Field("uid") String uid);


    /**
     * 获取资讯评论列表
     *
     * @param newsId
     * @param page
     * @return
     */
    @Headers(CacheHeaders.NORMAL)
    @POST("news/commentList")
    @FormUrlEncoded
    Observable<ResponseModel<NewsCommentModel>> commentList(@Field("newsId") String newsId, @Field("page") int page);

    /**
     * 提问的问答列表
     *
     * @param qId
     * @param page
     * @return
     */
    @Headers(CacheHeaders.NORMAL)
    @POST("question/replyList")
    @FormUrlEncoded
    Observable<ResponseModel<QuestionReplyModel>> QuestionReplyList(@Field("qId") String qId, @Field("page") int page);


    /**
     * 记录用户浏览应用
     *
     * @param appId
     * @return
     */
    @Headers(CacheHeaders.NORMAL)
    @POST("stat/recUseApp")
    @FormUrlEncoded
    Observable<ResponseModel<Object>> recUseApp(@Field("appId") String appId);

    /**
     * 获取黄页资讯评论列表
     *
     * @param newsId
     * @param page
     * @return
     */
    @Headers(CacheHeaders.NORMAL)
    @POST("company/commentList")
    @FormUrlEncoded
    Observable<ResponseModel<NewsCommentModel>> companycommentList(@Field("cId") String newsId, @Field("page") int page);

    /**
     * 评论
     *
     * @param newsId
     * @param content
     * @return
     */
    @POST("news/addComment")
    @FormUrlEncoded
    Observable<ResponseModel<Object>> addComment(@Field("newsId") String newsId, @Field("content") String content);

    /**
     * 黄页评论
     *
     * @param newsId
     * @param content
     * @return
     */
    @POST("company/addComment")
    @FormUrlEncoded
    Observable<ResponseModel<Object>> addCompanyComment(@Field("cId") String newsId, @Field("content") String content);

    /**
     * @param newsId  评论记录id
     * @param content 回复内容
     * @param toUid   对那个用户进行回复
     * @param replyId 对某个回复的内容进行在回复
     * @return
     */
    @POST("news/addReply")
    @FormUrlEncoded
    Observable<ResponseModel<Object>> addReply(@Field("commentId") String newsId,
                                               @Field("content") String content,
                                               @Field("toUid") String toUid,
                                               @Field("replyId") String replyId);

    /**
     * @param newsId  评论记录id
     * @param content 回复内容
     * @param toUid   对那个用户进行回复
     * @param replyId 对某个回复的内容进行在回复
     * @return
     */
    @POST("company/addReply")
    @FormUrlEncoded
    Observable<ResponseModel<Object>> companyaddReply(@Field("commentId") String newsId,
                                                      @Field("content") String content,
                                                      @Field("toUid") String toUid,
                                                      @Field("replyId") String replyId);

    /**
     * 获取黄页某条评论的回复列表
     *
     * @param commentId
     * @param page
     * @return
     */
    @Headers(CacheHeaders.NORMAL)
    @POST("company/replyList")
    @FormUrlEncoded
    Observable<ResponseModel<CommentReplyModel>> companyreplyList(@Field("commentId") String commentId, @Field("page") int page);

    /**
     * 获取某条评论的回复列表
     *
     * @param commentId
     * @param page
     * @return
     */
    @Headers(CacheHeaders.NORMAL)
    @POST("news/replyList")
    @FormUrlEncoded
    Observable<ResponseModel<CommentReplyModel>> replyList(@Field("commentId") String commentId, @Field("page") int page);

    /**
     * 获取黄页 层主评论详情
     *
     * @param commentId
     * @return
     */
    @Headers(CacheHeaders.NORMAL)
    @POST("company/topicDetail")
    @FormUrlEncoded
    Observable<ResponseModel<NewsCommentModel.CommentListBean>> companytopicDetail(@Field("commentId") String commentId);

    /**
     * 获取层主评论详情
     *
     * @param commentId
     * @return
     */
    @Headers(CacheHeaders.NORMAL)
    @POST("news/topicDetail")
    @FormUrlEncoded
    Observable<ResponseModel<NewsCommentModel.CommentListBean>> topicDetail(@Field("commentId") String commentId);

    /**
     * 收藏
     *
     * @param newsId
     * @return
     */
    @POST("news/collectNews")
    @FormUrlEncoded
    Observable<ResponseModel<Object>> collectNews(@Field("newsId") String newsId);

    /**
     * 取消收藏
     *
     * @param newsId
     * @return
     */
    @POST("news/cancelCollectNews")
    @FormUrlEncoded
    Observable<ResponseModel<Object>> cancelCollectNews(@Field("newsId") String newsId);

    /**
     * 黄页评论点赞或者取消回复点赞
     *
     * @param commentId 评论id
     * @param replyId   回复id
     * @return
     */
    @POST("company/praise")
    @FormUrlEncoded
    Observable<ResponseModel<Object>> companypraiseComment(@Field("commentId") String commentId, @Field("replyId") String replyId);


    /**
     * 黄页取消评论点赞或者取消回复点赞
     *
     * @param commentId 评论id
     * @param replyId   回复id
     * @return
     */
    @POST("company/cancelPraise")
    @FormUrlEncoded
    Observable<ResponseModel<Object>> companycancelPraise(@Field("commentId") String commentId, @Field("replyId") String replyId);


    /**
     * 朋友圈点赞
     *
     * @param mId 评论id
     * @return
     */
    @POST("moments/praise")
    @FormUrlEncoded
    Observable<ResponseModel<Object>> momentSpraise(@Field("mId") String mId);


    /**
     * 删除朋友圈
     *
     * @param mId 朋友圈记录id
     * @return
     */
    @POST("moments/del")
    @FormUrlEncoded
    Observable<ResponseModel<Object>> MomentsDel(@Field("mId") String mId);


    /**
     * 删除评论
     *
     * @param rId 回复记录id
     * @return
     */
    @POST("moments/delReply")
    @FormUrlEncoded
    Observable<ResponseModel<Object>> MomentsDelReply(@Field("rId") String rId);

    /**
     * 朋友圈去赞
     *
     * @param mId 回复id
     * @return
     */
    @POST("moments/cancelPraise")
    @FormUrlEncoded
    Observable<ResponseModel<Object>> momentsCancelPraise(@Field("mId") String mId);


    /**
     * 评论点赞或者取消回复点赞
     *
     * @param commentId 评论id
     * @param replyId   回复id
     * @return
     */
    @POST("news/praise")
    @FormUrlEncoded
    Observable<ResponseModel<Object>> praiseComment(@Field("commentId") String commentId, @Field("replyId") String replyId);


    /**
     * 取消评论点赞或者取消回复点赞
     *
     * @param commentId 评论id
     * @param replyId   回复id
     * @return
     */
    @POST("news/cancelPraise")
    @FormUrlEncoded
    Observable<ResponseModel<Object>> cancelPraise(@Field("commentId") String commentId, @Field("replyId") String replyId);


    /**
     * 删除回复
     *
     * @param replyId
     * @return
     */
    @POST("news/delReply")
    @FormUrlEncoded
    Observable<ResponseModel<Object>> delReply(@Field("replyId") String replyId);

    /**
     * 删除黄页回复
     *
     * @param replyId
     * @return
     */
    @POST("company/delReply")
    @FormUrlEncoded
    Observable<ResponseModel<Object>> companydelReply(@Field("replyId") String replyId);

    /**
     * 删除评论
     *
     * @param commentId
     * @return
     */
    @POST("news/delComment")
    @FormUrlEncoded
    Observable<ResponseModel<Object>> delComment(@Field("commentId") String commentId);

    /**
     * 删除黄页评论
     *
     * @param commentId
     * @return
     */
    @POST("company/delComment")
    @FormUrlEncoded
    Observable<ResponseModel<Object>> companydelComment(@Field("commentId") String commentId);

    /**
     * 删除有偿问答评论
     *
     * @param rid
     * @return
     */
    @POST("question/delReply")
    @FormUrlEncoded
    Observable<ResponseModel<Object>> delQuestionReply(@Field("rId") String rid);

    /**
     * 更新新闻阅读量
     *
     * @param newsId
     * @return
     */
    @POST("news/updateReadNum")
    @FormUrlEncoded
    Observable<ResponseModel<Object>> updateReadNum(@Field("newsId") String newsId);

    @Headers(CacheHeaders.NORMAL)
    @POST("news/myCollect")
    @FormUrlEncoded
    Observable<ResponseModel<NewsItemModel>> myCollect(@Field("page") int page);

    @Headers(CacheHeaders.NORMAL)
    @POST("news/myComment")
    @FormUrlEncoded
    Observable<ResponseModel<MyNewsCommentModel>> myNewsComment(@Field("page") int page);

}
