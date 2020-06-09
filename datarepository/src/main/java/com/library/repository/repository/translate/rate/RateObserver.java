package com.library.repository.repository.translate.rate;

import com.library.common.base.BaseView;
import com.library.repository.repository.translate.model.RateModel;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;


public abstract class RateObserver<T> implements Observer<RateModel<T>> {

    private BaseView mView;

    public RateObserver() {
    }

    public RateObserver(BaseView view) {
        mView = view;
    }


    @Override
    public void onSubscribe(Disposable d) {
        if (mView != null) mView.showLoading();
    }

    @Override
    public void onNext(RateModel<T> response) {
        if (mView != null) mView.hideLoading();
        if (response.getStatus() == 0) {
            onSuccess(response.getResult());
        } else {
            onError(response);
        }
    }

    @Override
    public void onError(Throwable e) {
        if (mView != null) mView.hideLoading();
        onError(new RateModel(-1, e.getMessage()));
    }

    @Override
    public void onComplete() {
        if (mView != null) mView.hideLoading();
    }


    public abstract void onSuccess(T data);

    public abstract void onError(RateModel data);


}
