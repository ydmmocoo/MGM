package com.fjx.mg.moments.add;

import android.widget.TextView;

import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.fjx.mg.R;
import com.library.common.base.adapter.BaseAdapter;
import com.library.common.utils.ViewUtil;
import com.library.repository.models.TypeListModel;

public class AddMomentsTypeAdapter extends BaseAdapter<TypeListModel.TypeListBean> {

    AddMomentsTypeAdapter() {
        super(R.layout.item_add_moments_type);
    }

    @Override
    protected void convert(BaseViewHolder helper, TypeListModel.TypeListBean item) {
        TextView type = helper.getView(R.id.tvType);
        type.setText(item.getTypeName());
        ViewUtil.setDrawableBackGround(type, item.getCliclk() ? R.drawable.solid_city_circle_red_d1 : R.drawable.solid_city_circle_gray_f1);
        ViewUtil.setTextColor(type, item.getCliclk() ? R.color.white : R.color.text_gray8e);
    }
}
