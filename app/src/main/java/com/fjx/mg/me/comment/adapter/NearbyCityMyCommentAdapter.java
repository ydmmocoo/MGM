package com.fjx.mg.me.comment.adapter;

import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.fjx.mg.R;
import com.library.common.base.adapter.BaseAdapter;
import com.library.common.utils.CommonImageLoader;
import com.library.common.utils.GradientDrawableHelper;
import com.library.common.utils.StringUtil;
import com.library.common.utils.ViewUtil;
import com.library.repository.data.UserCenter;
import com.library.repository.models.MyCommentListModel;
import com.library.repository.models.UserInfoModel;

/**
 * Author    by hanlz
 * Date      on 2019/10/23.
 * Description：
 */
public class NearbyCityMyCommentAdapter extends BaseAdapter<MyCommentListModel> {

    public NearbyCityMyCommentAdapter() {
        super(R.layout.item_my_house_comment);

        addChildClickViewIds(R.id.tvDelete,R.id.lltitle);
    }

    @Override
    protected void convert(BaseViewHolder helper, MyCommentListModel item) {

        UserInfoModel infoModel = UserCenter.getUserInfo();
        TextView tvReply = helper.getView(R.id.tvReplyCount);
        GradientDrawableHelper.whit(tvReply).setColor(R.color.textColorYellow3).setCornerRadius(50);
        helper.setVisible(R.id.tvFavNum, true);
        TextView tvFavNum = helper.getView(R.id.tvFavNum);
        if (StringUtil.equals("0", item.getLikeNum())) {
            ViewUtil.setDrawableLeft(tvFavNum, R.drawable.like_gray);
        } else {
            ViewUtil.setDrawableLeft(tvFavNum, R.drawable.like_red);
        }
        helper.setText(R.id.tvUserName, infoModel.getUNick())
                .setText(R.id.tvContent, item.getContent())
                .setText(R.id.tvReplyUser, "@".concat(item.getTypeName()).concat("："))
                .setText(R.id.tvReplyTitle, item.getContent())
                .setText(R.id.tvCreateDate, item.getCreateTime())
                .setText(R.id.tvReplyCount, item.getReplyNum().concat(getContext().getString(R.string.reply_num)))
                .setText(R.id.tvFavNum, item.getLikeNum());

        ImageView ivAvatar = helper.getView(R.id.ivUserAvatar);
        CommonImageLoader.load(infoModel.getUImg()).circle().placeholder(R.drawable.user_default).into(ivAvatar);
    }
}
