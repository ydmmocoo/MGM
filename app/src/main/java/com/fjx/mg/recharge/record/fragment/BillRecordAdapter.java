package com.fjx.mg.recharge.record.fragment;

import androidx.core.content.ContextCompat;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.fjx.mg.R;
import com.library.common.base.adapter.BaseAdapter;
import com.library.repository.models.PhoneRechargeModel;

import static com.fjx.mg.recharge.record.fragment.BillRecordTabFragment.TYPE_DATA;
import static com.fjx.mg.recharge.record.fragment.BillRecordTabFragment.TYPE_ELECT;
import static com.fjx.mg.recharge.record.fragment.BillRecordTabFragment.TYPE_MOBILE;
import static com.fjx.mg.recharge.record.fragment.BillRecordTabFragment.TYPE_NET;
import static com.fjx.mg.recharge.record.fragment.BillRecordTabFragment.TYPE_WATER;

public class BillRecordAdapter extends BaseAdapter<PhoneRechargeModel.RecordListBean> {

    private int type;

    public void setType(int type) {
        this.type = type;
    }

    public BillRecordAdapter() {
        super(R.layout.item_bill_record);
    }

    @Override
    protected void convert(BaseViewHolder helper, PhoneRechargeModel.RecordListBean item) {
        TextView tvTypeName = helper.getView(R.id.tvTypeName);
        TextView tvTypeName2 = helper.getView(R.id.tvTypeName2);
        TextView tvDate = helper.getView(R.id.tvDate);
        TextView tvPrice1 = helper.getView(R.id.tvPrice1);
        TextView tvPrice2 = helper.getView(R.id.tvPrice2);
        ImageView ivIcon = helper.getView(R.id.ivIcon);
        TextView tvStatus = helper.getView(R.id.tvStatus);
        TextView tvTAccount = helper.getView(R.id.tvTAccount);

        tvPrice1.setText(item.getPrice().concat("AR"));
        tvPrice2.setText(item.getPrice().concat("元"));
        tvDate.setText(item.getDatetime());
        tvStatus.setText(item.getStatusTip());

        if (type == TYPE_MOBILE) {
            tvTypeName.setText(getContext().getString(R.string.phone_recharge));
            ivIcon.setImageResource(R.drawable.ic_mobile);
            tvTypeName2.setText(getContext().getString(R.string.Pay_phone_bill));
            tvTAccount.setText(item.getPhone());
        } else if (type == TYPE_DATA) {
            tvTypeName.setText(getContext().getString(R.string.data_recharge));
            tvTypeName2.setText(getContext().getString(R.string.Charging_capacity));
            ivIcon.setImageResource(R.drawable.record_data);
            tvTAccount.setText(item.getPhone());
        } else if (type == TYPE_ELECT) {
            tvTypeName.setText(getContext().getString(R.string.elect_recharge));
            tvTypeName2.setText(item.getService());
            ivIcon.setImageResource(R.drawable.ic_elect);
            tvTAccount.setText(item.getAccount());
        } else if (type == TYPE_NET) {
            tvTypeName.setText(getContext().getString(R.string.net_recharge));
            tvTAccount.setText(item.getAccount());
            tvTypeName2.setText(item.getService());
            ivIcon.setImageResource(R.drawable.ic_net);
        } else if (type == TYPE_WATER) {
            tvTypeName.setText(getContext().getString(R.string.water_recharge));
            tvTAccount.setText(item.getAccount());
            tvTypeName2.setText(item.getService());
            ivIcon.setImageResource(R.drawable.ic_water);
        }

        //1:处理成功,2:等待处理,3:取消'
        if (TextUtils.equals("1", item.getStatus())) {
            tvStatus.setTextColor(ContextCompat.getColor(tvStatus.getContext(), R.color.textColorGray));
        } else if (TextUtils.equals("2", item.getStatus())) {
            tvStatus.setTextColor(ContextCompat.getColor(tvStatus.getContext(), R.color.colorGreen));
        } else {
            tvStatus.setTextColor(ContextCompat.getColor(tvStatus.getContext(), R.color.colorAccent));

        }

    }
}
