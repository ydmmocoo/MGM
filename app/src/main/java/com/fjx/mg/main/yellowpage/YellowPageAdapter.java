package com.fjx.mg.main.yellowpage;

import android.widget.ImageView;

import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.fjx.mg.R;
import com.library.common.base.adapter.BaseAdapter;
import com.library.common.utils.CommonImageLoader;
import com.library.repository.models.CmpanydetaisModel;
import com.library.repository.models.CompanyDetailModel;

public class YellowPageAdapter extends BaseAdapter<CmpanydetaisModel> {
    public YellowPageAdapter() {
        super(R.layout.item_company);
    }

    @Override
    protected void convert(BaseViewHolder helper, CmpanydetaisModel item) {

        ImageView ivIcon = helper.getView(R.id.ivIcon);
        String url = "";
        if
        (item.getImgs() != null) {
            if (!item.getImgs().isEmpty())
                url = item.getImgs().get(0);
            CommonImageLoader.load(url).placeholder(R.drawable.food_default).into(ivIcon);
            helper.setText(R.id.tvTitle, item.getTitle());
            helper.setText(R.id.tvDesc, item.getDesc());
        }
    }
}
