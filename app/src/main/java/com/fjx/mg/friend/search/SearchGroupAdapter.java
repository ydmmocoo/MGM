package com.fjx.mg.friend.search;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.fjx.mg.R;
import com.library.common.base.adapter.BaseAdapter;
import com.library.common.utils.CommonImageLoader;
import com.tencent.imsdk.ext.group.TIMGroupBaseInfo;

/**
 * Author    by hanlz
 * Date      on 2020/2/5.
 * Description：
 */
public class SearchGroupAdapter extends BaseAdapter<TIMGroupBaseInfo> {

    public SearchGroupAdapter() {
        super(R.layout.item_add_friend);
    }

    @Override
    protected void convert(BaseViewHolder helper, TIMGroupBaseInfo item) {
        TextView tvAddFriend = helper.getView(R.id.tvAddFriend);
        ImageView ivAvatar = helper.getView(R.id.ivAcatar);
        helper.setText(R.id.tvNickName, item.getGroupName());
        helper.setText(R.id.tvPhone, "（".concat(item.getGroupId()).concat("）"));
        CommonImageLoader.load(item.getFaceUrl()).round().placeholder(R.drawable.default_user_image).into(ivAvatar);
        tvAddFriend.setVisibility(View.GONE);
        helper.getView(R.id.tvPhone).setVisibility(View.GONE);
    }
}
