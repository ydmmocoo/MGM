package com.fjx.mg.utils;

import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.library.common.base.BaseApp;
import com.library.common.utils.AppUtil;
import com.library.common.utils.CommonToast;
import com.library.common.utils.ContextManager;
import com.library.common.utils.DeviceInfoUtils;
import com.library.common.utils.JsonUtil;
import com.library.common.utils.NetworkUtil;
import com.library.common.utils.StringUtil;
import com.library.repository.Constant;
import com.library.repository.core.interceptor.CommonParamsInterceptor;
import com.library.repository.core.net.CommonObserver;
import com.library.repository.core.net.RxScheduler;
import com.library.repository.data.MultipartBodyHBuilder;
import com.library.repository.data.UserCenter;
import com.library.repository.models.ResponseModel;
import com.library.repository.repository.RepositoryFactory;
import com.luck.picture.lib.config.PictureConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Author    by hanlz
 * Date      on 2019/10/21.
 * Description：网络请求
 */
public class HttpUtil {
    public void sendPost(String url, final OnRequestListener listener) {
        if (!new NetworkUtil().isNetworkConnected(ContextManager.getContext())) {
            if (listener != null) {
                listener.onFailed();
            }
        }
        String version = AppUtil.getVersionCode() + "";
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
        String token = UserCenter.getToken();
        String l = RepositoryFactory.getLocalRepository().getLangugeType();
        String deviceNo = DeviceInfoUtils.getAndroidId(BaseApp.getInstance());
        String deviceType = "1";//1:安卓、2:ios
        //添加公共参数
        FormBody.Builder boby = new FormBody.Builder()
                .add("version", version)
                .add("timestamp", timestamp)
                .add("l", l)
                .add("deviceNo", deviceNo)
                .add("deviceType", deviceType);
        if (!TextUtils.isEmpty(token)) {
            boby.add("token", token);
        }
        FormBody formBody = boby.build();
        //添加所有的参数之后，需要根据现有参数再生成一个加密字符串sign,然后把sign加入参数，传给后端
        String sign = getSignValue(formBody);
        boby.add("sign", sign);
        formBody = boby.build();
        //请求组合创建
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Log.d("AppLog", message);
            }
        });
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (listener != null) {
                    listener.onFailed();
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (listener != null) {
                    listener.onSuccess(response.body().string());
                    listener.onSuccess(response);
                }
            }
        });

    }

    public void sendPost(String url, String type, final OnRequestListener listener) {
        String version = AppUtil.getVersionCode() + "";
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
        String token = UserCenter.getToken();
        String l = RepositoryFactory.getLocalRepository().getLangugeType();
        String deviceNo = DeviceInfoUtils.getAndroidId(BaseApp.getInstance());
        String deviceType = "1";//1:安卓、2:ios
        //添加公共参数
        FormBody.Builder boby = new FormBody.Builder()
                .add("version", version)
                .add("timestamp", timestamp)
                .add("l", l)
                .add("deviceNo", deviceNo)
                .add("deviceType", deviceType)
                .add("type", type);
        if (!TextUtils.isEmpty(token)) {
            boby.add("token", token);
        }
        FormBody formBody = boby.build();
        //添加所有的参数之后，需要根据现有参数再生成一个加密字符串sign,然后把sign加入参数，传给后端
        String sign = getSignValue(formBody);
        boby.add("sign", sign);
        formBody = boby.build();
        //请求组合创建
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (listener != null) {
                    listener.onFailed();
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (listener != null) {
                    listener.onSuccess(response.body().string());
                }
            }
        });

    }

    public void sendPost(String url, Map<String, String> map, Callback callback) {
        String version = AppUtil.getVersionCode() + "";
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
        String token = UserCenter.getToken();
        String l = RepositoryFactory.getLocalRepository().getLangugeType();
        String deviceNo = DeviceInfoUtils.getAndroidId(BaseApp.getInstance());
        String deviceType = "1";//1:安卓、2:ios
        //添加公共参数
        FormBody.Builder boby = new FormBody.Builder()
                .add("version", version)
                .add("timestamp", timestamp)
                .add("l", l)
                .add("deviceNo", deviceNo)
                .add("deviceType", deviceType);
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String mapKey = entry.getKey();
            String mapValue = entry.getValue();
            boby.add(mapKey, mapValue);
        }
        if (!TextUtils.isEmpty(token)) {
            boby.add("token", token);
        }
        FormBody formBody = boby.build();
        //添加所有的参数之后，需要根据现有参数再生成一个加密字符串sign,然后把sign加入参数，传给后端
        String sign = getSignValue(formBody);
        boby.add("sign", sign);
        formBody = boby.build();
        //请求组合创建
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Log.d("AppLog", message);
            }
        });
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        client.newCall(request).enqueue(callback);

    }

    private String getSignValue(FormBody formBody) {
        Map<String, Object> map = new HashMap<>();
        for (int i = 0; i < formBody.size(); i++) {
            map.put(formBody.name(i), formBody.value(i));
        }

        return getString(map);
    }

    private String getString(Map<String, Object> paramsMap) {
        TreeMap<String, Object> tm = new TreeMap<>(paramsMap);
        Iterator<String> it = tm.keySet().iterator();
        StringBuilder buffer = new StringBuilder();
        while (it.hasNext()) {
            String key = it.next();
            buffer.append(key).append("=").append(tm.get(key)).append("&");
        }
        buffer.append("apiKey=").append(Constant.Api_key);
        return StringUtil.md5(buffer.toString()).toUpperCase();
    }

    private OkHttpClient okHttpClient;


    public void init() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {

            @Override
            public void log(String message) {
//                if (message.contains("Uploads/testdownload")) {
//                    if (message.contains("<-- HTTP FAILED: java.net.SocketTimeoutException: connect timed out")) {
//                        if (listener != null) {
//                            listener.onDownLoadTimeout();
//                        }
//                    }
//                } else if (message.contains("base/uploadFile ")) {
//                    if (message.contains("<-- HTTP FAILED: java.net.SocketTimeoutException: connect timed out")) {
//                        if (listener != null) {
//                            listener.onUploadTimeout();
//                        }
//                    }
//                }
                Log.d("AppLog", message);
            }
        });
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        okHttpClient = new OkHttpClient.Builder().addInterceptor(interceptor)
                .addInterceptor(new CommonParamsInterceptor())
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .retryOnConnectionFailure(false)
                .build();
    }

    /**
     * 下载图片
     *
     * @param url
     * @return byte[]
     * @throws IOException
     */
    public byte[] downloadImage(String url) throws IOException {

        Request request = new Request.Builder()
                .url(url)
                .tag("download")
                .build();
        Response response = okHttpClient.newCall(request).execute();
        byte[] bytes = response.body().bytes();
        return bytes;
    }

    /**
     * 下载图片g
     *
     * @param url
     * @throws IOException
     */
    public void downloadImage(String url, String tag, final OnSocketTimeListener onSocketTimeListener) throws IOException {

        Request request = new Request.Builder()
                .url(url)
                .tag(tag)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (onSocketTimeListener != null) {
                    if (e instanceof SocketTimeoutException) {
                        //判断超时异常
                        onSocketTimeListener.socketTimeoutException();
                    } else if (e instanceof ConnectException) {
                        ////判断连接异常，
                        onSocketTimeListener.connectException();
                    } else
                        onSocketTimeListener.onFailed();
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (onSocketTimeListener != null) {
                    onSocketTimeListener.onSuccess();
                    byte[] bytes = response.body().bytes();
                    FileCache.getInstance().saveToAlbum(BitmapFactory.decodeByteArray(bytes, 0, bytes.length), "test", ContextManager.getContext());
                }
            }
        });
    }

    /**
     * 上传图片
     *
     * @param url
     * @return 新图片的路径
     * @throws IOException
     * @throws JSONException
     */
    public void uploadImage(String url, String name, Callback callback) throws IOException, JSONException {
        File dir = new File(Environment.getExternalStorageDirectory(), "MG");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File file = new File(dir, name + ".jpg");
        if (!file.exists()) {
            callback.onFailure(null, null);
            return;
        }
        RequestBody image = RequestBody.create(MediaType.parse("image/jpg"), file);
        String key = "image0";
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("k", key)
                .addFormDataPart(key, file.getName(), image)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .tag("upload")
                .build();
        okHttpClient.newCall(request).enqueue(callback);
//        return response.body().string();
    }

    public static void upload(String url, String name) throws IOException, JSONException {
        File dir = new File(Environment.getExternalStorageDirectory(), "MG");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File file = new File(dir, name + ".jpg");
        if (!file.exists()) {
            return;
        }
        List<MultipartBody.Part> requestBody
                = MultipartBodyHBuilder.Builder()
                .addParams("k", "image")
                .addParams("image", file)
                .builder();

        RepositoryFactory.getRemoteRepository().uploadFile(requestBody)
                .compose(RxScheduler.<ResponseModel<JsonObject>>toMain())
                .subscribe(new CommonObserver<JsonObject>() {
                    @Override
                    public void onSuccess(JsonObject data) {
                        Map<String, Object> map = JsonUtil.jsonToMap(data.toString());
                        String value = (String) map.get("image");
                        Log.e("图片", "" + value);

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

    public void cancel(String tag) {
        for (Call call : okHttpClient.dispatcher().queuedCalls()) {
            if (call.request().tag().equals(tag))
                call.cancel();
        }

    }

    public interface OnRequestListener {
        void onSuccess(String json);

        void onFailed();

        void onSuccess(Response response);
    }

    public interface OnNetSpeedListener {
        void onDownLoadTimeout();

        void onUploadTimeout();
    }

    public interface OnSocketTimeListener {
        void socketTimeoutException();

        void connectException();

        void onSuccess();

        void onFailed();
    }

}
