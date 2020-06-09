package com.library.common.receiver;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import androidx.core.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.library.common.R;
import com.library.common.callback.CActivityManager;

/**
 * Created by yiang on 2018/4/23.
 * <p>
 * 显示等级权限弹窗 广播
 */

public class RankPermissionReceiver extends BroadcastReceiver {

    public static final String MG_RANK_ACTION = "mg_rank_action";
    private MaterialDialog dialog;

    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();
        if (TextUtils.equals(action, MG_RANK_ACTION)) {
            final Activity activity = CActivityManager.getAppManager().currentActivity();

            dialog = new MaterialDialog.Builder(activity)
                    .customView(R.layout.dialog_rank, true)
                    .backgroundColor(ContextCompat.getColor(activity, android.R.color.transparent))
                    .build();
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            View view = dialog.getCustomView();
            if (view == null) return;
            View llinvite = view.findViewById(R.id.llinvite);
            llinvite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent("mg_InviteActivity");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    activity.startActivity(intent);
                    dialog.dismiss();
                }
            });
            View llbuy = view.findViewById(R.id.llbuy);
            llbuy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent("mg_LevelHomeActivity");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    activity.startActivity(intent);
                    dialog.dismiss();
                }
            });
            view.findViewById(R.id.tvCancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            dialog.show();

        }
    }
}
