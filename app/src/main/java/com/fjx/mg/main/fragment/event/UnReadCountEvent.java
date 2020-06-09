package com.fjx.mg.main.fragment.event;

/**
 * create by hanlz
 * 2019/9/26
 * Describe:
 */
public class UnReadCountEvent {
    private String momentsFriendCount;//朋友圈未读数量
    private String momentsRecCount;//朋友推荐未读数量
    private boolean isShow;
    public UnReadCountEvent(String momentsFriendCount, String momentsRecCount,boolean isShow) {
        this.momentsFriendCount = momentsFriendCount;
        this.momentsRecCount = momentsRecCount;
        this.isShow = isShow;
    }

    public String getMomentsFriendCount() {
        return momentsFriendCount;
    }

    public void setMomentsFriendCount(String momentsFriendCount) {
        this.momentsFriendCount = momentsFriendCount;
    }

    public String getMomentsRecCount() {
        return momentsRecCount;
    }

    public void setMomentsRecCount(String momentsRecCount) {
        this.momentsRecCount = momentsRecCount;
    }

    public boolean isShow() {
        return isShow;
    }

    public void setShow(boolean show) {
        isShow = show;
    }
}
