package com.library.repository.models;

import java.util.List;

/**
 * Author    by hanlz
 * Date      on 2019/10/23.
 * Description：
 */
public class MyCommentModel {

    private List<MyCommentListModel> commentList;
    private NearbyCityUserInfoModel userInfo;
    private boolean hasNext;

    public List<MyCommentListModel> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<MyCommentListModel> commentList) {
        this.commentList = commentList;
    }

    public NearbyCityUserInfoModel getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(NearbyCityUserInfoModel userInfo) {
        this.userInfo = userInfo;
    }

    public boolean isHasNext() {
        return hasNext;
    }

    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }
}
