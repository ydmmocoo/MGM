package com.fjx.mg.food.adapter;

import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.fjx.mg.R;
import com.library.common.base.adapter.BaseAdapter;
import com.library.repository.models.GoogleMapGeocodeSearchBean;
import com.library.repository.models.GoogleMapKeywordSearchBean;

/**
 * @author yedeman
 * @date 2020/6/16.
 * email：ydmmocoo@gmail.com
 * description：
 */
public class RvGoogleMapSearchAdapter extends BaseAdapter<GoogleMapKeywordSearchBean.ResultsBean> {

    public RvGoogleMapSearchAdapter() {
        super(R.layout.item_aois);
    }

    @Override
    protected void convert(BaseViewHolder helper, GoogleMapKeywordSearchBean.ResultsBean item) {
        helper.setText(R.id.adress, "" + item.getName());
    }
}
