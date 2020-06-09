package com.library.repository.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Author    by hanlz
 * Date      on 2019/10/22.
 * Description：
 */
public class SearchAgentModel implements Parcelable {

    private List<SearchAgentListModel> agentList;

    protected SearchAgentModel(Parcel in) {
        agentList = in.createTypedArrayList(SearchAgentListModel.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(agentList);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SearchAgentModel> CREATOR = new Creator<SearchAgentModel>() {
        @Override
        public SearchAgentModel createFromParcel(Parcel in) {
            return new SearchAgentModel(in);
        }

        @Override
        public SearchAgentModel[] newArray(int size) {
            return new SearchAgentModel[size];
        }
    };

    public List<SearchAgentListModel> getAgentList() {
        return agentList;
    }

    public void setAgentList(List<SearchAgentListModel> agentList) {
        this.agentList = agentList;
    }

}
