package com.fjx.mg.nearbycity.adapter;

import android.widget.TextView;

import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.fjx.mg.R;
import com.library.common.base.adapter.BaseAdapter;
import com.library.common.utils.CommonImageLoader;
import com.library.common.utils.StringUtil;
import com.library.common.utils.ViewUtil;
import com.library.repository.models.NearbyCityCommentListModel;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Author    by hanlz
 * Date      on 2019/10/19.
 * Description：
 */
public class NearbyCityCommentListAdapter extends BaseAdapter<NearbyCityCommentListModel> {

    private boolean mIsLike;

    public NearbyCityCommentListAdapter() {
        super(R.layout.item_nearby_city_comment_list);

        addChildClickViewIds(R.id.tvPraise);
    }

    @Override
    protected void convert(BaseViewHolder helper, NearbyCityCommentListModel item) {

        mIsLike = item.isLike();

        helper.setText(R.id.tvUserName, item.getUserNickName());
        CircleImageView avatar = helper.getView(R.id.ivHeaderIcon);
        CommonImageLoader.load(item.getUserAvatar()).placeholder(R.drawable.default_user_image).into(avatar);
        helper.setText(R.id.tvDate, item.getCreateTime() + "");
//        helper.setText(R.id.tvContent, item.getContent());
        TextView content = helper.getView(R.id.tvContent);
        if (StringUtil.isNotEmpty(item.getContent())) {
            content.setText(item.getContent());
        }

        TextView praise = helper.getView(R.id.tvPraise);
        if (item.isLike()) {
            ViewUtil.setDrawableLeft(praise, R.drawable.like_red);
        } else {
            ViewUtil.setDrawableLeft(praise, R.drawable.like_gray);
        }
        helper.setText(R.id.tvCommentNum, item.getReplyNum().concat(getContext().getString(R.string.reply_num)));
    }

}
