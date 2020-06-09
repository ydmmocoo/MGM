package com.fjx.mg.main.more.search;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;

import com.library.repository.core.net.CommonObserver;
import com.library.repository.core.net.RxScheduler;
import com.library.repository.models.ResponseModel;
import com.library.repository.repository.RepositoryFactory;

public class SearchAppPresenter extends SearchAppContract.Presenter {
    public SearchAppPresenter(SearchAppContract.View view) {
        super(view);
    }

    @Override
    void bindTextWatcher(EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mView.watchSearch(!TextUtils.isEmpty(s.toString()) ? s.toString() : "");
            }

            @Override
            public void afterTextChanged(Editable s) {
                mView.showClearImage(!TextUtils.isEmpty(s.toString()));
            }
        });
    }

    @Override
    void recUseApp(final String appId) {
        RepositoryFactory.getRemoteNewsRepository()
                .recUseApp(appId)
                .compose(RxScheduler.<ResponseModel<Object>>toMain())
                .as(mView.<ResponseModel<Object>>bindAutoDispose())
                .subscribe(new CommonObserver<Object>() {
                    @Override
                    public void onSuccess(Object data) {
                        mView.showUsed(appId);
                    }

                    @Override
                    public void onError(ResponseModel data) {
                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                        mView.hideLoading();

                    }
                });
    }
}
