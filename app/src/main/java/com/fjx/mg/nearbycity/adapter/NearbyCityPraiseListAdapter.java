package com.fjx.mg.nearbycity.adapter;

import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.fjx.mg.R;
import com.library.common.base.adapter.BaseAdapter;
import com.library.common.utils.CommonImageLoader;
import com.library.repository.models.NearbyCityPraiseListModel;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Author    by hanlz
 * Date      on 2019/10/19.
 * Description：点赞列表适配器
 */
public class NearbyCityPraiseListAdapter extends BaseAdapter<NearbyCityPraiseListModel> {


    public NearbyCityPraiseListAdapter() {
        super(R.layout.item_nearby_city_praise_list);
    }

    @Override
    protected void convert(BaseViewHolder helper, NearbyCityPraiseListModel item) {
        helper.setText(R.id.tvUserName, item.getUserNickName());
        CircleImageView avatar = helper.getView(R.id.ivHeaderIcon);
        CommonImageLoader.load(item.getUserAvatar()).placeholder(R.drawable.default_user_image).into(avatar);
        helper.setText(R.id.tvDate, item.getCreateTime());
    }
}
