package com.fjx.mg;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;

import androidx.multidex.MultiDex;

import com.common.sharesdk.ShareSdkConfig;
import com.library.common.CommonConfig;
import com.library.common.base.BaseApp;
import com.library.common.utils.MulLanguageUtil;
import com.library.repository.core.net.HttpCenter;
import com.library.repository.db.base.DBHelper;
import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.qcloud.uikit.TimConfig;

public class App extends BaseApp {

    @Override
    public void onCreate() {
        super.onCreate();
        CommonConfig.init(this);
        ShareSdkConfig.init(this);
        TimConfig.config(this);
        HttpCenter.getInstance().defaultConfig();
        MulLanguageUtil.initAppLanguage();
        DBHelper.getInstance().initMBADb();
        /*腾讯Bugly初始化*/
        CrashReport.initCrashReport(getApplicationContext(), TimConfig.getBuglyAppId(), !TimConfig.isRelease);
    }


    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        MultiDex.install(this);
    }
}
