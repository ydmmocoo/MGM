package com.fjx.mg.moments.event;

public class RefreshEvent {
    private int flag;//1-TabFriendFragment 2--CityFriendsFragment

    public RefreshEvent(int flag) {
        this.flag = flag;
    }

    public int getFlag() {
        return flag;
    }
}
