package com.fjx.mg.nearbycity.adapter;

import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.fjx.mg.R;
import com.library.common.base.adapter.BaseAdapter;
import com.library.repository.models.NearbyCityExpListModel;

/**
 * Author    by Administrator
 * Date      on 2019/10/17.
 * Description：
 */
public class DeadlinePopAdapter extends BaseAdapter<NearbyCityExpListModel> {

    public DeadlinePopAdapter() {
        super(R.layout.item_publisher_pop_layout);
    }

    @Override
    protected void convert(BaseViewHolder helper, NearbyCityExpListModel item) {
        StringBuilder sb = new StringBuilder();
        sb.append(item.getTime());
        sb.append("(");
        sb.append(item.getPrice());
        sb.append(item.getUnit());
        sb.append(")");
        helper.setText(R.id.tvContent, sb.toString());
    }
}
