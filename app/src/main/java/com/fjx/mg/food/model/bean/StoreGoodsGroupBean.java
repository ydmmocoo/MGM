package com.fjx.mg.food.model.bean;

/**
 * @author yedeman
 * @date 2020/5/26.
 * email：ydmmocoo@gmail.com
 * description：
 */
public class StoreGoodsGroupBean {

    private long groupId;
    private String name;
    private long count;

    public long getGroupId() {
        return groupId;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }
}
