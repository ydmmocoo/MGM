package com.fjx.mg.food.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.library.repository.models.GoodsSearchBean;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @author yedeman
 * @date 2020/6/8.
 * email：ydmmocoo@gmail.com
 * description：
 */
public class RvGoodsSearchAdapter extends BaseQuickAdapter<GoodsSearchBean.GoodsListBean, BaseViewHolder> {

    public RvGoodsSearchAdapter(int layoutResId, @Nullable List<GoodsSearchBean.GoodsListBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder helper, GoodsSearchBean.GoodsListBean item) {
    }
}
