package com.fjx.mg.moments.city;

import android.widget.TextView;

import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.fjx.mg.R;
import com.library.common.base.adapter.BaseAdapter;
import com.library.common.utils.ViewUtil;

public class CityMomentsTypeAdapterx extends BaseAdapter<String> {
    public CityMomentsTypeAdapterx() {
        super(R.layout.item_citycircle_moments_type);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        TextView type = helper.getView(R.id.tvType);
        type.setText(item);
        ViewUtil.setDrawableBackGround(type, R.drawable.solid_city_circle_gray);
        ViewUtil.setTextColor(type, R.color.text_gray8e);
    }
}
