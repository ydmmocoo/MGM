package com.fjx.mg.recharge.center;

import android.text.TextUtils;
import android.util.Log;

import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.fjx.mg.R;
import com.library.common.base.adapter.BaseAdapter;
import com.library.repository.models.RechargePhoneModel;

public class PhoneListAdapter extends BaseAdapter<RechargePhoneModel> {
    public PhoneListAdapter() {
        super(R.layout.item_phone_list);
    }

    @Override
    protected void convert(BaseViewHolder helper, RechargePhoneModel item) {
        if (!TextUtils.isEmpty(item.getAreaCode())) {
            helper.setText(R.id.tvPhone, item.getAreaCode().concat(" ").concat(item.getPhone()));
        } else {
            helper.setText(R.id.tvPhone, item.getPhone());
        }
    }
}
