package com.library.repository.models;

import java.util.List;

public class QuestionListModel {


    /**
     * hasNext : false
     * houseList : [{"Hid":"9","title":"求购房子111111","countryName":"中国","cityName":"广州","price":"1000.00","layout":"Studio","language":"中文","contactPhone":"15860750234","contactName":"黄","contactWeixin":"ws2008","area":"30","sex":"男","desc":"单独卫生间、可养宠物","status":"2","statusTip":"","createTime":"05-21"}]
     */

    private boolean hasNext;
    private List<QuestionListDetailModel> questionList;

    public boolean isHasNext() {
        return hasNext;
    }

    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }

    public List<QuestionListDetailModel> getQuestionList() {
        return questionList;
    }

    public void setQuestionList(List<QuestionListDetailModel> questionList) {
        this.questionList = questionList;
    }
}
