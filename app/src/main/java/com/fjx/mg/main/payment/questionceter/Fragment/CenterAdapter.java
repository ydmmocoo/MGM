package com.fjx.mg.main.payment.questionceter.Fragment;

import androidx.core.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.fjx.mg.R;
import com.library.common.base.adapter.BaseAdapter;
import com.library.common.utils.CommonImageLoader;
import com.library.repository.models.MyQuestionListDetailModel;

public class CenterAdapter extends BaseAdapter<MyQuestionListDetailModel> {


    public CenterAdapter() {
        super(R.layout.item_question);
    }

    @Override
    protected void convert(BaseViewHolder helper, MyQuestionListDetailModel item) {
        ImageView imageView = helper.getView(R.id.ivImage);

        helper.setTextColor(R.id.tvMoney, ContextCompat.getColor(helper.itemView.getContext(),
                item.getType() == 1 ? R.color.colorAccent : R.color.gray_text));

        helper.setText(R.id.tvTitle, item.getQuestion());
        helper.setText(R.id.tvMoney, getContext().getString(R.string.money_reward).concat(item.getPrice()));
        helper.setText(R.id.tvNum, item.getReply_count().concat(getContext().getString(R.string.question)));
        helper.setText(R.id.tvTime, item.getCreateTime());
        imageView.setVisibility(TextUtils.isEmpty(item.getImg()) ? View.GONE : View.VISIBLE);
        CommonImageLoader.load(item.getImg()).placeholder(R.drawable.food_default).into(imageView);
    }
}
