package com.library.repository.repository.translate;


import android.util.Log;

import com.google.gson.JsonObject;
import com.library.common.utils.JsonUtil;
import com.library.repository.Constant;
import com.library.repository.core.net.HttpCenter;
import com.library.repository.core.net.RxScheduler;
import com.library.repository.models.LanTranslateResultM;
import com.library.repository.repository.translate.utils.MD5;
import com.library.repository.repository.translate.model.RateModel;
import com.library.repository.repository.translate.model.RateResultModel;
import com.library.repository.repository.translate.model.TranslateCurrencyM;
import com.library.repository.repository.translate.rate.RateObserver;
import com.library.repository.repository.translate.rate.TranslateListener;
import com.library.repository.repository.translate.utils.SignUtil;

//import org.jetbrains.annotations.NotNull;

import org.greenrobot.greendao.annotation.NotNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class TranslateImpl implements ITranslate {

    //腾讯云汇率 SecretId
    private final String RATE_SECRETID = "AKID6vq1vzn6nleg04zneejeVgmgbP9esa9t1l1a";
    //腾讯云汇率 SecretKey
    private final String RATE_SSECRETKEY = "emRv0rlp11Ksq1XnoyOiLXidpg588RjMmy10Zycy";

    //百度翻译api  appid
    private final String TRANSLATE_APPID = "20190603000304389";
    private final String TRANSLATE_SECURITYKEY = "SBac2Nl7iG3KcvPg2ZW4";

    private RateApi getRateApi() {
        return HttpCenter.getInstance().createRepertory(Constant.IM_TRANSLATE_HOST, RateApi.class);
    }

    private LanguageApi getLanguageApi() {
        return HttpCenter.getInstance().createRepertory(Constant.LAN_TRANSLATE_HOST, LanguageApi.class);

    }

    @Override
    public void getCurrency(final TranslateListener<List<TranslateCurrencyM>> listener) {
        String timeStr = getDateStr();
        String authen = getAuth(timeStr);
        getRateApi().getRateList(authen, timeStr)
                .compose(RxScheduler.<RateModel<List<TranslateCurrencyM>>>toMain())
                .subscribe(new RateObserver<List<TranslateCurrencyM>>() {
                    @Override
                    public void onSuccess(List<TranslateCurrencyM> data) {
                        listener.onSuccess(data);
                    }

                    @Override
                    public void onError(RateModel data) {
                        listener.onError(data.getMsg());
                    }
                });
    }


    @Override
    public void convert(String amount, String from, String to, final TranslateListener<RateResultModel> listener) {
        String timeStr = getDateStr();
        String authen = getAuth(timeStr);
        getRateApi().convert(authen, timeStr, amount, from, to)
                .compose(RxScheduler.<RateModel<RateResultModel>>toMain())
                .subscribe(new RateObserver<RateResultModel>() {
                    @Override
                    public void onSuccess(RateResultModel data) {
                        listener.onSuccess(data);
                        Log.d("convert", JsonUtil.moderToString(data));
                    }

                    @Override
                    public void onError(RateModel data) {
                        listener.onError(data.getMsg());
                    }
                });
    }

    @Override
    public void translate(String q, String from, String to, final TranslateListener<LanTranslateResultM> listener) {
        String salt = String.valueOf(System.currentTimeMillis());
        String src = TRANSLATE_APPID + q + salt + TRANSLATE_SECURITYKEY; // 加密前的原文
        String sign = MD5.md5(src);
        getLanguageApi().translate(q, from, to, TRANSLATE_APPID, salt, sign)
                .compose(RxScheduler.<JsonObject>toMain())
                .subscribe(new Observer<JsonObject>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(JsonObject object) {
                        Log.d("translate", object.toString());
                        if (object.has("error_msg")) {
                            String error_msg = object.get("error_msg").toString();
                            listener.onError(error_msg);

                        } else {
                            LanTranslateResultM translateM = JsonUtil.strToModel(object.toString(), LanTranslateResultM.class);
                            listener.onSuccess(translateM);
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        listener.onError(e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    @NotNull
    private String getAuth(String timeStr) {
        String sig = SignUtil.sign(RATE_SSECRETKEY, timeStr);
        return "hmac id=\"" + RATE_SECRETID + "\", algorithm=\"hmac-sha1\", headers=\"date source\", signature=\"" + sig + "\"";
    }

    private String getDateStr() {
        Calendar cd = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss 'GMT'", Locale.US);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        return sdf.format(cd.getTime());
    }


}
