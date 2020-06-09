package com.library.repository.models;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.util.Objects;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Author    by hanlz
 * Date      on 2020/1/15.
 * Description：用于存储充值转账等历史记录的数据
 */
@Entity
public class TransferLikerModel {
    @Id(autoincrement = true)
    Long _id;

    private String unique;//唯一标识符 mgm号+当前付款时间
    private String instruction;//转账说明
    private String amount;//转账金额
    private String type;
    private String phone;//用户输入的充值/转账等电话号码
    private String imUserId;//用户id
    private String nickname;//昵称
    private String faceIcon;//用户头像地址


    @Generated(hash = 834843320)
    public TransferLikerModel(Long _id, String unique, String instruction,
            String amount, String type, String phone, String imUserId,
            String nickname, String faceIcon) {
        this._id = _id;
        this.unique = unique;
        this.instruction = instruction;
        this.amount = amount;
        this.type = type;
        this.phone = phone;
        this.imUserId = imUserId;
        this.nickname = nickname;
        this.faceIcon = faceIcon;
    }

    @Generated(hash = 503401089)
    public TransferLikerModel() {
    }


    public String getUnique() {
        return unique;
    }

    public void setUnique(String unique) {
        this.unique = unique;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImUserId() {
        return imUserId;
    }

    public void setImUserId(String imUserId) {
        this.imUserId = imUserId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getFaceIcon() {
        return faceIcon;
    }

    public void setFaceIcon(String faceIcon) {
        this.faceIcon = faceIcon;
    }

    public Long get_id() {
        return this._id;
    }

    public void set_id(Long _id) {
        this._id = _id;
    }

//    @Override
//    public boolean equals(@Nullable Object obj) {
//        if (this == obj) {
//            return true;
//        }
//        if (obj == null) {
//            return false;
//        }
//        if (getClass() != obj.getClass()) {
//            return false;
//        }
//        PhoneHistoryModel other = (PhoneHistoryModel) obj;
//        if (type != other.type)
//            return false;
//        if (phone == null) {
//            if (other.phone != null) {
//                return false;
//
//            }
//        } else if (!name.equals(other.name))
//            return false;
//        return true;
//
//    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransferLikerModel that = (TransferLikerModel) o;
        return Objects.equals(type, that.type) &&
                Objects.equals(phone, that.phone) &&
                Objects.equals(imUserId, that.imUserId) &&
                Objects.equals(nickname, that.nickname) &&
                Objects.equals(faceIcon, that.faceIcon);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, phone, imUserId, nickname, faceIcon);
    }
}
