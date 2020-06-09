package com.library.repository.repository;

import android.util.Base64;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.library.common.utils.AppUtil;
import com.library.common.utils.StringUtil;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class BodyBuilder {
    private JsonObject jsonObject;
    private String methodName;

    public static BodyBuilder newBuilder() {
        return new BodyBuilder();
    }

    private BodyBuilder() {
        jsonObject = new JsonObject();
//        addParam("version", AppUtil.getVersionName());
//        addParam("timestamp", System.currentTimeMillis() / 1000);
//        addParam("token", "");
    }

    public BodyBuilder setMethodName(String methodName) {
        this.methodName = methodName;
        return this;
    }


    public BodyBuilder addParam(String key, int value) {
        jsonObject.addProperty(key, value);
        return this;
    }

    public BodyBuilder addParam(String key, Long value) {
        jsonObject.addProperty(key, value);
        return this;
    }

    public BodyBuilder addParam(String key, String value) {
        jsonObject.addProperty(key, value);
        return this;
    }

    public BodyBuilder addParam(String key, JsonObject value) {
        jsonObject.add(key, value);
        return this;
    }

    public BodyBuilder addParam(String key, JsonArray value) {
        jsonObject.add(key, value);
        return this;
    }

    public Map<String, Object> builder() {
        Type type = new TypeToken<Map<String, String>>() {
        }.getType();
        Map<String, Object> mapTemp = new Gson().fromJson(jsonObject, type);

        mapTemp.put("timestamp", (System.currentTimeMillis() / 1000 + ""));

//        String cmd = new String(Base64.encode(methodName.getBytes(), Base64.DEFAULT));
//        String dcmd = new String(Base64.decode(cmd.getBytes(), Base64.DEFAULT));
//        mapTemp.put("cmd", cmd);

        mapTemp.put("sign", getString(mapTemp));
//        String str = getKeyValues(mapTemp);
//        mapTemp = new HashMap<>();
//        mapTemp.put("param", str);


        Map<String, Object> mapResult = new TreeMap<>(mapTemp);
        JSONObject object = new JSONObject(mapResult);

        Log.d("BodyBuilder", object.toString());

//        return RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), object.toString());

        return mapResult;
    }

    private String getString(Map<String, Object> paramsMap) {
        TreeMap<String, Object> tm = new TreeMap<>(paramsMap);
        Iterator i = tm.descendingMap().entrySet().iterator();
        StringBuilder buffer = new StringBuilder();
        while (i.hasNext()) {
            buffer.append(i.next()).append("&");
        }
//        buffer.append(AppKey);
        return StringUtil.md5(buffer.toString());
    }

    // 得到键值对
    private String getKeyValues(Map<String, Object> paramsMap) {
        TreeMap<String, Object> tm = new TreeMap<>(paramsMap);
        Iterator i = tm.descendingKeySet().iterator();
        String jsonText = new Gson().toJson(tm.descendingMap());
        return jsonText;
    }
}
