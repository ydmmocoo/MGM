package com.fjx.mg.me.comment.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.fjx.mg.R;
import com.library.common.base.adapter.BaseAdapter;
import com.library.common.utils.CommonImageLoader;
import com.library.common.utils.GradientDrawableHelper;
import com.library.repository.data.UserCenter;
import com.library.repository.models.MyNewsCommentModel;
import com.library.repository.models.UserInfoModel;

public class NewsCommentAdapter extends BaseAdapter<MyNewsCommentModel.CommentListBean> {

    public NewsCommentAdapter() {
        super(R.layout.item_my_house_comment);

        addChildClickViewIds(R.id.tvDelete,R.id.lltitle);
    }

    @Override
    protected void convert(BaseViewHolder helper, MyNewsCommentModel.CommentListBean item) {
        UserInfoModel infoModel = UserCenter.getUserInfo();
        TextView tvReply = helper.getView(R.id.tvReplyCount);
        GradientDrawableHelper.whit(tvReply).setColor(R.color.textColorYellow3).setCornerRadius(50);

        helper.setText(R.id.tvUserName, infoModel.getUNick())
                .setText(R.id.tvContent, item.getContent())
                .setText(R.id.tvReplyUser, "@".concat(item.getAuthNickName()).concat("："))
                .setText(R.id.tvReplyTitle, item.getNewsTitle())
                .setText(R.id.tvCreateDate, item.getCreateTime())
                .setText(R.id.tvReplyCount, item.getReplyNum().concat(getContext().getString(R.string.reply_num)))
                .setText(R.id.tvFavNum, item.getLikeNum());

        helper.getView(R.id.tvFavNum).setVisibility(View.VISIBLE);
        ImageView ivAvatar = helper.getView(R.id.ivUserAvatar);
        CommonImageLoader.load(infoModel.getUImg()).circle().placeholder(R.drawable.user_default).into(ivAvatar);

    }
}
