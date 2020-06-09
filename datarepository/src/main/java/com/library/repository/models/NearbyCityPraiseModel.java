package com.library.repository.models;

import java.util.List;

/**
 * Author    by hanlz
 * Date      on 2019/10/21.
 * Description：同城点赞列表
 */
public class NearbyCityPraiseModel {

    private boolean hasNext;//是否有下一页
    private String likeNum;//	点赞数
    private List<NearbyCityPraiseListModel> parseList;//点赞列表

    public boolean isHasNext() {
        return hasNext;
    }

    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }

    public String getLikeNum() {
        return likeNum;
    }

    public void setLikeNum(String likeNum) {
        this.likeNum = likeNum;
    }

    public List<NearbyCityPraiseListModel> getParseList() {
        return parseList;
    }

    public void setParseList(List<NearbyCityPraiseListModel> parseList) {
        this.parseList = parseList;
    }
}
