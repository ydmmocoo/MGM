package com.fjx.mg.moments.add.pois;

import android.util.Log;

import com.amap.api.services.core.PoiItem;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.fjx.mg.R;
import com.library.common.base.adapter.BaseAdapter;

public class AoisAdapter extends BaseAdapter<PoiItem> {

    public AoisAdapter() {
        super(R.layout.item_aois);
    }

    @Override
    protected void convert(BaseViewHolder helper, PoiItem item) {
        helper.setText(R.id.adress, "" + item.toString());
        Log.e("adress", ""+item.toString());
    }
}
