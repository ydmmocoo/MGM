package com.library.repository.core.net;

import android.util.Log;

import com.library.repository.core.interceptor.CommonParamsInterceptor;

import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class HttpCenter {

    private OkHttpClient.Builder builder;
    private OkHttpClient okHttpClient;

    private HttpCenter() {
        builder = new OkHttpClient.Builder();
    }


    private static class HttpCenterHolder {
        private static HttpCenter INSTANCE = new HttpCenter();
    }

    public static HttpCenter getInstance() {
        return HttpCenterHolder.INSTANCE;
    }

    public HttpCenter addInterceptor(Interceptor interceptor) {
        builder.addInterceptor(interceptor);
        return this;
    }

    public HttpCenter connectTimeout(long timeout, TimeUnit timeUnit) {
        builder.connectTimeout(timeout, timeUnit);
        return this;

    }

    public HttpCenter readTimeout(long timeout, TimeUnit timeUnit) {
        builder.readTimeout(timeout, timeUnit);
        return this;

    }

    public HttpCenter writeTimeout(long timeout, TimeUnit timeUnit) {
        builder.writeTimeout(timeout, timeUnit);
        return this;

    }

    public HttpCenter cache(Cache cache) {
        builder.cache(cache);
        return this;
    }

    public HttpCenter customerConfig() {
        okHttpClient = builder.build();
        return this;
    }

    public void defaultConfig() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {

            @Override
            public void log(String message) {
                Log.d("AppLog", message);
            }
        });
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        okHttpClient = builder.addInterceptor(loggingInterceptor)
                .

                        addInterceptor(new CommonParamsInterceptor())
//                .addInterceptor(new CacheInterceptor(ContextManager.getContext()))
                .

                        connectTimeout(30L, TimeUnit.SECONDS)
                .

                        readTimeout(30L, TimeUnit.SECONDS)
                .

                        writeTimeout(30L, TimeUnit.SECONDS)
                .

                        retryOnConnectionFailure(true)
                .

                        build();

    }


    public OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }


    public <T> T createRepertory(String host, Class<T> clazz) {
        return RetrofitFactary.newClient(host).create(clazz);
    }


}
