package com.fjx.mg.nearbycity.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.fjx.mg.R;
import com.library.common.base.adapter.BaseAdapter;
import com.library.common.utils.CommonImageLoader;
import com.library.repository.models.NearbyCityCompanyListModel;

/**
 * Author    by Administrator
 * Date      on 2019/10/17.
 * Description：
 */
public class HotShopAdapter extends BaseAdapter<NearbyCityCompanyListModel> {

    public HotShopAdapter() {
        super(R.layout.item_nearby_city_hot_shop_layout);
    }

    @Override
    protected void convert(BaseViewHolder helper, NearbyCityCompanyListModel item) {
        helper.setText(R.id.tvHotShopName, item.getTitle());
        ImageView imageView = helper.getView(R.id.ivHotShopPic);
        CommonImageLoader.load(item.getImg()).placeholder(R.drawable.food_default).into(imageView);
    }
}
