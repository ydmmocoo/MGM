package com.library.common.base;

public abstract class BasePresenter<V extends BaseView> {
    protected V mView;

    public BasePresenter(V view) {
        this.mView = view;
    }


    void detachView() {
        mView = null;
    }


}
