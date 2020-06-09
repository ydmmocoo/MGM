package com.fjx.mg.network.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.fjx.mg.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.library.repository.models.SearchAgentListModel;

/**
 * Author    by hanlz
 * Date      on 2020/1/14.
 * Description：
 */
public class GoogleMapsInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private Context mContext;


    public GoogleMapsInfoWindowAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_info_window, null);
        render(marker, view);
        return view;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

    /**
     * 自定义infowinfow窗口
     */
    public void render(Marker marker, View view) {
        //如果想修改自定义Infow中内容，请通过view找到它并修改
        SearchAgentListModel mData = (SearchAgentListModel) marker.getTag();
        TextView tvNetworkName = view.findViewById(R.id.tvNetworkName);
        TextView tvLimit = view.findViewById(R.id.tvLimit);
        TextView tvAddress = view.findViewById(R.id.tvAddress);
        TextView tvNavigation = view.findViewById(R.id.tvNavigation);
        tvNetworkName.setText(mData.getNickName());
        tvLimit.setText(view.getContext().getString(R.string.conversion_money).concat(" <") + mData.getAvailableMargin() + "AR");
        tvAddress.setText(mData.getAddress());
    }
}
