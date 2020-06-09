package com.fjx.mg.setting.feedback;

import android.text.TextUtils;

import com.fjx.mg.R;
import com.google.gson.JsonObject;
import com.library.common.utils.CommonToast;
import com.library.common.utils.JsonUtil;
import com.library.repository.core.net.CommonObserver;
import com.library.repository.core.net.RxScheduler;
import com.library.repository.data.MultipartBodyHBuilder;
import com.library.repository.models.ResponseModel;
import com.library.repository.repository.RepositoryFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;

public class FeedBackPresenter extends FeedBackContract.Presenter {

    public FeedBackPresenter(FeedBackContract.View view) {
        super(view);
    }


    @Override
    void updateImage(final String content, final List<String> filePaths, final int type, final String identifier) {
        if (TextUtils.isEmpty(content)) {
            CommonToast.toast(mView.getCurContext().getString(R.string.hint_feedback));
            return;
        }

        if (filePaths.isEmpty()) {
            feedback(content, "", type, identifier);
            return;
        }
        if (filePaths.contains("")) filePaths.remove("");
        final List<String> remoteUrl = new ArrayList<>();

        if (filePaths.size() == 0) {
            feedback(content, "", type, identifier);
        } else {
            mView.showLoading();
            for (int i = 0; i < filePaths.size(); i++) {

                final String key = "feedback".concat(String.valueOf(i));
                List<MultipartBody.Part> requestBody
                        = MultipartBodyHBuilder.Builder()
                        .addParams("k", key)
                        .addParams(key, new File(filePaths.get(i)))
                        .builder();

                RepositoryFactory.getRemoteRepository().uploadFile(requestBody)
                        .compose(RxScheduler.<ResponseModel<JsonObject>>toMain())
                        .as(mView.<ResponseModel<JsonObject>>bindAutoDispose())
                        .subscribe(new CommonObserver<JsonObject>() {
                            @Override
                            public void onSuccess(JsonObject data) {
                                Map<String, Object> map = JsonUtil.jsonToMap(data.toString());
                                String value = (String) map.get(key);
                                remoteUrl.add(value);
                                if (remoteUrl.size() == filePaths.size()) {
                                    mView.uploadImageSucces(remoteUrl);
                                    String urls = "";
                                    for (String url : remoteUrl) {
                                        urls = urls.concat(url).concat(",");
                                    }
                                    feedback(content, urls, type, identifier);
                                }

                            }

                            @Override
                            public void onError(ResponseModel data) {
                                mView.hideLoading();
                                CommonToast.toast(data.getMsg());
                            }

                            @Override
                            public void onNetError(ResponseModel data) {
                                CommonToast.toast(data.getMsg());
                                if (mView != null)
                                    mView.hideLoading();
                            }
                        });
            }
        }

    }

    @Override
    void feedback(String content, String filePaths, int type, String identifier) {
        if (type == -1) {
            RepositoryFactory.getRemoteRepository().feedbacks(content, filePaths, identifier)
                    .compose(RxScheduler.<ResponseModel<Object>>toMain())
                    .as(mView.<ResponseModel<Object>>bindAutoDispose())
                    .subscribe(new CommonObserver<Object>() {
                        @Override
                        public void onSuccess(Object data) {
                            mView.hideLoading();
                            CommonToast.toast(mView.getCurContext().getString(R.string.commit_success));
                            mView.feedbackSuccess();
                        }

                        @Override
                        public void onError(ResponseModel data) {
                            mView.hideLoading();
                            CommonToast.toast(data.getMsg());
                        }

                        @Override
                        public void onNetError(ResponseModel data) {
                            CommonToast.toast(data.getMsg());
                            if (mView != null)
                                mView.hideLoading();
                        }
                    });
        } else {
            RepositoryFactory.getRemoteRepository().feedback(content, filePaths, type, identifier)
                    .compose(RxScheduler.<ResponseModel<Object>>toMain())
                    .as(mView.<ResponseModel<Object>>bindAutoDispose())
                    .subscribe(new CommonObserver<Object>() {
                        @Override
                        public void onSuccess(Object data) {
                            mView.hideLoading();
                            CommonToast.toast(mView.getCurContext().getString(R.string.commit_success));
                            mView.feedbackSuccess();
                        }

                        @Override
                        public void onError(ResponseModel data) {
                            mView.hideLoading();
                            CommonToast.toast(data.getMsg());
                        }

                        @Override
                        public void onNetError(ResponseModel data) {
                            CommonToast.toast(data.getMsg());
                            if (mView != null)
                                mView.hideLoading();
                        }
                    });
        }
    }
}
