package com.fjx.mg.main.payment.answer;

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

class AnswerPresenter extends AnswerContract.Presenter {

    AnswerPresenter(AnswerContract.View view) {
        super(view);
    }


    @Override
    void updateImage(final String qId, final String content, final List<String> filePaths) {
        if (TextUtils.isEmpty(content)) {
            CommonToast.toast(R.string.please_input_answer_content);
            return;
        }

        if (filePaths.isEmpty()) {
            Reply(qId, content, "");
            return;
        }
        if (filePaths.contains("")) filePaths.remove("");
        final List<String> remoteUrl = new ArrayList<>();


        if (filePaths.size() == 0) {
            Reply(qId, content, "");
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
                                    String urls = "";
                                    for (String url : remoteUrl) {
                                        urls = urls.concat(url).concat(",");
                                    }
                                    mView.hideLoading();
                                    Reply(qId, content, urls.endsWith(",") ? urls.substring(0, urls.length() - 1) : urls);
                                }

                            }

                            @Override
                            public void onError(ResponseModel data) {
                                mView.hideLoading();
                                CommonToast.toast(data.getMsg());
                            }

                            @Override
                            public void onNetError(ResponseModel data) {
                                mView.hideLoading();
                            }
                        });
            }
        }


    }

    private void Reply(String qId, String content, String images) {
        RepositoryFactory.getRemoteRepository().QuestionReply(qId, content, images)
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
                        mView.hideLoading();
                    }
                });
    }
}

