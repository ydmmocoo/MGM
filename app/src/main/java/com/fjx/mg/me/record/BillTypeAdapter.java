package com.fjx.mg.me.record;

import androidx.core.content.ContextCompat;
import android.widget.TextView;

import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.fjx.mg.R;
import com.library.common.base.adapter.BaseAdapter;
import com.library.repository.models.BillRecordModel;

public class BillTypeAdapter extends BaseAdapter<BillRecordModel.AccountTypeBean> {

    public BillTypeAdapter() {
        super(R.layout.item_bill_type);
    }

    @Override
    protected void convert(BaseViewHolder helper, BillRecordModel.AccountTypeBean item) {
        helper.setText(R.id.tvName, item.getName());
        TextView textView = helper.getView(R.id.tvName);
        if (item.isSelect()) {
            textView.setBackgroundColor(ContextCompat.getColor(helper.itemView.getContext(), R.color.colorAccent));
            textView.setTextColor(ContextCompat.getColor(helper.itemView.getContext(), R.color.white));
        } else {
            textView.setBackgroundColor(ContextCompat.getColor(helper.itemView.getContext(), R.color.colorGrayBg));
            textView.setTextColor(ContextCompat.getColor(helper.itemView.getContext(), R.color.textColor));
        }

    }

    public void setSelect(int position) {
        boolean isSelect = getItem(position).isSelect();
        if (isSelect) {
            getItem(position).setSelect(!isSelect);
            return;
        }
        for (BillRecordModel.AccountTypeBean b : getData()) {
            b.setSelect(false);
        }
        getItem(position).setSelect(true);
    }


    public BillRecordModel.AccountTypeBean getSellectItem() {
        for (BillRecordModel.AccountTypeBean b : getData()) {
            if (b.isSelect()) return b;
        }
        return null;
    }

}
