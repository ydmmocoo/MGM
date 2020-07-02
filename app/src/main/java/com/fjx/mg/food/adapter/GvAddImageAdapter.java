package com.fjx.mg.food.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.fjx.mg.R;
import com.library.common.utils.CommonImageLoader;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author yedeman
 * @date 2020/6/2.
 * email：ydmmocoo@gmail.com
 * description：
 */
public class GvAddImageAdapter extends BaseAdapter {

    private Context mContext;
    private List<String> mList;

    public GvAddImageAdapter(Context context, List<String> list) {
        mContext = context;
        mList = list;
    }

    public void setData(List<String> list) {
        mList = list;
        notifyDataSetChanged();
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
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_gv_add_image, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (!TextUtils.isEmpty(mList.get(position))) {
            CommonImageLoader.load(mList.get(position))
                    .placeholder(R.drawable.big_image_default).into(holder.mIvPic);
        } else {
            CommonImageLoader.load(R.drawable.add_image_default)
                    .placeholder(R.drawable.big_image_default).into(holder.mIvPic);
        }
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.iv_pic)
        ImageView mIvPic;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
