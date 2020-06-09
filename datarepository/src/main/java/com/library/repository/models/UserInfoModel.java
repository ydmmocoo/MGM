package com.library.repository.models;

import android.text.TextUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

public class UserInfoModel extends BaseM {

    /**
     * uId : 20
     * uNick : 17359276732
     * uImg :
     * uSex : 未知
     * uMoney : 0.94
     * uCard : 0
     * isRealName : 未实名
     * token : 1ec33920f0c6ed24ed7467e5a1cf3430
     */

    private String uId;
    private String uNick;
    private String uImg;
    private String uSex;
    private String uMoney;
    private String uCard;
    private int isRealName;//0,未认证，1已认证，2审核中
    private int isCompanyCert;//0,未认证，1已认证，2审核中
    private boolean isSetSecurityIssues;
    private boolean isSetLoginPsw;
    private String token;
    private String address;
    private String score;
    private String rank;
    private int isSetPayPsw;
    private String status;//状态1:正常，2：禁用
    private String identifier;//状态1:正常，2：禁用
    private String inviteCode;

    /**
     * phone : 18850528382
     * useRig : eJxNjV1PgzAYRv8Ltxj3UtbSmXihRBPmEHEusGUJKVCwmZSu1AVi-O-iPqK35*Q8z5f1tlhes6JoP6XJzKC4dWOBdXXEouTSiEpwPUKHUgwYUZeis2ZKiTJjJnN1*a-qyl12VL-RFACB5yF6lrxXQvOMVeY0ijFGAJf0wHUnWjkKBA52kAvwJ41o*CmhDqEEzy5-oh5x*LDyg9gnm-TlfU24XtQdyNfHuFgdfHc7aSNX3kdprPebppqTmS1ZHdRBAvlOme1kb7c*tfOPuRrkU5*oZFj3UZE2*XPIvLtlRKbhrfX9A2JCWPI_
     */

    private String phone;
    private String useRig;
    private String sn;
    private String recommendInviteCode;
    private String gestureCode;
    private String securityLevelScore;
    private String momentAccessForFriend;
    private String momentAccessForStranger;
    private String name;
    private boolean isCheckDevice;

    private String isUsePayCode;//1：开启 2:关闭  是否开启付款码

    public String getMomentAccessForFriend() {
        return momentAccessForFriend;
    }

    public void setMomentAccessForFriend(String momentAccessForFriend) {
        this.momentAccessForFriend = momentAccessForFriend;
    }

    public String getMomentAccessForStranger() {
        return momentAccessForStranger;
    }

    public void setMomentAccessForStranger(String momentAccessForStranger) {
        this.momentAccessForStranger = momentAccessForStranger;
    }

    public String getSn() {
        return sn;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }

    public String getRank() {
        return TextUtils.isEmpty(rank) ? "1" : rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getRecommendInviteCode() {
        return recommendInviteCode;
    }

    public void setRecommendInviteCode(String recommendInviteCode) {
        this.recommendInviteCode = recommendInviteCode;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getScore() {
        return score;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getUId() {
        return uId;
    }

    public void setUId(String uId) {
        this.uId = uId;
    }

    public String getUNick() {
        return uNick;
    }

    public int getIsSetPayPsw() {
        return isSetPayPsw;
    }

    public boolean hasSetPayPwd() {
        return getIsSetPayPsw() == 1;
    }

    public void setIsSetPayPsw(int isSetPayPsw) {
        this.isSetPayPsw = isSetPayPsw;
    }

    public void setUNick(String uNick) {
        this.uNick = uNick;
    }

    public String getUImg() {
        if (!TextUtils.isEmpty(uImg)) {
            try {
                return URLDecoder.decode(uImg, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return uImg;
    }

    public void setUImg(String uImg) {
        this.uImg = uImg;
    }

    public String getUSex() {
        return uSex;
    }

    public void setUSex(String uSex) {
        this.uSex = uSex;
    }

    public String getUMoney() {
        return uMoney;
    }

    public void setUMoney(String uMoney) {
        this.uMoney = uMoney;
    }

    public String getUCard() {
        return uCard;
    }

    public void setUCard(String uCard) {
        this.uCard = uCard;
    }

    public int isRealName() {
        return isRealName;
    }

    public void setRealName(int realName) {
        isRealName = realName;
    }

    public int isCompanyCert() {
        return isCompanyCert;
    }

    public void setCompanyCert(int companyCert) {
        isCompanyCert = companyCert;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUseRig() {
        return useRig;
    }

    public void setUseRig(String useRig) {
        this.useRig = useRig;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public boolean isSetSecurityIssues() {
        return isSetSecurityIssues;
    }

    public void setSetSecurityIssues(boolean setSecurityIssues) {
        isSetSecurityIssues = setSecurityIssues;
    }

    public boolean isSetLoginPsw() {
        return isSetLoginPsw;
    }

    public void setSetLoginPsw(boolean setLoginPsw) {
        isSetLoginPsw = setLoginPsw;
    }

    public String getGestureCode() {
        return gestureCode;
    }

    public void setGestureCode(String gestureCode) {
        this.gestureCode = gestureCode;
    }

    public boolean hasGesture() {
        return !TextUtils.isEmpty(getGestureCode());
    }

    public String getSecurityLevelScore() {
        return securityLevelScore;
    }

    public boolean isCheckDevice() {
        return isCheckDevice;
    }

    public void setCheckDevice(boolean checkDevice) {
        isCheckDevice = checkDevice;
    }

    public void setSecurityLevelScore(String securityLevelScore) {
        this.securityLevelScore = securityLevelScore;
    }

    public boolean isForbidden() {
        return TextUtils.equals(status, "2");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIsUsePayCode() {
        return isUsePayCode;
    }

    public void setIsUsePayCode(String isUsePayCode) {
        this.isUsePayCode = isUsePayCode;
    }

    //    public boolean hasNecessaryConditions() {
//        return isRealName != 0 && isSetSecurityIssues && isSetPayPsw == 1;
//    }
    //TODO  2019年11月4日10:39:53 目前版本不需要实名认证 先屏蔽
    public boolean hasNecessaryConditions() {
        return isSetSecurityIssues && isSetPayPsw == 1;
    }


}
