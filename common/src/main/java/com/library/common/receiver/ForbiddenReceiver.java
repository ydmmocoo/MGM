package com.library.common.receiver;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.library.common.R;
import com.library.common.callback.CActivityManager;

/**
 * Created by yiang on 2018/4/23.
 * <p>
 * 显示等级权限弹窗 广播
 */

public class ForbiddenReceiver extends BroadcastReceiver {

    public static final String MG_FORBIDDEN_ACTION = "mg_forbidden_action";
    private MaterialDialog dialog;

    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();
        if (TextUtils.equals(action, MG_FORBIDDEN_ACTION)) {
            final Activity activity = CActivityManager.getAppManager().currentActivity();
            dialog = new MaterialDialog.Builder(activity)
                    .title(R.string.att)
                    .content(R.string.hint_forbidden)
                    .cancelable(false)
                    .positiveText(R.string.close)
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            dialog.dismiss();
                        }
                    })
                    .build();

            dialog.show();
        }
    }
}
