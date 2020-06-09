package com.fjx.mg.food.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.fjx.mg.R;
import com.fjx.mg.view.flowlayout.FlowLayout;
import com.fjx.mg.view.flowlayout.TagAdapter;
import com.library.repository.models.StoreGoodsBean;

import java.util.List;

/**
 * @author yedeman
 * @date 2020/5/27.
 * email：ydmmocoo@gmail.com
 * description：
 */
public class TextTagAdapter extends TagAdapter<String> {

    private Context mContext;
    private List<String> mList;

    public TextTagAdapter(Context context, List<String> list) {
        super(list);
        mContext=context;
        mList=list;
    }

    @Override
    public View getView(FlowLayout parent, int position, String s) {
        TextView tv= (TextView) View.inflate(mContext, R.layout.item_tag,null);
        tv.setText(s);
        return tv;
    }
}
