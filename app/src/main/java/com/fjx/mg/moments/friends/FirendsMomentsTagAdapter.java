package com.fjx.mg.moments.friends;

import android.widget.TextView;

import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.fjx.mg.R;
import com.fjx.mg.moments.util.FriendsTagUtils;
import com.library.common.base.adapter.BaseAdapter;

/**
 * create by hanlz
 * 2019/9/23
 * Describe:
 */
public class FirendsMomentsTagAdapter extends BaseAdapter<String> {

    public FirendsMomentsTagAdapter() {
        super(R.layout.item_friends_type_tags);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        FriendsTagUtils friendsTagUtils = new FriendsTagUtils();
        friendsTagUtils.setTextView(item, (TextView) helper.getView(R.id.tvFriendsTags));
    }
}
