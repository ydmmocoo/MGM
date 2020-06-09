package com.library.repository.models;

import com.chad.library.adapter.base.entity.JSectionEntity;
import com.chad.library.adapter.base.entity.SectionEntity;
import com.tencent.imsdk.friendship.TIMFriend;

public class FriendContactSectionModel extends JSectionEntity {

    private boolean isHeader;
    private String header;
    private TIMFriend data;

    public FriendContactSectionModel(boolean isHeader, String header) {
        this.isHeader = isHeader;
        this.header = header;
    }

    public FriendContactSectionModel(boolean isHeader, String header, TIMFriend data) {
        this.isHeader = isHeader;
        this.header = header;
        this.data = data;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public TIMFriend getData() {
        return data;
    }

    public void setData(TIMFriend data) {
        this.data = data;
    }

    @Override
    public boolean isHeader() {
        return isHeader;
    }
}
