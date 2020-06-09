package com.fjx.mg.food.model;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.library.repository.models.HomeShopListBean;

/**
 * @author yedeman
 * @date 2020/5/19.
 * email：ydmmocoo@gmail.com
 * description：
 */
public class SearchEntity implements MultiItemEntity {

    public static final int TYPE_HEADER = 0;
    public static final int TYPE_FOOTER = 1;
    public static final int TYPE_TAKE_OUT_FOOD = 2;
    public static final int TYPE_TO_THE_STORE = 3;

    private int itemType;
    private String title;
    private HomeShopListBean.ShopListBean item;

    public SearchEntity(int itemType) {
        this.itemType = itemType;
    }

    public SearchEntity(String title,int itemType) {
        this.title = title;
        this.itemType = itemType;
    }

    public SearchEntity(String title,HomeShopListBean.ShopListBean item,int itemType) {
        this.title = title;
        this.item=item;
        this.itemType = itemType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public HomeShopListBean.ShopListBean getData() {
        return item;
    }

    public void setData(HomeShopListBean.ShopListBean item) {
        this.item = item;
    }

    @Override
    public int getItemType() {
        return itemType;
    }
}
