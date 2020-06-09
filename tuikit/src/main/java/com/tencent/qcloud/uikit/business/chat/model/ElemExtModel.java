package com.tencent.qcloud.uikit.business.chat.model;

import java.util.List;

public class ElemExtModel {
    public static final String SHARE_YELLOWPAGE = "ShareYellowPage";
    public static final String SHARE_NEWS = "ShareNews";
    public static final String SHARE_CITY = "ShareCity";
    public static final String SHARE_PERSONAL_CARD = "SharePersonalCard";
    private String money;
    private String remark;
    private String beReceivedMessageId;
    private long createTime;
    private String messageType;
    private String transferId;//转账id,转账消息与后端的关联
    private String title;
    private List<String> imgs;
    private List<String> images;
    private String typeName;
    private String cId;
    private String newsId;
    private String content;
    private String newsTitle;
    private String img;
    private String rId;//红包记录id

    private String receiveUserId;//收红包的人id
    private String sendUserId;//发红包的人id
    private String sendUserName;//
    private String receiveUserName;

    private String name;
    private String identifier;
    private String faceUrl;
    private String tIMUserProfile;

    public String gettIMUserProfile() {
        return tIMUserProfile;
    }

    public void settIMUserProfile(String tIMUserProfile) {
        this.tIMUserProfile = tIMUserProfile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getFaceUrl() {
        return faceUrl;
    }

    public void setFaceUrl(String faceUrl) {
        this.faceUrl = faceUrl;
    }

    public String getReceiveUserId() {
        return receiveUserId;
    }

    public void setReceiveUserId(String receiveUserId) {
        this.receiveUserId = receiveUserId;
    }

    public String getSendUserId() {
        return sendUserId;
    }

    public void setSendUserId(String sendUserId) {
        this.sendUserId = sendUserId;
    }

    public String getSendUserName() {
        return sendUserName;
    }

    public void setSendUserName(String sendUserName) {
        this.sendUserName = sendUserName;
    }

    public String getReceiveUserName() {
        return receiveUserName;
    }

    public void setReceiveUserName(String receiveUserName) {
        this.receiveUserName = receiveUserName;
    }

    public String getrId() {
        return rId;
    }

    public void setrId(String rId) {
        this.rId = rId;
    }

    public String getNewsTitle() {
        return newsTitle;
    }

    public void setNewsTitle(String newsTitle) {
        this.newsTitle = newsTitle;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getImgs() {
        return imgs;
    }

    public void setImgs(List<String> imgs) {
        this.imgs = imgs;
    }

    public String getcId() {
        return cId;
    }

    public void setcId(String cId) {
        this.cId = cId;
    }

    public String getNewsId() {
        return newsId;
    }

    public void setNewsId(String newsId) {
        this.newsId = newsId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getMessageType() {
        return messageType;
    }


    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getBeReceivedMessageId() {
        return beReceivedMessageId;
    }

    public void setBeReceivedMessageId(String beReceivedMessageId) {
        this.beReceivedMessageId = beReceivedMessageId;
    }

    public String getTransferId() {
        return transferId;
    }

    public void setTransferId(String transferId) {
        this.transferId = transferId;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }
}
