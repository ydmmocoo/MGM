package com.fjx.mg.friend.nearby;

import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.fjx.mg.R;
import com.library.common.base.adapter.BaseAdapter;
import com.library.common.utils.CommonImageLoader;
import com.library.repository.models.NearbyUserModel;

import java.util.List;

public class NearbyAdapter extends BaseAdapter<NearbyUserModel> {

    public NearbyAdapter() {
        super(R.layout.item_nearby_user);
    }

    public NearbyAdapter(int layoutResId, @Nullable List<NearbyUserModel> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, NearbyUserModel item) {
        helper.setText(R.id.tvNickName, item.getNickName());
        helper.setText(R.id.tvDistance, item.getDistance().concat(item.getDistanceUnit()).concat("以内"));
        if (item.getSex() == 1) {
            helper.setImageResource(R.id.ivGanderTag, R.drawable.gender_man);
        } else if (item.getSex() == 2) {
            helper.setImageResource(R.id.ivGanderTag, R.drawable.gender_woman);
        } else {
            helper.setImageResource(R.id.ivGanderTag, 0);
        }

        ImageView ivAvatar = helper.getView(R.id.ivAcatar);
        CommonImageLoader.load(item.getAvatar()).round().placeholder(R.drawable.default_user_image).into(ivAvatar);
    }
}
