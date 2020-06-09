package com.library.repository.models;

import java.util.List;

public class NewsRecommendReadModel {

    List<RecommentListModel> recommentList;

    public List<RecommentListModel> getRecommentList() {
        return recommentList;
    }

    public void setRecommentList(List<RecommentListModel> recommentList) {
        this.recommentList = recommentList;
    }
}
