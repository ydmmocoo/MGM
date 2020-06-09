package com.fjx.mg.food.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.fjx.mg.R;
import com.fjx.mg.view.flowlayout.FlowLayout;
import com.fjx.mg.view.flowlayout.TagAdapter;

import java.util.List;

/**
 * @author yedeman
 * @date 2020/5/28.
 * email：ydmmocoo@gmail.com
 * description：
 */
public class ShopFullReductionTagAdapter extends TagAdapter<String> {

    private Context mContext;

    public ShopFullReductionTagAdapter(Context context,List<String> datas) {
        super(datas);
        mContext=context;
    }

    @Override
    public View getView(FlowLayout parent, int position, String s) {
        TextView tv= (TextView) View.inflate(mContext, R.layout.item_tag,null);
        tv.setText(s);
        return tv;
    }
}
