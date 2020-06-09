package com.fjx.mg.main.payment.ask.editquestion;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;

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

public class EditPresenter extends EditContract.Presenter {
    EditPresenter(EditContract.View view) {
        super(view);
    }

    @Override
    public void searchWatcher(EditText etSearch) {

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mView.ShowNum("" + s.toString().length());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }


    @Override
    public void updateImage(final String question, final String desc, final List<String> images, final String qid) {
        if (TextUtils.isEmpty(question)) {
            CommonToast.toast("请输入问题");
            return;
        }
        if (TextUtils.isEmpty(desc)) {
            CommonToast.toast("请输入问题详情");
            return;
        }

        if (images.isEmpty()) {
            CommonToast.toast("请上传上传图片");
            return;
        }
        if (images.contains("")) images.remove("");
        final List<String> remoteUrl = new ArrayList<>();
        mView.showLoading();
        for (int i = 0; i < images.size(); i++) {

            final String key = "feedback".concat(String.valueOf(i));
            List<MultipartBody.Part> requestBody
                    = MultipartBodyHBuilder.Builder()
                    .addParams("k", key)
                    .addParams(key, new File(images.get(i)))
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
                            if (remoteUrl.size() == images.size()) {
                                String urls = "";
                                for (String url : remoteUrl) {
                                    urls = urls.concat(url).concat(",");
                                }
                                mView.hideLoading();
                                QuestionEdit(question, desc, urls.endsWith(",") ? urls.substring(0, urls.length() - 1) : urls, qid);
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

    private void QuestionEdit(String question, String desc, String urls, String qid) {
        RepositoryFactory.getRemoteRepository().setQuestionEdit(qid, question, desc, urls)
                .compose(RxScheduler.<ResponseModel<Object>>toMain())
                .as(mView.<ResponseModel<Object>>bindAutoDispose())
                .subscribe(new CommonObserver<Object>() {
                    @Override
                    public void onSuccess(Object data) {
                        mView.hideLoading();
                        mView.voidEditSuccess();
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
