package com.fjx.mg.food.adapter;

import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.fjx.mg.R;
import com.fjx.mg.view.flowlayout.FlowLayout;
import com.fjx.mg.view.flowlayout.TagAdapter;
import com.library.common.utils.DimensionUtil;

import java.util.List;

/**
 * @author yedeman
 * @date 2020/6/12.
 * email：ydmmocoo@gmail.com
 * description：
 */
public class RemarkTagAdapter extends TagAdapter<String> {

    private Context mContext;

    public RemarkTagAdapter(Context context, List<String> datas) {
        super(datas);
        mContext = context;
    }

    @Override
    public View getView(FlowLayout parent, int position, String s) {
        TextView tv = (TextView) View.inflate(mContext, R.layout.item_tag, null);
        tv.setBackgroundColor(ContextCompat.getColor(mContext,R.color.white));
        tv.setTextColor(ContextCompat.getColor(mContext,R.color.black));
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP,12);
        tv.setPadding(DimensionUtil.dip2px(10),DimensionUtil.dip2px(7),
                DimensionUtil.dip2px(10),DimensionUtil.dip2px(7));
        tv.setText(s);
        return tv;
    }
}
