package com.library.repository.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Author    by hanlz
 * Date      on 2019/10/17.
 * Description：
 */
public class MyCityCircleListModel implements Parcelable {

    private String cId;//同城记录id
    private String content;//内容
    private String typeName;//分类名称
    private String typeId;//	分类id
    private List<String> images;//	同城配图
    private int readNum;//	浏览次数
    private String commentNum;//	评论次数
    private String likeNum;//点赞次数
    private String createTime;//时间
    private String status;//状态,1:发布成功，2：等待发布,3:过期,4:关闭
    private boolean isTop;//是否置顶，true：是，false：否
    private boolean isLike;

    public MyCityCircleListModel() {
    }


    protected MyCityCircleListModel(Parcel in) {
        cId = in.readString();
        content = in.readString();
        typeName = in.readString();
        typeId = in.readString();
        images = in.createStringArrayList();
        readNum = in.readInt();
        commentNum = in.readString();
        likeNum = in.readString();
        createTime = in.readString();
        status = in.readString();
        isTop = in.readByte() != 0;
        isLike = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(cId);
        dest.writeString(content);
        dest.writeString(typeName);
        dest.writeString(typeId);
        dest.writeStringList(images);
        dest.writeInt(readNum);
        dest.writeString(commentNum);
        dest.writeString(likeNum);
        dest.writeString(createTime);
        dest.writeString(status);
        dest.writeByte((byte) (isTop ? 1 : 0));
        dest.writeByte((byte) (isLike ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MyCityCircleListModel> CREATOR = new Creator<MyCityCircleListModel>() {
        @Override
        public MyCityCircleListModel createFromParcel(Parcel in) {
            return new MyCityCircleListModel(in);
        }

        @Override
        public MyCityCircleListModel[] newArray(int size) {
            return new MyCityCircleListModel[size];
        }
    };

    public String getcId() {
        return cId;
    }

    public void setcId(String cId) {
        this.cId = cId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public int getReadNum() {
        return readNum;
    }

    public void setReadNum(int readNum) {
        this.readNum = readNum;
    }

    public String getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(String commentNum) {
        this.commentNum = commentNum;
    }

    public String getLikeNum() {
        return likeNum;
    }

    public void setLikeNum(String likeNum) {
        this.likeNum = likeNum;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean getIsTop() {
        return isTop;
    }

    public void setIsTop(boolean isTop) {
        this.isTop = isTop;
    }

    public boolean isLike() {
        return isLike;
    }

    public void setLike(boolean like) {
        isLike = like;
    }
}
