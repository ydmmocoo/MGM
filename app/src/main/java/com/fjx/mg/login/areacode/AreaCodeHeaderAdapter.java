package com.fjx.mg.login.areacode;

import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.fjx.mg.R;
import com.library.common.base.adapter.BaseAdapter;
import com.library.repository.models.AreaCodeModel;

public class AreaCodeHeaderAdapter extends BaseAdapter<AreaCodeModel> {

    public AreaCodeHeaderAdapter() {
        super(R.layout.item_area_code);
    }

    @Override
    protected void convert(BaseViewHolder helper, AreaCodeModel item) {
        helper.setText(R.id.tvArea, item.getName());
        helper.setText(R.id.tvCode, "+" + item.getTel());
    }
}
