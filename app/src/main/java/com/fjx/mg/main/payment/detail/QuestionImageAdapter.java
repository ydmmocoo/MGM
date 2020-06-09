package com.fjx.mg.main.payment.detail;

import android.widget.ImageView;

import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.fjx.mg.R;
import com.library.common.base.adapter.BaseAdapter;
import com.library.common.utils.CommonImageLoader;
import com.tencent.qcloud.uikit.common.widget.ShadeImageView;

public class QuestionImageAdapter extends BaseAdapter<String> {

    public QuestionImageAdapter() {
        super(R.layout.item_question_img);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        ImageView ivImage = helper.getView(R.id.ivImage);
        CommonImageLoader.load(item).noAnim().placeholder(R.drawable.food_default).into(ivImage);
    }
}
