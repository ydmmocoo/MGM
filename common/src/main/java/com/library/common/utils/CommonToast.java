package com.library.common.utils;

import android.widget.Toast;

import es.dmoral.toasty.Toasty;

public class CommonToast {


    public static void init() {
        Toasty.Config.getInstance()
                .tintIcon(false) // optional (apply textColor also to the icon)
                .allowQueue(false) // optional (prevents several Toastys from queuing)
                .apply(); // required
    }

    public static void toast(String message) {
        if (StringUtil.isNotEmpty(message)) {
            Toasty.normal(ContextManager.getContext(), message).show();
        }
    }

    public static void toast(int string/*<p>R.string.xxx</p>*/) {
        Toasty.normal(ContextManager.getContext(), ContextManager.getContext().getString(string)).show();
    }
    public static void toastLong(int string/*<p>R.string.xxx</p>*/) {
        Toast.makeText(ContextManager.getContext(), ContextManager.getContext().getString(string), Toast.LENGTH_LONG).show();
    }
//    public static void toastError(String message) {
//        Toasty.error(ContextManager.getContext(), message, Toast.LENGTH_SHORT, true).show();
//    }
//    public static void toastSuccess(String message) {
//        Toasty.success( ContextManager.getContext(), message, Toast.LENGTH_SHORT, true).show();
//    }


}
