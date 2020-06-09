package com.fjx.mg.food.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fjx.mg.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author yedeman
 * @date 2020/6/5.
 * email：ydmmocoo@gmail.com
 * description：
 */
public class LvFilterAdapter extends BaseAdapter {

    private Context mContext;
    private List<String> mList;

    public LvFilterAdapter(Context context, List<String> list) {
        mContext = context;
        mList = list;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_lv_filter, null);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder= (ViewHolder) convertView.getTag();
        }
        holder.mTvContent.setText(mList.get(position));
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.tv_content)
        TextView mTvContent;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
