package com.fjx.mg.friend.addfriend;

import androidx.core.content.ContextCompat;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.fjx.mg.R;
import com.library.common.base.adapter.BaseAdapter;
import com.library.common.utils.CommonImageLoader;
import com.library.common.utils.GradientDrawableHelper;
import com.library.common.utils.StringUtil;
import com.library.repository.models.ImUserRelaM;

public class SearchFeiendAdapter extends BaseAdapter<ImUserRelaM> {

    public SearchFeiendAdapter() {
        super(R.layout.item_add_friend);
    }

    @Override
    protected void convert(BaseViewHolder helper, ImUserRelaM item) {
        TextView tvAddFriend = helper.getView(R.id.tvAddFriend);
        ImageView ivAvatar = helper.getView(R.id.ivAcatar);
        helper.setText(R.id.tvNickName, item.getUserProfile().getNickName());
        if (StringUtil.isNotEmpty(item.getUserProfile().getIdentifier())) {
            helper.setText(R.id.tvPhone, "（".concat(StringUtil.phoneText(item.getUserProfile().getIdentifier())).concat("）"));
        }
        CommonImageLoader.load(item.getUserProfile().getFaceUrl()).round().placeholder(R.drawable.default_user_image).into(ivAvatar);
        if (item.isFriend()) {
            tvAddFriend.setText(getContext().getString(R.string.had_add));
            tvAddFriend.setTextColor(ContextCompat.getColor(helper.itemView.getContext(), R.color.textColorGray));
            GradientDrawableHelper.whit(tvAddFriend).setColor(R.color.white).setStroke(1, R.color.white);
        } else {
            tvAddFriend.setText(getContext().getString(R.string.add));
            tvAddFriend.setTextColor(ContextCompat.getColor(helper.itemView.getContext(), R.color.textColorAccent));
            GradientDrawableHelper.whit(tvAddFriend).setColor(R.color.white).setStroke(1, R.color.colorAccent);
        }
    }

    public interface OnCliclAddListener {
        void onClickAdd();
    }
}
