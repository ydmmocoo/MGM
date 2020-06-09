package com.common.sharesdk.repostory;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;

import com.common.sharesdk.R;
import com.common.sharesdk.ShareLoginListener;
import com.common.sharesdk.PlatformType;
import com.library.common.callback.OnUiCallback;
import com.library.common.utils.ContextManager;
import com.library.common.utils.JsonUtil;
import com.library.common.utils.LogTUtil;
import com.library.common.utils.RxJavaUtls;
import com.library.common.utils.SharedPreferencesUtil;
import com.library.common.utils.StringUtil;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;

import cn.sharesdk.facebook.Facebook;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.PlatformDb;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

public class ShareApiImpl implements ShareApi {

    private final String SHARESDK_DATA_FILE = "sharesdk_data_file";

    @Override
    public void loginWx(Activity activity, final ShareLoginListener loginListener) {

        ShareSDK.setEnableAuthTag(true);
        Platform platform = ShareSDK.getPlatform(Wechat.NAME);

        platform.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                Log.d("loginWx", JsonUtil.moderToString(hashMap));
                final String openId = (String) hashMap.get("openid");
                final String nickName = (String) hashMap.get("nickname");
                int sex = (int) hashMap.get("sex");
                final String avatar = (String) hashMap.get("headimgurl");
                final String gender = String.valueOf(sex);
                RxJavaUtls.runOnUiThread(new OnUiCallback() {
                    @Override
                    public void onUiThread() {
                        loginListener.loginSuccess(openId, nickName, avatar, gender);
                    }
                });

            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                Log.d("loginWx", throwable.getMessage());
                loginListener.loginError(false);
            }

            @Override
            public void onCancel(Platform platform, int i) {
                Log.d("loginWx", "onCancel");
                loginListener.loginError(true);
            }
        });
        platform.showUser(null);
//        platform. authorize();
    }

    @Override
    public void loginFacebook(Activity activity, final ShareLoginListener loginListener) {
        Platform platform = ShareSDK.getPlatform(Facebook.NAME);
        platform.removeAccount(true);  // 移除授权的缓存的数据
        //SSO授权，传false默认是客户端授权
        platform.SSOSetting(false);
        ShareSDK.setActivity(activity);
        platform.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                LogTUtil.d("", JsonUtil.moderToString(hashMap));
                PlatformDb pdb = platform.getDb();
                String icon = "";
                try {
                    icon = URLEncoder.encode(pdb.getUserIcon(), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                final String avatar = icon;
                final String id = pdb.getUserId();
                final String nickName = pdb.getUserName();
                String gender = "1";
                if (StringUtil.equals("m", pdb.getUserGender())) {
                    gender = "1";
                } else if (StringUtil.equals("f", pdb.getUserGender())) {
                    gender = "2";
                } else {
                    gender = "1";
                }
                final String finalGender = gender;
                RxJavaUtls.runOnUiThread(new OnUiCallback() {
                    @Override
                    public void onUiThread() {
                        loginListener.loginSuccess(id, nickName, avatar, finalGender);
                    }
                });
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                LogTUtil.d("", "onError");
                loginListener.loginError(false);
            }

            @Override
            public void onCancel(Platform platform, int i) {
                LogTUtil.d("", "onCancel");
                loginListener.loginError(true);
            }
        });
        platform.showUser(null);
    }

    @Override
    public void saveRegistrationId(String registerId) {
        SharedPreferencesUtil.name(SHARESDK_DATA_FILE).put("registerId", registerId).apply();
    }

    @Override
    public String getRegistrationId() {
        String text = SharedPreferencesUtil.name(SHARESDK_DATA_FILE).getString("registerId", "");
        return text;
    }

    @Override
    public void wechatShareImage(PlatformType shareType, String imageUrl, final ShareCallback listener) {
        Platform.ShareParams params = new Platform.ShareParams();
        params.setShareType(Platform.SHARE_IMAGE);

        if (TextUtils.isEmpty(imageUrl)) {
            Bitmap bitmap = BitmapFactory.decodeResource(ContextManager.getContext().getResources(), R.drawable.share_image);
            params.setImageData(bitmap);
        } else {
            params.setImageUrl(imageUrl);
        }
        Platform platform = null;
        switch (shareType) {
            case WECHAT:
                platform = ShareSDK.getPlatform(Wechat.NAME);
                break;
            case CIRCLE:
                platform = ShareSDK.getPlatform(WechatMoments.NAME);
                break;
        }
        if (platform == null) return;
        platform.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                listener.onSucces();
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {

            }

            @Override
            public void onCancel(Platform platform, int i) {

            }
        });
        platform.share(params);
    }

    @Override
    public void wechareShareWeb(PlatformType shareType, String title, String desc, String imageUrl, String webUrl, final ShareCallback listener) {
        Platform.ShareParams params = new Platform.ShareParams();
        params.setShareType(Platform.SHARE_WEBPAGE);
        if (TextUtils.isEmpty(imageUrl)) {
            Bitmap bitmap = BitmapFactory.decodeResource(ContextManager.getContext().getResources(), R.drawable.share_image);
            params.setImageData(bitmap);
        } else {
            params.setImageUrl(imageUrl);
        }
        params.setTitle(title);
        params.setText(desc);
        params.setUrl(webUrl);
        Platform platform = null;
        switch (shareType) {
            case WECHAT:
                platform = ShareSDK.getPlatform(Wechat.NAME);
                break;
            case CIRCLE:
                platform = ShareSDK.getPlatform(WechatMoments.NAME);
                break;
        }
        if (platform == null) return;
        platform.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                listener.onSucces();
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {

            }

            @Override
            public void onCancel(Platform platform, int i) {

            }
        });
        platform.share(params);
    }

    @Override
    public void fackbookShareWeb(PlatformType shareType, String title, String desc, String imageUrl, String webUrl, final ShareCallback listener) {
        Platform platform = ShareSDK.getPlatform(Facebook.NAME);
        Platform.ShareParams shareParams = new Platform.ShareParams();
        shareParams.setUrl(webUrl);
        shareParams.setQuote(title);
//        shareParams.setImageUrl("https://a3.att.hudong.com/68/61/300000839764127060614318218_950.jpg");
        shareParams.setShareType(Platform.SHARE_WEBPAGE);

        platform.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                listener.onSucces();
                LogTUtil.d("", "分享成功==》" + hashMap.toString());
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                LogTUtil.d("", "分享失败==》" + throwable.getMessage());
            }


            @Override
            public void onCancel(Platform platform, int i) {
                LogTUtil.d("", "分享取消==》 ");
            }
        });
        platform.share(shareParams);
    }

    @Override
    public void fackbookShareImage(PlatformType shareType, String imageUrl, final ShareCallback listener) {
        Platform.ShareParams params = new Platform.ShareParams();
        params.setShareType(Platform.SHARE_IMAGE);

        if (TextUtils.isEmpty(imageUrl)) {
            Bitmap bitmap = BitmapFactory.decodeResource(ContextManager.getContext().getResources(), R.drawable.share_image);
            params.setImageData(bitmap);
        } else {
            params.setImageUrl(imageUrl);
        }
        Platform platform = ShareSDK.getPlatform(Facebook.NAME);
        platform.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                listener.onSucces();
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {

            }

            @Override
            public void onCancel(Platform platform, int i) {

            }
        });
        platform.share(params);
    }


}
