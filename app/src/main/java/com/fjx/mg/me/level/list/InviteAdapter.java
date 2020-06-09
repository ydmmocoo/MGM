package com.fjx.mg.me.level.list;

import android.widget.ImageView;

import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.fjx.mg.R;
import com.library.common.base.adapter.BaseAdapter;
import com.library.common.utils.CommonImageLoader;
import com.library.repository.models.InviteListModel;

public class InviteAdapter extends BaseAdapter<InviteListModel.InviteBean> {
    public InviteAdapter() {
        super(R.layout.item_invite);
    }

    @Override
    protected void convert(BaseViewHolder helper, InviteListModel.InviteBean item) {
        ImageView tvAvatar = helper.getView(R.id.tvAvatar);
        CommonImageLoader.load(item.getUImg()).round().into(tvAvatar);
        helper.setText(R.id.tvNickName, item.getNickName());
    }
}
