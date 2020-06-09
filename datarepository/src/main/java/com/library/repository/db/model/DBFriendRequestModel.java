package com.library.repository.db.model;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class DBFriendRequestModel {

    @Id(autoincrement = true)
    private Long _id;

    @Unique
    private String identityId;
    private String nickName;
    private String addWording;
    private String imageUrl;
    private int status;//0:未处理，1，接受，2，拒绝
    private String uid;

    @Generated(hash = 433122646)
    public DBFriendRequestModel(Long _id, String identityId, String nickName,
            String addWording, String imageUrl, int status, String uid) {
        this._id = _id;
        this.identityId = identityId;
        this.nickName = nickName;
        this.addWording = addWording;
        this.imageUrl = imageUrl;
        this.status = status;
        this.uid = uid;
    }

    @Generated(hash = 1245398185)
    public DBFriendRequestModel() {
    }

    public Long get_id() {
        return this._id;
    }

    public void set_id(Long _id) {
        this._id = _id;
    }

    public String getIdentityId() {
        return this.identityId;
    }

    public void setIdentityId(String identityId) {
        this.identityId = identityId;
    }

    public String getNickName() {
        return this.nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getAddWording() {
        return this.addWording;
    }

    public void setAddWording(String addWording) {
        this.addWording = addWording;
    }

    public String getImageUrl() {
        return this.imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getUid() {
        return this.uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }


}
