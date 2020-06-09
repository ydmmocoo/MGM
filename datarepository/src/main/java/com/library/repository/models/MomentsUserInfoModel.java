package com.library.repository.models;

import java.util.List;

public class MomentsUserInfoModel {
    private String userId;
    private String userNick;
    private String userName;
    private String identifier;
    private String phone;
    private String avatar;
    private String address;
    private int isFriend;
    private List<String> momentList;
    private List<String> tags;
    private List<MomentsCityCircleImagesModel> cityCircleImages;
    private List<MomentsCityCircleImagesModel> companyImages;


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserNick() {
        return userNick;
    }

    public void setUserNick(String userNick) {
        this.userNick = userNick;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getIsFriend() {
        return isFriend;
    }

    public void setIsFriend(int isFriend) {
        this.isFriend = isFriend;
    }

    public List<String> getMomentList() {
        return momentList;
    }

    public void setMomentList(List<String> momentList) {
        this.momentList = momentList;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public List<MomentsCityCircleImagesModel> getCityCircleImages() {
        return cityCircleImages;
    }

    public void setCityCircleImages(List<MomentsCityCircleImagesModel> cityCircleImages) {
        this.cityCircleImages = cityCircleImages;
    }

    public List<MomentsCityCircleImagesModel> getCompanyImages() {
        return companyImages;
    }

    public void setCompanyImages(List<MomentsCityCircleImagesModel> companyImages) {
        this.companyImages = companyImages;
    }
}
