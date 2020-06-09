package com.fjx.mg.house.fragment;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.fjx.mg.R;
import com.library.common.base.adapter.BaseAdapter;
import com.library.common.utils.CommonImageLoader;
import com.library.repository.models.HouseDetailModel;

public class HouseLeaseAdapter extends BaseAdapter<HouseDetailModel> {

    private boolean isSale;//是否是售房

    public void setSale(boolean sale) {
        isSale = sale;
    }

    public HouseLeaseAdapter() {
        super(R.layout.item_house);
    }

    @Override
    protected void convert(BaseViewHolder helper, HouseDetailModel item) {
        helper.setText(R.id.tvTitle, item.getTitle());
        helper.setText(R.id.tvMoney, item.getPrice());

        ImageView ivStatus = helper.getView(R.id.ivStatus);

        String address = item.getCountryName().concat(item.getCityName());
        helper.setText(R.id.tvAddress, address);
        helper.setText(R.id.tvHouseScale, item.getLayout().concat("/").concat(item.getArea()).concat("m²"));
        helper.setText(R.id.tvDate, item.getCreateTime());
        ImageView imageView = helper.getView(R.id.ivImage);
        TextView tvHint = helper.getView(R.id.tvHint);
        tvHint.setVisibility((isSale || (2 == item.getType())) ? View.GONE : View.VISIBLE);
        imageView.setVisibility(TextUtils.isEmpty(item.getImg()) ? View.GONE : View.VISIBLE);
        CommonImageLoader.load(item.getImg()).placeholder(R.drawable.food_default).into(imageView);

        //类型：1:求租，2：求购，3：出租，4：出售
        switch (item.getType()) {
            case 1:
                ivStatus.setImageResource(R.drawable.house_had_rent);
                break;
            case 2:
                ivStatus.setImageResource(R.drawable.house_had_buy);
                break;
            case 3:
                ivStatus.setImageResource(R.drawable.house_had_rentout);
                break;
            case 4:
                ivStatus.setImageResource(R.drawable.house_had_sell);
                break;
        }

        if (TextUtils.equals(item.getStatus(), "3")) {
            ivStatus.setImageResource(R.drawable.publish_invalid);
        }else{
            ivStatus.setImageResource(R.drawable.ic_closed);

        }

        helper.getView(R.id.ivStatus).setVisibility(TextUtils.equals(item.getStatus(), "1") || TextUtils.equals(item.getStatus(), "3") ? View.VISIBLE : View.GONE);
    }
}
