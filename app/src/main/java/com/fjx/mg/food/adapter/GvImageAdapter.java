package com.fjx.mg.food.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.fjx.mg.R;
import com.fjx.mg.view.RoundImageView;
import com.library.common.utils.CommonImageLoader;
import com.library.common.utils.DimensionUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author yedeman
 * @date 2020/6/1.
 * email：ydmmocoo@gmail.com
 * description：
 */
public class GvImageAdapter extends BaseAdapter {

    private Context mContext;
    private List<String> mList;

    public GvImageAdapter(Context context, List<String> list) {
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
            convertView = View.inflate(mContext, R.layout.item_gv_image, null);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder= (ViewHolder) convertView.getTag();
        }
        holder.mIvPic.setRectAdius(DimensionUtil.dip2px(5));
        CommonImageLoader.load(mList.get(position))
                .placeholder(R.drawable.food_default).into(holder.mIvPic);
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.iv_pic)
        RoundImageView mIvPic;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
