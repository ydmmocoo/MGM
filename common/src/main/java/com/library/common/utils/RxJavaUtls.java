package com.library.common.utils;

import android.annotation.SuppressLint;


import com.library.common.callback.OnUiCallback;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

/**
 * Created by WYiang on 2017/10/28.
 */

public class RxJavaUtls {

    @SuppressLint("CheckResult")
    public static void runOnUiThread(final OnUiCallback callback) {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@io.reactivex.annotations.NonNull ObservableEmitter<Integer> e) throws Exception {
                e.onNext(1);
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull Integer integer) throws Exception {
                callback.onUiThread();
            }
        });
    }


}
