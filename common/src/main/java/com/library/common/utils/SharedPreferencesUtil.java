package com.library.common.utils;

import android.content.Context;
import android.content.SharedPreferences;


/**
 * SharedPreferences工具类
 */
public class SharedPreferencesUtil {
    private SharedPreferences.Editor editor;
    private SharedPreferences preferences;

    public static SharedPreferencesUtil name(String fileName) {
        return new SharedPreferencesUtil(fileName);
    }


    private SharedPreferencesUtil(String fileName) {
        preferences = ContextManager.getContext().getSharedPreferences(fileName, Context.MODE_PRIVATE);
        editor = preferences.edit();
        editor.apply();
    }

    public SharedPreferencesUtil put(String key, String value) {
        editor.putString(key, value);
        return this;
    }

    public SharedPreferencesUtil put(String key, int value) {
        editor.putInt(key, value);
        return this;
    }

    public SharedPreferencesUtil put(String key, float value) {
        editor.putFloat(key, value);
        return this;
    }

    public SharedPreferencesUtil put(String key, boolean value) {
        editor.putBoolean(key, value);
        return this;
    }

    public String getString(String key, String defaultValue) {
        return preferences.getString(key, defaultValue);
    }

    public int getInt(String key, int defaultValue) {
        return preferences.getInt(key, defaultValue);
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        return preferences.getBoolean(key, defaultValue);
    }

    public float getFloat(String key, float defaultValue) {
        return preferences.getFloat(key, defaultValue);
    }


    public void apply() {
        editor.apply();
    }

    public void clear() {
        preferences.edit().clear().apply();
    }


}
