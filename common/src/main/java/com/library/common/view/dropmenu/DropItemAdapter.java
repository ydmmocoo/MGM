package com.library.common.view.dropmenu;

import androidx.core.content.ContextCompat;

import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.library.common.R;
import com.library.common.base.adapter.BaseAdapter;

public class DropItemAdapter extends BaseAdapter<DropMenuModel> {
    public DropItemAdapter() {
        super(R.layout.item_drop);
    }

    @Override
    protected void convert(BaseViewHolder helper, DropMenuModel item) {
        helper.setText(R.id.tvName, item.getTypeName());
        helper.setTextColor(R.id.tvName, ContextCompat.getColor(helper.itemView.getContext(),
                item.isSelect ? R.color.colorAccent : R.color.textColor));
        helper.itemView.setBackgroundColor(ContextCompat
                .getColor(helper.itemView.getContext(), item.isSelect ? R.color.dropSelectColor : R.color.dropUnSelectColor));
    }
}
