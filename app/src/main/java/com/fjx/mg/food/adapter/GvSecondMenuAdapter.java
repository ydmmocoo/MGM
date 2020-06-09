package com.fjx.mg.food.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fjx.mg.R;
import com.library.common.utils.CommonImageLoader;
import com.library.repository.models.ShopTypeBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author yedeman
 * @date 2020/5/18.
 * email：ydmmocoo@gmail.com
 * description：
 */
public class GvSecondMenuAdapter extends BaseAdapter {

    private Context mContext;
    private List<ShopTypeBean.ShopTypeListBean> mList;

    public GvSecondMenuAdapter(Context context, List<ShopTypeBean.ShopTypeListBean> list) {
        mContext = context;
        mList = list;
    }

    public void setData(List<ShopTypeBean.ShopTypeListBean> list){
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
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_gv_second_menu, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        CommonImageLoader.load(mList.get(position).getImg())
                .placeholder(R.drawable.food_default).into(holder.mIvIcon);
        holder.mTvName.setText(mList.get(position).getName());
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.iv_icon)
        ImageView mIvIcon;
        @BindView(R.id.tv_name)
        TextView mTvName;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
