package com.library.common.download;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import androidx.annotation.RequiresApi;
import androidx.core.content.FileProvider;
import android.util.Log;

import java.io.File;

public class InstallReceiver extends BroadcastReceiver {

    private static final String TAG = "InstallReceiver";


    // 安装下载接收器
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
            long downloadApkId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            installApk(context, downloadApkId);
        }
    }

    private void installApk(Context context, long downloadApkId) {

        try {
            Intent intent1 = new Intent(Intent.ACTION_VIEW);
            String filePath = CommonCons.APP_FILE_NAME;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent1.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                Uri contentUri = FileProvider.getUriForFile(context, "com.fjx.mg.fileprovider", new File(filePath));
                intent1.setDataAndType(contentUri, "application/vnd.android.package-archive");
                intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                //兼容8.0
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    boolean hasInstallPermission = context.getPackageManager().canRequestPackageInstalls();
                    if (!hasInstallPermission) {
                        startInstallPermissionSettingActivity(context);
                        return;
                    }
                }
            } else {
                intent1.setDataAndType(Uri.fromFile(new File(filePath)), "application/vnd.android.package-archive");
                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            context.startActivity(intent1);
        } catch (Exception e) {
            Log.e(TAG, "安装失败" + e.getMessage());
            e.printStackTrace();
        }

    }

    public void openAPKFile(Context mContext) {
        // 核心是下面几句代码
        try {
            String fileUri = CommonCons.APP_FILE_NAME;
            Intent intent = new Intent(Intent.ACTION_VIEW);
            File apkFile = new File(fileUri);
            //兼容7.0
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                Uri contentUri = FileProvider.getUriForFile(mContext, mContext.getPackageName() + ".fileProvider", apkFile);
                intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
                //兼容8.0
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    boolean hasInstallPermission = mContext.getPackageManager().canRequestPackageInstalls();
                    if (!hasInstallPermission) {
                        startInstallPermissionSettingActivity(mContext);
                        return;
                    }
                }
            } else {
                intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            if (mContext.getPackageManager().queryIntentActivities(intent, 0).size() > 0) {
                mContext.startActivity(intent);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startInstallPermissionSettingActivity(Context context) {
        Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}