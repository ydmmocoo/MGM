package com.library.repository.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Author    by hanlz
 * Date      on 2019/10/19.
 * Description：
 */
public class NearbyCityCommentModel implements Parcelable {

    private boolean hasNext;

    private List<NearbyCityCommentListModel> commentList;

    protected NearbyCityCommentModel(Parcel in) {
        hasNext = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (hasNext ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<NearbyCityCommentModel> CREATOR = new Creator<NearbyCityCommentModel>() {
        @Override
        public NearbyCityCommentModel createFromParcel(Parcel in) {
            return new NearbyCityCommentModel(in);
        }

        @Override
        public NearbyCityCommentModel[] newArray(int size) {
            return new NearbyCityCommentModel[size];
        }
    };

    public boolean isHasNext() {
        return hasNext;
    }

    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }

    public List<NearbyCityCommentListModel> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<NearbyCityCommentListModel> commentList) {
        this.commentList = commentList;
    }
}
