package com.library.repository.repository.translate.rate;

public interface TranslateListener<T> {
    void onSuccess(T t);

    void onError(String code);
}
