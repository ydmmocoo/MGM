package com.library.repository.repository.translate;

import com.google.gson.JsonObject;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface LanguageApi {


    /**
     * @param q     请求翻译query
     * @param from  翻译源语言
     * @param to    译文语言
     * @param appid
     * @param salt  随机数
     * @param sign  签名
     * @return
     */
    @GET("api/trans/vip/translate")
    Observable<JsonObject> translate(@Query("q") String q,
                                     @Query("from") String from,
                                     @Query("to") String to,
                                     @Query("appid") String appid,
                                     @Query("salt") String salt,
                                     @Query("sign") String sign);

}
