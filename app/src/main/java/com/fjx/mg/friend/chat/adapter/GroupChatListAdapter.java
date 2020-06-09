package com.fjx.mg.friend.chat.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.fjx.mg.R;
import com.library.common.base.adapter.BaseAdapter;
import com.library.common.utils.CommonImageLoader;
import com.tencent.imsdk.ext.group.TIMGroupBaseInfo;

/**
 * Author    by hanlz
 * Date      on 2019/12/5.
 * Description：
 */
public class GroupChatListAdapter extends BaseAdapter<TIMGroupBaseInfo> {

    public GroupChatListAdapter() {
        super(R.layout.item_group_chat_list);
    }

    @Override
    protected void convert(BaseViewHolder helper, TIMGroupBaseInfo item) {
        ImageView ivAcatar = helper.getView(R.id.ivAcatar);
        CommonImageLoader.load(item.getFaceUrl()).round().placeholder(R.drawable.food_default).into(ivAcatar);
        helper.setText(R.id.tvUserName, item.getGroupName());
    }
}
