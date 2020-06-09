package com.fjx.mg.main.yellowpage.detail;

import android.app.Activity;
import android.widget.ImageView;

import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.fjx.mg.R;
import com.library.common.base.adapter.BaseAdapter;
import com.library.common.utils.CommonImageLoader;

public class ImageAdapter extends BaseAdapter<String> {

    private Activity context;

    public ImageAdapter(Activity context) {
        super(R.layout.item_company_image);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        ImageView ivImage = helper.getView(R.id.ivImage);
        CommonImageLoader.load(item).noAnim().placeholder(R.drawable.food_default).into(ivImage);
    }
}
