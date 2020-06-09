package com.fjx.mg.food.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @author yedeman
 * @date 2020/6/8.
 * email：ydmmocoo@gmail.com
 * description：
 */
public class RvGoodsSearchAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public RvGoodsSearchAdapter(int layoutResId, @Nullable List<String> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, String s) {
    }
}
