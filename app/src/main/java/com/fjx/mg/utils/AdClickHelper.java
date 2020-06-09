package com.fjx.mg.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.fjx.mg.web.CommonWebActivity;
import com.library.common.callback.CActivityManager;
import com.library.common.utils.ContextManager;
import com.library.common.utils.JsonUtil;
import com.library.common.utils.StringUtil;
import com.library.repository.Constant;
import com.library.repository.data.UserCenter;
import com.library.repository.models.AdModel;
import com.library.repository.models.UserInfoModel;
import com.library.repository.repository.RepositoryFactory;

public class AdClickHelper {

    public static void clickAd(AdModel adModel) {
        switch (adModel.getJumpType()) {
            case "1":
                openAppPage(adModel);
                break;
            case "2":
                openWebPae(adModel);
                break;
            case "3":
                if (adModel.getAndroidPath() != null && !adModel.getAndroidPath().equals("")) {
                    openDetail(adModel);
                }
                break;

        }
    }

    private static void openDetail(AdModel adModel) {
        //mg_RechargeCenterActivityx#id=13
        String androidPath = adModel.getAndroidPath();
        int i = androidPath.indexOf("#id=");
        String substring = androidPath.substring(0, i);
        String substring1 = androidPath.substring(i + 4);
        Log.e("跳转：" + substring, "ID:" + substring1);
        Context context = CActivityManager.getAppManager().currentActivity();
        Intent intent = new Intent(substring);
        intent.putExtra("cid", substring1);
        context.startActivity(intent);
    }

    private static void openWebPae(AdModel adModel) {
        StringBuilder sb = new StringBuilder();
        sb.append(Constant.HOST);

        if (adModel.getUrl().contains("invite/flowAdv")) {
            sb.append("invite/flowAdv?ispType=");
        } else if (adModel.getUrl().contains(Constant.getSearch())) {
            sb.append(Constant.getSearch());
        }
        try {
            UserInfoModel userInfo = UserCenter.getUserInfo();
            if (userInfo != null) {
                String phone = userInfo.getPhone();
                if (StringUtil.isNotEmpty(phone)) {
                    sb.append(phone.substring(0, 3));
                }
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        sb.append("&l=");
        sb.append(RepositoryFactory.getLocalRepository().getLangugeType());
        Log.e("资费套餐链接:", sb.toString());

        Activity activity = CActivityManager.getAppManager().currentActivity();
        String url = adModel.getUrl();
        url = url.concat("?l=").concat(RepositoryFactory.getLocalRepository().getLangugeType());
        CommonWebActivity.Options options = new CommonWebActivity.Options();
        options.setTitle(adModel.getTitle());
        options.setLoadUrl(url.contains("invite/flowAdv") ? sb.toString() : url);
        Log.e("资费套餐:", url);
        Log.e("资费套餐链接2:", options.getLoadUrl());
        activity.startActivity(CommonWebActivity.newInstance(activity, JsonUtil.moderToString(options)));
    }


    private static void openAppPage(AdModel adModel) {
        if ("MGInviteFriendsViewController".equals(adModel.getPath())) {
            if (UserCenter.needLogin()) return;
            Intent intent = new Intent("mg_InviteActivity");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ContextManager.getContext().startActivity(intent);
        }
        if (adModel.getAndroidPath() != null && !adModel.getAndroidPath().equals("")) {
            openDetail(adModel);
        }

    }


}
