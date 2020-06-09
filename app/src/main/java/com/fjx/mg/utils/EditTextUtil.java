package com.fjx.mg.utils;

import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * Author    by hanlz
 * Date      on 2019/10/28.
 * Description：
 */
public class EditTextUtil {
    /**
     * 获取焦点
     *
     * @param context
     * @param editText
     */
    public static void searchPoint(Context context, EditText editText) {
        InputMethodManager mInputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        editText.findFocus();
        mInputMethodManager.showSoftInput(editText, InputMethodManager.SHOW_FORCED);
    }

    public static void losePoint(Context context, EditText editText) {
        InputMethodManager mInputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        editText.setFocusable(false);
        if (mInputMethodManager.isActive()) {
            mInputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        }
    }
}
