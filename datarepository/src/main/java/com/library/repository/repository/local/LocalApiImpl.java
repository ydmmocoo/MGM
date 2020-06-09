package com.library.repository.repository.local;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.library.common.constant.IntentConstants;
import com.library.common.utils.JsonUtil;
import com.library.common.utils.LogTUtil;
import com.library.common.utils.SharedPreferencesUtil;
import com.library.repository.Constant;
import com.library.repository.models.AdModel;
import com.library.repository.models.HouseConfigModel;
import com.library.repository.models.JobConfigModel;
import com.library.repository.models.RateModel;
import com.library.repository.models.RechargePhoneModel;
import com.library.repository.models.UserInfoModel;
import com.tencent.imsdk.TIMUserProfile;
import com.tencent.imsdk.friendship.TIMFriend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class LocalApiImpl implements LocalApi {
    private final String ENVIRONMENT_FILENAME = "environment_data";//测试用，环境信息
    private final String USER_FILENAME = "user_data";//用户信息
    private final String PHONELIST_FILENAME = "phonelist_filename";//充值手机号码
    private final String LOCATION_FILENAME = "location_filename";//地址信息
    private final String USER_AVATAR_FILENAME = "" +
            "user_avatar_data";//im头像
    private final String TIM_FRIEND_DATA = "tim_friend_data";//im朋友
    private final String CONFIG_DATA = "config_data";//房屋租售，求职招聘配置
    private final String RATE_DATA = "rate_data";//房屋租售，求职招聘配置
    private final String LANGUAGE_DATA = "language_data";//当前国际化语言
    private final String IM_DATA = "im_data";//聊天模块信息缓存


    @Override
    public void logoutClear() {
        SharedPreferencesUtil.name(IM_DATA).clear();
//        SharedPreferencesUtil.name(USER_FILENAME).clear();
    }

    @Override
    public UserInfoModel getLocalUserInfo() {
        String str = SharedPreferencesUtil.name(USER_FILENAME).getString("UserInfoModel", "");
        return new Gson().fromJson(str, UserInfoModel.class);
    }


    @Override
    public void saveUserInfo(UserInfoModel userInfoModel) {
        String userInfo = "";
        if (userInfoModel != null) {
            userInfo = userInfoModel.toString();
        }
        SharedPreferencesUtil.name(USER_FILENAME).put("UserInfoModel", userInfo).apply();
    }

    @Override
    public void saveOffLineStatus(boolean isShow) {
        SharedPreferencesUtil.name(USER_FILENAME).put(IntentConstants.FLAG, isShow).apply();
    }

    @Override
    public boolean getOffLineStatus() {
        return SharedPreferencesUtil.name(USER_FILENAME).getBoolean(IntentConstants.FLAG, false);
    }

    @Override
    public TIMUserProfile getTIMUser() {
        String str = SharedPreferencesUtil.name(USER_FILENAME).getString("TIMUserProfile", "");
        return new Gson().fromJson(str, TIMUserProfile.class);
    }

    @Override
    public void saveTIMUser(TIMUserProfile userProfile) {
        if (userProfile == null) return;
        SharedPreferencesUtil.name(USER_FILENAME).put("TIMUserProfile", JsonUtil.moderToString(userProfile)).apply();
    }

    @Override
    public void saveUserAvatar(List<TIMFriend> friends) {
        LogTUtil.d(Constant.TIM_LOG, JsonUtil.moderToString(friends));
        SharedPreferencesUtil util = SharedPreferencesUtil.name(USER_AVATAR_FILENAME);
        for (TIMFriend friend : friends) {
            util.put(friend.getIdentifier(), friend.getTimUserProfile().getFaceUrl());
        }
        util.apply();
    }

    @Override
    public void saveAllFriend(List<TIMFriend> friends) {
        SharedPreferencesUtil util = SharedPreferencesUtil.name(TIM_FRIEND_DATA);
        util.put("all_friend", JsonUtil.moderToString(friends)).apply();

    }

    @Override
    public List<TIMFriend> getAllFriend() {
        String text = SharedPreferencesUtil.name(TIM_FRIEND_DATA).getString("all_friend", "");
        if (TextUtils.isEmpty(text)) return new ArrayList<>();
        return JsonUtil.jsonToList(text, TIMFriend.class);
    }

    @Override
    public void saveHouseConfig(HouseConfigModel configModel) {
        SharedPreferencesUtil.name(CONFIG_DATA).put("house_config", JsonUtil.moderToString(configModel)).apply();

    }

    @Override
    public HouseConfigModel getHouseConfig() {
        String text = SharedPreferencesUtil.name(CONFIG_DATA).getString("house_config", "");
        HouseConfigModel configModel = JsonUtil.strToModel(text, HouseConfigModel.class);
        return configModel;
    }

    @Override
    public void saveJobConfig(JobConfigModel configModel) {
        SharedPreferencesUtil.name(CONFIG_DATA).put("job_config", JsonUtil.moderToString(configModel)).apply();
    }

    @Override
    public JobConfigModel getJobConfig() {
        String text = SharedPreferencesUtil.name(CONFIG_DATA).getString("job_config", "");
        JobConfigModel configModel = JsonUtil.strToModel(text, JobConfigModel.class);
        return configModel;
    }


    @Override
    public void saveLastIMNoticeDate(String date) {
        SharedPreferencesUtil.name(IM_DATA).put("last_notice_date", date).apply();

    }

    @Override
    public String getLaseImNoticeDate() {
        String date = SharedPreferencesUtil.name(IM_DATA).getString("last_notice_date", "");
        return date;
    }

    @Override
    public void saveLatitude(String latitude) {
        SharedPreferencesUtil.name(LOCATION_FILENAME).put("latitude", latitude).apply();

    }

    @Override
    public void saveLongitude(String longitude) {
        SharedPreferencesUtil.name(LOCATION_FILENAME).put("longitude", longitude).apply();
    }

    @Override
    public String getLatitude() {
        String str = SharedPreferencesUtil.name(LOCATION_FILENAME).getString("latitude", "");
        return str;
    }

    @Override
    public String getLongitude() {
        String str = SharedPreferencesUtil.name(LOCATION_FILENAME).getString("longitude", "");
        return str;
    }

    @Override
    public void saveLastRechargePhone(RechargePhoneModel phone) {
        List<RechargePhoneModel> phoneLst = getLastRechargePhone();
        for (RechargePhoneModel model : phoneLst) {
            if (TextUtils.equals(model.getPhone(), phone.getPhone())) {
                phoneLst.remove(model);
                break;
            }
        }
        phoneLst.add(0, phone);
        SharedPreferencesUtil.name(PHONELIST_FILENAME).put("last_recharge_phone", JsonUtil.moderToString(phoneLst)).apply();

    }

    @Override
    public void clearPhoneList() {
        SharedPreferencesUtil.name(PHONELIST_FILENAME).put("last_recharge_phone", "").apply();
    }

    @Override
    public List<RechargePhoneModel> getLastRechargePhone() {
        String phone = SharedPreferencesUtil.name(PHONELIST_FILENAME).getString("last_recharge_phone", "");
        List<RechargePhoneModel> phoneLst = JsonUtil.jsonToList(phone, RechargePhoneModel.class);
        if (phoneLst == null) return new ArrayList<>();
        return phoneLst;
    }

    @Override
    public void saveRateConvertList(List<RateModel> list) {

        String text = "";
        if (list != null && list.size() != 0) {
            text = JsonUtil.moderToString(list);
        }
        SharedPreferencesUtil.name(RATE_DATA).put("rate_last_list", text).apply();
    }

    @Override
    public List<RateModel> getRateConvertList() {
        String text = SharedPreferencesUtil.name(RATE_DATA).getString("rate_last_list", "");
        List<RateModel> list = new ArrayList<>();
        if (!TextUtils.isEmpty(text)) {
            list = JsonUtil.jsonToList(text, RateModel.class);
        }
        return list;
    }

    @Override
    public void saveRateAmount(String amount) {
        if (TextUtils.isEmpty(amount)) amount = "1000";
        SharedPreferencesUtil.name(RATE_DATA).put("rate_last_amount", amount).apply();
    }

    @Override
    public String getRateAmount() {
        String amount = SharedPreferencesUtil.name(RATE_DATA).getString("rate_last_amount", "1000");
        return amount;
    }

    @Override
    public void saveLastTranslate(String fromText, String from, String toText, String to) {
        SharedPreferencesUtil.name(RATE_DATA).put("fromText", fromText)
                .put("from", from)
                .put("toText", toText)
                .put("to", to)
                .apply();
    }

    @Override
    public Map<String, String> getLastTranslate() {
        String fromText = SharedPreferencesUtil.name(RATE_DATA).getString("fromText", "");
        String from = SharedPreferencesUtil.name(RATE_DATA).getString("from", "");
        String toText = SharedPreferencesUtil.name(RATE_DATA).getString("toText", "");
        String to = SharedPreferencesUtil.name(RATE_DATA).getString("to", "");
        Map<String, String> map = new HashMap<>();
        map.put("fromText", fromText);
        map.put("from", from);
        map.put("toText", toText);
        map.put("to", to);
        return map;
    }

    @Override
    public String getHostEnvironment() {
        String env = SharedPreferencesUtil.name(ENVIRONMENT_FILENAME).getString("environment", "");
        return env;
    }

    @Override
    public void saveHostEnvironment(String environment) {
        SharedPreferencesUtil.name(ENVIRONMENT_FILENAME).put("environment", environment).apply();

    }

    @Override
    public void saveLanguageType(String type) {
        SharedPreferencesUtil.name(LANGUAGE_DATA).put("language_type", type).apply();
    }

    @Override
    public String getLangugeType() {
        // l = "zh-cn";   ( zh-ch:中文简体,zh-tw:中文繁体，en-us:英语，fr:法语)
        String type = SharedPreferencesUtil.name(LANGUAGE_DATA).getString("language_type", "zh-cn");
        return type;
    }

    @Override
    public AdModel getEntranceAd() {
        String adString = SharedPreferencesUtil.name(USER_FILENAME).getString("ad_model", "");

        if (!TextUtils.isEmpty(adString)) {
            AdModel adModel = JsonUtil.strToModel(adString, AdModel.class);
            return adModel;
        }
        return null;
    }

    @Override
    public void saveEntranceAd(AdModel adModel) {
        String adString = "";
        if (adModel != null)
            adString = JsonUtil.moderToString(adModel);
        SharedPreferencesUtil.name(USER_FILENAME).put("ad_model", adString).apply();
    }
}
