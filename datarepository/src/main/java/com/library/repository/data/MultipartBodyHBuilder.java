package com.library.repository.data;

import com.library.common.utils.AppUtil;
import com.library.common.utils.HanziToPinyinUtil;
import com.library.common.utils.StringUtil;
import com.library.repository.Constant;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;


/**
 * 上传图片和文字专用body
 */
public class MultipartBodyHBuilder {
    private Map<String, Object> map = new HashMap<>();

    private MultipartBodyHBuilder() {
        String version = AppUtil.getVersionName();
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
        String token = UserCenter.getToken();
        map.put("version", version);
        map.put("timestamp", timestamp);
        map.put("token", token);

    }

    public static MultipartBodyHBuilder Builder() {
        return new MultipartBodyHBuilder();
    }

    public MultipartBodyHBuilder addParams(String key, Object value) {
        map.put(key, value);
        return this;
    }


    public List<MultipartBody.Part> builder() {
        MultipartBody.Builder builder = new MultipartBody.Builder();
        String sign = getSignValue(map);
        map.put("sign", sign);
        Set<String> set = map.keySet();
        Iterator<String> it = set.iterator();
        while (it.hasNext()) {
            String key = it.next();
            Object obj = map.get(key);
            if (obj instanceof File) {
                File file = (File) obj;
                String fileName = file.getName();
                try {
                    fileName = URLEncoder.encode(file.getName(), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data;charset=UTF-8"), file);
                builder.addFormDataPart(key, fileName, requestBody);
            } else if (obj instanceof String) {
                builder.addFormDataPart(key, (String) obj);
            }
        }
        return builder.build().parts();
//        return requestBodyMap;
    }


    private String getSignValue(Map<String, Object> paramMap) {
        return getString(paramMap);
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
        return StringUtil.md5(buffer.toString()).toUpperCase();
    }
}
