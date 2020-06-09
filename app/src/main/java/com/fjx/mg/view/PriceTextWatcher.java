package com.fjx.mg.view;

import android.text.Editable;
import android.text.TextWatcher;

public class PriceTextWatcher implements TextWatcher {
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s.length() == 0) return;
        char firstChar = s.charAt(0);
        if (firstChar == '0') {
            s.delete(0, 1);
        }


        if (s.toString().contains(".")) {
            s.delete(s.toString().indexOf("."), s.toString().indexOf(".") + 1);
//            int sub = stroke.length() - stroke.toString().indexOf(".");
//            if (sub > 2) {
//                stroke.delete(stroke.toString().indexOf(".") + 3, stroke.length());
//            }
        }
    }
}
