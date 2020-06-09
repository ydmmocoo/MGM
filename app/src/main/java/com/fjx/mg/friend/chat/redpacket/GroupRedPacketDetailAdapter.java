package com.fjx.mg.friend.chat.redpacket;

import android.widget.ImageView;

import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.fjx.mg.R;
import com.library.common.base.adapter.BaseAdapter;
import com.library.common.utils.CommonImageLoader;
import com.library.repository.models.ReciveRedRrecordModel;

/**
 * Author    by hanlz
 * Date      on 2019/12/8.
 * Description：
 */
public class GroupRedPacketDetailAdapter extends BaseAdapter<ReciveRedRrecordModel.ReciveListBean> {

    public GroupRedPacketDetailAdapter() {
        super(R.layout.item_new_friend);
    }

    @Override
    protected void convert(BaseViewHolder helper, ReciveRedRrecordModel.ReciveListBean item) {
        CommonImageLoader.load(item.getAvatar()).placeholder(R.drawable.food_default).into((ImageView)helper.getView(R.id.ivAcatar));
        helper.setText(R.id.tvNickName,item.getNickName())
                .setText(R.id.tvMessage,item.getCreateTime())
                .setText(R.id.tvAddFriend,item.getPrice().concat("AR"));


    }
}
