package com.fjx.mg.network.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.fjx.mg.R;
import com.library.common.base.adapter.BaseAdapter;
import com.library.common.utils.CommonImageLoader;
import com.library.repository.models.SearchAgentListModel;


/**
 * Author    by hanlz
 * Date      on 2019/11/5.
 * Description：网点搜索页适配器
 */
public class SalesNetworkSearchAdapter extends BaseAdapter<SearchAgentListModel> {

    public SalesNetworkSearchAdapter() {
        super(R.layout.item_sales_network_search);
    }

    @Override
    protected void convert(BaseViewHolder helper, SearchAgentListModel item) {
        helper.setText(R.id.tvNetworkName, item.getNickName())
                .setText(R.id.tvLimit, getContext().getString(R.string.conversion_money_less) + item.getAvailableMargin() + "AR")
                .setText(R.id.tvAddress, item.getAddress());
        ImageView imageView = helper.getView(R.id.ivHeaderIcon);
        CommonImageLoader.load(item.getAvatar()).placeholder(R.drawable.food_default).into(imageView);
    }
}
