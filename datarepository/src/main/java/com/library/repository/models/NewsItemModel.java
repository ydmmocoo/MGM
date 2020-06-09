package com.library.repository.models;

import java.util.List;

public class NewsItemModel {


    /**
     * hasNext : true
     * newsList : [{"newsId":"9","layoutType":"3","newsTitle":"test3","newsAuth":"人民网3333","commentNum":"30","publishTime":"3小时前10:36","imgs":[]},{"newsId":"6","layoutType":"3","newsTitle":"test3","newsAuth":"人民网3333","commentNum":"30","publishTime":"3小时前10:36","imgs":[]}]
     */

    private boolean hasNext;
    private List<NewsListModel> newsList;

    public boolean isHasNext() {
        return hasNext;
    }

    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }

    public List<NewsListModel> getNewsList() {
        return newsList;
    }

    public void setNewsList(List<NewsListModel> newsList) {
        this.newsList = newsList;
    }

}
