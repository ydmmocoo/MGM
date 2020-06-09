package com.fjx.mg.news.detail;

import android.widget.ImageView;

import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.fjx.mg.R;
import com.library.common.base.adapter.BaseAdapter;
import com.library.common.utils.CommonImageLoader;
import com.library.repository.models.AdModel;

public class AdAdapter extends BaseAdapter<AdModel> {
    public AdAdapter() {
        super(R.layout.item_ad_image);
    }

    @Override
    protected void convert(BaseViewHolder helper, AdModel item) {
        ImageView ivImage = helper.getView(R.id.ivImage);
        CommonImageLoader.load(item.getImg()).placeholder(R.drawable.banner_logo_ic).into(ivImage);

    }
}
