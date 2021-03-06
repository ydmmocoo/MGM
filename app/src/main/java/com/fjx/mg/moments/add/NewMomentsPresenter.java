package com.fjx.mg.moments.add;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;

import com.fjx.mg.R;
import com.fjx.mg.utils.HttpUtil;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.gson.JsonObject;
import com.library.common.utils.CommonToast;
import com.library.common.utils.ContextManager;
import com.library.common.utils.JsonUtil;
import com.library.common.utils.LogTUtil;
import com.library.repository.core.net.CommonObserver;
import com.library.repository.core.net.RxScheduler;
import com.library.repository.data.MultipartBodyHBuilder;
import com.library.repository.models.GoogleMapGeocodeSearchBean;
import com.library.repository.models.ResponseModel;
import com.library.repository.models.TypeListModel;
import com.library.repository.repository.RepositoryFactory;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;

class NewMomentsPresenter extends NewMomentsContract.Presenter {

    NewMomentsPresenter(NewMomentsContract.View view) {
        super(view);
    }

    @Override
    void getType() {
        RepositoryFactory.getRemoteRepository()
                .getType()
                .compose(RxScheduler.<ResponseModel<TypeListModel>>toMain())
                .as(mView.<ResponseModel<TypeListModel>>bindAutoDispose())
                .subscribe(new CommonObserver<TypeListModel>() {
                    @Override
                    public void onSuccess(TypeListModel model) {
                        if (model.getTypeList() != null && mView != null) {
                            mView.ShowTypeList(model.getTypeList());
                        }
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
    void updateImage(final String content, final List<LocalMedia> selectList) {
        if (TextUtils.isEmpty(content)&&selectList.size() == 0) {
            CommonToast.toast(R.string.please_input_thoughts_at_the_moment);
            return;
        }
        if (selectList.size() == 0) {
            mView.updateImageSuccess("");
            return;
        }
        final List<String> remoteUrl = new ArrayList<>();
        for (int i = 0; i < selectList.size(); i++) {
            mView.createAndShowDialog();

            int pictureType = PictureMimeType.isPictureType(selectList.get(i).getPictureType());
            final String key = pictureType == PictureConfig.TYPE_VIDEO ? "video" : "image".concat(String.valueOf(i));
            File file = new File(pictureType == PictureConfig.TYPE_VIDEO ? selectList.get(i).getPath() : selectList.get(i).getCompressPath());
            LogTUtil.i("", file.getPath());
            List<MultipartBody.Part> requestBody
                    = MultipartBodyHBuilder.Builder()
                    .addParams("k", key)
                    .addParams(key, file)
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
                            if (remoteUrl.size() == selectList.size()) {
                                String urls = "";
                                for (String url : remoteUrl) {
                                    urls = urls.concat(url).concat(",");
                                }
                                Log.e("图片", "" + urls.substring(0, urls.length() - 1));
                                mView.updateImageSuccess(urls.endsWith(",") ? urls.substring(0, urls.length() - 1) : urls);
                                mView.destoryAndDismissDialog();
                            }

                        }

                        @Override
                        public void onError(ResponseModel data) {
                            mView.destoryAndDismissDialog();
                            CommonToast.toast(data.getMsg());

                        }

                        @Override
                        public void onNetError(ResponseModel data) {
                            mView.destoryAndDismissDialog();
                            CommonToast.toast(data.getMsg());
                        }
                    });
        }


    }

    void updateImage(final String content, File file, int i, final int size) {
        final List<String> remoteUrl = new ArrayList<>();
        mView.createAndShowDialog();

        final String key = "image".concat(String.valueOf(i));
        List<MultipartBody.Part> requestBody
                = MultipartBodyHBuilder.Builder()
                .addParams("k", key)
                .addParams(key, file)
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
                        if (remoteUrl.size() == size) {
                            String urls = "";
                            for (String url : remoteUrl) {
                                urls = urls.concat(url).concat(",");
                            }
                            Log.e("图片", "" + urls.substring(0, urls.length() - 1));
                            mView.updateImageSuccess(urls.endsWith(",") ? urls.substring(0, urls.length() - 1) : urls);
                            mView.destoryAndDismissDialog();
                        }

                    }

                    @Override
                    public void onError(ResponseModel data) {
                        mView.destoryAndDismissDialog();
                        CommonToast.toast(data.getMsg());

                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                        mView.destoryAndDismissDialog();
                        CommonToast.toast(data.getMsg());
                    }
                });
    }


    @Override
    void AddMoments(String content, String type, String tIds, String showType, String address, String lng, String lat, String urls) {
        mView.createAndShowDialog();
        RepositoryFactory.getRemoteRepository().addMoments(content, type, tIds.endsWith(",") ? tIds.substring(0, tIds.length() - 1) : tIds, showType, address, lng, lat, urls)
                .compose(RxScheduler.<ResponseModel<Object>>toMain())
                .as(mView.<ResponseModel<Object>>bindAutoDispose())
                .subscribe(new CommonObserver<Object>() {
                    @Override
                    public void onSuccess(Object data) {
                        mView.destoryAndDismissDialog();
                        mView.MomentsAddSuccess();
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        mView.destoryAndDismissDialog();
                        CommonToast.toast(data.getMsg());
                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                        mView.destoryAndDismissDialog();
                        CommonToast.toast(data.getMsg());
                    }
                });
    }

    void shareAddMoments(String content, String type, String tIds, String showType, String address, String lng, String lat, String urls, String shareType, String id) {
        mView.createAndShowDialog();
        RepositoryFactory.getRemoteRepository()
                .shareMoments(content, type, tIds.endsWith(",") ? tIds.substring(0, tIds.length() - 1) : tIds
                        , showType, address, lng, lat, urls
                        , shareType, id)
                .compose(RxScheduler.<ResponseModel<Object>>toMain())
                .as(mView.<ResponseModel<Object>>bindAutoDispose())
                .subscribe(new CommonObserver<Object>() {
                    @Override
                    public void onSuccess(Object data) {
                        if (mView != null) {
                            mView.destoryAndDismissDialog();
                            mView.MomentsAddSuccess();
                        }
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        if (mView != null) {
                            mView.destoryAndDismissDialog();
                        }
                        CommonToast.toast(data.getMsg());
                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                        mView.destoryAndDismissDialog();
                        CommonToast.toast(data.getMsg());
                    }
                });
    }
}

