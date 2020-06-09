package com.fjx.mg.main.payment.ask;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;

import com.common.paylibrary.model.PayExtModel;
import com.common.paylibrary.model.UsagePayMode;
import com.fjx.mg.R;
import com.google.gson.JsonObject;
import com.library.common.utils.CommonToast;
import com.library.common.utils.JsonUtil;
import com.library.repository.core.net.CommonObserver;
import com.library.repository.core.net.RxScheduler;
import com.library.repository.data.MultipartBodyHBuilder;
import com.library.repository.models.ResponseModel;
import com.library.repository.models.ServicePriceModel;
import com.library.repository.repository.RepositoryFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;

public class AskPresenter extends AskContract.Presenter {
    public AskPresenter(AskContract.View view) {
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
    public void PayWatcher(EditText etSearch) {

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mView.ShowPrice("" + s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    @Override
    public void getServicePrice() {
        if (mView==null)return;
        RepositoryFactory.getRemoteRepository()
                .getServicePrice()
                .compose(RxScheduler.<ResponseModel<ServicePriceModel>>toMain())
                .as(mView.<ResponseModel<ServicePriceModel>>bindAutoDispose())
                .subscribe(new CommonObserver<ServicePriceModel>() {
                    @Override
                    public void onSuccess(ServicePriceModel model) {
                        mView.ShowServicePrice(model.getServicePrice());
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        CommonToast.toast(data.getMsg());
                    }

                    @Override
                    public void onNetError(ResponseModel data) {

                    }
                });
    }

    @Override
    public void updateImage(final String question, final String desc, final List<String> images, final String price) {
        if (TextUtils.isEmpty(question)) {
            CommonToast.toast(R.string.hint_input_question);
            return;
        }
        if (TextUtils.isEmpty(desc)) {
            CommonToast.toast(R.string.please_input_question_detail);
            return;
        }
        if (TextUtils.isEmpty(price)) {
            CommonToast.toast(R.string.please_input_question_meney);
            return;
        }
        if (images.contains("")) images.remove("");
        final List<String> remoteUrl = new ArrayList<>();

        if (images.size() == 0) {
            Map<String, Object> mapa = new HashMap<>();
            mapa.put("question", question);
            mapa.put("desc", desc);
            mapa.put("price", price);
            mapa.put("urls", "");
            String ext = JsonUtil.moderToString(new PayExtModel(UsagePayMode.question_pay, mapa));
            mView.updateImageSuccess(ext);
        } else {
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

                                    Map<String, Object> mapa = new HashMap<>();
                                    mapa.put("question", question);
                                    mapa.put("desc", desc);
                                    mapa.put("price", price);
                                    mapa.put("urls", urls.endsWith(",") ? urls.substring(0, urls.length() - 1) : urls);
                                    String ext = JsonUtil.moderToString(new PayExtModel(UsagePayMode.question_pay, mapa));
                                    mView.hideLoading();
                                    mView.updateImageSuccess(ext);
                                }

                            }

                            @Override
                            public void onError(ResponseModel data) {
                                mView.hideLoading();
                                CommonToast.toast(data.getMsg());
                            }

                            @Override
                            public void onNetError(ResponseModel data) {

                            }
                        });
            }
        }

    }


}
