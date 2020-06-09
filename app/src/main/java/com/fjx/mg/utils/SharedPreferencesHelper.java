package com.fjx.mg.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

@SuppressLint("CommitPrefEdits")
public class SharedPreferencesHelper {
	private SharedPreferences sp;
	private SharedPreferences.Editor edit;

	public SharedPreferencesHelper(Context context) {
		sp = context.getSharedPreferences("SharedPreferencesHelper", Context.MODE_PRIVATE);
		edit = sp.edit();
	}

	public boolean putString(String key, String value) {
		edit.putString(key, value);
		return edit.commit();
	}

	public String getString(String key) {
		return sp.getString(key, "");
	}

	public boolean putInt(String key, int value) {
		edit.putInt(key, value);
		return edit.commit();
	}

	public int getInt(String key) {
		return sp.getInt(key, -1);
	}

	public boolean putBoolean(String key, boolean value) {
		edit.putBoolean(key, value);
		return edit.commit();
	}

	public boolean getBoolean(String key) {
		return sp.getBoolean(key, false);
	}

	public boolean clear() {
		edit.clear();
		return edit.commit();
	}

	public boolean remove(String key) {
		edit.remove(key);
		return edit.commit();
	}

	public void close() {
		sp = null;
	}

}
