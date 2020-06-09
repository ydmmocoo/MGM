package com.fjx.mg.main.more;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.TextView;

import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.fjx.mg.R;
import com.library.common.base.adapter.BaseAdapter;
import com.library.repository.models.AppModel;
import com.library.repository.models.RecAppListModel;

public class RecentlyUsedAdapter extends BaseAdapter<RecAppListModel.AccessListBean> {

    private Context context;
    private Drawable drawable;
    private String name;

    RecentlyUsedAdapter(Context context) {
        super(R.layout.item_app);
        this.context = context;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void convert(BaseViewHolder helper, RecAppListModel.AccessListBean item) {
        TextView tvContent = helper.getView(R.id.tvApp);
        switch (item.getAppId()) {
            case "1":
                drawable = context.getResources().getDrawable(R.drawable.ic_home_cz);
                name = context.getResources().getString(R.string.recharge_center);
                break;
            case "2":
                drawable = context.getResources().getDrawable(R.drawable.ic_home_zs);
                name = context.getResources().getString(R.string.house_rent);
                break;
            case "3":
                drawable = context.getResources().getDrawable(R.drawable.ic_home_zp);
                name = context.getResources().getString(R.string.employment);
                break;
            case "4":
                drawable = context.getResources().getDrawable(R.drawable.ic_home_hy);
                name = context.getResources().getString(R.string.yellow_page);
                break;
            case "5":
                drawable = context.getResources().getDrawable(R.drawable.ic_home_search);
                name = context.getResources().getString(R.string.exchange_rate_inquiry);
                break;
            case "6":
                drawable = context.getResources().getDrawable(R.drawable.ic_home_fy);
                name = context.getResources().getString(R.string.translate_online);
                break;
            case "7":
                drawable = context.getResources().getDrawable(R.drawable.home_feedback);
                name = context.getResources().getString(R.string.feedback);
                break;
            case "8":
                drawable = context.getResources().getDrawable(R.drawable.payment_problem);
                name = context.getResources().getString(R.string.payment_problem);
                break;
            case "9":
                drawable = context.getResources().getDrawable(R.drawable.payment_problem);
                name = context.getResources().getString(R.string.home_nearby_city);
                break;
            default:
                break;
        }

        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        tvContent.setCompoundDrawables(null, drawable, null, null);
        tvContent.setText(name);
    }


}
