package com.fjx.mg.news.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.fjx.mg.R;
import com.library.common.base.adapter.BaseAdapter;
import com.library.common.utils.CommonImageLoader;
import com.library.common.utils.StringUtil;
import com.library.repository.models.RecommentListModel;

public class NewsRecommendReadAdapter extends BaseAdapter<RecommentListModel> {


    public NewsRecommendReadAdapter() {
        super(R.layout.item_news_recommend_read);
    }

    @Override
    protected void convert(BaseViewHolder helper, RecommentListModel item) {
        helper.setText(R.id.tvRecommendTitle, item.getNewsTitle());
        helper.setText(R.id.tvRecommendAuthor, item.getNewsAuth());
        helper.setText(R.id.tvRecommendTime, item.getPublishTime());
        TextView tvRecommendCommentNumber = helper.getView(R.id.tvRecommendCommentNumber);
        String format = String.format(getContext().getString(R.string.read), item.getCommentNum());
        tvRecommendCommentNumber.setText(format);
        try {
            if (item.getImgs() != null && item.getImgs().size() > 0) {
                String s = item.getImgs().get(0);
                if (StringUtil.isNotEmpty(s)) {
                    CommonImageLoader.load(s).placeholder(R.drawable.food_default).into((ImageView) helper.getView(R.id.ivRecommendPic));
                }
            }
        } catch (IndexOutOfBoundsException e) {
            CommonImageLoader.load(R.drawable.food_default).into((ImageView) helper.getView(R.id.ivRecommendPic));
        }

    }

}
