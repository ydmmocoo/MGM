package com.fjx.mg.friend.search;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.fjx.mg.R;
import com.library.common.base.adapter.BaseAdapter;
import com.library.common.utils.CommonImageLoader;
import com.library.repository.models.SearchTimFriendModel;
import com.tencent.imsdk.friendship.TIMFriend;
import com.tencent.qcloud.uikit.common.utils.TIMStringUtil;

public class SearchFeiendAdapter extends BaseAdapter<SearchTimFriendModel> {
    public SearchFeiendAdapter() {
        super(R.layout.item_search_friend);
    }

    @Override
    protected void convert(BaseViewHolder helper, SearchTimFriendModel item) {
        TextView tvAddFriend = helper.getView(R.id.tvAddFriend);
        ImageView ivAvatar = helper.getView(R.id.ivAcatar);
        TIMFriend timFriend = item.getTimFriend();
        helper.setText(R.id.tvNickName, timFriend.getTimUserProfile().getNickName());
        String no = "";
        if (TextUtils.equals("1", item.getType())) {
            no = getContext().getString(R.string.mgm_no).concat("：").concat(timFriend.getIdentifier());
        } else if (TextUtils.equals("2", item.getType())) {
            no = getContext().getString(R.string.phone_num).concat("：").concat(TIMStringUtil.getPhone(timFriend.getTimUserProfile()));
        } else {
            no = "";
        }
        helper.setText(R.id.tvPhone, no);
        CommonImageLoader.load(timFriend.getTimUserProfile().getFaceUrl()).round().placeholder(R.drawable.default_user_image).into(ivAvatar);
        tvAddFriend.setVisibility(View.GONE);

    }
}
