package com.fjx.mg.me.wallet.detail;

import android.text.TextUtils;

import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.fjx.mg.R;
import com.library.common.base.adapter.BaseAdapter;
import com.library.repository.models.BalanceDetailModel;

public class BalanceDetailAdapter extends BaseAdapter<BalanceDetailModel.BalanceListBean> {

    public BalanceDetailAdapter() {
        super(R.layout.item_balance_detail);
    }

    @Override
    protected void convert(BaseViewHolder helper, BalanceDetailModel.BalanceListBean item) {
        if (TextUtils.equals("7", item.getCate())) {//转账
            helper.setText(R.id.tvTitle, item.getType().concat(getContext().getString(R.string.to)).concat(item.getTo()));
        } else if (TextUtils.equals("9", item.getCate())) {//收款
            helper.setText(R.id.tvTitle, item.getType().concat(getContext().getString(R.string.form)).concat(item.getFrom()));
        } else if (TextUtils.equals("8", item.getCate())) {//发红包
            helper.setText(R.id.tvTitle, item.getType().concat(getContext().getString(R.string.rp_form)).concat(item.getTo()));
        } else {
            helper.setText(R.id.tvTitle, item.getType());
        }
        helper.setText(R.id.tvPrice, item.getPrice());
        helper.setText(R.id.tvDate, item.getCreateTime());
        helper.setText(R.id.tvPrice2, helper.itemView.getContext().getString(R.string.my_balance).concat(item.getRemainBalance()));
    }
}
