package com.library.repository.models;

import com.tencent.imsdk.TIMUserProfile;

public class ImUserRelaM {

    private TIMUserProfile userProfile;
    private boolean isFriend;

    public ImUserRelaM(TIMUserProfile userProfile, boolean isFriend) {
        this.userProfile = userProfile;
        this.isFriend = isFriend;
    }

    public TIMUserProfile getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(TIMUserProfile userProfile) {
        this.userProfile = userProfile;
    }

    public boolean isFriend() {
        return isFriend;
    }

    public void setFriend(boolean friend) {
        isFriend = friend;
    }
}
