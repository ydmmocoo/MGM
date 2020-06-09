package com.fjx.mg.main.rate;

import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.fjx.mg.R;
import com.library.common.base.adapter.BaseAdapter;
import com.library.repository.models.RateModel;

public class RateAdapter extends BaseAdapter<RateModel> {

    public RateAdapter() {
        super(R.layout.item_rate);
    }

    @Override
    protected void convert(BaseViewHolder helper, RateModel item) {
        helper.setImageResource(R.id.tvImage, item.getDrawableId());
        helper.setText(R.id.tvCountryValue, item.getAmountName().concat("  ").concat(item.getAmountKey()));
        helper.setText(R.id.tvAmounteValue, item.getToAmount());
    }
}
