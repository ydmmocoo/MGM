package com.fjx.mg.recharge.center;

import android.annotation.SuppressLint;
import androidx.core.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.fjx.mg.R;
import com.library.common.base.adapter.BaseAdapter;
import com.library.common.utils.GradientDrawableHelper;
import com.library.repository.models.RechargeModel;

public class RechargeAdapter extends BaseAdapter<RechargeModel> {
    private boolean isPhoneCharge = true;

    private boolean isPhone;

    public void setPhoneCharge(boolean phoneCharge) {
        isPhoneCharge = phoneCharge;
    }

    public void setPhone(boolean phone) {
        isPhone = phone;
    }

    public RechargeAdapter() {
        super(R.layout.item_recharge);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void convert(BaseViewHolder helper, RechargeModel item) {
        View view = helper.getView(R.id.llParent);
        TextView tvContent = helper.getView(R.id.tvContent);
        TextView tvPrice = helper.getView(R.id.tvPrice);

        tvPrice.setVisibility(isPhoneCharge ? View.VISIBLE : View.GONE);

        if (isPhoneCharge) {
            if (isPhone) {
                tvContent.setText(item.getPackageX() + item.getUnit());
                tvPrice.setText(getContext().getString(R.string.selling_price).concat("：") + item.getPrice() + item.getUnit());
            } else {
                tvContent.setText(item.getPackageX() + item.getCunit());
                tvPrice.setText(getContext().getString(R.string.selling_price).concat("：") + item.getPrice() + item.getFunit());
            }
        } else {
            tvContent.setText(item.getPackageX());
            tvPrice.setText(getContext().getString(R.string.selling_price).concat("：") + item.getPrice() + "AR");
        }

        if (item.isCheck()) {
            GradientDrawableHelper.whit(view).setColor(R.color.trans).setStroke(1, R.color.colorAccent);
            tvContent.setTextColor(ContextCompat.getColor(view.getContext(), R.color.textColorAccent));
            tvPrice.setTextColor(ContextCompat.getColor(view.getContext(), R.color.textColorAccent));
        } else {
            GradientDrawableHelper.whit(view).setColor(R.color.trans).setStroke(1, R.color.textColorHint);
            tvContent.setTextColor(ContextCompat.getColor(view.getContext(), R.color.textColorHint));
            tvPrice.setTextColor(ContextCompat.getColor(view.getContext(), R.color.textColorHint));
        }
    }

    public void reCheck(int position) {
        for (int i = 0; i < getData().size(); i++) {
            getData().get(i).setCheck(i == position);
        }
        notifyDataSetChanged();
    }

    public RechargeModel getCheckModel() {
        for (int i = 0; i < getData().size(); i++) {
            if (getItem(i).isCheck()) return getItem(i);
        }
        return null;
    }

}
