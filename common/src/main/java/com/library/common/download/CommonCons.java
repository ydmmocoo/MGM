package com.library.common.download;

import android.os.Environment;

import com.library.common.utils.ContextManager;

import java.io.File;

public class CommonCons {
    public final static String SAVE_APP_NAME = "download.apk";

    public final static String SAVE_APP_LOCATION = "/download";


    public final static String APP_FILE_NAME = Environment.getExternalStorageDirectory() + SAVE_APP_LOCATION + File.separator + SAVE_APP_NAME;
    public static final String DOWNLOAD_APK_ID_PREFS = "download_apk_id_prefs";
}
