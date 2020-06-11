package com.fjx.mg.moments.add.pois;

import android.util.Log;

import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.fjx.mg.R;
import com.library.common.base.adapter.BaseAdapter;
import com.library.repository.models.GoogleMapGeocodeSearchBean;

public class AoisAdapter extends BaseAdapter<GoogleMapGeocodeSearchBean.ResultsBean> {

    public AoisAdapter() {
        super(R.layout.item_aois);
    }

    @Override
    protected void convert(BaseViewHolder helper, GoogleMapGeocodeSearchBean.ResultsBean item) {
        helper.setText(R.id.adress, "" + item.getFormatted_address());
    }
}
