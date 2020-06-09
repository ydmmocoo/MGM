package com.library.repository.repository.translate;

import com.library.repository.repository.translate.model.RateModel;
import com.library.repository.repository.translate.model.RateResultModel;
import com.library.repository.repository.translate.model.TranslateCurrencyM;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface RateApi {


    @Headers({
            "Accept: text/html, */*; q=0.01",
            "Source: source",
            "X-Requested-With: XMLHttpRequest"
    })
    @GET("release/exchange/currency")
    Observable<RateModel<List<TranslateCurrencyM>>> getRateList(@Header("Authorization") String auth, @Header("Date") String date);

    @Headers({
            "Accept: text/html, */*; q=0.01",
            "Source: source",
            "X-Requested-With: XMLHttpRequest"
    })
    @GET("release/exchange/convert")
    Observable<RateModel<RateResultModel>> convert(@Header("Authorization") String auth,
                                                   @Header("Date") String date,
                                                   @Query("amount") String amount,
                                                   @Query("from") String from,
                                                   @Query("to") String to);


}
