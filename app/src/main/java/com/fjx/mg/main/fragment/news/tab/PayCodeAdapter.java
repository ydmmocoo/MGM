package com.fjx.mg.main.fragment.news.tab;

import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.fjx.mg.R;
import com.library.common.base.adapter.BaseAdapter;
import com.library.common.utils.CommonImageLoader;
import com.library.repository.models.PayStatusModel;

public class PayCodeAdapter extends BaseAdapter<PayStatusModel> {

    public PayCodeAdapter() {
        super(R.layout.item_pay_status);
    }


    @Override
    protected void convert(BaseViewHolder helper, PayStatusModel item) {
        helper.setText(R.id.tvNmae, item.getNickName());
        helper.setText(R.id.tvPayS, item.getStatus().equals("1") ? item.getPrice() : item.getPayTip());
        ImageView ivImage = helper.getView(R.id.ivImage);
        CommonImageLoader.load(item.getAvatar()).placeholder(R.drawable.user_default).into(ivImage);
        ivImage.setClickable(false);
    }
}
