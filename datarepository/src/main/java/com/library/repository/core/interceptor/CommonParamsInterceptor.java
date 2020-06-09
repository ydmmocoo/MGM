package com.library.repository.core.interceptor;

import android.text.TextUtils;
import android.util.Log;

import com.library.common.base.BaseApp;
import com.library.common.utils.AppUtil;
import com.library.common.utils.DeviceInfoUtils;
import com.library.common.utils.JsonUtil;
import com.library.common.utils.StringUtil;
import com.library.repository.Constant;
import com.library.repository.data.UserCenter;
import com.library.repository.repository.RepositoryFactory;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;
import okio.BufferedSource;


/**
 * 添加公共参数的拦截器
 */
public class CommonParamsInterceptor implements Interceptor {
    private Response response;
    private static final Charset UTF8 = Charset.forName("UTF-8");

    @Override
    public Response intercept(Chain chain) throws IOException {
        //获取原始的Requset请求
        Request originalRequest = chain.request();

        String version = AppUtil.getVersionCode() + "";
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
        String token = UserCenter.getToken();
        String l = RepositoryFactory.getLocalRepository().getLangugeType();
        if (l.contains("zh-ch")) {//TODO 华为手机获取系统语言为简体中文时不是zh-cn而是zh-ch 呵呵哒
            l = "zh-cn";
        }
        String deviceNo = DeviceInfoUtils.getAndroidId(BaseApp.getInstance());
//        String deviceNo = DeviceInfoUtils.getSerial();
        String deviceType = "1";//1:安卓、2:ios
        String systemVersion = DeviceInfoUtils.getReleaseVersion();//  系统版本号
        String deviceVersion = DeviceInfoUtils.getDeviceModel();// 设备型号

        //获取请求的方法
        String method = originalRequest.method();
        if ("GET".equals(method)) {

            //百度翻译里有个字段和mg公共参数冲突，百度翻译不需要用到mg里的公共参数
            if (originalRequest.url().toString().contains(Constant.LAN_TRANSLATE_HOST)) {
                response = chain.proceed(originalRequest);
                return response;
            }

            //添加公共参数
            HttpUrl.Builder builder = originalRequest.url().newBuilder()
                    .addQueryParameter("version", version)
                    .addQueryParameter("timestamp", timestamp)
                    .addQueryParameter("l", l)
                    .addQueryParameter("deviceNo", deviceNo)
                    .addQueryParameter("deviceType", deviceType)
                    .addQueryParameter("systemVersion", systemVersion)
                    .addQueryParameter("deviceVersion", deviceVersion);

            if (!TextUtils.isEmpty(token)) {
                builder.addQueryParameter("token", token);
            }

            //参数添加完毕之后，需要根据已有参数再生成一个加密的字符串，再把字符串加入到请求参数中
            HttpUrl httpUrl = builder.build();
            String sign = getSignValue(httpUrl);
            httpUrl = httpUrl.newBuilder().addQueryParameter("sign", sign).build();
            Request request = new Request.Builder().headers(originalRequest.headers()).url(httpUrl).build();
            response = chain.proceed(request);
        } else if ("POST".equals(method)) {
            RequestBody requestBody = originalRequest.body();
            if (requestBody instanceof FormBody) {
                FormBody.Builder builder = new FormBody.Builder();
                FormBody originalBody = (FormBody) originalRequest.body();
                for (int i = 0; i < originalBody.size(); i++) {
                    builder.add(originalBody.name(i), originalBody.value(i));
                }

                //添加公共参数
                builder.add("version", version);
                builder.add("timestamp", timestamp);
                builder.add("l", l);
                builder.add("deviceNo", deviceNo);
                builder.add("deviceType", deviceType);
                builder.add("systemVersion", systemVersion);
                builder.add("deviceVersion", deviceVersion);
                if (!TextUtils.isEmpty(token)) {
                    builder.add("token", token);
                }
                FormBody formBody = builder.build();
                //添加所有的参数之后，需要根据现有参数再生成一个加密字符串sign,然后把sign加入参数，传给后端
                String sign = getSignValue(formBody);
                builder.add("sign", sign);
                formBody = builder.build();
                Request request = originalRequest.newBuilder().post(formBody).build();
                response = chain.proceed(request);
            } else if (requestBody instanceof MultipartBody) {
                response = chain.proceed(originalRequest);

            } else {

                FormBody.Builder builder = new FormBody.Builder();
                //添加公共参数
                builder.add("version", version);
                builder.add("timestamp", timestamp);
                builder.add("deviceNo", deviceNo);
                builder.add("deviceType", deviceType);
                builder.add("l", l);
                builder.add("systemVersion", systemVersion);
                builder.add("deviceVersion", deviceVersion);
                if (!TextUtils.isEmpty(token)) {
                    builder.add("token", token);
                }

                FormBody formBody = builder.build();
                //添加所有的参数之后，需要根据现有参数再生成一个加密字符串sign,然后把sign加入参数，传给后端
                String sign = getSignValue(formBody);
                builder.add("sign", sign);
                formBody = builder.build();
                Request request = originalRequest.newBuilder().post(formBody).build();
                response = chain.proceed(request);
            }
        }

//        Log.d("intercept", getResult(response));
        return response;
    }

    private String getResult(Response response) {
        BufferedSource source = response.body().source();
        try {
            source.request(Long.MAX_VALUE); // Buffer the entire body.
        } catch (IOException e) {
            e.printStackTrace();
        }
        Buffer buffer = source.buffer();
        Charset charset = UTF8;
        MediaType contentType = response.body().contentType();
        if (contentType != null) {
            charset = contentType.charset(UTF8);
        }

        String bodyString = buffer.clone().readString(charset);
        return bodyString;
    }

    private String getSignValue(HttpUrl httpUrl) {
        Set<String> set = httpUrl.queryParameterNames();
        Iterator<String> it = set.iterator();

        Map<String, Object> map = new HashMap<>();
        while (it.hasNext()) {
            String key = it.next();
            Object value = httpUrl.queryParameterValues(key).get(0);
            map.put(key, value);
        }
        return getString(map);
    }

    private String getSignValue(FormBody formBody) {
        Map<String, Object> map = new HashMap<>();
        for (int i = 0; i < formBody.size(); i++) {
            map.put(formBody.name(i), formBody.value(i));
        }

        Log.e("getSignValue", JsonUtil.moderToString(map));
        return getString(map);
    }


    private String getString(Map<String, Object> paramsMap) {
        TreeMap<String, Object> tm = new TreeMap<>(paramsMap);

//        Iterator i = tm.descendingMap().entrySet().i();
        Iterator<String> it = tm.keySet().iterator();
        StringBuilder buffer = new StringBuilder();
        while (it.hasNext()) {
            String key = it.next();
            buffer.append(key).append("=").append(tm.get(key)).append("&");
        }
        buffer.append("apiKey=").append(Constant.Api_key);
        Log.e("getSignValue", buffer.toString());
        Log.e("getSignValue", StringUtil.md5(buffer.toString()).toUpperCase());
        return StringUtil.md5(buffer.toString()).toUpperCase();
    }

}
