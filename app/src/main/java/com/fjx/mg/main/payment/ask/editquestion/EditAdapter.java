package com.fjx.mg.main.payment.ask.editquestion;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.fjx.mg.R;
import com.library.common.base.adapter.BaseAdapter;
import com.library.common.utils.CommonImageLoader;

public class EditAdapter extends BaseAdapter<String> {

    public EditAdapter() {
        super(R.layout.item_ask_image);
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
