package com.library.repository.core.net;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitFactary {

    public static Retrofit newClient(String host) {
        return new Retrofit.Builder()
                .baseUrl(host)  //自己配置
                .client(HttpCenter.getInstance().getOkHttpClient())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}
