package com.common.sharesdk.repostory;

import android.app.Activity;
import android.content.pm.ActivityInfo;

import com.common.sharesdk.ShareLoginListener;
import com.common.sharesdk.PlatformType;

import cn.sharesdk.framework.PlatformActionListener;

public interface ShareApi {

    void loginWx(Activity activity, ShareLoginListener loginListener);

    void loginFacebook(Activity activity, ShareLoginListener loginListener);

    void saveRegistrationId(String registerId);

    String getRegistrationId();

    void wechatShareImage(PlatformType shareType, String imageUrl, ShareCallback listener);

    void wechareShareWeb(PlatformType shareType, String title, String desc, String imageUrl, String webUrl, ShareCallback listener);

    void fackbookShareWeb(PlatformType shareType, String title, String desc, String imageUrl, String webUrl, ShareCallback listener);

    void fackbookShareImage(PlatformType shareType, String imageUrl, ShareCallback listener);

}
