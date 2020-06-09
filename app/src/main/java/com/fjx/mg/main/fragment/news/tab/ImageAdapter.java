package com.fjx.mg.main.fragment.news.tab;

import android.widget.ImageView;

import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.fjx.mg.R;
import com.library.common.base.adapter.BaseAdapter;
import com.library.common.utils.CommonImageLoader;

public class ImageAdapter extends BaseAdapter<String> {
    public ImageAdapter() {
        super(R.layout.item_new_mul_image);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        ImageView ivImage = helper.getView(R.id.ivImage);
        CommonImageLoader.load(item).placeholder(R.drawable.food_default).into(ivImage);
        ivImage.setClickable(false);
    }
}
