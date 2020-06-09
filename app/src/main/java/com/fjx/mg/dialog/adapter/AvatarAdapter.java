package com.fjx.mg.dialog.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.fjx.mg.R;
import com.library.common.base.adapter.BaseAdapter;
import com.library.common.utils.CommonImageLoader;

public class AvatarAdapter extends BaseAdapter<String> {
    public AvatarAdapter() {
        super(R.layout.item_avatar);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        ImageView ivAvatar = helper.getView(R.id.ivAvatar);
        CommonImageLoader.load(item).circle().into(ivAvatar);

    }
}
