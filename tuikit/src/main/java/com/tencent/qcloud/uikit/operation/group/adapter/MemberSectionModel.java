package com.tencent.qcloud.uikit.operation.group.adapter;

import com.chad.library.adapter.base.entity.JSectionEntity;
import com.chad.library.adapter.base.entity.SectionEntity;
import com.tencent.imsdk.friendship.TIMFriend;

public class MemberSectionModel extends JSectionEntity {

    private boolean isHeader;
    private String header;
    private String nickName;
    private String faceUrl;
    private String remark;
    private String identifier;

    public MemberSectionModel(boolean isHeader, String header) {
        this.isHeader = isHeader;
        this.header = header;
    }

    public MemberSectionModel(boolean isHeader, String header, String nickName, String faceUrl, String remark, String identifier) {
        this.isHeader = isHeader;
        this.header=header;
        this.nickName = nickName;
        this.faceUrl = faceUrl;
        this.remark = remark;
        this.identifier = identifier;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getFaceUrl() {
        return faceUrl;
    }

    public void setFaceUrl(String faceUrl) {
        this.faceUrl = faceUrl;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    @Override
    public boolean isHeader() {
        return isHeader;
    }
}
