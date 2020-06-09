package com.fjx.mg.moments.all.adapter;

import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.fjx.mg.R;
import com.library.common.base.adapter.BaseAdapter;
import com.library.common.utils.CommonImageLoader;
import com.library.common.utils.StringUtil;
import com.library.repository.models.MomentsReplyListModel;

import java.util.regex.Pattern;

/**
 * create by hanlz
 * 2019/9/23
 * Describe:用户朋友圈消息适配器
 */
public class AllMomentsMessageAdapter extends BaseAdapter<MomentsReplyListModel.ReplyListBean> {

    public AllMomentsMessageAdapter() {
        super(R.layout.item_all_moments_message);
    }

    @Override
    protected void convert(BaseViewHolder helper, MomentsReplyListModel.ReplyListBean item) {
        helper.setText(R.id.tvUserName, item.getUserNickName());
        helper.setText(R.id.tvContent, item.getContent());
        helper.setText(R.id.tvDate, item.getCreateTime());
        CommonImageLoader.load(item.getUserAvatar()).placeholder(R.drawable.food_default).into((ImageView) helper.getView(R.id.ivUserAvatar));

        ImageView ivImage = helper.getView(R.id.ivImage);
        ImageView tv_duration = helper.getView(R.id.tv_duration);
        String reg = "(mp4|flv|avi|rm|rmvb|wmv)";
        Pattern p = Pattern.compile(reg);
        tv_duration.setVisibility(p.matcher(item.getUrl()).find() ? View.VISIBLE : View.GONE);
        CommonImageLoader.load(item.getUrl()).noAnim().placeholder(R.drawable.food_default).into(ivImage);
        if (StringUtil.isNotEmpty(item.getUrl())) {
            ivImage.setVisibility(View.VISIBLE);
        } else {
            ivImage.setVisibility(View.INVISIBLE);
        }
        //1:评论，2：点赞
        if (StringUtil.isNotEmpty(item.getType())) {
            if ("1".equals(item.getType())) {
                helper.setVisible(R.id.ivDianZan, false);
                helper.setVisible(R.id.tvContent, true);
            } else if ("2".equals(item.getType())) {
                helper.setVisible(R.id.ivDianZan, true);
                helper.setVisible(R.id.tvContent, false);
            }
        }
    }
}
