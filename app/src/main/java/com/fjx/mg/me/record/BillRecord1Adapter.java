package com.fjx.mg.me.record;

import android.text.TextUtils;

import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.fjx.mg.R;
import com.library.common.base.adapter.BaseAdapter;
import com.library.repository.models.BillRecordModel;

public class BillRecord1Adapter extends BaseAdapter<BillRecordModel.RecordListBean> {
    public BillRecord1Adapter() {
        super(R.layout.item_bill_record);
    }

    @Override
    protected void convert(BaseViewHolder helper, BillRecordModel.RecordListBean item) {
        try {
            if (TextUtils.equals("7", item.getType())) {//转账
                helper.setText(R.id.tvTypeName, item.getTypeName().concat(getContext().getString(R.string.to)).concat(item.getTo()));
                helper.setImageResource(R.id.ivIcon,R.drawable.ic_mobile);
            } else if (TextUtils.equals("9", item.getType())) {//收款
                helper.setImageResource(R.id.ivIcon,R.mipmap.icon_bill_refund);
                helper.setText(R.id.tvTypeName, item.getTypeName().concat(getContext().getString(R.string.form)).concat(item.getFrom()));
                helper.setImageResource(R.id.ivIcon,R.drawable.ic_mobile);
            } else if (TextUtils.equals("8", item.getType())) {//发红包
                helper.setText(R.id.tvTypeName, item.getTypeName().concat(getContext().getString(R.string.rp_form)).concat(item.getTo()));
                helper.setImageResource(R.id.ivIcon,R.drawable.ic_mobile);
            } else {
                helper.setText(R.id.tvTypeName, item.getTypeName());
                if ("14".equals(item.getType())){
                    helper.setImageResource(R.id.ivIcon,R.mipmap.icon_bill_refund);
                }else if ("11".equals(item.getType())){
                    helper.setImageResource(R.id.ivIcon,R.mipmap.icon_bill_food);
                }else {
                    helper.setImageResource(R.id.ivIcon,R.drawable.ic_mobile);
                }
            }
            helper.setText(R.id.tvTypeName2, item.getRemark());
            helper.setText(R.id.tvDate, item.getCreateTime());
            helper.setText(R.id.tvPrice1, item.getPrice().concat("AR"));
            helper.setText(R.id.tvPrice2, item.getCnyPrice().concat(getContext().getString(R.string.yuan)));
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
}
