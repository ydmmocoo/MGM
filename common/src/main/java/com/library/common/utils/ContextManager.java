package com.library.common.utils;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

public class ContextManager {

    private static Map<String, Object> configMap = new HashMap<>();

    public static void init(Context context) {
        configMap.put("context", context);
    }

    public static Context getContext() {
        return (Context) configMap.get("context");
    }


}
