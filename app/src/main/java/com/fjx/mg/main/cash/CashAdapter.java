package com.fjx.mg.main.cash;

import android.text.TextUtils;
import android.widget.TextView;

import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.fjx.mg.R;
import com.library.common.base.adapter.BaseAdapter;
import com.library.common.utils.GradientDrawableHelper;
import com.library.common.utils.StringUtil;
import com.library.repository.models.CashListModel;

public class CashAdapter extends BaseAdapter<CashListModel.CashModel> {
    public CashAdapter() {
        super(R.layout.item_cash);

        addChildClickViewIds(R.id.tvReceive);
    }

    @Override
    protected void convert(BaseViewHolder helper, CashListModel.CashModel item) {
        helper.setText(R.id.tvPhone, StringUtil.phoneText(item.getToNickname()));
        TextView textView = helper.getView(R.id.tvReceive);

        if (TextUtils.equals(item.getStatus(), "1")) {
            textView.setText(item.getBalance() + "AR");
            GradientDrawableHelper.whit(textView).setColor(R.color.yellow_bg1);
        } else {
            textView.setText(getContext().getString(R.string.get_cash));
            GradientDrawableHelper.whit(textView).setColor(R.color.colorAccent);
        }
    }
}
