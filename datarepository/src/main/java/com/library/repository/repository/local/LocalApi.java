package com.library.repository.repository.local;

import com.library.repository.models.AdModel;
import com.library.repository.models.HouseConfigModel;
import com.library.repository.models.JobConfigModel;
import com.library.repository.models.RateModel;
import com.library.repository.models.RechargePhoneModel;
import com.library.repository.models.UserInfoModel;
import com.tencent.imsdk.TIMUserProfile;
import com.tencent.imsdk.friendship.TIMFriend;

import java.util.List;
import java.util.Map;

public interface LocalApi {

    void logoutClear();

    /**
     * 获取本地用户数据
     *
     * @return
     */
    UserInfoModel getLocalUserInfo();




    /**
     * 保存本地用户数据
     *
     * @param userInfoModel
     */
    void saveUserInfo(UserInfoModel userInfoModel);


    /**
     * 用户判断是否是离线被踢
     */
    void saveOffLineStatus( boolean isShow);
    boolean getOffLineStatus();




    TIMUserProfile getTIMUser();

    void saveTIMUser(TIMUserProfile userProfile);


    /**
     * 缓存用户头像，在聊天界面中，消息体中没有返回头像字段，需要自己获取
     */
    void saveUserAvatar(List<TIMFriend> friends);


    /**
     * 缓存全部好友信息
     *
     * @param friends
     */
    void saveAllFriend(List<TIMFriend> friends);

    List<TIMFriend> getAllFriend();


    /**
     * 房屋租售的一些信息
     */
    void saveHouseConfig(HouseConfigModel configModel);

    HouseConfigModel getHouseConfig();

    void saveJobConfig(JobConfigModel configModel);

    JobConfigModel getJobConfig();


    /**
     * 上次保保存通知的时间，用来判断是否有最新的通知
     *
     * @param date
     */
    void saveLastIMNoticeDate(String date);

    String getLaseImNoticeDate();


    void saveLatitude(String latitude);

    void saveLongitude(String longitude);

    String getLatitude();

    String getLongitude();


    /**
     * 保存上次充值的手机号码
     *
     * @param phone
     */
    void saveLastRechargePhone(RechargePhoneModel phone);

    void clearPhoneList();

    /**
     * 获取上次保存的手机号码
     *
     * @return
     */
    List<RechargePhoneModel> getLastRechargePhone();


    void saveRateConvertList(List<RateModel> list);


    /**
     * 上一次汇率查询的货币国家
     *
     * @return
     */
    List<RateModel> getRateConvertList();


    void saveRateAmount(String amount);

    String getRateAmount();


    void saveLastTranslate(String fromText, String from, String toText, String to);

    /**
     * 上一次翻译的类型
     *
     * @return
     */
    Map<String, String> getLastTranslate();


    /**
     * 获取当前开发环境
     *
     * @return
     */
    String getHostEnvironment();


    /**
     * 保存当前开发环境
     *
     * @param envirment
     */
    void saveHostEnvironment(String envirment);


    /**
     * 保存当前的国际化语言
     *
     * @param type
     */
    void saveLanguageType(String type);

    String getLangugeType();


    AdModel getEntranceAd();

    void saveEntranceAd(AdModel model);


}
