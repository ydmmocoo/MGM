package com.library.common.base;

import android.app.Activity;
import android.content.Context;

import com.uber.autodispose.AutoDisposeConverter;

public interface BaseView {

    Context getCurContext();

    Activity getCurActivity();

    void showLoading();

    void hideLoading();

    void createAndShowDialog();

    void destoryAndDismissDialog();

    /**
     * 绑定Android生命周期 防止RxJava内存泄漏
     *
     * @param <T>
     * @return
     */
    <T> AutoDisposeConverter<T> bindAutoDispose();

}
