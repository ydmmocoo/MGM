package com.fjx.mg.moments.city;

import android.widget.TextView;

import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.fjx.mg.R;
import com.fjx.mg.moments.util.FriendsTagUtils;
import com.library.common.base.adapter.BaseAdapter;
import com.library.common.utils.ViewUtil;
import com.library.repository.models.TypeListModel;

import java.util.Random;

public class CityMomentsTypeAdapter extends BaseAdapter<String> {
    public CityMomentsTypeAdapter() {
        super(R.layout.item_citycircle_moments_type);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        TextView type = helper.getView(R.id.tvType);
        type.setText(item);
        int randomNum = new Random().nextInt(4);
        int drawableIds = -1;
        int textColorIds = -1;
        switch (randomNum) {
            case FriendsTagUtils.BLUE:
                drawableIds = R.drawable.friends_tv_blue_bg;
                textColorIds = R.color.friends_blue;
                break;
            case FriendsTagUtils.RED:
                drawableIds = R.drawable.friends_tv_red_bg;
                textColorIds = R.color.friends_red;
                break;
            case FriendsTagUtils.GREEN:
                drawableIds = R.drawable.friends_tv_green_bg;
                textColorIds = R.color.friends_green;
                break;
            case FriendsTagUtils.YELLOW:
                drawableIds = R.drawable.friends_tv_yellow_bg;
                textColorIds = R.color.friends_yellow;
                break;
            default:
                drawableIds = R.drawable.friends_tv_blue_bg;
                textColorIds = R.color.friends_blue;
                break;
        }
        ViewUtil.setDrawableBackGround(type, drawableIds);
        ViewUtil.setTextColor(type, textColorIds);

        //solid_stroke_city_circle_red
        //solid_stroke_city_circle_blue2
    }
}
