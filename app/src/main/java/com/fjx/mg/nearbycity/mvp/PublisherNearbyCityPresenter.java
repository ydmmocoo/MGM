package com.fjx.mg.nearbycity.mvp;


import android.text.TextUtils;
import android.util.Log;

import com.fjx.mg.R;
import com.google.gson.JsonObject;
import com.library.common.utils.CommonToast;
import com.library.common.utils.JsonUtil;
import com.library.common.utils.StringUtil;
import com.library.repository.core.net.CommonObserver;
import com.library.repository.core.net.RxScheduler;
import com.library.repository.data.MultipartBodyHBuilder;
import com.library.repository.db.base.DBHelper;
import com.library.repository.db.model.DaoSession;
import com.library.repository.models.NearbyCityConfigModel;
import com.library.repository.models.NearbyCityConfigModelDao;
import com.library.repository.models.ResponseModel;
import com.library.repository.repository.RepositoryFactory;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;

public class PublisherNearbyCityPresenter extends PublisherNearbyCityContract.Presenter {

    public PublisherNearbyCityPresenter(PublisherNearbyCityContract.View view) {
        super(view);
    }

    @Override
    public void requestConfig() {
        RepositoryFactory.getRemoteNearbyCitysApi()
                .getConf()
                .compose(RxScheduler.<ResponseModel<NearbyCityConfigModel>>toMain())
                .as(mView.<ResponseModel<NearbyCityConfigModel>>bindAutoDispose())
                .subscribe(new CommonObserver<NearbyCityConfigModel>() {
                    @Override
                    public void onSuccess(NearbyCityConfigModel data) {
                        if (mView != null) {
                            mView.responseConfigDatas(data);
                            DaoSession daoSession = DBHelper.getInstance().getDaoSession();
                            NearbyCityConfigModelDao dao = daoSession.getNearbyCityConfigModelDao();
                            if (dao.loadAll().isEmpty()) {
                                dao.insertOrReplace(data);
                            } else {
                                dao.deleteAll();
                                dao.insertOrReplace(data);
                            }
                        }
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        if (mView != null) {
                            mView.responseFailed(data);
                        }
                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                        if (mView != null) {
                            mView.responseFailed(data);
                        }
                    }
                });
    }

    @Override
    public void updateImage(String content, final List<LocalMedia> selectList) {
        if (TextUtils.isEmpty(content)) {
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
            List<MultipartBody.Part> requestBody
                    = MultipartBodyHBuilder.Builder()
                    .addParams("k", key)
                    .addParams(key, new File(pictureType == PictureConfig.TYPE_VIDEO ? selectList.get(i).getPath() : selectList.get(i).getCompressPath()))
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
                            if (StringUtil.isNotEmpty(data.getMsg())) {
                                CommonToast.toast(data.getMsg());
                            }

                        }

                        @Override
                        public void onNetError(ResponseModel data) {
                            mView.destoryAndDismissDialog();
                            if (StringUtil.isNotEmpty(data.getMsg())) {
                                CommonToast.toast(data.getMsg());
                            }
                        }
                    });
        }

    }

}
