package com.fjx.mg.setting.feedback;

import android.text.TextUtils;
import android.widget.ImageView;

import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.fjx.mg.R;
import com.library.common.base.adapter.BaseAdapter;
import com.library.common.utils.CommonImageLoader;

public class FeedbackImageAdapter extends BaseAdapter<String> {

    public FeedbackImageAdapter() {
        super(R.layout.item_feed_image);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        ImageView image = helper.getView(R.id.ivImage);
        if (TextUtils.isEmpty(item)) {
            CommonImageLoader.load(R.drawable.add_image_default).into(image);
        } else {
            CommonImageLoader.load(item).into(image);
        }
    }
}
