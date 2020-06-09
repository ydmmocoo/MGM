package com.fjx.mg.food.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.fjx.mg.R;
import com.library.common.utils.CommonImageLoader;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @author yedeman
 * @date 2020/6/9.
 * email：ydmmocoo@gmail.com
 * description：
 */
public class RvRealPicAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public RvRealPicAdapter(int layoutResId, @Nullable List<String> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder helper, String s) {
        CommonImageLoader.load(s)
                .placeholder(R.drawable.big_image_default).into(helper.getView(R.id.iv_pic));
    }
}
