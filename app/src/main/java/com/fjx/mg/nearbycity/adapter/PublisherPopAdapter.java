package com.fjx.mg.nearbycity.adapter;

import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.fjx.mg.R;
import com.library.common.base.adapter.BaseAdapter;
import com.library.repository.models.NearbyCityTypeListModel;

/**
 * Author    by Administrator
 * Date      on 2019/10/17.
 * Description：
 */
public class PublisherPopAdapter extends BaseAdapter<NearbyCityTypeListModel> {

    public PublisherPopAdapter() {
        super(R.layout.item_publisher_pop_layout);
    }

    @Override
    protected void convert(BaseViewHolder helper, NearbyCityTypeListModel item) {
        helper.setText(R.id.tvContent, item.getTypeName());
    }
}
