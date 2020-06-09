package com.fjx.mg.food.presenter;

import android.util.Log;

import com.fjx.mg.food.contract.ChooseCouponContract;
import com.fjx.mg.food.contract.OrderEvaluateContract;
import com.google.gson.JsonObject;
import com.library.common.utils.CommonToast;
import com.library.common.utils.JsonUtil;
import com.library.common.utils.StringUtil;
import com.library.repository.Constant;
import com.library.repository.core.net.CommonObserver;
import com.library.repository.core.net.RxScheduler;
import com.library.repository.data.MultipartBodyHBuilder;
import com.library.repository.models.CouponBean;
import com.library.repository.models.ResponseModel;
import com.library.repository.repository.RepositoryFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;

/**
 * @author yedeman
 * @date 2020/5/20.
 * email：ydmmocoo@gmail.com
 * description：
 */
public class OrderEvaluatePresenter extends OrderEvaluateContract.Presenter {

    public OrderEvaluatePresenter(OrderEvaluateContract.View view) {
        super(view);
    }

    @Override
    public void addEvaluate(String oId, String globalScore, String tasteScore, String packageScore, String deliveryScore, String content, String img) {
        RepositoryFactory.getRemoteFoodApi().addEvaluate(oId,globalScore,tasteScore,packageScore,deliveryScore,content,img)
                .compose(RxScheduler.<ResponseModel<Object>>toMain())
                .as(mView.<ResponseModel<Object>>bindAutoDispose())
                .subscribe(new CommonObserver<Object>() {
                    @Override
                    public void onSuccess(Object data) {
                        mView.hideLoading();
                        if (mView != null) {
                            mView.addEvaluateSuccess();
                        }
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        CommonToast.toast(data.getMsg());
                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                        CommonToast.toast(data.getMsg());
                    }
                });
    }

    @Override
    public void updateImage(List<String> filePaths, String oId, String globalScore, String tasteScore, String packageScore, String deliveryScore, String content) {
        filePaths.remove("");
        if (filePaths==null||filePaths.size()==0){
            addEvaluate(oId,globalScore,tasteScore,packageScore,deliveryScore,content,"");
            return;
        }
        mView.showLoading();
        List<String> remoteUrl = new ArrayList<>();
        for (int i=0;i<filePaths.size();i++) {
            List<MultipartBody.Part> requestBody
                    = MultipartBodyHBuilder.Builder()
                    .addParams("k", "image")
                    .addParams("image", new File(filePaths.get(i)))
                    .builder();
            mView.createAndShowDialog();
            RepositoryFactory.getRemoteRepository().uploadFile(requestBody)
                    .compose(RxScheduler.<ResponseModel<JsonObject>>toMain())
                    .as(mView.<ResponseModel<JsonObject>>bindAutoDispose())
                    .subscribe(new CommonObserver<JsonObject>() {
                        @Override
                        public void onSuccess(JsonObject data) {
                            Map<String, Object> map = JsonUtil.jsonToMap(data.toString());
                            String value = (String) map.get("image");
                            remoteUrl.add(value);
                            if (remoteUrl.size() == filePaths.size()) {
                                String urls = "";
                                for (String url : remoteUrl) {
                                    urls = urls.concat(url).concat(",");
                                }
                                addEvaluate(oId,globalScore,tasteScore,packageScore,deliveryScore,content,urls);
                            }
                        }

                        @Override
                        public void onError(ResponseModel data) {
                            mView.destoryAndDismissDialog();
                            if (StringUtil.isNotEmpty(data.getMsg())) {
                                CommonToast.toast(data.getMsg());
                            }
                        }

                        @Override
                        public void onNetError(ResponseModel data) {
                            mView.destoryAndDismissDialog();
                        }
                    });
        }
    }
}
