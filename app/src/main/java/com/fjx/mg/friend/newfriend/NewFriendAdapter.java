package com.fjx.mg.friend.newfriend;

import androidx.core.content.ContextCompat;

import android.widget.TextView;

import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.fjx.mg.R;
import com.library.common.base.adapter.BaseAdapter;
import com.library.common.utils.GradientDrawableHelper;
import com.library.common.utils.StringUtil;
import com.library.repository.db.model.DBFriendRequestModel;

public class NewFriendAdapter extends BaseAdapter<DBFriendRequestModel> {

    public NewFriendAdapter() {
        super(R.layout.item_new_friend);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final DBFriendRequestModel item) {
        helper.setText(R.id.tvNickName, item.getNickName());
        helper.setText(R.id.tvPhone, " (".concat(StringUtil.phoneText(item.getNickName())).concat(")"));
        helper.setText(R.id.tvMessage, getContext().getString(R.string.additional_information).concat(item.getAddWording()));
        TextView tvAddFriend = helper.getView(R.id.tvAddFriend);

        if (item.getStatus() == 0) {
            tvAddFriend.setText(getContext().getString(R.string.agree_add_friends));
            tvAddFriend.setTextColor(ContextCompat.getColor(helper.itemView.getContext(), R.color.textColorAccent));
            GradientDrawableHelper.whit(tvAddFriend).setColor(R.color.white).setStroke(1, R.color.colorAccent);
        } else {
            tvAddFriend.setText(item.getStatus() == 1 ? getContext().getString(R.string.had_agree_add_friends) : getContext().getString(R.string.had_reject));
            tvAddFriend.setTextColor(ContextCompat.getColor(helper.itemView.getContext(), R.color.textColorGray));
            GradientDrawableHelper.whit(tvAddFriend).setColor(R.color.white).setStroke(1, R.color.white);
        }
    }
}
