package com.fjx.mg.main.payment.detail;

import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.fjx.mg.R;
import com.library.common.base.adapter.BaseAdapter;
import com.library.common.utils.CommonImageLoader;

import java.util.regex.Pattern;

public class AskDetailImageAdapter extends BaseAdapter<String> {

    public AskDetailImageAdapter() {
        super(R.layout.item_askdetail_image);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        ImageView ivImage = helper.getView(R.id.ivImage);
        ImageView tv_duration = helper.getView(R.id.tv_duration);
        String reg = "(mp4|flv|avi|rm|rmvb|wmv)";
        Pattern p = Pattern.compile(reg);
        tv_duration.setVisibility(p.matcher(item).find() ? View.VISIBLE : View.GONE);
        CommonImageLoader.load(item).noAnim().placeholder(R.drawable.food_default).into(ivImage);
    }
}
