package com.library.common.base.adapter;


import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import java.util.List;


/**
 * 虽然使用的第三方适配器有刷新加载功能，但是刷新的仍然使用smartrefresh在实现
 * 除非项目需求，不然尽量不用这边的刷新加载
 *
 * @param <T>
 */
public abstract class BaseAdapter<T> extends BaseQuickAdapter<T, BaseViewHolder> {

    public BaseAdapter(int layoutResId) {
        super(layoutResId);
    }

    public BaseAdapter(int layoutResId, @Nullable List<T> data) {
        super(layoutResId, data);
    }
}
