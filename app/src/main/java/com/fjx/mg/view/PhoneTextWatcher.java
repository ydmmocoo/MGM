package com.fjx.mg.view;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;

public class PhoneTextWatcher implements TextWatcher {

    private String areaCode;
    private InputCompleteListener completeListener;

    public void setCompleteListener(InputCompleteListener completeListener) {
        this.completeListener = completeListener;
    }



    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (completeListener!=null){
            completeListener.afterTextChanged(s,start,before,count);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        Log.e("areaCode",""+areaCode);
        if (TextUtils.equals("86", areaCode)) {
            if (s.length() > 11) {
                s.delete(11, s.length());
                if (completeListener != null) completeListener.onInputComplete(s.toString());

            }
        } else if (TextUtils.equals("261", areaCode)) {
            if (s.length() > 10) {
                s.delete(10, s.length());
                if (completeListener != null) completeListener.onInputComplete(s.toString());
            }

            if (!s.toString().contains(" ") && s.length() > 2) {
                if (s.length() > 2) {
                    s.insert(2, " ");
                }
                if (completeListener != null) completeListener.onInputComplete(s.toString());
            }
        }
    }

    public interface InputCompleteListener {
        void onInputComplete(String phone);

        void afterTextChanged(CharSequence s, int start, int before, int count);
    }

}
