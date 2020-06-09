package com.fjx.mg.main.fragment.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.fjx.mg.R;
import com.stx.xmarqueeview.XMarqueeView;
import com.stx.xmarqueeview.XMarqueeViewAdapter;

import java.util.List;

public class MarqueeViewAdapter extends XMarqueeViewAdapter<String> {

    private Context mContext;

    public MarqueeViewAdapter(List<String> datas, Context context) {
        super(datas);
        mContext = context;
    }

    @Override
    public View onCreateView(XMarqueeView parent) {
        //跑马灯单个显示条目布局，自定义
        return LayoutInflater.from(parent.getContext()).inflate(R.layout.item_marqueeview, null);
    }

    @Override
    public void onBindView(View parent, View view, final int position) {
        //布局内容填充
        TextView tvOne = view.findViewById(R.id.marquee_tv_one);
        tvOne.setText(mDatas.get(position));
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}