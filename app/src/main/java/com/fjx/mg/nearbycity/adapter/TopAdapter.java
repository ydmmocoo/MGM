package com.fjx.mg.nearbycity.adapter;


import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.fjx.mg.R;
import com.library.common.base.adapter.BaseAdapter;
import com.library.common.utils.CommonImageLoader;
import com.library.repository.models.NearbyCityTypeListModel;

import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Author    by Administrator
 * Date      on 2019/10/17.
 * Description：
 */
public class TopAdapter extends BaseAdapter<NearbyCityTypeListModel> {

    private JSONObject mData;

    public TopAdapter() {
        super(R.layout.item_nearby_city_top_layout);
    }

    public void setUnReadCountData(JSONObject cityCircleUnReadList) {
        mData = cityCircleUnReadList;
        notifyDataSetChanged();
    }

    @Override
    protected void convert(BaseViewHolder helper, NearbyCityTypeListModel item) {
        helper.setText(R.id.tvTypeDescription, item.getTypeName());
        CircleImageView imageView = helper.getView(R.id.ivTypePic);
        CommonImageLoader.load(item.getImg()).placeholder(R.drawable.food_default).into(imageView);
        try {
            int count;
            count = mData.optInt(item.getcId());
            if (count > 0) {
                helper.setVisible(R.id.tvTips, true);
            } else {
                helper.setVisible(R.id.tvTips, false);
            }
            helper.setText(R.id.tvTips, count + "");
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

    }
}
