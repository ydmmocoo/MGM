package com.fjx.mg.food.adapter;

import android.graphics.Color;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.fjx.mg.R;
import com.fjx.mg.food.model.bean.StoreGoodsGroupBean;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @author yedeman
 * @date 2020/5/26.
 * email：ydmmocoo@gmail.com
 * description：
 */
public class RvTypeAdapter extends BaseQuickAdapter<StoreGoodsGroupBean, BaseViewHolder> {

    //当前选中的位置
    public long mSelectTypeId;

    public RvTypeAdapter(int layoutResId, @Nullable List<StoreGoodsGroupBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder helper, StoreGoodsGroupBean item) {
        helper.setText(R.id.tv_name, item.getName());
        long count = item.getCount();
        helper.setText(R.id.tv_count, String.valueOf(count));

        if (count < 1) {
            helper.setVisible(R.id.tv_count, false);
        } else {
            helper.setVisible(R.id.tv_count, true);
        }

        if (item.getGroupId() == mSelectTypeId) {
            helper.itemView.setBackgroundColor(Color.WHITE);
        } else {
            helper.itemView.setBackgroundColor(Color.TRANSPARENT);
        }
    }
}
